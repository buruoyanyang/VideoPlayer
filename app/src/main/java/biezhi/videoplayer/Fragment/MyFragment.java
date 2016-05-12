package biezhi.videoplayer.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rey.material.widget.ImageButton;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contextView = inflater.inflate(R.layout.my_fragment, container, false);
        superContext = container.getContext();
        initClass(contextView);
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
        ImageView user_image = (ImageView) contextView.findViewById(R.id.user_image);
        TextView login_tv = (TextView) contextView.findViewById(R.id.my_login_tv);
        Button get_vip_bt = (Button) contextView.findViewById(R.id.my_vip_bt);
        ImageButton refresh_user = (ImageButton) contextView.findViewById(R.id.my_refresh_login_icon);\
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
        }

    }

    public void startActivityById(int activityId)
    {
        
    }
}
