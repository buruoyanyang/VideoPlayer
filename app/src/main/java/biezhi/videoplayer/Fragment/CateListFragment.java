package biezhi.videoplayer.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import biezhi.videoplayer.Data;
import biezhi.videoplayer.R;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by xiaofeng on 16/5/9.
 */
public class CateListFragment extends Fragment {
    Context superContext;
    GridView gridView;
    Data appData;
    LayoutInflater inflater;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View contextView = inflater.inflate(R.layout.cate_list_fragment, container, false);
        superContext = container.getContext();
        //todo 初始化
        initFragment(contextView, superContext);
        initCateGrid();
        return contextView;
    }

    public void initFragment(View contextView, Context superContext) {
        appData = (Data) superContext.getApplicationContext();
        gridView = (GridView) contextView.findViewById(R.id.cate_grid);
        inflater = (LayoutInflater) superContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void initCateGrid() {
        GridViewAdapter adapter = new GridViewAdapter();
        gridView.setAdapter(adapter);
    }

    private class GridViewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return appData.getCateUrls().size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.video_item_adapter, parent, false);
            }
            ImageView imageView = ViewHolder.get(convertView,R.id.cate_image);
            TextView textView = ViewHolder.get(convertView,R.id.cate_name);
            //使用glide-transformations加载图片
            Glide.with(superContext)
                    .load(appData.getCateUrls().get(position))
                    .bitmapTransform(new CropCircleTransformation(superContext))
                    .into(imageView);
            textView.setText(appData.getCateNames().get(position));
            return convertView;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
