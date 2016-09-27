package simpleapdu;

import applets.SimpleECCApplet;
import static applets.SimpleECCApplet.ECTEST_GENERATE_KEYPAIR_CUSTOMCURVE;
import static applets.SimpleECCApplet.ECTEST_SET_INVALIDCURVE;
import javacard.framework.ISO7816;
import javacard.security.CryptoException;
import javacard.security.KeyPair;
import javax.smartcardio.ResponseAPDU;

/**
 *
 * @author Petr Svenda petr@svenda.com
 */
public class SimpleAPDU {
    static CardMngr cardManager = new CardMngr();

    private final static byte SELECT_ECTESTERAPPLET[] = {(byte) 0x00, (byte) 0xa4, (byte) 0x04, (byte) 0x00, (byte) 0x0a, 
        (byte) 0x45, (byte) 0x43, (byte) 0x54, (byte) 0x65, (byte) 0x73, (byte) 0x74, (byte) 0x65, (byte) 0x72, (byte) 0x30, (byte) 0x31};

    private static byte TESTECSUPPORTALL_FP[] = {(byte) 0xB0, (byte) 0x5E, (byte) 0x00, (byte) 0x00, (byte) 0x00};
    private static byte TESTECSUPPORTALL_F2M[] = {(byte) 0xB0, (byte) 0x5F, (byte) 0x00, (byte) 0x00, (byte) 0x00};
    private static byte TESTECSUPPORTALL_FP_KEYGEN_INVALIDCURVEB[] = {(byte) 0xB0, (byte) 0x70, (byte) 0x10, (byte) 0x00, (byte) 0x00};
    
    static short getShort(byte[] array, int offset) {
        return (short) (((array[offset] & 0xFF) << 8) | (array[offset + 1] & 0xFF));        
    }
    
    public static void main(String[] args) {
        try {
            //
            // REAL CARDS
            //
            if (cardManager.ConnectToCard()) {
                // Select our application on card
                cardManager.sendAPDU(SELECT_ECTESTERAPPLET);
                // Test setting invalid curves    
                ResponseAPDU resp_fp_keygen = cardManager.sendAPDU(TESTECSUPPORTALL_FP_KEYGEN_INVALIDCURVEB);
                PrintECKeyGenInvalidCurveB(resp_fp_keygen);
                
                // Test support for different types of curves
                ResponseAPDU resp_fp = cardManager.sendAPDU(TESTECSUPPORTALL_FP);
                ResponseAPDU resp_f2m = cardManager.sendAPDU(TESTECSUPPORTALL_F2M);
                PrintECSupport(resp_fp);
                PrintECSupport(resp_f2m);
                
                
                
                cardManager.DisconnectFromCard();
            } else {
                System.out.println("Failed to connect to card");
            }
        } catch (Exception ex) {
            System.out.println("Exception : " + ex);
        }
    }
    
    static String getPrintError(short code) {
        if (code == ISO7816.SW_NO_ERROR) {
            return "OK\t(0x9000)";
        }
        else {
            String codeStr = "unknown";
            if (code == CryptoException.ILLEGAL_VALUE) {
                codeStr = "ILLEGAL_VALUE";
            }
            if (code == CryptoException.UNINITIALIZED_KEY) {
                codeStr = "UNINITIALIZED_KEY";
            }
            if (code == CryptoException.NO_SUCH_ALGORITHM) {
                codeStr = "NO_SUCH_ALG";
            }
            if (code == CryptoException.INVALID_INIT) {
                codeStr = "INVALID_INIT";
            }
            if (code == CryptoException.ILLEGAL_USE) {
                codeStr = "ILLEGAL_USE";
            }
            if (code == SimpleECCApplet.SW_SKIPPED) {
                codeStr = "skipped";
            }
            return String.format("fail\t(%s,\t0x%4x)", codeStr, code);
        }    
    }
    
