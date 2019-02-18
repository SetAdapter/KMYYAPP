package com.kmwlyy.registry.bean;

import android.content.Context;
import android.text.TextUtils;

import com.kmwlyy.core.util.CircleDisplayer;
import com.kmwlyy.registry.R;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

/**
 * Created by Administrator on 2016-8-16.
 */
public class Contants {

    public final static int[] zcArr = {
            R.string.empty,
            R.string.master_aesculapius,
            R.string.minor_aesculapius,
            R.string.master_treat_doctor,
            R.string.aesculapius,
            R.string.feldsher,
            R.string.associate_chief_pharmacist,
            R.string.master_apothecary,
            R.string.primary_apothecary,
            R.string.apothecary,
            R.string.pharmacist,
            R.string.none,
            R.string.artificer,
            R.string.minor_nurse,
            R.string.master_nurse,
            R.string.master_manager_nurse,
            R.string.primary_nurse,
            R.string.nurse,
            R.string.minor_inspect_artificer,
            R.string.primary_docimaster,
            R.string.docimaster,
            R.string.minor_artificer,
            R.string.master_artificer,
            R.string.primary_manager_artificer,
            R.string.assistant_experts,
            R.string.boffin,
            R.string.professor,
            R.string.adjunct_professor,
            R.string.house_staff
    };

//    public static String[] zcArr = {
//            "",
//            "主任医师",
//            "副主任医师",
//            "主治医师",
//            "医师",
//            "医士",
//            "副主任药师",
//            "主任药师",
//            "主管药师",
//            "药师",
//            "药士",
//            "暂无",
//            "技师",
//            "副主任护师",
//            "主任护师",
//            "主管护师",
//            "护师",
//            "护士",
//            "副主任检验技师",
//            "主管检验师",
//            "检验师",
//            "副主任技师",
//            "主任技师",
//            "主管技师",
//            "技士",
//            "研究员",
//            "教授",
//            "副教授",
//            "住院医师"};

    public static String getDoctorZC(Context context, String index) {
        if (TextUtils.isEmpty(index)) {
            return context.getResources().getString(zcArr[0]);
        } else {
            return context.getResources().getString(zcArr[Integer.valueOf(index)]);
        }
    }

    public static void initImageLoader(Context context) {
        //缓存文件的目录
        File cacheDir = StorageUtils.getOwnCacheDirectory(context, "imageloader/Cache");
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .memoryCacheExtraOptions(480, 800) // max width, max height，即保存的每个缓存文件的最大长宽
                .threadPoolSize(3) //线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator()) //将保存的时候的URI名称用MD5 加密
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现
                .memoryCacheSize(2 * 1024 * 1024) // 内存缓存的最大值
                .diskCacheSize(50 * 1024 * 1024)  // 50 Mb sd卡(本地)缓存的最大值
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                        // 由原先的discCache -> diskCache
                .diskCache(new UnlimitedDiscCache(cacheDir))//自定义缓存路径
                .imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
                .writeDebugLogs() // Remove for release app
                .build();
        //全局初始化此配置
        ImageLoader.getInstance().init(config);
    }

    public static DisplayImageOptions getDisplayOptions() {
        // 使用DisplayImageOptions.Builder()创建DisplayImageOptions
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.bg_area_check) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.bg_area_check) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.bg_area_check) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
                .displayer(new RoundedBitmapDisplayer(10)) // 设置成圆角图片
                .build(); // 构建完成
        return options;
    }

    public static DisplayImageOptions getCircleDisplayOptions() {
        // 使用DisplayImageOptions.Builder()创建DisplayImageOptions
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.default_avatar_shape) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.default_avatar_shape) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.default_avatar_shape) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
                .displayer(new CircleDisplayer(10)) // 设置成圆角图片
                .build(); // 构建完成
        return options;
    }

}
