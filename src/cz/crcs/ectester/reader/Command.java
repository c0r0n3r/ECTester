package cz.crcs.ectester.reader;

import cz.crcs.ectester.applet.ECTesterApplet;
import cz.crcs.ectester.applet.EC_Consts;
import cz.crcs.ectester.data.EC_Store;
import cz.crcs.ectester.reader.ec.EC_Curve;
import cz.crcs.ectester.reader.ec.EC_Key;
import cz.crcs.ectester.reader.ec.EC_Keypair;
import cz.crcs.ectester.reader.ec.EC_Params;
import javacard.security.KeyPair;

import javax.smartcardio.CardException;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jan Jancar johny@neuromancer.sk
 */
public abstract class Command {
    protected CommandAPDU cmd;
    protected CardMngr cardManager;

    protected Command(CardMngr cardManager) {
        this.cardManager = cardManager;
    }

    public CommandAPDU getAPDU() {
        return cmd;
    }

    public abstract Response send() throws CardException;

    public static List<Response> sendAll(List<Command> commands) throws CardException {
        List<Response> result = new ArrayList<>();
        for (Command cmd : commands) {
            result.add(cmd.send());
        }
        return result;
    }


    /**
     * @param keyPair   which keyPair/s (local/remote) to set curve domain parameters on
     * @param keyLength key length to choose
     * @param keyClass  key class to choose
     * @return a list of Commands to send in order to prepare the curve on the keypairs.
     * @throws IOException if curve file cannot be found/opened
     */
    public static List<Command> prepareCurve(CardMngr cardManager, EC_Store dataStore, ECTester.Config cfg, byte keyPair, short keyLength, byte keyClass) throws IOException {
        List<Command> commands = new ArrayList<>();

        if (cfg.customCurve) {
            // Set custom curve (one of the SECG curves embedded applet-side)
            short domainParams = keyClass == KeyPair.ALG_EC_FP ? EC_Consts.PARAMETERS_DOMAIN_FP : EC_Consts.PARAMETERS_DOMAIN_F2M;
            commands.add(new Command.Set(cardManager, keyPair, EC_Consts.getCurve(keyLength, keyClass), domainParams, null));
        } else if (cfg.namedCurve != null) {
            // Set a named curve.
            // parse cfg.namedCurve -> cat / id | cat | id
            EC_Curve curve = dataStore.getObject(EC_Curve.class, cfg.namedCurve);
            if (curve == null) {
                throw new IOException("Curve could no be found.");
            }
            if (curve.getBits() != keyLength) {
                throw new IOException("Curve bits mismatch: " + curve.getBits() + " vs " + keyLength + " entered.");
            }

            byte[] external = curve.flatten();
            if (external == null) {
                throw new IOException("Couldn't read named curve data.");
            }
            commands.add(new Command.Set(cardManager, keyPair, EC_Consts.CURVE_external, curve.getParams(), external));
        } else if (cfg.curveFile != null) {
            // Set curve loaded from a file
            EC_Curve curve = new EC_Curve(null, keyLength, keyClass);

            FileInputStream in = new FileInputStream(cfg.curveFile);
            curve.readCSV(in);
            in.close();

            byte[] external = curve.flatten();
            if (external == null) {
                throw new IOException("Couldn't read the curve file correctly.");
            }
            commands.add(new Command.Set(cardManager, keyPair, EC_Consts.CURVE_external, curve.getParams(), external));
        } else {
            // Set default curve
            /* This command was generally causing problems for simulating on jcardsim.
             * Since there, .clearKey() resets all the keys values, even the domain.
             * This might break some other stuff.. But should not.
             */
            //commands.add(new Command.Clear(cardManager, keyPair));
        }

        return commands;
    }


