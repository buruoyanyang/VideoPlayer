package biezhi.videoplayer;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.rey.material.widget.ImageButton;

import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.IndicatorViewPager.IndicatorFragmentPagerAdapter;
import com.shizhefei.view.viewpager.SViewPager;

import java.net.HttpURLConnection;
import java.util.HashMap;

import biezhi.videoplayer.Fragment.MyFragment;
import biezhi.videoplayer.Fragment.NoNetFragment;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private String[] tabName = new String[]{"推荐", "频道", "我的"};
    Data appData;
    public ImageButton titleSearch;
    public ImageButton titleDownload;
    public ImageButton titleHistory;
    private IndicatorViewPager indicatorViewPager;
    private int lastView = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initClass();
        initTab();

    }

    private void initClass() {
        appData = (Data) this.getApplicationContext();
        //先处理所有的按钮事件
        titleSearch = (ImageButton) findViewById(R.id.title_app_search);
        titleDownload = (ImageButton) findViewById(R.id.title_app_download);
        titleHistory = (ImageButton) findViewById(R.id.title_app_history);
        assert titleHistory != null;
        titleHistory.setOnClickListener(this);
        titleDownload.setOnClickListener(this);
        titleSearch.setOnClickListener(this);
    }

    private void initTab() {
        SViewPager pager = (SViewPager) findViewById(R.id.pager);
        Indicator indicator = (Indicator) findViewById(R.id.indicator);
        assert pager != null;
        indicatorViewPager = new IndicatorViewPager(indicator, pager);
        indicatorViewPager.setIndicatorOnTransitionListener(new Indicator.OnTransitionListener() {
            @Override
            public void onTransition(View selectItemView, int select, float preSelect) {
                Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.refresh);
                if (lastView != select && preSelect > 0.5)
                {

                }
                if (select == 1)
                {
//                    selectItemView.startAnimation(animation);
                }
                else if (select == 2)
                {
                    selectItemView.clearAnimation();
                }

            }
        });
        indicatorViewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager()));
        //允许滑动
        pager.setCanScroll(true);
        //设置不重新加载页数
        pager.setOffscreenPageLimit(3);
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
//            textView.setText(tabName[position]);
//            textView.setCompoundDrawablesWithIntrinsicBounds(0, tabIcons[position], 0, 0);
            textView.setImageResource(tabIcons[position]);
            return convertView;
        }

        @Override
        public Fragment getFragmentForPage(int i) {
            if (i == 0) {
                return new NoNetFragment();
            } else if (i == 2) {
                return new MyFragment();
            } else {
                return new NoNetFragment();
            }

        }
    }


    @Override
    public void onClick(View v) {

    }
}
