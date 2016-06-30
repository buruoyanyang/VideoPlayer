package biezhi.videoplayer;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.rey.material.widget.ImageButton;

import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.IndicatorViewPager.IndicatorFragmentPagerAdapter;
import com.shizhefei.view.viewpager.SViewPager;

import java.util.ArrayList;

import biezhi.videoplayer.Fragment.CateListFragment;
import biezhi.videoplayer.Fragment.MyFragment;
import biezhi.videoplayer.Fragment.NoNetFragment;
import biezhi.videoplayer.Fragment.RecomListFragment;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private String[] tabName = new String[]{"推荐", "频道", "我的"};
    Data appData;
    public ImageButton titleSearch;
    public ImageButton titleDownload;
    public ImageButton titleHistory;
    private boolean isOnLine = true;
    RelativeLayout titleBarRl;
    int[] location = new int[2];
    int[] rlMargin = new int[4];
    RelativeLayout.LayoutParams oldLP;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        steepStatusBar();
        isOnLine = checkNet();
        initClass();
        initTab();

    }

    private void steepStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    private void initClass() {
        appData = (Data) this.getApplicationContext();
        //先处理所有的按钮事件
        titleSearch = (ImageButton) findViewById(R.id.title_app_search);
        titleDownload = (ImageButton) findViewById(R.id.title_app_download);
        titleHistory = (ImageButton) findViewById(R.id.title_app_history);
        titleBarRl = (RelativeLayout) findViewById(R.id.title_bar);
        getLocation();
        assert titleHistory != null;
        titleHistory.setOnClickListener(this);
        titleDownload.setOnClickListener(this);
        titleSearch.setOnClickListener(this);
    }

    private void getLocation() {
        titleBarRl.getLocationOnScreen(location);
        rlMargin[0] = titleBarRl.getLeft();
        rlMargin[1] = titleBarRl.getRight();
        rlMargin[2] = titleBarRl.getTop();
        rlMargin[3] = titleBarRl.getBottom();

    }

    private boolean checkNet() {
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

    private void initTab() {
        SViewPager pager = (SViewPager) findViewById(R.id.pager);
        Indicator indicator = (Indicator) findViewById(R.id.indicator);
        assert pager != null;
        IndicatorViewPager indicatorViewPager = new IndicatorViewPager(indicator, pager);
        indicatorViewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager()));
        //允许滑动
        pager.setCanScroll(true);
        //设置不重新加载页数
        pager.setOffscreenPageLimit(3);
//        indicatorViewPager.setOnIndicatorPageChangeListener(new IndicatorViewPager.OnIndicatorPageChangeListener() {
//            @Override
//            public void onIndicatorPageChange(int i, int i1) {
//                Log.d("i",String.valueOf(i));
//                Log.d("i1",String.valueOf(i1));
//                if (i1 == 2)
//                {
//                    Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.video_title_out);
//                    titleBarRl.startAnimation(animation);
//                    titleBarRl.setVisibility(View.GONE);
//
//                }
//                else
//                {
//                    Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.video_title_in);
//                    titleBarRl.startAnimation(animation);
//                    titleBarRl.setVisibility(View.VISIBLE);
//                }
//            }
//        });
//        indicatorViewPager.setIndicatorOnTransitionListener(new Indicator.OnTransitionListener() {
//            @Override
//            public void onTransition(View view, int i, float v) {
//                if(rlMargin[1] + rlMargin[3] == 0) {
//                    getLocation();
//                }
//                if (i == 2 && v!= 0.0) {
//                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) titleBarRl.getLayoutParams();
//                    layoutParams.setMargins(0,0,rlMargin[1],(int)(rlMargin[3] * v));
//                    titleBarRl.requestLayout();
//                }
//            }
//        });
    }

    private class FragmentAdapter extends IndicatorFragmentPagerAdapter {

        private int[] tabIcons =
                {
                        R.drawable.maintab_recome_selector,
                        R.drawable.maintab_chanel_selector,
                        R.drawable.maintab_my_selector
                };
        private LayoutInflater inflater;
        public FragmentAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            inflater = LayoutInflater.from(getApplicationContext());
        }

        @Override
        public int getCount() {
            return tabIcons.length;
        }

        @Override
        public View getViewForTab(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.tab_layout, container, false);
            }
            ImageView textView = (ImageView) convertView;
            textView.setImageResource(tabIcons[position]);
            if(position == 2)
            {
                titleBarRl.setVisibility(View.GONE);
            }
            else
            {
                titleBarRl.setVisibility(View.VISIBLE);
            }
            return convertView;
        }

        @Override
        public Fragment getFragmentForPage(int position) {
            if (!isOnLine) {
                return new NoNetFragment();
            } else {
                if (position == 0) {
                    //处理home数据
                    ArrayList<String> hotIdList = new ArrayList<>();
                    ArrayList<String> hotNameList = new ArrayList<>();
                    if (appData.getHomeEntityList().size() > 0) {
                        for (int i = 0; i < appData.getHomeEntityList().size(); i++) {
                            hotIdList.add(String.valueOf(appData.getHomeEntityList().get(i).getId()));
                            hotNameList.add(String.valueOf(appData.getHomeEntityList().get(i).getName()));
                        }
                    }
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("hotIds", hotIdList);
                    bundle.putStringArrayList("hotNames", hotNameList);
                    Fragment fragment = new RecomListFragment();
                    fragment.setArguments(bundle);
                    return fragment;
                } else if (position == 2) {
                    return new MyFragment();
                } else {
                    return new CateListFragment();
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(MainActivity.this, videoPlay.class));

    }
}
