package biezhi.videoplayer.Fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import biezhi.videoplayer.CustomerClass.BaseHttpClient;
import biezhi.videoplayer.CustomerClass.HttpErrorCatch;
import biezhi.videoplayer.Data;
import biezhi.videoplayer.DataModel.RecomModel;
import biezhi.videoplayer.MessageBox.HotMessage;
import biezhi.videoplayer.MessageBox.RecomAdapterMessage;
import biezhi.videoplayer.R;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by xiaofeng on 16/5/9.
 */
public class RecomListFragment extends Fragment {
    Context superContext;
    ArrayList<String> hotIdList;
    ArrayList<String> hotNameList;
    Data appData;
    ListView recomList;
    PtrClassicFrameLayout refresh;
    LayoutInflater inflater;
    boolean isRefresh = false;
    List<String> jsonList = new ArrayList<>();
    BitmapDrawable holdBD;
    Bitmap holdBM;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.recom_list_fragment, container, false);
        superContext = container.getContext();
        EventBus.getDefault().register(this);
        Bundle bundle = getArguments();
        hotIdList = bundle.getStringArrayList("hotIds");
        hotNameList = bundle.getStringArrayList("hotNames");
        initClass(contentView);
        EventBus.getDefault().post(new HotMessage());
        return contentView;
    }

    private void initClass(View contentView) {
        appData = (Data) superContext.getApplicationContext();
        recomList = (ListView) contentView.findViewById(R.id.recom_list);
        refresh = (PtrClassicFrameLayout) contentView.findViewById(R.id.recom_refresh);
        inflater = (LayoutInflater) superContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        refresh.setLastUpdateTimeRelateObject(superContext);
        refresh.disableWhenHorizontalMove(false);
        refresh.setPtrHandler(new refreshHandler());
        holdBM = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.item_bg), appData.getScreenHeight() / 6, appData.getScreenWidth() * 2 / 5, false);
        holdBD = new BitmapDrawable(holdBM);


    }

    class refreshHandler extends PtrDefaultHandler {

        @Override
        public void onRefreshBegin(PtrFrameLayout frame) {
            isRefresh = !isRefresh;
            EventBus.getDefault().post(new HotMessage());
        }

        @Override
        public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
            return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
        }
    }

    //设置adapter
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setAdapter(RecomAdapterMessage begin) {
        RecomeAdapter adapter = new RecomeAdapter();
        recomList.setAdapter(adapter);
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void getHots(HotMessage hotMessage) {
        if (jsonList.size() != 0) {
            jsonList.clear();
        }
        for (Object hotId : hotIdList) {
            String json = BaseHttpClient.getInfoWithData(superContext.getString(R.string.recom_url) + "?hotid=" + hotId + "&appid=" + appData.appId + "&version=" + appData.appVersion,
                    superContext.getString(R.string.aesKey));
            if (json.length() < 10) {
                HttpErrorCatch errorCatch = new HttpErrorCatch(json);
            } else {
                jsonList.add(json);
            }
        }
        //所有json完成，准备修改adapter
        EventBus.getDefault().post(new RecomAdapterMessage());
    }

    private class RecomeAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return hotIdList.size();
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
            if (jsonList.size() == 0) {
                return null;
            }
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.recom_item, parent, false);
            }
            int[] ivIds = new int[]{R.id.recom_image1, R.id.recom_image2, R.id.recom_image3};
            int[] tvIds = new int[]{R.id.recom_videoName_tv1, R.id.recom_videoName_tv2, R.id.recom_videoName_tv3};
            TextView recome_name_tv = ViewHolder.get(convertView, R.id.recom_name_tv);
            recome_name_tv.setText(hotNameList.get(position));
            Gson gson = new Gson();
            RecomModel recomModel;
            String json = jsonList.get(position);
            ImageView imageView;
            TextView textView;
            recomModel = gson.fromJson(json, RecomModel.class);
            if (recomModel != null) {
                List<RecomModel.RecommendsEntity> list = recomModel.getRecommends();
                for (int i = 0; i < list.size(); i++) {
                    imageView = ViewHolder.get(convertView, ivIds[i % 3]);
                    textView = ViewHolder.get(convertView, tvIds[i % 3]);
                    int loop = i;
                    if (isRefresh) {
                        loop = (loop + 3) % 6;
                    }
                    Glide.with(superContext)
                            .load(list.get(loop).getCover())
                            .override(appData.getScreenHeight() / 6, appData.getScreenWidth() * 2 / 5)
                            .centerCrop()
                            .placeholder(holdBD)
                            .error(holdBD)
                            .into(imageView);
                    imageView.setTag(R.id.image_tag, list.get(loop).getId());
                    textView.setText(list.get(loop).getName());
                }
            }
            refresh.refreshComplete();
            return convertView;
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

}




























































