package com.sdust.gankor.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import static u.aly.au.m;

/**
 * Created by Liu Yongwei on 2016/10/30.
 * <p>
 * version : 1.0
 */

public class ImageUtil {

    private ImageLoader mImageLoader;

    private ImageUtil() {
        mImageLoader = ImageLoader.getInstance();
    }

    private static class ImageUtilHolder {
        private static final ImageUtil instance = new ImageUtil();
    }

    public static final ImageUtil getInstance() {
        return ImageUtilHolder.instance;
    }

    /**
     * 加载图片到指定ImageView上
     *
     * @param uri  图片路径
     * @param view
     */
    public void displayImage(String uri, ImageView view) {
        DisplayImageOptions options = new DisplayImageOptions
                .Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        mImageLoader.displayImage(uri, view, options);
    }

    /**
     * 加载图片到指定ImageView上，并设置加载中和加载失败时的默认图片
     *
     * @param uri
     * @param view
     * @param resId
     */
    public void displayImageOnLoading(String uri, ImageView view, int resId) {
        DisplayImageOptions options = new DisplayImageOptions
                .Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .showImageOnLoading(resId)
                .showImageOnFail(resId)
                .showImageForEmptyUri(resId)
                .build();
    }

    /**
     * 得到图片在磁盘中的缓存路径
     *
     * @param uri
     * @return
     */
    public String getAbsolutePath(String uri) {
        return mImageLoader.getDiskCache().get(uri).getAbsolutePath();
    }

    /**
     * 判断是否有缓存
     *
     * @param uri
     * @return
     */
    public boolean isExits(String uri) {
        return mImageLoader.getDiskCache().get(uri).exists();
    }

    /**
     * 保存图片
     *
     * @param activity
     * @param name     图片名字
     * @param url      原图片路径
     */
    public void saveImage(Activity activity, String name, String url) {
        //  6.0检查权限
        if (Build.VERSION.SDK_INT >= 23) {
            int write = activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int read = activity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            if (write != PackageManager.PERMISSION_GRANTED || read != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, 300);
            }
        }

        File saveFile = new File(Environment.getExternalStorageDirectory(), "GankOr");
        if (!saveFile.exists()) {
            saveFile.mkdirs();
        }

        if (copyImage(saveFile.getAbsolutePath() + "//" + name + ".jpg", url)) {
            ToastUtils.show("save success! save path: " + saveFile.getAbsolutePath());
        } else {
            ToastUtils.show("save failed");
        }
    }

    /**
     * copy图片   获取缓存在磁盘中的图片，然后copy该图片
     *
     * @param newPath
     * @param url
     * @return
     */
    public boolean copyImage(String newPath, String url) {
        String oldPath = getAbsolutePath(url);
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            File oldFile = new File(oldPath);
            if (oldFile.exists()) {
                fis = new FileInputStream(oldFile);
                fos = new FileOutputStream(newPath);
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = fis.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CommonUtils.close(fis);
            CommonUtils.close(fos);
        }

        return false;
    }
}
