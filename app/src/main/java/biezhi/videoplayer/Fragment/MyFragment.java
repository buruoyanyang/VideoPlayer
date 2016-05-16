package biezhi.videoplayer.Fragment;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import biezhi.videoplayer.Data;
import biezhi.videoplayer.R;

/**
 * Created by xiaofeng on 16/5/12.
 */
public class MyFragment extends Fragment implements View.OnClickListener {
    Context superContext;
    LayoutInflater inflater;
    Data appData;
    RelativeLayout layout_cache;
    RelativeLayout layout_setting;
    RelativeLayout layout_update;
    RelativeLayout layout_about;
    ImageButton refresh_user;
    ImageView user_image;
    TextView login_tv;
    Button get_vip_bt;
    com.rey.material.widget.Button download;
    com.rey.material.widget.Button favorate;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contextView = inflater.inflate(R.layout.my_fragment, container, false);
        superContext = container.getContext();
        initClass(contextView);
        initUser();
        return contextView;
    }

    public void initClass(View contextView) {
        appData = (Data) superContext.getApplicationContext();
        layout_cache = (RelativeLayout) contextView.findViewById(R.id.my_cache_layout);
        layout_setting = (RelativeLayout) contextView.findViewById(R.id.my_setting_layout);
        layout_update = (RelativeLayout) contextView.findViewById(R.id.my_update_layout);
        layout_about = (RelativeLayout) contextView.findViewById(R.id.my_about_layout);
        TextView cache_tv = (TextView) layout_cache.findViewById(R.id.my_tv);
        TextView setting_tv = (TextView) layout_setting.findViewById(R.id.my_tv);
        TextView update_tv = (TextView) layout_update.findViewById(R.id.my_tv);
        TextView about_tv = (TextView) layout_about.findViewById(R.id.my_tv);
        cache_tv.setText("观看记录");
        setting_tv.setText("设置");
        update_tv.setText("检查更新");
        about_tv.setText("关于我们");
        user_image = (ImageView) contextView.findViewById(R.id.user_image);
        login_tv = (TextView) contextView.findViewById(R.id.my_login_tv);
        get_vip_bt = (Button) contextView.findViewById(R.id.my_vip_bt);
        refresh_user = (ImageButton) contextView.findViewById(R.id.my_refresh_login_icon);
        refresh_user.setOnClickListener(this);
        user_image.setOnClickListener(this);
        login_tv.setOnClickListener(this);
        get_vip_bt.setOnClickListener(this);
        download = (com.rey.material.widget.Button) contextView.findViewById(R.id.my_download_bt);
        favorate = (com.rey.material.widget.Button) contextView.findViewById(R.id.my_favorate_bt);
        download.setOnClickListener(this);
        favorate.setOnClickListener(this);
    }

    public void initUser() {
        if (!appData.getUserIsVIP()) {
            user_image.setImageResource(R.drawable.user_vip_icon);
            login_tv.setTextColor(Color.rgb(255, 225, 0));
        }
        if (!appData.getUserName().equals("")) {
            login_tv.setText(appData.getUserName());
            get_vip_bt.setText(R.string.click_to_logout);
            get_vip_bt.setCompoundDrawables(null, null, null, null);
        }
    }



    @Override
    public void onClick(View v) {
        if (v == layout_cache) {
            startActivityById(1);
        } else if (v == layout_about) {
            startActivityById(2);
        } else if (v == layout_setting) {
            startActivityById(3);
        } else if (v == layout_update) {
            startActivityById(4);
        } else if (v == refresh_user) {
            Animation animation = AnimationUtils.loadAnimation(superContext, R.anim.refresh);
            v.startAnimation(animation);
        } else if (v == login_tv) {
            if (appData.getUserName().equals("")) {
                //说明已经处于非登陆状态
                //跳转到登陆网页
                startActivityById(5);
            }
        } else if (v == get_vip_bt) {
            if (!appData.getUserName().equals("")) {
                //说明已经处于登陆状态
                appData.setUserName("");
                appData.setUserInfo("");
                appData.setUserIsVIP(false);
                appData.setUserPassword("");
            } else {
                //添加微信界面
                startActivityById(8);
            }
        } else if (v == download) {
            startActivityById(6);
        } else if (v == favorate) {
            startActivityById(7);
        }
    }

    public void startActivityById(int activityId) {

    }
}
