package biezhi.videoplayer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import com.jaeger.library.StatusBarUtil;

import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


import biezhi.videoplayer.CustomerClass.AESDecoder;
import biezhi.videoplayer.CustomerClass.BaseAESDecoder;
import biezhi.videoplayer.CustomerClass.BaseHttpClient;
import biezhi.videoplayer.DataModel.CateModel;
import biezhi.videoplayer.DataModel.HomeModel;
import biezhi.videoplayer.DataModel.LoginModel;
import biezhi.videoplayer.MessageBox.ChannelBox;
import biezhi.videoplayer.MessageBox.CheckUserMessage;

//需要完成的操作
//判断是否是首次开启，首次开启要写文件
//判断账号是否异常，异常要清空登陆信息
//请求分类信息，只保存分类图片的地址信息（用map保存）


public class InitActivity extends AppCompatActivity {

    Data appData;
    private boolean isFirstInit = false;
    private boolean isNoNet = false;
    List<String> urlList = new ArrayList<>();
    List<Integer> cateIdList = new ArrayList<>();
    List<String> nameList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //直接在这里获取首次运行状态
        getFirstInfo();
        setContentView(R.layout.activity_init);
        StatusBarUtil.setTranslucent(this, 0);
        EventBus.getDefault().register(this.getApplicationContext());
        initClass();
    }

    private void initClass() {
        if (isFirstInit) {
            AVLoadingIndicatorView avLoadingIndicatorView = (AVLoadingIndicatorView) findViewById(R.id.av_loading);
            assert avLoadingIndicatorView != null;
            avLoadingIndicatorView.setVisibility(View.VISIBLE);
            TextView textView = (TextView) findViewById(R.id.tv_setting);
            assert textView != null;
            textView.setVisibility(View.VISIBLE);
        }
        getDeviceId();
        //先判断网络状态
        appData = (Data) this.getApplicationContext();
        if (!checkNetWork()) {
            //以离线状态启动
            AlertDialog mDialog = new AlertDialog.Builder(this.getApplicationContext()).create();
            mDialog.setTitle(R.string.noNetWork);
            mDialog.setMessage("无网络链接，现在就去打开WiFi？");
            mDialog.setButton(1, "好", buttonClick);
            mDialog.setButton(2, "离线启动", buttonClick);
        }
        //请求网络
        EventBus.getDefault().post(new ChannelBox());
        getFromSDCard();
    }

    /**
     * 网络信息弹窗提示
     */
    DialogInterface.OnClickListener buttonClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (which == 1) {
                startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
            } else {
                //离线启动
                isNoNet = true;
                dialog.dismiss();
            }
        }
    };

    private void getDeviceId() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (!tm.getDeviceId().equals("")) {
            appData.setDeviceId(tm.getDeviceId());
        }
    }

    private boolean checkNetWork() {
        //检查网络信息，允许以离线状态启动
        boolean netInfo;
        ConnectivityManager connectivityManage = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo.State mobile = connectivityManage.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        NetworkInfo.State wifi = connectivityManage.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        if (mobile == NetworkInfo.State.CONNECTED && wifi == NetworkInfo.State.DISCONNECTED) {
            netInfo = true;
            Toast.makeText(this.getApplicationContext(), R.string.netInfoWarning, Toast.LENGTH_SHORT).show();
        } else {
            netInfo = wifi == NetworkInfo.State.CONNECTED;
        }
        return netInfo;
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    private void getChannelFromServer(ChannelBox channelBox) {
        //异步请求网络
        String result = BaseHttpClient.getInfoWithData("http://115.29.190.54:99/category.aspx?appid=" + appData.appId + "&version=" + appData.appVersion, getString(R.string.aesKey));
        if (result.length() < 100) {
            switch (result) {
                case "0":
                    //服务器连接失败
                    break;
                case "1":
                    //io读写错误
                    break;
                case "2":
                    //解密错误
                    break;
                case "5":
                    //未知错误
                    break;
            }
        } else {
            Gson gson = new Gson();
            CateModel cateModel = gson.fromJson(result, CateModel.class);
            List<CateModel.ContentEntity> contentEntities = cateModel.getContent();
            for (CateModel.ContentEntity content : contentEntities) {
                urlList.add(content.getCover());
                cateIdList.add(content.getCateId());
                nameList.add(content.getName());
                //不加载图片
            }
            //通知频道获取完成
        }
    }

    private void getFromSDCard() {
        File appDir = new File(Environment.getExternalStorageDirectory(), "BieZhi");
        if (appDir.exists()) {
            File file = new File(appDir, "UI");
            if (file.exists()) {
                try {
                    InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), "UTF-8");
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String str = "";
                    String mineTypeLine;
                    while ((mineTypeLine = bufferedReader.readLine()) != null) {
                        str = str + mineTypeLine;
                    }
                    appData.setUserInfo(str);
                    JSONObject jsonObject = new JSONObject(str);
                    String responseData = jsonObject.optString("responseData");

                    //解密
                    responseData = BaseAESDecoder.decoder(getString(R.string.aesKey), responseData);
                    if (responseData.length() > 200) {
                        responseData = responseData.split("\\},\\{")[0];
                        responseData = responseData + "}";
                    }
                    Gson gson = new Gson();
                    LoginModel loginModel = gson.fromJson(responseData, LoginModel.class);
                    String tel = loginModel.getTel();
                    String password = loginModel.getPassword();
                    boolean vip = loginModel.isVip();
                    appData.setUserName(tel);
                    appData.setUserPassword(password);
                    appData.setUserIsVIP(vip);
                    //获取信息完毕，验证账号有效
                    EventBus.getDefault().post(new CheckUserMessage());

                } catch (UnsupportedEncodingException | FileNotFoundException ignored) {

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    private void checkUser(CheckUserMessage checkUserMessage) {
        if (!appData.getUserName().equals("")) {
            //请求网络
            String result = BaseHttpClient.getInfoWithData("http://115.29.190.54:12345/mLogin.aspx?tel=" + appData.getUserName() + "&password=" + appData.getUserPassword() + "&idfa=" + appData.getDeviceId(), getString(R.string.aesKey));
            if (result.length() < 10) {
                //账号失效或者服务器异常
                appData.setUserIsVIP(false);
                appData.setUserInfo("");
                appData.setUserName("");
                appData.setUserPassword("");
                appData.setExUser(true);
            }
        }
    }

    private void getFirstInfo() {
        File appDir = new File(Environment.getExternalStorageDirectory(), "BieZhi");
        isFirstInit = !appDir.exists();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this.getApplicationContext());
    }


}
