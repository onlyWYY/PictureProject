package com.star.app.circleimageview.util;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.star.app.R;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * 从Uri中加载图片，并设置到imageView中
 */

public class RoadImageViewUtil {

    /**
     * @param uri
     * @param imageView
     * @return 从Uri中加载图片，并设置到imageView中
     */
    public static void showImageView(Context context, Uri uri, ImageView imageView) {
        Glide.with(context)
                .load(uri)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .bitmapTransform(new CropCircleTransformation(context))
                .thumbnail(0.5f)
                .placeholder(R.drawable.head_bitmap).
                priority(Priority.LOW).
                error(R.drawable.head_bitmap).
                fallback(R.drawable.head_bitmap).
                into(imageView);
        new File(RxPhotoTool.getImageAbsolutePath(context, uri));
    }


    /**
     * 初始化 图片剪切类
     *
     * @param uri
     */
    public static void initUCrop(Activity activity, Fragment fragment, Uri uri) {
        SimpleDateFormat timeFormatter = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
        long time = System.currentTimeMillis();
        String imageName = timeFormatter.format(new Date(time));
        Uri destinationUri = Uri.fromFile(new File(activity.getCacheDir(), imageName + ".jpeg"));
        UCrop.Options options = new UCrop.Options();
        //设置裁剪图片可操作的手势
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
        //设置隐藏底部容器，默认显示
        //options.setHideBottomControls(true);
        //设置toolbar颜色
        options.setToolbarColor(ActivityCompat.getColor(activity, R.color.colorPrimary));
        //设置状态栏颜色
        options.setStatusBarColor(ActivityCompat.getColor(activity, R.color.colorPrimaryDark));
        //开始设置
        //设置最大缩放比例
        options.setMaxScaleMultiplier(5);
        //设置图片在切换比例时的动画
        options.setImageToCropBoundsAnimDuration(666);
        //设置裁剪窗口是否为椭圆
        //options.setOvalDimmedLayer(true);
        //设置是否展示矩形裁剪框
        // options.setShowCropFrame(false);
        //设置裁剪框横竖线的宽度
        //options.setCropGridStrokeWidth(20);
        //设置裁剪框横竖线的颜色
        //options.setCropGridColor(Color.GREEN);
        //设置竖线的数量
        //options.setCropGridColumnCount(2);
        //设置横线的数量
        //options.setCropGridRowCount(1);
        if (null == fragment) {
            UCrop.of(uri, destinationUri)
                    .withAspectRatio(1, 1)
                    .withMaxResultSize(1000, 1000)
                    .withOptions(options)
                    .start(activity);
        } else {
            UCrop.of(uri, destinationUri)
                    .withAspectRatio(1, 1)
                    .withMaxResultSize(1000, 1000)
                    .withOptions(options)
                    .start(activity, fragment);
        }
    }
}
