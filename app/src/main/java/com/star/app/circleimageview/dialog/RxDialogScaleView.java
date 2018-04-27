package com.star.app.circleimageview.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.star.app.R;
import com.star.app.circleimageview.imageview.ImageSource;
import com.star.app.circleimageview.imageview.RxScaleImageView;

/**
 * Mainly used for confirmation and cancel.
 */
public class RxDialogScaleView extends RxDialog {

    private RxScaleImageView mRxScaleImageView;
    private String filePath;
    private Uri fileUri;
    private String fileAssetName;
    private Bitmap fileBitmap;
    private int resId;

    public RxDialogScaleView(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    public RxDialogScaleView(Context context, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    public RxDialogScaleView(Context context) {
        super(context);
        initView();
    }

    public RxDialogScaleView(Activity context) {
        super(context);
        initView();
    }

    public RxDialogScaleView(Context context, float alpha, int gravity) {
        super(context, alpha, gravity);
        initView();
    }

    public RxScaleImageView getRxScaleImageView() {
        return mRxScaleImageView;
    }

    public void setImagePath(String filePath) {
        this.filePath = filePath;
        mRxScaleImageView.setImage(ImageSource.uri(filePath));
    }

    public void setImageUri(Uri uri) {
        this.fileUri = uri;
        mRxScaleImageView.setImage(ImageSource.uri(uri));
    }

    public void setImageAssets(String assetName) {
        this.fileAssetName = assetName;
        mRxScaleImageView.setImage(ImageSource.asset(assetName));
    }

    public void setImageRes(int resId) {
        this.resId = resId;
        mRxScaleImageView.setImage(ImageSource.resource(resId));
    }

    public void setImageBitmap(Bitmap bitmap) {
        this.fileBitmap = bitmap;
        mRxScaleImageView.setImage(ImageSource.bitmap(fileBitmap));
    }

    private void initView() {
        View dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_scaleview, null);
        mRxScaleImageView = (RxScaleImageView) dialogView.findViewById(R.id.rx_scale_view);
        mRxScaleImageView.setMaxScale(20);
        ImageView ivClose = (ImageView) dialogView.findViewById(R.id.iv_close);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });
        setFullScreen();
        setContentView(dialogView);
    }

}
