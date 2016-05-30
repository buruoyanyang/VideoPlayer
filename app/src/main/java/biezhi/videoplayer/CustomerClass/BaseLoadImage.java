package biezhi.videoplayer.CustomerClass;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
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


    public static void load(Context context, String url, Transformation transformation, int width, int height, ImageView imageView, BitmapDrawable bitmapDrawable) {
        Glide.with(context)
                .load(url)
                .override(width,height)
                .centerCrop()
                .placeholder(bitmapDrawable)
                .error(bitmapDrawable)
                .into(imageView);
    }


//    Glide.with(superContext)
//            .load(list.get(loop).getCover())
//            .override(appData.getScreenHeight() / 6, appData.getScreenWidth() * 2 / 5)
//            .centerCrop()
//    .placeholder(holdBD)
//    .error(holdBD)
//    .into(imageView);

    /**
     Glide.with(superContext)
     .load(appData.getCateUrls().get(position))
     .bitmapTransform(new CropCircleTransformation(superContext))
     .into(imageView);
     * */


}
