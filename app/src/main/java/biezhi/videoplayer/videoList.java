package biezhi.videoplayer;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jaeger.library.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import biezhi.videoplayer.CustomerClass.BaseHttpClient;
import biezhi.videoplayer.CustomerClass.BaseLoadImage;
import biezhi.videoplayer.CustomerClass.HttpErrorCatch;
import biezhi.videoplayer.DataModel.VideoModel;
import biezhi.videoplayer.MessageBox.VideoListMessage;
import biezhi.videoplayer.MessageBox.VideoListOKMessage;
import in.srain.cube.views.GridViewWithHeaderAndFooter;
import in.srain.cube.views.loadmore.LoadMoreContainer;
import in.srain.cube.views.loadmore.LoadMoreGridViewContainer;
import in.srain.cube.views.loadmore.LoadMoreHandler;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;


public class videoList extends AppCompatActivity {


    LoadMoreGridViewContainer loadMoreGridViewContainer;
    GridViewWithHeaderAndFooter mGridView;
    PtrClassicFrameLayout ptrRefresh;
    int currentPageNum = 0;
    List<VideoModel> videosList = new ArrayList<>();
    List<VideoModel.ChannelsEntity> videoChanels = new ArrayList<>();
    List<VideoModel.ContentEntity> videoContents = new ArrayList<>();
    Data appData;
    boolean isLoadMore = false;
    boolean hasMore = true;
    int loadedVideoCount = 0;
    private LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        StatusBarUtil.setTranslucent(this, 100);
        initClass();
        initGridList();
    }

    private void initClass() {
        appData = (Data) this.getApplicationContext();
        EventBus.getDefault().register(this);
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //首先进来应该直接加载一次
        loadMoreGridViewContainer = (LoadMoreGridViewContainer) findViewById(R.id.load_more_grid_view_container);
        mGridView = (GridViewWithHeaderAndFooter) findViewById(R.id.load_more_grid_view);
        ptrRefresh = (PtrClassicFrameLayout) findViewById(R.id.video_list_refresh);
        loadMoreGridViewContainer.useDefaultHeader();
        loadMoreGridViewContainer.setAutoLoadMore(true);
        loadMoreGridViewContainer.setLoadMoreHandler(new loadMoreHandler());
        ptrRefresh.setLastUpdateTimeRelateObject(this);
        ptrRefresh.setPtrHandler(new refreshHandler());
        ptrRefresh.disableWhenHorizontalMove(false);
        EventBus.getDefault().post(new VideoListMessage());
    }

    private void initGridList() {

    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void getVideoList(VideoListMessage message) {
        String url = "http://www.biezhi360.cn:99/Videos.aspx?page=" + currentPageNum + "&cat=" +
                appData.getClickedCateId() + "&size=" + getString(R.string.default_page_num) +
                "&order=" + getString(R.string.default_order) + "&district=" +
                getString(R.string.default_district) + "&kind=" + getString(R.string.default_kind) +
                "&appid=" + appData.appId + "&version=" + appData.appVersion;
        String json = BaseHttpClient.getInfoWithData(url, getString(R.string.aesKey));
        if (json.length() < 10) {
            HttpErrorCatch errorCatch = new HttpErrorCatch(json);
        } else {
            Gson gson = new Gson();
            VideoModel videos = gson.fromJson(json, VideoModel.class);
            if (isLoadMore) {
                if (videosList.size() > 4) {
                    loadedVideoCount = videos.getContent().size();
                    videosList.clear();
                    videosList.add(videos);
                } else {
                    loadedVideoCount += videos.getContent().size();
                    videosList.add(videos);
                }
                hasMore = Boolean.valueOf(videos.getHas_next());
            } else {
                loadedVideoCount = videos.getContent().size();
                videosList.clear();
                videosList.add(videos);
            }
            //处理完毕，通知准备修改UI

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void videoListOK(VideoListOKMessage message) {

    }

    class GridAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return loadedVideoCount;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.video_item_adapter, parent, false);
            }
            ImageView imageView = ViewHolder.get(convertView, R.id.cate_image);
            TextView textView = ViewHolder.get(convertView, R.id.cate_name);



        }
    }

    static class ViewHolder {
        public static <T extends View> T get(View view, int id) {
            SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
            if (viewHolder == null) {
                viewHolder = new SparseArray<>();
                view.setTag(viewHolder);
            }
            View childView = viewHolder.get(id);
            if (childView == null) {
                childView = view.findViewById(id);
                viewHolder.put(id, childView);
            }
            return (T) childView;
        }
    }


    class loadMoreHandler implements LoadMoreHandler {

        @Override
        public void onLoadMore(LoadMoreContainer loadMoreContainer) {

        }
    }


    class refreshHandler implements PtrHandler {

        @Override
        public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
            return false;
        }

        @Override
        public void onRefreshBegin(PtrFrameLayout frame) {

        }
    }

}
