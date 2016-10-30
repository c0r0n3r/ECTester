package applets;

import javacard.framework.ISO7816;
import javacard.framework.ISOException;
import javacard.framework.Util;
import javacard.security.KeyPair;
import javacard.security.RandomData;

public class EC_Consts {
    private static byte[] EC_FP_P = null;    //p
    private static byte[] EC_A = null;    //a
    private static byte[] EC_B = null;    //b
    private static byte[] EC_G_X = null;  //G[x,y]
    private static byte[] EC_G_Y = null;  //
    private static byte[] EC_R = null;    //n
    private static short EC_K = 1;        //h

    private static byte[] EC_F2M_F2M = null; //[short ii, short i2, short i3], f = x^m + x^i1 + x^i2 + x^i3 + 1

    public static final byte PARAMETER_FP = 1;
    public static final byte PARAMETER_F2M = 2;

    public static final byte PARAMETER_A = 3;
    public static final byte PARAMETER_B = 4;
    public static final byte PARAMETER_G = 5;
    public static final byte PARAMETER_R = 6;
    public static final byte PARAMETER_K = 7;

    //TODO make params maskable, to allow for PARAMETER_A | PARAMETER_B passed to for example ECKeyGenerator.setInvalidCustomCurve
    public static final byte PARAMETER_S = 8; //private key
    public static final byte PARAMETER_W = 9; //public key


    public static RandomData m_random = null;

    // secp128r1 
    public static final byte[] EC128_FP_P = new byte[]{
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFD,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};

    public static final byte[] EC128_FP_A = new byte[]{
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFD,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFC};

    public static final byte[] EC128_FP_B = new byte[]{
            (byte) 0xE8, (byte) 0x75, (byte) 0x79, (byte) 0xC1,
            (byte) 0x10, (byte) 0x79, (byte) 0xF4, (byte) 0x3D,
            (byte) 0xD8, (byte) 0x24, (byte) 0x99, (byte) 0x3C,
            (byte) 0x2C, (byte) 0xEE, (byte) 0x5E, (byte) 0xD3};

    // G in compressed form / first part of ucompressed
    public static final byte[] EC128_FP_G_X = new byte[]{
            (byte) 0x16, (byte) 0x1F, (byte) 0xF7, (byte) 0x52,
            (byte) 0x8B, (byte) 0x89, (byte) 0x9B, (byte) 0x2D,
            (byte) 0x0C, (byte) 0x28, (byte) 0x60, (byte) 0x7C,
            (byte) 0xA5, (byte) 0x2C, (byte) 0x5B, (byte) 0x86};

    // second part of G uncompressed
    public static final byte[] EC128_FP_G_Y = new byte[]{
            (byte) 0xCF, (byte) 0x5A, (byte) 0xC8, (byte) 0x39,
            (byte) 0x5B, (byte) 0xAF, (byte) 0xEB, (byte) 0x13,
            (byte) 0xC0, (byte) 0x2D, (byte) 0xA2, (byte) 0x92,
            (byte) 0xDD, (byte) 0xED, (byte) 0x7A, (byte) 0x83};
    // Order of G
    public static final byte[] EC128_FP_R = new byte[]{
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFE,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x75, (byte) 0xA3, (byte) 0x0D, (byte) 0x1B,
            (byte) 0x90, (byte) 0x38, (byte) 0xA1, (byte) 0x15};
    // cofactor of G
    public static final short EC128_FP_K = 1;

    // secp160r1 
    public static final byte[] EC160_FP_P = new byte[]{
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0x7F, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};

    public static final byte[] EC160_FP_A = new byte[]{
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0x7F, (byte) 0xFF, (byte) 0xFF, (byte) 0xFC};

    public static final byte[] EC160_FP_B = new byte[]{
            (byte) 0x1C, (byte) 0x97, (byte) 0xBE, (byte) 0xFC,
            (byte) 0x54, (byte) 0xBD, (byte) 0x7A, (byte) 0x8B,
            (byte) 0x65, (byte) 0xAC, (byte) 0xF8, (byte) 0x9F,
            (byte) 0x81, (byte) 0xD4, (byte) 0xD4, (byte) 0xAD,
            (byte) 0xC5, (byte) 0x65, (byte) 0xFA, (byte) 0x45};

    // G in compressed form / first part of ucompressed
    public static final byte[] EC160_FP_G_X = new byte[]{
            (byte) 0x4A, (byte) 0x96, (byte) 0xB5, (byte) 0x68,
            (byte) 0x8E, (byte) 0xF5, (byte) 0x73, (byte) 0x28,
            (byte) 0x46, (byte) 0x64, (byte) 0x69, (byte) 0x89,
            (byte) 0x68, (byte) 0xC3, (byte) 0x8B, (byte) 0xB9,
            (byte) 0x13, (byte) 0xCB, (byte) 0xFC, (byte) 0x82};

