
package cn.core.mobile.library.tools;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5类
 * 
 * @author Richard.Ma
 */
public class MD5Utils {
    private static final String HASH_ALGORITHM = "MD5";
    /** 10 digits + 26letters */
    private static final int    RADIX          = 36;

    public static String Md5(byte[] bytes) {
        byte[] md5 = getMD5(bytes);
        BigInteger bi = new BigInteger(md5).abs();
        return bi.toString(RADIX);
    }

    private static byte[] getMD5(byte[] data) {
        byte[] hash = null;
        try {
            MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
            digest.update(data);
            hash = digest.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return hash;
    }
}
