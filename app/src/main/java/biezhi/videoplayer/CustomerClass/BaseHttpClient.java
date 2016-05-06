package biezhi.videoplayer.CustomerClass;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by xiaofeng on 16/5/6.
 */

/**
错误码：
    0：服务器掉线
    1：读写失败
    2：解密失败
    5：未知错误
**/
public class BaseHttpClient {
    public static boolean serverisonline = true;
    public static String SERVER_IS_TIMEOUT = "0";
    public static String IO_FAIL = "1";
    public static String UNKNOW_ERROR = "5";
    public static String getInfoBase(String url)
    {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request
                .Builder()
                .url(url)
                .build();
        Response response;
        try {
            response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                serverisonline = false;
                return SERVER_IS_TIMEOUT;
            } else {
                return response.body().string();
            }
        }
        catch (Exception ignored)
        {
            return UNKNOW_ERROR;
        }
    }

    public static String getInfoWithAES(String url,String key)
    {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request
                .Builder()
                .url(url)
                .build();
        Response response;
        try
        {
            response = client.newCall(request).execute();
            if (!response.isSuccessful())
            {
                serverisonline = false;
                return SERVER_IS_TIMEOUT;
            }
            else
            {
                //解密操作
                return BaseAESDecoder.decoder(key,response.body().string());
            }
        } catch (IOException e) {
            //读写失败
            return IO_FAIL;
        }
    }

    public static String getInfoWithData(String url,String key)
    {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request
                .Builder()
                .url(url)
                .build();
        Response response;
        try
        {
            response = client.newCall(request).execute();
            if (!response.isSuccessful())
            {
                serverisonline = false;
                return SERVER_IS_TIMEOUT;
            }
            else
            {
                //先进行json解析
                Gson gson = new Gson();
                Data resultData = gson.fromJson(response.body().toString(), Data.class);
                return BaseAESDecoder.decoder(key,resultData.data);
            }
        } catch (IOException e) {
            return IO_FAIL;
        } catch (Exception e) {
            return UNKNOW_ERROR;
        }
    }
    static class Data {
        String data;
    }
}