    /**
     * @param keyPair which keyPair/s to set the key params on
     * @return a CommandAPDU setting params loaded on the keyPair/s
     * @throws IOException if any of the key files cannot be found/opened
     */
    public static Command prepareKey(CardMngr cardManager, EC_Store dataStore, ECTester.Config cfg, byte keyPair) throws IOException {
        short params = EC_Consts.PARAMETERS_NONE;
        byte[] data = null;

        if (cfg.key != null || cfg.namedKey != null) {
            params |= EC_Consts.PARAMETERS_KEYPAIR;
            EC_Params keypair;
            if (cfg.key != null) {
                keypair = new EC_Params(EC_Consts.PARAMETERS_KEYPAIR);

                FileInputStream in = new FileInputStream(cfg.key);
                keypair.readCSV(in);
                in.close();
            } else {
                keypair = dataStore.getObject(EC_Keypair.class, cfg.namedKey);
            }

            data = keypair.flatten();
            if (data == null) {
                throw new IOException("Couldn't read the key file correctly.");
            }
        }

        if (cfg.publicKey != null || cfg.namedPublicKey != null) {
            params |= EC_Consts.PARAMETER_W;
            EC_Params pub;
            if (cfg.publicKey != null) {
                pub = new EC_Params(EC_Consts.PARAMETER_W);

                FileInputStream in = new FileInputStream(cfg.publicKey);
                pub.readCSV(in);
                in.close();
            } else {
                pub = dataStore.getObject(EC_Key.Public.class, cfg.namedPublicKey);
                if (pub == null) {
                    pub = dataStore.getObject(EC_Keypair.class, cfg.namedPublicKey);
                }
            }

            byte[] pubkey = pub.flatten(EC_Consts.PARAMETER_W);
            if (pubkey == null) {
                throw new IOException("Couldn't read the public key file correctly.");
            }
            data = pubkey;
        }
        if (cfg.privateKey != null || cfg.namedPrivateKey != null) {
            params |= EC_Consts.PARAMETER_S;
            EC_Params priv;
            if (cfg.privateKey != null) {
                priv = new EC_Params(EC_Consts.PARAMETER_S);

                FileInputStream in = new FileInputStream(cfg.privateKey);
                priv.readCSV(in);
                in.close();
            } else {
                priv = dataStore.getObject(EC_Key.Public.class, cfg.namedPrivateKey);
                if (priv == null) {
                    priv = dataStore.getObject(EC_Keypair.class, cfg.namedPrivateKey);
                }
            }

            byte[] privkey = priv.flatten(EC_Consts.PARAMETER_S);
            if (privkey == null) {
                throw new IOException("Couldn't read the private key file correctly.");
            }
            data = Util.concatenate(data, privkey);
        }
        return new Command.Set(cardManager, keyPair, EC_Consts.CURVE_external, params, data);
    }


    /**
     *
     */
    public static class Allocate extends Command {
        private byte keyPair;
        private short keyLength;
        private byte keyClass;

        /**
         * Creates the INS_ALLOCATE instruction.
         *
         * @param cardManager cardManager to send APDU through
         * @param keyPair     which keyPair to use, local/remote (KEYPAIR_* | ...)
         * @param keyLength   key length to set
         * @param keyClass    key class to allocate
         */
        protected Allocate(CardMngr cardManager, byte keyPair, short keyLength, byte keyClass) {
            super(cardManager);
            this.keyPair = keyPair;
            this.keyLength = keyLength;
            this.keyClass = keyClass;

            byte[] data = new byte[]{0, 0, keyClass};
            Util.setShort(data, 0, keyLength);
            this.cmd = new CommandAPDU(ECTesterApplet.CLA_ECTESTERAPPLET, ECTesterApplet.INS_ALLOCATE, keyPair, 0x00, data);
        }

        @Override
        public Response.Allocate send() throws CardException {
            long elapsed = -System.nanoTime();
            ResponseAPDU response = cardManager.send(cmd);
            elapsed += System.nanoTime();
            return new Response.Allocate(response, elapsed, keyPair, keyLength, keyClass);
        }
    }

    /**
     *
     */
    public static class Clear extends Command {
        private byte keyPair;

        /**
         * @param cardManager cardManager to send APDU through
         * @param keyPair     which keyPair clear, local/remote (KEYPAIR_* || ...)
         */
        protected Clear(CardMngr cardManager, byte keyPair) {
            super(cardManager);
            this.keyPair = keyPair;

            this.cmd = new CommandAPDU(ECTesterApplet.CLA_ECTESTERAPPLET, ECTesterApplet.INS_CLEAR, keyPair, 0x00);
        }

        @Override
        public Response.Clear send() throws CardException {
            long elapsed = -System.nanoTime();
            ResponseAPDU response = cardManager.send(cmd);
            elapsed += System.nanoTime();
            return new Response.Clear(response, elapsed, keyPair);
        }
    }

    /**
     *
     */
    public static class Set extends Command {
        private byte keyPair;
        private byte curve;
        private short params;
        private byte[] external;

