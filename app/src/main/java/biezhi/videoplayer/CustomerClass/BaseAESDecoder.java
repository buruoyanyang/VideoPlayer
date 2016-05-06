package biezhi.videoplayer.CustomerClass;

import java.io.IOException;

/**
 * Created by xiaofeng on 16/5/6.
 */
public class BaseAESDecoder {
    public static String IO_FAIL = "1";
    public static String DECODER_FAIL = "2";
    public static String UNKNOW_ERROR = "5";
    public static String decoder(String key,String ciphertext)
    {
        String result = "";
        try {
            byte[] tempResult = AESDecoder.Decrypt(key, ciphertext);
            if (tempResult != null) {
                return new String(tempResult, "UTF-8");
            }
        } catch (IOException e) {
            //io读写错误
            e.printStackTrace();
            return IO_FAIL;
        } catch (Exception e) {
            //解密失败
            e.printStackTrace();
            return DECODER_FAIL;
        }
        //未知错误
        return UNKNOW_ERROR;
    }
}
