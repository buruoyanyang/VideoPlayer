package biezhi.videoplayer.CustomerClass;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;

/**
 * Created by xiaofeng on 16/5/11.
 */
public class BaseLoadImage {
    //transformation
    //crop
    //CropTransformation, CropCircleTransformation, CropSquareTransformation, RoundedCornersTransformation
    //color
    //ColorFilterTransformation, GrayscaleTransformation
//    public static int CT = 1;
//    public static int CCT = 2;
//    public static int CST = 3;
//    public static int RCT = 4;


    public static void load(Context context, String url,Transformation transformation,int width,int height,ImageView imageView) {
        Glide.with(context)
                .load(url)
                .override(width,height)
                .bitmapTransform(transformation)
                .into(imageView);
    }

    /**
     Glide.with(superContext)
     .load(appData.getCateUrls().get(position))
     .bitmapTransform(new CropCircleTransformation(superContext))
     .into(imageView);
     * */
}
