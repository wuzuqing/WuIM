package com.wuzuqing.component_base.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.TypedValue;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.wuzuqing.component_base.R;
import com.wuzuqing.component_data.constant.BaseHost;

public class ImageLoadUtils {

    private static final int IMG_DEFAULT = R.mipmap.ic_image_loading;
    private static final int IMG_AVATAR_DEFAULT = R.mipmap.ic_image_loading;
    public static int IMG_LOADING = R.mipmap.ic_image_loading;
    public static int IMG_ERROR = R.mipmap.ic_empty_picture;
    public static void display(Context context, ImageView imageView, Object url, int placeholder, int error, BitmapTransformation transformation, DiskCacheStrategy
            diskCacheStrategy) {
        display(context, imageView, url, placeholder, error, true, transformation, diskCacheStrategy);
    }

    public static void display(Context context, ImageView imageView, Object url, int placeholder, int error, boolean crop, BitmapTransformation transformation, DiskCacheStrategy
            diskCacheStrategy) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        if (url == null || (url instanceof String && TextUtils.isEmpty((String) url))) {
            if (placeholder > 0) {
                imageView.setImageResource(placeholder);
            } else if (error > 0) {
                imageView.setImageResource(error);
            }
            return;
        }
        RequestOptions options = new RequestOptions();

        options.diskCacheStrategy(diskCacheStrategy);
        if (placeholder != 0) {
            options.placeholder(placeholder);
        }
        if (error != 0) {
            options.error(error);
        }
        if (crop) {
            options.centerCrop();
        } else {
            options.circleCrop();
        }
        options.dontAnimate();
        if (transformation != null) {
            options.transform(transformation);
        }

        Glide.with(context).load(url).apply(options).into(imageView);

    }

    public static void display(Context context, ImageView imageView, Object url) {
        display(context, imageView, url, IMG_DEFAULT, IMG_DEFAULT, null, DiskCacheStrategy.ALL);
    }

    public static void display(ImageView imageView, Object url) {
        if (imageView == null) return;
        display(imageView.getContext(), imageView, url, IMG_DEFAULT, IMG_ERROR, null, DiskCacheStrategy.ALL);
    }

    public static void displayStatic(ImageView imageView, String url) {
        if (imageView == null) return;
        display(imageView.getContext(), imageView,BaseHost.STATIC_HOST+ url, IMG_DEFAULT, IMG_ERROR, null, DiskCacheStrategy.ALL);
    }

    public static  void displayChatImg(ImageView imageView, String path,SimpleTarget target ) {
        if (imageView == null) return;
        RequestOptions options = new RequestOptions();
        options.placeholder(IMG_DEFAULT);
        options.error(IMG_DEFAULT);
        options.dontAnimate();
        Glide.with(imageView.getContext()).load(String.format("%s%s",BaseHost.STATIC_HOST,path)).apply(options)
                .into(target);
    }
    public static void displayRounded(ImageView imageView, Object url, int radius) {
        if (imageView == null) return;
        display(imageView.getContext(), imageView, url, IMG_DEFAULT, IMG_ERROR,
                new RoundedCorners((int) TypedValue.applyDimension(1, radius, imageView.getResources().getDisplayMetrics())), DiskCacheStrategy.ALL);
    }

    public static void display(ImageView imageView, Object url, boolean isCircle) {
        if (imageView == null) return;
        display(imageView.getContext(), imageView, url, IMG_DEFAULT, IMG_ERROR, isCircle, null, DiskCacheStrategy.ALL);
    }

    public static void displayAvatar(ImageView imageView, Object url) {
        if (imageView == null) return;
        display(imageView.getContext(), imageView, url, IMG_AVATAR_DEFAULT, IMG_ERROR, false, null, DiskCacheStrategy.ALL);
    }

    public static void display(Context context, ImageView imageView, String url, boolean crop) {
        display(context, imageView, url, IMG_DEFAULT, IMG_ERROR, crop, null, DiskCacheStrategy.ALL);
    }


    public static void display(Context context, ImageView imageView, String url, int errorId) {
        display(context, imageView, url, errorId, errorId, null, DiskCacheStrategy.ALL);
    }

}