    enum ExpResult {
        SHOULD_SUCCEDD,
        MAY_FAIL,
        MUST_FAIL
    }
    static int VerifyPrintResult(String message, byte expectedTag, byte[] buffer, int bufferOffset, ExpResult expRes) {
        if (buffer[bufferOffset] != expectedTag) {
            System.out.println("ERROR: mismatched tag");
            assert(buffer[bufferOffset] == expectedTag);
        }
        bufferOffset++;
        short resCode = getShort(buffer, bufferOffset);
        bufferOffset += 2;
        
        boolean bHiglight = false;
        if ((expRes == ExpResult.MUST_FAIL) && (resCode == ISO7816.SW_NO_ERROR)) {
            bHiglight = true;
        }
        if ((expRes == ExpResult.SHOULD_SUCCEDD) && (resCode != ISO7816.SW_NO_ERROR)) {
            bHiglight = true;
        }
        if (bHiglight) {
            System.out.println(String.format("!! %-50s%s", message, getPrintError(resCode)));
        }
        else {
            System.out.println(String.format("   %-50s%s", message, getPrintError(resCode)));
        }
        return bufferOffset;
    }
    static void PrintECSupport(ResponseAPDU resp) {
        byte[] buffer = resp.getData();

        System.out.println();System.out.println();
        int bufferOffset = 0;
        while (bufferOffset < buffer.length) {
            assert(buffer[bufferOffset] == SimpleECCApplet.ECTEST_SEPARATOR);
            bufferOffset++;
            String ecType = "unknown";
            if (buffer[bufferOffset] == KeyPair.ALG_EC_FP) {
                ecType = "ALG_EC_FP";
            }
            if (buffer[bufferOffset] == KeyPair.ALG_EC_F2M) {
                ecType = "ALG_EC_F2M";
            }
            System.out.println(String.format("%-53s%s", "EC type:", ecType));
            bufferOffset++;
            short keyLen = getShort(buffer, bufferOffset);
            System.out.println(String.format("%-53s%d bits", "EC key length (bits):", keyLen));
            bufferOffset += 2;

            bufferOffset = VerifyPrintResult("KeyPair object allocation:", SimpleECCApplet.ECTEST_ALLOCATE_KEYPAIR, buffer, bufferOffset, ExpResult.SHOULD_SUCCEDD);
            bufferOffset = VerifyPrintResult("Generate key with def curve (fails if no def):", SimpleECCApplet.ECTEST_GENERATE_KEYPAIR_DEFCURVE, buffer, bufferOffset, ExpResult.MAY_FAIL);
            bufferOffset = VerifyPrintResult("Set valid custom curve:", SimpleECCApplet.ECTEST_SET_VALIDCURVE, buffer, bufferOffset, ExpResult.SHOULD_SUCCEDD);
            bufferOffset = VerifyPrintResult("Generate key with valid curve:", SimpleECCApplet.ECTEST_GENERATE_KEYPAIR_CUSTOMCURVE, buffer, bufferOffset, ExpResult.SHOULD_SUCCEDD);
            bufferOffset = VerifyPrintResult("ECDH agreement with valid point:", SimpleECCApplet.ECTEST_ECDH_AGREEMENT_VALID_POINT, buffer, bufferOffset, ExpResult.SHOULD_SUCCEDD);
            bufferOffset = VerifyPrintResult("ECDH agreement with invalid point (fail is good):", SimpleECCApplet.ECTEST_ECDH_AGREEMENT_INVALID_POINT, buffer, bufferOffset, ExpResult.MUST_FAIL);
            bufferOffset = VerifyPrintResult("Set invalid custom curve (my fail):", SimpleECCApplet.ECTEST_SET_INVALIDCURVE, buffer, bufferOffset, ExpResult.MAY_FAIL);
            bufferOffset = VerifyPrintResult("Generate key with invalid curve (fail is good):", SimpleECCApplet.ECTEST_GENERATE_KEYPAIR_INVALIDCUSTOMCURVE, buffer, bufferOffset, ExpResult.MUST_FAIL);
            
            System.out.println();
        }
    }
    static void PrintECKeyGenInvalidCurveB(ResponseAPDU resp) {
        byte[] buffer = resp.getData();

        System.out.println();
        System.out.println();
        int bufferOffset = 0;
        while (bufferOffset < buffer.length) {
            assert (buffer[bufferOffset] == SimpleECCApplet.ECTEST_SEPARATOR);
            bufferOffset++;
            String ecType = "unknown";
            if (buffer[bufferOffset] == KeyPair.ALG_EC_FP) {
                ecType = "ALG_EC_FP";
            }
            if (buffer[bufferOffset] == KeyPair.ALG_EC_F2M) {
                ecType = "ALG_EC_F2M";
            }
            System.out.println(String.format("%-53s%s", "EC type:", ecType));
            bufferOffset++;
            short keyLen = getShort(buffer, bufferOffset);
            System.out.println(String.format("%-53s%d bits", "EC key length (bits):", keyLen));
            bufferOffset += 2;

            bufferOffset = VerifyPrintResult("KeyPair object allocation:", SimpleECCApplet.ECTEST_ALLOCATE_KEYPAIR, buffer, bufferOffset, ExpResult.SHOULD_SUCCEDD);
            while (bufferOffset < buffer.length) {
                bufferOffset = VerifyPrintResult("Set invalid custom curve:", SimpleECCApplet.ECTEST_SET_INVALIDCURVE, buffer, bufferOffset, ExpResult.SHOULD_SUCCEDD);
                bufferOffset = VerifyPrintResult("Generate key with invalid curve (fail is good):", SimpleECCApplet.ECTEST_GENERATE_KEYPAIR_INVALIDCUSTOMCURVE, buffer, bufferOffset, ExpResult.MUST_FAIL);
                bufferOffset = VerifyPrintResult("Set valid custom curve:", SimpleECCApplet.ECTEST_SET_VALIDCURVE, buffer, bufferOffset, ExpResult.SHOULD_SUCCEDD);
                bufferOffset = VerifyPrintResult("Generate key with valid curve:", SimpleECCApplet.ECTEST_GENERATE_KEYPAIR_CUSTOMCURVE, buffer, bufferOffset, ExpResult.SHOULD_SUCCEDD);
            }

            System.out.println();
        }
    }
}