        /**
         * Creates the INS_SET instruction.
         *
         * @param cardManager cardManager to send APDU through
         * @param keyPair     which keyPair to set params on, local/remote (KEYPAIR_* || ...)
         * @param curve       curve to set (EC_Consts.CURVE_*)
         * @param params      parameters to set (EC_Consts.PARAMETER_* | ...)
         * @param external    external curve data, can be null
         */
        protected Set(CardMngr cardManager, byte keyPair, byte curve, short params, byte[] external) {
            super(cardManager);
            this.keyPair = keyPair;
            this.curve = curve;
            this.params = params;
            this.external = external;

            int len = external != null ? 2 + external.length : 2;
            byte[] data = new byte[len];
            Util.setShort(data, 0, params);
            if (external != null) {
                System.arraycopy(external, 0, data, 2, external.length);
            }

            this.cmd = new CommandAPDU(ECTesterApplet.CLA_ECTESTERAPPLET, ECTesterApplet.INS_SET, keyPair, curve, data);
        }

        @Override
        public Response.Set send() throws CardException {
            long elapsed = -System.nanoTime();
            ResponseAPDU response = cardManager.send(cmd);
            elapsed += System.nanoTime();
            return new Response.Set(response, elapsed, keyPair, curve, params);
        }
    }

    /**
     *
     */
    public static class Corrupt extends Command {
        private byte keyPair;
        private byte key;
        private short params;
        private byte corruption;

        /**
         * @param cardManager cardManager to send APDU through
         * @param keyPair     which keyPair to corrupt, local/remote (KEYPAIR_* || ...)
         * @param key         key to corrupt (EC_Consts.KEY_* | ...)
         * @param params      parameters to corrupt (EC_Consts.PARAMETER_* | ...)
         * @param corruption  corruption type (EC_Consts.CORRUPTION_*)
         */
        protected Corrupt(CardMngr cardManager, byte keyPair, byte key, short params, byte corruption) {
            super(cardManager);
            this.keyPair = keyPair;
            this.key = key;
            this.params = params;
            this.corruption = corruption;

            byte[] data = new byte[3];
            Util.setShort(data, 0, params);
            data[2] = corruption;

            this.cmd = new CommandAPDU(ECTesterApplet.CLA_ECTESTERAPPLET, ECTesterApplet.INS_CORRUPT, keyPair, key, data);
        }

        @Override
        public Response.Corrupt send() throws CardException {
            long elapsed = -System.nanoTime();
            ResponseAPDU response = cardManager.send(cmd);
            elapsed += System.nanoTime();
            return new Response.Corrupt(response, elapsed, keyPair, key, params, corruption);
        }
    }

    /**
     *
     */
    public static class Generate extends Command {
        private byte keyPair;

        /**
         * Creates the INS_GENERATE instruction.
         *
         * @param cardManager cardManager to send APDU through
         * @param keyPair     which keyPair to generate, local/remote (KEYPAIR_* || ...)
         */
        protected Generate(CardMngr cardManager, byte keyPair) {
            super(cardManager);
            this.keyPair = keyPair;

            this.cmd = new CommandAPDU(ECTesterApplet.CLA_ECTESTERAPPLET, ECTesterApplet.INS_GENERATE, keyPair, 0);
        }

        @Override
        public Response.Generate send() throws CardException {
            long elapsed = -System.nanoTime();
            ResponseAPDU response = cardManager.send(cmd);
            elapsed += System.nanoTime();
            return new Response.Generate(response, elapsed, keyPair);
        }
    }

    /**
     *
     */
    public static class Export extends Command {
        private byte keyPair;
        private byte key;
        private short params;

        /**
         * Creates the INS_EXPORT instruction.
         *
         * @param cardManager cardManager to send APDU through
         * @param keyPair     keyPair to export from (KEYPAIR_* | ...)
         * @param key         key to export from (EC_Consts.KEY_* | ...)
         * @param params      params to export (EC_Consts.PARAMETER_* | ...)
         */
        protected Export(CardMngr cardManager, byte keyPair, byte key, short params) {
            super(cardManager);
            this.keyPair = keyPair;
            this.key = key;
            this.params = params;

            byte[] data = new byte[2];
            Util.setShort(data, 0, params);

            this.cmd = new CommandAPDU(ECTesterApplet.CLA_ECTESTERAPPLET, ECTesterApplet.INS_EXPORT, keyPair, key, data);
        }

