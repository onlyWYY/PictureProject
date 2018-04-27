package com.star.app;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.star.app.circleimageview.dialog.RxDialogChooseImage;
import com.star.app.circleimageview.util.PermissionHelper;
import com.star.app.circleimageview.util.RoadImageViewUtil;
import com.star.app.circleimageview.util.RxPhotoTool;
import com.star.app.circleimageview.util.RxSPTool;
import com.yalantis.ucrop.UCrop;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView = findViewById(R.id.img);
        mImageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        showChooseImageDialog();
    }

    //权限名称
    private static final String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final String CAMERA = Manifest.permission.CAMERA;
    public final static int REQUEST_READ_CONTACTS = 1; //返回值
    private static final String HEAD_PHOTO = "HEAD_PHOTO"; // sp存储头像key
    private PermissionHelper mPermissionHelper;

    /**
     * 选择图片dialog
     */
    private void showChooseImageDialog() {
        mPermissionHelper = new PermissionHelper(this);
        //判断权限授权状态
        boolean b = mPermissionHelper.checkPermission(CAMERA);
        //如果没有获取到权限,则尝试获取权限
        if (!b) {
            mPermissionHelper.permissionsCheck(CAMERA, REQUEST_READ_CONTACTS);
        } else {
            RxDialogChooseImage dialogChooseImage = new RxDialogChooseImage(this, RxDialogChooseImage.LayoutType.TITLE);
            dialogChooseImage.show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_READ_CONTACTS:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    boolean bb = mPermissionHelper.checkPermission(WRITE_EXTERNAL_STORAGE);
                    if (!bb) {
                        mPermissionHelper.permissionsCheck(WRITE_EXTERNAL_STORAGE, REQUEST_READ_CONTACTS);
                    } else {
                        RxDialogChooseImage dialogChooseImage = new RxDialogChooseImage(this, RxDialogChooseImage.LayoutType.TITLE);
                        dialogChooseImage.show();
                    }
                } else {
                    //如果请求失败
                    mPermissionHelper.startAppSettings();
                    Toast.makeText(this, "已禁用此权限", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }


    private Uri resultUri; //选择图片uri

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RxPhotoTool.GET_IMAGE_FROM_PHONE://选择相册之后的处理
                if (resultCode == RESULT_OK) {
                    RoadImageViewUtil.initUCrop(MainActivity.this, null, data.getData());
                }
                break;

            case RxPhotoTool.GET_IMAGE_BY_CAMERA://选择照相机之后的处理
                if (resultCode == RESULT_OK) {
                    RoadImageViewUtil.initUCrop(MainActivity.this, null, RxPhotoTool.imageUriFromCamera);
                }
                break;

            case RxPhotoTool.CROP_IMAGE://普通裁剪后的处理
                Glide.with(MainActivity.this).
                        load(RxPhotoTool.cropImageUri).
                        diskCacheStrategy(DiskCacheStrategy.RESULT).
                        bitmapTransform(new CropCircleTransformation(MainActivity.this)).
                        thumbnail(0.5f).
                        placeholder(R.drawable.head_bitmap).
                        priority(Priority.LOW).
                        error(R.drawable.head_bitmap).
                        fallback(R.drawable.head_bitmap).
                        into(mImageView);
                break;

            case UCrop.REQUEST_CROP://UCrop裁剪之后的处理
                if (resultCode == RESULT_OK) {
                    resultUri = UCrop.getOutput(data);
                    RoadImageViewUtil.showImageView(MainActivity.this, resultUri, mImageView);
                    RxSPTool.putContent(MainActivity.this, HEAD_PHOTO, resultUri.toString());
                } else if (resultCode == UCrop.RESULT_ERROR) {
                    final Throwable cropError = UCrop.getError(data);
                }
                break;

            case UCrop.RESULT_ERROR://UCrop裁剪错误之后的处理
                final Throwable cropError = UCrop.getError(data);
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