    // second part of G uncompressed
    public static final byte[] EC160_FP_G_Y = new byte[]{
            (byte) 0x23, (byte) 0xA6, (byte) 0x28, (byte) 0x55,
            (byte) 0x31, (byte) 0x68, (byte) 0x94, (byte) 0x7D,
            (byte) 0x59, (byte) 0xDC, (byte) 0xC9, (byte) 0x12,
            (byte) 0x04, (byte) 0x23, (byte) 0x51, (byte) 0x37,
            (byte) 0x7A, (byte) 0xC5, (byte) 0xFB, (byte) 0x32};
    // Order of G
    public static final byte[] EC160_FP_R = new byte[]{
            (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x01, (byte) 0xF4, (byte) 0xC8,
            (byte) 0xF9, (byte) 0x27, (byte) 0xAE, (byte) 0xD3,
            (byte) 0xCA, (byte) 0x75, (byte) 0x22, (byte) 0x57};
    // cofactor of G
    public static final short EC160_FP_K = 1;


    // secp192r1 from http://www.secg.org/sec2-v2.pdf
    public static final byte[] EC192_FP_P = new byte[]{
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFE,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};
    public static final byte[] EC192_FP_A = new byte[]{
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFE,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFC};
    public static final byte[] EC192_FP_B = new byte[]{
            (byte) 0x64, (byte) 0x21, (byte) 0x05, (byte) 0x19,
            (byte) 0xE5, (byte) 0x9C, (byte) 0x80, (byte) 0xE7,
            (byte) 0x0F, (byte) 0xA7, (byte) 0xE9, (byte) 0xAB,
            (byte) 0x72, (byte) 0x24, (byte) 0x30, (byte) 0x49,
            (byte) 0xFE, (byte) 0xB8, (byte) 0xDE, (byte) 0xEC,
            (byte) 0xC1, (byte) 0x46, (byte) 0xB9, (byte) 0xB1};
    // G in compressed form / first part of ucompressed
    public static final byte[] EC192_FP_G_X = new byte[]{
            (byte) 0x18, (byte) 0x8D, (byte) 0xA8, (byte) 0x0E,
            (byte) 0xB0, (byte) 0x30, (byte) 0x90, (byte) 0xF6,
            (byte) 0x7C, (byte) 0xBF, (byte) 0x20, (byte) 0xEB,
            (byte) 0x43, (byte) 0xA1, (byte) 0x88, (byte) 0x00,
            (byte) 0xF4, (byte) 0xFF, (byte) 0x0A, (byte) 0xFD,
            (byte) 0x82, (byte) 0xFF, (byte) 0x10, (byte) 0x12};
    // second part of G uncompressed
    public static final byte[] EC192_FP_G_Y = new byte[]{
            (byte) 0x07, (byte) 0x19, (byte) 0x2B, (byte) 0x95,
            (byte) 0xFF, (byte) 0xC8, (byte) 0xDA, (byte) 0x78,
            (byte) 0x63, (byte) 0x10, (byte) 0x11, (byte) 0xED,
            (byte) 0x6B, (byte) 0x24, (byte) 0xCD, (byte) 0xD5,
            (byte) 0x73, (byte) 0xF9, (byte) 0x77, (byte) 0xA1,
            (byte) 0x1E, (byte) 0x79, (byte) 0x48, (byte) 0x11};
    // Order of G
    public static final byte[] EC192_FP_R = new byte[]{
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0x99, (byte) 0xDE, (byte) 0xF8, (byte) 0x36,
            (byte) 0x14, (byte) 0x6B, (byte) 0xC9, (byte) 0xB1,
            (byte) 0xB4, (byte) 0xD2, (byte) 0x28, (byte) 0x31};
    // cofactor of G
    public static final short EC192_FP_K = 1;

    // secp224r1 from http://www.secg.org/sec2-v2.pdf
    public static final byte[] EC224_FP_P = new byte[]{
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01};

    public static final byte[] EC224_FP_A = new byte[]{
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFE,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFE};

    public static final byte[] EC224_FP_B = new byte[]{
            (byte) 0xB4, (byte) 0x05, (byte) 0x0A, (byte) 0x85,
            (byte) 0x0C, (byte) 0x04, (byte) 0xB3, (byte) 0xAB,
            (byte) 0xF5, (byte) 0x41, (byte) 0x32, (byte) 0x56,
            (byte) 0x50, (byte) 0x44, (byte) 0xB0, (byte) 0xB7,
            (byte) 0xD7, (byte) 0xBF, (byte) 0xD8, (byte) 0xBA,
            (byte) 0x27, (byte) 0x0B, (byte) 0x39, (byte) 0x43,
            (byte) 0x23, (byte) 0x55, (byte) 0xFF, (byte) 0xB4};

    // G in compressed form / first part of ucompressed
    public static final byte[] EC224_FP_G_X = new byte[]{
            (byte) 0xB7, (byte) 0x0E, (byte) 0x0C, (byte) 0xBD,
            (byte) 0x6B, (byte) 0xB4, (byte) 0xBF, (byte) 0x7F,
            (byte) 0x32, (byte) 0x13, (byte) 0x90, (byte) 0xB9,
            (byte) 0x4A, (byte) 0x03, (byte) 0xC1, (byte) 0xD3,
            (byte) 0x56, (byte) 0xC2, (byte) 0x11, (byte) 0x22,
            (byte) 0x34, (byte) 0x32, (byte) 0x80, (byte) 0xD6,
            (byte) 0x11, (byte) 0x5C, (byte) 0x1D, (byte) 0x21};
    // second part of G uncompressed
    public static final byte[] EC224_FP_G_Y = new byte[]{
            (byte) 0xBD, (byte) 0x37, (byte) 0x63, (byte) 0x88,
            (byte) 0xB5, (byte) 0xF7, (byte) 0x23, (byte) 0xFB,
            (byte) 0x4C, (byte) 0x22, (byte) 0xDF, (byte) 0xE6,
            (byte) 0xCD, (byte) 0x43, (byte) 0x75, (byte) 0xA0,
            (byte) 0x5A, (byte) 0x07, (byte) 0x47, (byte) 0x64,
            (byte) 0x44, (byte) 0xD5, (byte) 0x81, (byte) 0x99,
            (byte) 0x85, (byte) 0x00, (byte) 0x7E, (byte) 0x34};
    // Order of G
    public static final byte[] EC224_FP_R = new byte[]{
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0x16, (byte) 0xA2,
            (byte) 0xE0, (byte) 0xB8, (byte) 0xF0, (byte) 0x3E,
            (byte) 0x13, (byte) 0xDD, (byte) 0x29, (byte) 0x45,
            (byte) 0x5C, (byte) 0x5C, (byte) 0x2A, (byte) 0x3D};
    // cofactor of G
    public static final short EC224_FP_K = 1;

    // secp256r1 from http://www.secg.org/sec2-v2.pdf
    public static final byte[] EC256_FP_P = new byte[]{
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};
    public static final byte[] EC256_FP_A = new byte[]{
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFC};
    public static final byte[] EC256_FP_B = new byte[]{
            (byte) 0x5A, (byte) 0xC6, (byte) 0x35, (byte) 0xD8,
            (byte) 0xAA, (byte) 0x3A, (byte) 0x93, (byte) 0xE7,
            (byte) 0xB3, (byte) 0xEB, (byte) 0xBD, (byte) 0x55,
            (byte) 0x76, (byte) 0x98, (byte) 0x86, (byte) 0xBC,
            (byte) 0x65, (byte) 0x1D, (byte) 0x06, (byte) 0xB0,
            (byte) 0xCC, (byte) 0x53, (byte) 0xB0, (byte) 0xF6,
            (byte) 0x3B, (byte) 0xCE, (byte) 0x3C, (byte) 0x3E,
            (byte) 0x27, (byte) 0xD2, (byte) 0x60, (byte) 0x4B};
    // G in compressed form / first part of ucompressed
    public static final byte[] EC256_FP_G_X = new byte[]{
            (byte) 0x6B, (byte) 0x17, (byte) 0xD1, (byte) 0xF2,
            (byte) 0xE1, (byte) 0x2C, (byte) 0x42, (byte) 0x47,
            (byte) 0xF8, (byte) 0xBC, (byte) 0xE6, (byte) 0xE5,
            (byte) 0x63, (byte) 0xA4, (byte) 0x40, (byte) 0xF2,
            (byte) 0x77, (byte) 0x03, (byte) 0x7D, (byte) 0x81,
            (byte) 0x2D, (byte) 0xEB, (byte) 0x33, (byte) 0xA0,
            (byte) 0xF4, (byte) 0xA1, (byte) 0x39, (byte) 0x45,
            (byte) 0xD8, (byte) 0x98, (byte) 0xC2, (byte) 0x96};
    // second part of G uncompressed
    public static final byte[] EC256_FP_G_Y = new byte[]{
            (byte) 0x4F, (byte) 0xE3, (byte) 0x42, (byte) 0xE2,
            (byte) 0xFE, (byte) 0x1A, (byte) 0x7F, (byte) 0x9B,
            (byte) 0x8E, (byte) 0xE7, (byte) 0xEB, (byte) 0x4A,
            (byte) 0x7C, (byte) 0x0F, (byte) 0x9E, (byte) 0x16,
            (byte) 0x2B, (byte) 0xCE, (byte) 0x33, (byte) 0x57,
            (byte) 0x6B, (byte) 0x31, (byte) 0x5E, (byte) 0xCE,
            (byte) 0xCB, (byte) 0xB6, (byte) 0x40, (byte) 0x68,
            (byte) 0x37, (byte) 0xBF, (byte) 0x51, (byte) 0xF5};
    // Order of G
    public static final byte[] EC256_FP_R = new byte[]{
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xBC, (byte) 0xE6, (byte) 0xFA, (byte) 0xAD,
            (byte) 0xA7, (byte) 0x17, (byte) 0x9E, (byte) 0x84,
            (byte) 0xF3, (byte) 0xB9, (byte) 0xCA, (byte) 0xC2,
            (byte) 0xFC, (byte) 0x63, (byte) 0x25, (byte) 0x51};
    // cofactor of G
    public static final short EC256_FP_K = 1;

    // secp384r1 from http://www.secg.org/sec2-v2.pdf
    public static final byte[] EC384_FP_P = new byte[]{
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFE,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};

    public static final byte[] EC384_FP_A = new byte[]{
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFE,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFC};

    public static final byte[] EC384_FP_B = new byte[]{
            (byte) 0xB3, (byte) 0x31, (byte) 0x2F, (byte) 0xA7,
            (byte) 0xE2, (byte) 0x3E, (byte) 0xE7, (byte) 0xE4,
            (byte) 0x98, (byte) 0x8E, (byte) 0x05, (byte) 0x6B,
            (byte) 0xE3, (byte) 0xF8, (byte) 0x2D, (byte) 0x19,
            (byte) 0x18, (byte) 0x1D, (byte) 0x9C, (byte) 0x6E,
            (byte) 0xFE, (byte) 0x81, (byte) 0x41, (byte) 0x12,
            (byte) 0x03, (byte) 0x14, (byte) 0x08, (byte) 0x8F,
            (byte) 0x50, (byte) 0x13, (byte) 0x87, (byte) 0x5A,
            (byte) 0xC6, (byte) 0x56, (byte) 0x39, (byte) 0x8D,
            (byte) 0x8A, (byte) 0x2E, (byte) 0xD1, (byte) 0x9D,
            (byte) 0x2A, (byte) 0x85, (byte) 0xC8, (byte) 0xED,
            (byte) 0xD3, (byte) 0xEC, (byte) 0x2A, (byte) 0xEF};

    // G in compressed form / first part of ucompressed
    public static final byte[] EC384_FP_G_X = new byte[]{
            (byte) 0xAA, (byte) 0x87, (byte) 0xCA, (byte) 0x22,
            (byte) 0xBE, (byte) 0x8B, (byte) 0x05, (byte) 0x37,
            (byte) 0x8E, (byte) 0xB1, (byte) 0xC7, (byte) 0x1E,
            (byte) 0xF3, (byte) 0x20, (byte) 0xAD, (byte) 0x74,
            (byte) 0x6E, (byte) 0x1D, (byte) 0x3B, (byte) 0x62,
            (byte) 0x8B, (byte) 0xA7, (byte) 0x9B, (byte) 0x98,
            (byte) 0x59, (byte) 0xF7, (byte) 0x41, (byte) 0xE0,
            (byte) 0x82, (byte) 0x54, (byte) 0x2A, (byte) 0x38,
            (byte) 0x55, (byte) 0x02, (byte) 0xF2, (byte) 0x5D,
            (byte) 0xBF, (byte) 0x55, (byte) 0x29, (byte) 0x6C,
            (byte) 0x3A, (byte) 0x54, (byte) 0x5E, (byte) 0x38,
            (byte) 0x72, (byte) 0x76, (byte) 0x0A, (byte) 0xB7};
    // second part of G uncompressed
    public static final byte[] EC384_FP_G_Y = new byte[]{
            (byte) 0x36, (byte) 0x17, (byte) 0xDE, (byte) 0x4A,
            (byte) 0x96, (byte) 0x26, (byte) 0x2C, (byte) 0x6F,
            (byte) 0x5D, (byte) 0x9E, (byte) 0x98, (byte) 0xBF,
            (byte) 0x92, (byte) 0x92, (byte) 0xDC, (byte) 0x29,
            (byte) 0xF8, (byte) 0xF4, (byte) 0x1D, (byte) 0xBD,
            (byte) 0x28, (byte) 0x9A, (byte) 0x14, (byte) 0x7C,
            (byte) 0xE9, (byte) 0xDA, (byte) 0x31, (byte) 0x13,
            (byte) 0xB5, (byte) 0xF0, (byte) 0xB8, (byte) 0xC0,
            (byte) 0x0A, (byte) 0x60, (byte) 0xB1, (byte) 0xCE,
            (byte) 0x1D, (byte) 0x7E, (byte) 0x81, (byte) 0x9D,
            (byte) 0x7A, (byte) 0x43, (byte) 0x1D, (byte) 0x7C,
            (byte) 0x90, (byte) 0xEA, (byte) 0x0E, (byte) 0x5F};

    // Order of G
    public static final byte[] EC384_FP_R = new byte[]{
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xC7, (byte) 0x63, (byte) 0x4D, (byte) 0x81,
            (byte) 0xF4, (byte) 0x37, (byte) 0x2D, (byte) 0xDF,
            (byte) 0x58, (byte) 0x1A, (byte) 0x0D, (byte) 0xB2,
            (byte) 0x48, (byte) 0xB0, (byte) 0xA7, (byte) 0x7A,
            (byte) 0xEC, (byte) 0xEC, (byte) 0x19, (byte) 0x6A,
            (byte) 0xCC, (byte) 0xC5, (byte) 0x29, (byte) 0x73};
    // cofactor of G
    public static final short EC384_FP_K = 1;


    // secp521r1 from http://www.secg.org/sec2-v2.pdf
    public static final byte[] EC521_FP_P = new byte[]{
            (byte) 0x01, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};

    public static final byte[] EC521_FP_A = new byte[]{
            (byte) 0x01, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFC};

    public static final byte[] EC521_FP_B = new byte[]{
            (byte) 0x00, (byte) 0x51, (byte) 0x95, (byte) 0x3E,
            (byte) 0xB9, (byte) 0x61, (byte) 0x8E, (byte) 0x1C,
            (byte) 0x9A, (byte) 0x1F, (byte) 0x92, (byte) 0x9A,
            (byte) 0x21, (byte) 0xA0, (byte) 0xB6, (byte) 0x85,
            (byte) 0x40, (byte) 0xEE, (byte) 0xA2, (byte) 0xDA,
            (byte) 0x72, (byte) 0x5B, (byte) 0x99, (byte) 0xB3,
            (byte) 0x15, (byte) 0xF3, (byte) 0xB8, (byte) 0xB4,
            (byte) 0x89, (byte) 0x91, (byte) 0x8E, (byte) 0xF1,
            (byte) 0x09, (byte) 0xE1, (byte) 0x56, (byte) 0x19,
            (byte) 0x39, (byte) 0x51, (byte) 0xEC, (byte) 0x7E,
            (byte) 0x93, (byte) 0x7B, (byte) 0x16, (byte) 0x52,
            (byte) 0xC0, (byte) 0xBD, (byte) 0x3B, (byte) 0xB1,
            (byte) 0xBF, (byte) 0x07, (byte) 0x35, (byte) 0x73,
            (byte) 0xDF, (byte) 0x88, (byte) 0x3D, (byte) 0x2C,
            (byte) 0x34, (byte) 0xF1, (byte) 0xEF, (byte) 0x45,
            (byte) 0x1F, (byte) 0xD4, (byte) 0x6B, (byte) 0x50,
            (byte) 0x3F, (byte) 0x00};

    // G in compressed form / first part of ucompressed
    public static final byte[] EC521_FP_G_X = new byte[]{
            (byte) 0x00, (byte) 0xC6, (byte) 0x85, (byte) 0x8E,
            (byte) 0x06, (byte) 0xB7, (byte) 0x04, (byte) 0x04,
            (byte) 0xE9, (byte) 0xCD, (byte) 0x9E, (byte) 0x3E,
            (byte) 0xCB, (byte) 0x66, (byte) 0x23, (byte) 0x95,
            (byte) 0xB4, (byte) 0x42, (byte) 0x9C, (byte) 0x64,
            (byte) 0x81, (byte) 0x39, (byte) 0x05, (byte) 0x3F,
            (byte) 0xB5, (byte) 0x21, (byte) 0xF8, (byte) 0x28,
            (byte) 0xAF, (byte) 0x60, (byte) 0x6B, (byte) 0x4D,
            (byte) 0x3D, (byte) 0xBA, (byte) 0xA1, (byte) 0x4B,
            (byte) 0x5E, (byte) 0x77, (byte) 0xEF, (byte) 0xE7,
            (byte) 0x59, (byte) 0x28, (byte) 0xFE, (byte) 0x1D,
            (byte) 0xC1, (byte) 0x27, (byte) 0xA2, (byte) 0xFF,
            (byte) 0xA8, (byte) 0xDE, (byte) 0x33, (byte) 0x48,
            (byte) 0xB3, (byte) 0xC1, (byte) 0x85, (byte) 0x6A,
            (byte) 0x42, (byte) 0x9B, (byte) 0xF9, (byte) 0x7E,
            (byte) 0x7E, (byte) 0x31, (byte) 0xC2, (byte) 0xE5,
            (byte) 0xBD, (byte) 0x66};

    // second part of G uncompressed
    public static final byte[] EC521_FP_G_Y = new byte[]{
            (byte) 0x01, (byte) 0x18, (byte) 0x39, (byte) 0x29,
            (byte) 0x6A, (byte) 0x78, (byte) 0x9A, (byte) 0x3B,
            (byte) 0xC0, (byte) 0x04, (byte) 0x5C, (byte) 0x8A,
            (byte) 0x5F, (byte) 0xB4, (byte) 0x2C, (byte) 0x7D,
            (byte) 0x1B, (byte) 0xD9, (byte) 0x98, (byte) 0xF5,
            (byte) 0x44, (byte) 0x49, (byte) 0x57, (byte) 0x9B,
            (byte) 0x44, (byte) 0x68, (byte) 0x17, (byte) 0xAF,
            (byte) 0xBD, (byte) 0x17, (byte) 0x27, (byte) 0x3E,
            (byte) 0x66, (byte) 0x2C, (byte) 0x97, (byte) 0xEE,
            (byte) 0x72, (byte) 0x99, (byte) 0x5E, (byte) 0xF4,
            (byte) 0x26, (byte) 0x40, (byte) 0xC5, (byte) 0x50,
            (byte) 0xB9, (byte) 0x01, (byte) 0x3F, (byte) 0xAD,
            (byte) 0x07, (byte) 0x61, (byte) 0x35, (byte) 0x3C,
            (byte) 0x70, (byte) 0x86, (byte) 0xA2, (byte) 0x72,
            (byte) 0xC2, (byte) 0x40, (byte) 0x88, (byte) 0xBE,
            (byte) 0x94, (byte) 0x76, (byte) 0x9F, (byte) 0xD1,
            (byte) 0x66, (byte) 0x50};

    // Order of G
    public static final byte[] EC521_FP_R = new byte[]{
            (byte) 0x01, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFA,
            (byte) 0x51, (byte) 0x86, (byte) 0x87, (byte) 0x83,
            (byte) 0xBF, (byte) 0x2F, (byte) 0x96, (byte) 0x6B,
            (byte) 0x7F, (byte) 0xCC, (byte) 0x01, (byte) 0x48,
            (byte) 0xF7, (byte) 0x09, (byte) 0xA5, (byte) 0xD0,
            (byte) 0x3B, (byte) 0xB5, (byte) 0xC9, (byte) 0xB8,
            (byte) 0x89, (byte) 0x9C, (byte) 0x47, (byte) 0xAE,
            (byte) 0xBB, (byte) 0x6F, (byte) 0xB7, (byte) 0x1E,
            (byte) 0x91, (byte) 0x38, (byte) 0x64, (byte) 0x09};

    // cofactor of G
    public static final short EC521_FP_K = 1;

    //sect163r1 from http://www.secg.org/sec2-v2.pdf
    // [short i1, short i2, short i3] f = x^163 + x^i1 + x^i2 + x^i3 + 1
    public static final byte[] EC163_F2M_F = new byte[]{
            (byte) 0, (byte) 7, (byte) 0, (byte) 6, (byte) 0, (byte) 3
    };

    public static final byte[] EC163_F2M_A = new byte[]{
            (byte) 0x07, (byte) 0xB6, (byte) 0x88, (byte) 0x2C,
            (byte) 0xAA, (byte) 0xEF, (byte) 0xA8, (byte) 0x4F,
            (byte) 0x95, (byte) 0x54, (byte) 0xFF, (byte) 0x84,
            (byte) 0x28, (byte) 0xBD, (byte) 0x88, (byte) 0xE2,
            (byte) 0x46, (byte) 0xD2, (byte) 0x78, (byte) 0x2A,
            (byte) 0xE2
    };

    public static final byte[] EC163_F2M_B = new byte[]{
            (byte) 0x07, (byte) 0x13, (byte) 0x61, (byte) 0x2D,
            (byte) 0xCD, (byte) 0xDC, (byte) 0xB4, (byte) 0x0A,
            (byte) 0xAB, (byte) 0x94, (byte) 0x6B, (byte) 0xDA,
            (byte) 0x29, (byte) 0xCA, (byte) 0x91, (byte) 0xF7,
            (byte) 0x3A, (byte) 0xF9, (byte) 0x58, (byte) 0xAF,
            (byte) 0xD9
    };

    // G in compressed form / first part of ucompressed
    public static final byte[] EC163_F2M_G_X = new byte[]{
            (byte) 0x03, (byte) 0x69, (byte) 0x97, (byte) 0x96,
            (byte) 0x97, (byte) 0xAB, (byte) 0x43, (byte) 0x89,
            (byte) 0x77, (byte) 0x89, (byte) 0x56, (byte) 0x67,
            (byte) 0x89, (byte) 0x56, (byte) 0x7F, (byte) 0x78,
            (byte) 0x7A, (byte) 0x78, (byte) 0x76, (byte) 0xA6,
            (byte) 0x54
    };

    // second part of G uncompressed
    public static final byte[] EC163_F2M_G_Y = new byte[]{
            (byte) 0x00, (byte) 0x43, (byte) 0x5E, (byte) 0xDB,
            (byte) 0x42, (byte) 0xEF, (byte) 0xAF, (byte) 0xB2,
            (byte) 0x98, (byte) 0x9D, (byte) 0x51, (byte) 0xFE,
            (byte) 0xFC, (byte) 0xE3, (byte) 0xC8, (byte) 0x09,
            (byte) 0x88, (byte) 0xF4, (byte) 0x1F, (byte) 0xF8,
            (byte) 0x83
    };

    // order of G
    public static final byte[] EC163_F2M_R = new byte[]{
            (byte) 0x03, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0x48,
            (byte) 0xAA, (byte) 0xB6, (byte) 0x89, (byte) 0xC2,
            (byte) 0x9C, (byte) 0xA7, (byte) 0x10, (byte) 0x27,
            (byte) 0x9B
    };

    // cofactor of G
    public static final short EC163_F2M_K = 2;

    // getCorruptCurveParameter PARAMETER_CORRUPTION TYPES
    public static final short CORRUPTION_NONE = 0x01;
    public static final short CORRUPTION_FIXED = 0x02;
    public static final short CORRUPTION_FULLRANDOM = 0x03;
    public static final short CORRUPTION_ONEBYTERANDOM = 0x04;
    public static final short CORRUPTION_ZERO = 0x05;
    public static final short CORRUPTION_ONE = 0x06;

    // Supported embedded curves, getCurveParameter
    // SECP recommended curves over FP
    public static final byte CURVE_secp128r1 = 1;
    public static final byte CURVE_secp160r1 = 2;
    public static final byte CURVE_secp192r1 = 3;
    public static final byte CURVE_secp224r1 = 4;
    public static final byte CURVE_secp256r1 = 5;
    public static final byte CURVE_secp384r1 = 6;
    public static final byte CURVE_secp521r1 = 7;

    public static final byte FP_CURVES = 7;

    // SECP recommended curves over F2M
    public static final byte CURVE_sect163r1 = 8;
    public static final byte CURVE_sect233r1 = 9;
    public static final byte CURVE_sect283r1 = 10;
    public static final byte CURVE_sect409r1 = 11;
    public static final byte CURVE_sect571r1 = 12;

    public static final byte F2M_CURVES = 12;

    public static byte getCurve(short keyClass, short keyLength) {
        if (keyClass == KeyPair.ALG_EC_FP) {
            switch (keyLength) {
                case (short) 128:
                    return CURVE_secp128r1;
                case (short) 160:
                    return CURVE_secp160r1;
                case (short) 192:
                    return CURVE_secp192r1;
                case (short) 224:
                    return CURVE_secp224r1;
                case (short) 256:
                    return CURVE_secp256r1;
                case (short) 384:
                    return CURVE_secp384r1;
                case (short) 521:
                    return CURVE_secp521r1;
                default:
                    ISOException.throwIt(ISO7816.SW_FUNC_NOT_SUPPORTED);
            }
        } else if (keyClass == KeyPair.ALG_EC_F2M) {
            switch (keyLength) {
                case (short) 163:
                    return CURVE_sect163r1;
                case (short) 233:
                    return CURVE_sect233r1;
                case (short) 283:
                    return CURVE_sect283r1;
                case (short) 409:
                    return CURVE_sect409r1;
                case (short) 571:
                    return CURVE_sect571r1;
                default:
                    ISOException.throwIt(ISO7816.SW_FUNC_NOT_SUPPORTED);
            }
        } else {
            ISOException.throwIt(ISO7816.SW_FUNC_NOT_SUPPORTED);
        }
        return 0; //will not be reached
    }

    public static short getCurveParameter(byte curve, byte param, byte[] outputBuffer, short outputOffset) {
        byte alg = getCurveType(curve);
        switch (curve) {
            case CURVE_secp128r1: {
                EC_FP_P = EC128_FP_P;
                EC_A = EC128_FP_A;
                EC_B = EC128_FP_B;
                EC_G_X = EC128_FP_G_X;
                EC_G_Y = EC128_FP_G_Y;
                EC_R = EC128_FP_R;
                EC_K = EC128_FP_K;
                break;
            }
            case CURVE_secp160r1: {
                EC_FP_P = EC160_FP_P;
                EC_A = EC160_FP_A;
                EC_B = EC160_FP_B;
                EC_G_X = EC160_FP_G_X;
                EC_G_Y = EC160_FP_G_Y;
                EC_R = EC160_FP_R;
                EC_K = EC160_FP_K;
                break;
            }
            case CURVE_secp192r1: {
                EC_FP_P = EC192_FP_P;
                EC_A = EC192_FP_A;
                EC_B = EC192_FP_B;
                EC_G_X = EC192_FP_G_X;
                EC_G_Y = EC192_FP_G_Y;
                EC_R = EC192_FP_R;
                EC_K = EC192_FP_K;
                break;
            }
            case CURVE_secp224r1: {
                EC_FP_P = EC224_FP_P;
                EC_A = EC224_FP_A;
                EC_B = EC224_FP_B;
                EC_G_X = EC224_FP_G_X;
                EC_G_Y = EC224_FP_G_Y;
                EC_R = EC224_FP_R;
                EC_K = EC224_FP_K;
                break;
            }
            case CURVE_secp256r1: {
                EC_FP_P = EC256_FP_P;
                EC_A = EC256_FP_A;
                EC_B = EC256_FP_B;
                EC_G_X = EC256_FP_G_X;
                EC_G_Y = EC256_FP_G_Y;
                EC_R = EC256_FP_R;
                EC_K = EC256_FP_K;
                break;
            }
            case CURVE_secp384r1: {
                EC_FP_P = EC384_FP_P;
                EC_A = EC384_FP_A;
                EC_B = EC384_FP_B;
                EC_G_X = EC384_FP_G_X;
                EC_G_Y = EC384_FP_G_Y;
                EC_R = EC384_FP_R;
                EC_K = EC384_FP_K;
                break;
            }
            case CURVE_secp521r1: {
                EC_FP_P = EC521_FP_P;
                EC_A = EC521_FP_A;
                EC_B = EC521_FP_B;
                EC_G_X = EC521_FP_G_X;
                EC_G_Y = EC521_FP_G_Y;
                EC_R = EC521_FP_R;
                EC_K = EC521_FP_K;
                break;
            }
            case CURVE_sect163r1: {
                EC_F2M_F2M = EC163_F2M_F;
                EC_A = EC163_F2M_A;
                EC_B = EC163_F2M_B;
                EC_G_X = EC163_F2M_G_X;
                EC_G_Y = EC163_F2M_G_Y;
                EC_R = EC163_F2M_R;
                EC_K = EC163_F2M_K;
                break;
            }
            default:
                ISOException.throwIt(ISO7816.SW_FUNC_NOT_SUPPORTED);
        }
        short length = 0;
        switch (param) {
            case PARAMETER_FP:
                if (alg == KeyPair.ALG_EC_FP) {
                    length = Util.arrayCopyNonAtomic(outputBuffer, outputOffset, EC_FP_P, (short) 0, (short) EC_FP_P.length);
                }
                break;
            case PARAMETER_F2M:
                if (alg == KeyPair.ALG_EC_F2M) {
                    length = Util.arrayCopyNonAtomic(outputBuffer, outputOffset, EC_F2M_F2M, (short) 0, (short) EC_F2M_F2M.length);
                }
                break;
            case PARAMETER_A:
                length = Util.arrayCopyNonAtomic(outputBuffer, outputOffset, EC_A, (short) 0, (short) EC_A.length);
                break;
            case PARAMETER_B:
                length = Util.arrayCopyNonAtomic(outputBuffer, outputOffset, EC_B, (short) 0, (short) EC_B.length);
                break;
            case PARAMETER_G:
                length = decompressG(outputBuffer, outputOffset, EC_G_X, (short) 0, (short) EC_G_X.length, EC_G_Y, (short) 0, (short) EC_G_Y.length);
                break;
            case PARAMETER_R:
                length = Util.arrayCopyNonAtomic(outputBuffer, outputOffset, EC_R, (short) 0, (short) EC_R.length);
                break;
            case PARAMETER_K:
                length = 2;
                Util.setShort(outputBuffer, outputOffset, EC_K);
                break;
            default:
                length = -1;
        }
        return length;
    }

    public static short getCorruptCurveParameter(byte curve, byte param, byte[] outputBuffer, short outputOffset, short corruptionType) {
        short length = getCurveParameter(curve, param, outputBuffer, outputOffset);
        if (length <= 0) {
            return length;
        }
        switch (corruptionType) {
            case CORRUPTION_NONE:
                break;
            case CORRUPTION_FIXED:
                if (length >= 1) {
                    outputBuffer[outputOffset] = (byte) 0xcc;
                    outputBuffer[(short) (outputOffset + length - 1)] = (byte) 0xcc;
                }
                break;
            case CORRUPTION_FULLRANDOM:
                m_random.generateData(outputBuffer, outputOffset, length);
                break;
            case CORRUPTION_ONEBYTERANDOM:
                short first = Util.getShort(outputBuffer, (short) 0); // save first two bytes

                m_random.generateData(outputBuffer, (short) 0, (short) 2); // generate position
                short rngPos = Util.getShort(outputBuffer, (short) 0); // save generated position

                Util.setShort(outputBuffer, (short) 0, first); // restore first two bytes

                if (rngPos < 0) { // make positive
                    rngPos = (short) -rngPos;
                }
                rngPos %= length; // make < param length

                byte original = outputBuffer[rngPos];
                while (original != outputBuffer[rngPos]){
                    m_random.generateData(outputBuffer, rngPos, (short) 1);
                }
                break;
            case CORRUPTION_ZERO:
                Util.arrayFillNonAtomic(outputBuffer, outputOffset, length, (byte) 0);
                break;
            case CORRUPTION_ONE:
                Util.arrayFillNonAtomic(outputBuffer, outputOffset, length, (byte) 1);
                break;
            default:
                ISOException.throwIt(ISO7816.SW_FUNC_NOT_SUPPORTED);
        }
        return length;
    }

    public static byte getCurveType(byte curve) {
        return curve <= FP_CURVES ? KeyPair.ALG_EC_FP : KeyPair.ALG_EC_F2M;
    }

    private static short decompressG(byte[] outputBuffer, short outputOffset, byte[] gx, short gxOffset, short gxLength, byte[] gy, short gyOffset, short gyLength) {
        short size = 1;
        size += gxLength;
        size += gyLength;
        short offset = outputOffset;
        offset += 1;

        outputBuffer[offset] = 0x04;
        offset = Util.arrayCopyNonAtomic(gx, gxOffset, outputBuffer, offset, gxLength);
        Util.arrayCopyNonAtomic(gy, gyOffset, outputBuffer, offset, gyLength);
        return size;
    }
}
