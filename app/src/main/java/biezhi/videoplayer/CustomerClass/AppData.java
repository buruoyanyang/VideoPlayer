package biezhi.videoplayer.CustomerClass;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import biezhi.videoplayer.DataModel.HomeModel;

/**
 * biezhi.videoplayer.CustomerClass
 * author xiaofeng
 * 16/6/12
 * 包含app当前所有的数据信息，本地持久化
 * 启动的时候，删除本地类，再初始化
 */
public class AppData implements Parcelable {

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
    private String clickedCateId;






    protected AppData(Parcel in) {
    }

    public static final Creator<AppData> CREATOR = new Creator<AppData>() {
        @Override
        public AppData createFromParcel(Parcel in) {
            return new AppData(in);
        }

        @Override
        public AppData[] newArray(int size) {
            return new AppData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }


    public int getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public boolean isUserIsVIP() {
        return userIsVIP;
    }

    public void setUserIsVIP(boolean userIsVIP) {
        this.userIsVIP = userIsVIP;
    }

    public String getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(String userInfo) {
        this.userInfo = userInfo;
    }

    public boolean isExUser() {
        return isExUser;
    }

    public void setExUser(boolean exUser) {
        isExUser = exUser;
    }

    public List<String> getCateUrls() {
        return cateUrls;
    }

    public void setCateUrls(List<String> cateUrls) {
        this.cateUrls = cateUrls;
    }

    public List<Integer> getCateIds() {
        return cateIds;
    }

    public void setCateIds(List<Integer> cateIds) {
        this.cateIds = cateIds;
    }

    public List<String> getCateNames() {
        return cateNames;
    }

    public void setCateNames(List<String> cateNames) {
        this.cateNames = cateNames;
    }

    public List<HomeModel.HomeEntity> getHomeEntityList() {
        return homeEntityList;
    }

    public void setHomeEntityList(List<HomeModel.HomeEntity> homeEntityList) {
        this.homeEntityList = homeEntityList;
    }

    public String getWeixinId() {
        return weixinId;
    }

    public void setWeixinId(String weixinId) {
        this.weixinId = weixinId;
    }

    public String getWeixinBanner() {
        return weixinBanner;
    }

    public void setWeixinBanner(String weixinBanner) {
        this.weixinBanner = weixinBanner;
    }

    public String getClickedCateId() {
        return clickedCateId;
    }

    public void setClickedCateId(String clickedCateId) {
        this.clickedCateId = clickedCateId;
    }
}
