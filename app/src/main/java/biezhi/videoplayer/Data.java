package biezhi.videoplayer;

import android.app.Application;

/**
 * Created by xiaofeng on 16/5/6.
 */
public class Data extends Application {
    //设备信息
    private int screenHeight = 0;
    private int screenWidth = 0;
    private String deviceId = "";

    //用户信息
    private String userName = "";
    private String userPassword = "";
    private boolean userIsVIP = false;
    private String userInfo = "";
    //异常账户
    private boolean isExUser = false;

    //当前版本信息
    public String appId = getString(R.string.appId);
    public String appVersion = getString(R.string.appVersion);

    public void setExUser(boolean isExUser)
    {
        this.isExUser = isExUser;
    }

    public boolean getExUser()
    {
        return this.isExUser;
    }

    public void setDeviceId(String deviceId)
    {
        this.deviceId = deviceId;
    }

    public String getDeviceId()
    {
        return this.deviceId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserPassword() {
        return this.userPassword;
    }

    public void setUserIsVIP(boolean userIsVIP) {
        this.userIsVIP = userIsVIP;
    }

    public boolean getUserIsVIP() {
        return this.userIsVIP;
    }

    public void setUserInfo(String userInfo) {
        this.userInfo = userInfo;
    }

    public String getUserInfo() {
        return this.userInfo;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    public int getScreenHeight() {
        return this.screenHeight;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public int getScreenWidth() {
        return this.screenWidth;
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }

}
