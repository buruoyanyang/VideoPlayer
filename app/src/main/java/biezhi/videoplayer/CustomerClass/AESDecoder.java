package biezhi.videoplayer.CustomerClass;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.util.Base64;


import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by xiaofeng on 16/5/6.
 */
public class AESDecoder {
    private static final byte[] ivBytes = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
    private final static String HEX = "1234567890123456";

    public static byte[] Decrypt(String sSrc, String sKey) throws Exception {

        byte[] keyBytes = sKey.getBytes("UTF-8");
        byte[] cipherData = null;
        IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        SecretKeySpec newKey = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
        byte[] encrypted1 = Base64.decode(sSrc, Base64.DEFAULT);//先用base64解密
        //byte[] decodedVal = Base64.decode(sSrc.getBytes("UTF-8"));
        try {
            cipherData = cipher.doFinal(encrypted1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cipherData;
    }

    @NonNull
    public static String encrypt(String key, String src) {
        try {
            byte[] rawKey = getRawKey(key.getBytes());
            byte[] result = encrypt(rawKey, src.getBytes());
            return toHex(result);
        } catch (Exception ex) {

            return "";
        }
    }

    @SuppressLint("TrulyRandom")
    private static byte[] getRawKey(byte[] seed) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom sr;
        // 在4.2以上版本中，SecureRandom获取方式发生了改变
        if (android.os.Build.VERSION.SDK_INT >= 17) {
            sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");
        } else {
            sr = SecureRandom.getInstance("SHA1PRNG");
        }
        sr.setSeed(seed);
        // 256 bits or 128 bits,192bits
        kgen.init(256, sr);
        SecretKey skey = kgen.generateKey();
        return skey.getEncoded();
    }

    private static byte[] encrypt(byte[] key, byte[] src) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        return cipher.doFinal(src);
    }

    @NonNull
    public static String toHex(String txt) {
        return toHex(txt.getBytes());
    }

    @NonNull
    public static String toHex(byte[] buf) {
        if (buf == null)
            return "";
        StringBuffer result = new StringBuffer(2 * buf.length);
        for (int i = 0; i < buf.length; i++) {
            appendHex(result, buf[i]);
        }
        return result.toString();
    }

    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
    }

}
