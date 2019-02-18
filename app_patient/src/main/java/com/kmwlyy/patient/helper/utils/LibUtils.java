package com.kmwlyy.patient.helper.utils;

import com.kmwlyy.core.util.CircleDisplayer;
import com.kmwlyy.patient.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

/**
 * Created by Winson on 2016/8/19.
 */
public class LibUtils {

    public static DisplayImageOptions getCircleDisplayOptions(int defalultRes) {
        // 使用DisplayImageOptions.Builder()创建DisplayImageOptions
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(defalultRes) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(defalultRes) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(defalultRes) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
                .displayer(new CircleDisplayer(10)) // 设置成圆角图片
                .build(); // 构建完成
        return options;
    }

    public static DisplayImageOptions getCircleDisplayOptions() {
        return getCircleDisplayOptions(R.drawable.default_avatar);
    }

    public static DisplayImageOptions getSquareDisplayOptions(int defalultRes) {
        // 使用DisplayImageOptions.Builder()创建DisplayImageOptions
        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.default_avatar) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(defalultRes) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(defalultRes) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
                .displayer(new SimpleBitmapDisplayer()) // 设置成圆角图片
                .build(); // 构建完成
        return options;
    }
    public static DisplayImageOptions getSquareDisplayOptions() {
        return getSquareDisplayOptions(R.drawable.add_square);
    }

    public static DisplayImageOptions getSquareDisplayOptionsServicePackage() {
        // 使用DisplayImageOptions.Builder()创建DisplayImageOptions
        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.default_avatar) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.vcg) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.vcg) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
                .displayer(new SimpleBitmapDisplayer()) // 设置成圆角图片
                .build(); // 构建完成
        return options;
    }

}
