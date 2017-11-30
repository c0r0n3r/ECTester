package cz.crcs.ectester.standalone.libs.jni;

import cz.crcs.ectester.common.util.ECUtil;

import javax.crypto.KeyAgreementSpi;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import java.security.*;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.ECParameterSpec;

/**
 * @author Jan Jancar johny@neuromancer.sk
 */
public abstract class NativeKeyAgreementSpi extends KeyAgreementSpi {
    private ECPrivateKey privateKey;
    private ECPublicKey publicKey;
    private ECParameterSpec params;

    @Override
    protected void engineInit(Key key, SecureRandom random) throws InvalidKeyException {
        if (!(key instanceof ECPrivateKey)) {
            throw new InvalidKeyException
                    ("Key must be instance of ECPrivateKey");
        }
        privateKey = (ECPrivateKey) key;
        this.params = privateKey.getParams();
    }

    @Override
    protected void engineInit(Key key, AlgorithmParameterSpec params, SecureRandom random) throws InvalidKeyException, InvalidAlgorithmParameterException {
        if (!(params instanceof ECParameterSpec)) {
            throw new InvalidAlgorithmParameterException();
        }
        engineInit(key, random);
        this.params = (ECParameterSpec) params;
    }

    @Override
    protected Key engineDoPhase(Key key, boolean lastPhase) throws InvalidKeyException, IllegalStateException {
        if (privateKey == null) {
            throw new IllegalStateException("Not initialized");
        }
        if (publicKey != null) {
            throw new IllegalStateException("Phase already executed");
        }
        if (!lastPhase) {
            throw new IllegalStateException
                    ("Only two party agreement supported, lastPhase must be true");
        }
        if (!(key instanceof ECPublicKey)) {
            throw new InvalidKeyException
                    ("Key must be an instance of ECPublicKey");
        }
        ECParameterSpec publicParams = ((ECPublicKey) key).getParams();
        if (!(params.getCurve().equals(publicParams.getCurve()) &&
                params.getGenerator().equals(publicParams.getGenerator()) &&
                params.getOrder().equals(publicParams.getOrder()) &&
                params.getCofactor() == publicParams.getCofactor())) {
            throw new IllegalStateException("Mismatched parameters.");
        }
        publicKey = (ECPublicKey) key;
        return null;
    }

    @Override
    protected byte[] engineGenerateSecret() throws IllegalStateException {
        byte[] pubkey = ECUtil.toX962Uncompressed(publicKey.getW(), params.getCurve());
        byte[] privkey = ECUtil.toByteArray(privateKey.getS(), params.getCurve().getField().getFieldSize());
        return generateSecret(pubkey, privkey, params);
    }

    @Override
    protected int engineGenerateSecret(byte[] sharedSecret, int offset) throws IllegalStateException, ShortBufferException {
        byte[] secret = engineGenerateSecret();
        if (sharedSecret.length < offset + secret.length) {
            throw new ShortBufferException();
        }
        System.arraycopy(secret, 0, sharedSecret, offset, secret.length);
        return secret.length;
    }

    @Override
    protected SecretKey engineGenerateSecret(String algorithm) throws IllegalStateException, NoSuchAlgorithmException, InvalidKeyException {
        throw new NoSuchAlgorithmException(algorithm);
    }

    abstract byte[] generateSecret(byte[] pubkey, byte[] privkey, ECParameterSpec params);


    public static class TomCrypt extends NativeKeyAgreementSpi {

        @Override
        native byte[] generateSecret(byte[] pubkey, byte[] privkey, ECParameterSpec params);
    }
}
