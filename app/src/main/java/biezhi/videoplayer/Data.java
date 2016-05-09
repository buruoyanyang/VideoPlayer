package biezhi.videoplayer;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import biezhi.videoplayer.DataModel.HomeModel;

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
    public String appId = "74";
    public String appVersion = "1.0";

    //频道信息
    private List<String> cateUrls = new ArrayList<>();
    private List<Integer> cateIds = new ArrayList<>();
    private List<String> cateNames = new ArrayList<>();



    //首页推荐信息
    private List<HomeModel.HomeEntity> homeEntityList = new ArrayList<>();


    //微信
    private String weixinId = "";
    private String weixinBanner = "";


    public List<HomeModel.HomeEntity> getHomeEntityList() {
        return homeEntityList;
    }

    public void setHomeEntityList(List<HomeModel.HomeEntity> homeEntityList) {
        this.homeEntityList = homeEntityList;
    }

    public void setWeixinBanner(String weixinBanner) {
        this.weixinBanner = weixinBanner;
    }

    public String getWeixinBanner() {
        return weixinBanner;
    }

    public String getWeixinId() {
        return weixinId;
    }

    public void setWeixinId(String weixinId) {
        this.weixinId = weixinId;
    }

    public void setCateUrls(List<String> cateUrls) {
        this.cateUrls = cateUrls;
    }

    public List<String> getCateUrls() {
        return this.cateUrls;
    }

    public void setCateIds(List<Integer> cateIds) {
        this.cateIds = cateIds;
    }

    public List<Integer> getCateIds() {
        return this.cateIds;
    }

    public void setCateNames(List<String> cateNames) {
        this.cateNames = cateNames;
    }

    public List<String> getCateNames() {
        return this.cateNames;
    }


    public void setExUser(boolean isExUser) {
        this.isExUser = isExUser;
    }

    public boolean getExUser() {
        return this.isExUser;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceId() {
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
