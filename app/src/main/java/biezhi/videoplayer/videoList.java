package biezhi.videoplayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jaeger.library.StatusBarUtil;

import in.srain.cube.views.GridViewWithHeaderAndFooter;
import in.srain.cube.views.loadmore.LoadMoreContainer;
import in.srain.cube.views.loadmore.LoadMoreGridViewContainer;
import in.srain.cube.views.loadmore.LoadMoreHandler;



public class videoList extends AppCompatActivity {


    LoadMoreGridViewContainer loadMoreGridViewContainer;
    GridViewWithHeaderAndFooter mGridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        StatusBarUtil.setTranslucent(this, 100);
        initClass();
    }

    private void initClass()
    {
        loadMoreGridViewContainer = (LoadMoreGridViewContainer)findViewById(R.id.load_more_grid_view_container);
        mGridView = (GridViewWithHeaderAndFooter)findViewById(R.id.load_more_grid_view);
        loadMoreGridViewContainer.useDefaultHeader();
        loadMoreGridViewContainer.setAutoLoadMore(true);


    }
}
