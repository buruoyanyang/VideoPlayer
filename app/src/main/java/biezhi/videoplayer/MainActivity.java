package biezhi.videoplayer;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.rey.material.widget.ImageButton;
import com.rey.material.widget.TabPageIndicator;

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
        FragmentPagerAdapter adapter = new TabPageIndicatorAdapter(getSupportFragmentManager());
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        assert pager != null;
        pager.setAdapter(adapter);

        //实例化TabPageIndicator然后设置ViewPager与之关联
        TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);
        assert indicator != null;
        indicator.setViewPager(pager);
        //如果我们要对ViewPager设置监听，用indicator设置就行了
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
//                Toast.makeText(getApplicationContext(), TITLE[arg0], Toast.LENGTH_SHORT).show();
                if (arg0 == 2) {
                    titleSearch.setVisibility(View.INVISIBLE);
                    titleSearch.startAnimation(AnimationUtils.loadAnimation(MainActivity.this,R.anim.roll_out));
                    titleDownload.setVisibility(View.INVISIBLE);
                    titleDownload.startAnimation(AnimationUtils.loadAnimation(MainActivity.this,R.anim.roll_out));
                    titleHistory.setVisibility(View.INVISIBLE);
                    titleHistory.startAnimation(AnimationUtils.loadAnimation(MainActivity.this,R.anim.roll_out));
                } else if (arg0 == 1) {
                    if (titleSearch.getVisibility() != View.VISIBLE) {
                        titleSearch.setVisibility(View.VISIBLE);
                        titleSearch.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.roll_in));
                        titleDownload.setVisibility(View.VISIBLE);
                        titleDownload.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.roll_in));
                        titleHistory.setVisibility(View.VISIBLE);
                        titleHistory.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.roll_in));
                    }
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

    }

    /**
     * ViewPager适配器
     */
    class TabPageIndicatorAdapter extends FragmentPagerAdapter {
        public TabPageIndicatorAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            //需要判断下是否是联网状态
            if (appData.getCateUrls().size() <= 0) {
                //非联网状态
                return new NoNetFragment();
            } else {
                //联网状态
                if (position == 1) {
                    return new CateListFragment();
                } else if (position == 0) {
                    ArrayList<String> hotIdList = new ArrayList<>();
                    ArrayList<String> hotNameList = new ArrayList<>();
                    Fragment fragment = new RecomListFragment();
                    if (appData.getHomeEntityList().size() != 0) {
                        for (int i = 0; i < appData.getHomeEntityList().size(); i++) {
                            hotIdList.add(String.valueOf(appData.getHomeEntityList().get(i).getId()));
                            hotNameList.add(String.valueOf(appData.getHomeEntityList().get(i).getName()));
                        }
                    }
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("hotIds", hotIdList);
                    bundle.putStringArrayList("hotNames", hotNameList);
                    fragment.setArguments(bundle);
                    return fragment;
                } else {
                    // todo 返回我的Fragment
                    //同时隐藏按钮
                    return new MyFragment();
                }
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabName[position % tabName.length];
        }

        @Override
        public int getCount() {
            return tabName.length;
        }

    }

    @Override
    public void onClick(View v) {

    }
}