        @Override
        public Response.Export send() throws CardException {
            long elapsed = -System.nanoTime();
            ResponseAPDU response = cardManager.send(cmd);
            elapsed += System.nanoTime();
            return new Response.Export(response, elapsed, keyPair, key, params);
        }
    }

    /**
     *
     */
    public static class ECDH extends Command {
        private byte pubkey;
        private byte privkey;
        private byte export;
        private byte corruption;
        private byte type;

        /**
         * Creates the INS_ECDH instruction.
         *
         * @param cardManager cardManager to send APDU through
         * @param pubkey      keyPair to use for public key, (KEYPAIR_LOCAL || KEYPAIR_REMOTE)
         * @param privkey     keyPair to use for private key, (KEYPAIR_LOCAL || KEYPAIR_REMOTE)
         * @param export      whether to export ECDH secret
         * @param corruption  whether to invalidate the pubkey before ECDH (EC_Consts.CORRUPTION_* || ...)
         * @param type        ECDH algorithm type (EC_Consts.KA_* | ...)
         */
        protected ECDH(CardMngr cardManager, byte pubkey, byte privkey, byte export, byte corruption, byte type) {
            super(cardManager);
            this.pubkey = pubkey;
            this.privkey = privkey;
            this.export = export;
            this.corruption = corruption;
            this.type = type;

            byte[] data = new byte[]{export, corruption, type};

            this.cmd = new CommandAPDU(ECTesterApplet.CLA_ECTESTERAPPLET, ECTesterApplet.INS_ECDH, pubkey, privkey, data);
        }

        @Override
        public Response.ECDH send() throws CardException {
            long elapsed = -System.nanoTime();
            ResponseAPDU response = cardManager.send(cmd);
            elapsed += System.nanoTime();
            return new Response.ECDH(response, elapsed, pubkey, privkey, export, corruption, type);
        }
    }

    public static class ECDSA extends Command {
        private byte keyPair;
        private byte export;
        private byte[] raw;

        /**
         * Creates the INS_ECDSA instruction.
         *
         * @param cardManager cardManager to send APDU through
         * @param keyPair     keyPair to use for signing and verification (KEYPAIR_LOCAL || KEYPAIR_REMOTE)
         * @param export      whether to export ECDSA signature
         * @param raw         data to sign, can be null, in which case random data is signed.
         */
        protected ECDSA(CardMngr cardManager, byte keyPair, byte export, byte[] raw) {
            super(cardManager);
            this.keyPair = keyPair;
            this.export = export;
            this.raw = raw;

            int len = raw != null ? raw.length : 0;
            byte[] data = new byte[2 + len];
            Util.setShort(data, 0, (short) len);
            if (raw != null) {
                System.arraycopy(raw, 0, data, 2, len);
            }

            this.cmd = new CommandAPDU(ECTesterApplet.CLA_ECTESTERAPPLET, ECTesterApplet.INS_ECDSA, keyPair, export, data);
        }

        @Override
        public Response.ECDSA send() throws CardException {
            long elapsed = -System.nanoTime();
            ResponseAPDU response = cardManager.send(cmd);
            elapsed += System.nanoTime();
            return new Response.ECDSA(response, elapsed, keyPair, export, raw);
        }
    }

    /**
     *
     */
    public static class Cleanup extends Command {

        /**
         * @param cardManager cardManager to send APDU through
         */
        protected Cleanup(CardMngr cardManager) {
            super(cardManager);

            this.cmd = new CommandAPDU(ECTesterApplet.CLA_ECTESTERAPPLET, ECTesterApplet.INS_CLEANUP, 0, 0);
        }

        @Override
        public Response.Cleanup send() throws CardException {
            long elapsed = -System.nanoTime();
            ResponseAPDU response = cardManager.send(cmd);
            elapsed += System.nanoTime();
            return new Response.Cleanup(response, elapsed);
        }
    }

    /**
     *
     */
    public static class Support extends Command {

        /**
         * @param cardManager cardManager to send APDU through
         */
        protected Support(CardMngr cardManager) {
            super(cardManager);

            this.cmd = new CommandAPDU(ECTesterApplet.CLA_ECTESTERAPPLET, ECTesterApplet.INS_SUPPORT, 0, 0);
        }

        @Override
        public Response.Support send() throws CardException {
            long elapsed = -System.nanoTime();
            ResponseAPDU response = cardManager.send(cmd);
            elapsed += System.nanoTime();
            return new Response.Support(response, elapsed);
        }
    }
}

