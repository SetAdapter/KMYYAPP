package com.kmwlyy.core.util;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.widget.Toast;

import com.kmwlyy.core.R;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.net.bean.SysParms;
import com.kmwlyy.core.net.event.Http_checkSysParms_Event;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.winson.ui.widget.AlterDialogView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 检查自动更新
 */
public class UpdateManager {
    public static final String TAG = "UpdateManager";
    public ProgressDialog mLoadingDialog;
    public static UpdateManager mUpdateManager;
    public static String USER = "1";
    public static String DOCTOR = "2";
    public static String APK_DIR = Environment.getExternalStorageDirectory() + "/KMYYApp/";
    public static String SP_NAME = "SP";

    /**
     * 获取实例
     * @return
     */
    public static UpdateManager getUpdateManager(){
        if (mUpdateManager == null) {
            mUpdateManager = new UpdateManager();
            return mUpdateManager;
        }
        return mUpdateManager;
    }

    /**
     * 获取后台版本，检查是否要更新
     * type： 1=医生端 2=患者端
     * return: false不更新 true更新
     */
    public void CheckUpdate(Context context, String type, Handler handler){
        int versionCode = 0;
        try {
            //获取当前版本号
            versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
            LogUtils.i(TAG, "当前版本号为:" + versionCode);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(versionCode > 0 && type.length() > 0){
            //发请求，检查是否需要更新
            checkParms(versionCode,type,handler,context);
        }
    }

    /**
     * 发请求
     * @param mContext
     * @param type
     */
    private void checkParms(int versionCode, final String type , final Handler handler, final Context mContext){
        Http_checkSysParms_Event event = new Http_checkSysParms_Event(""+versionCode,type,new HttpListener<SysParms>() {
            @Override
            public void onError(int code, String msg) {
                ToastUtils.showShort(mContext,msg);
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onSuccess(final SysParms parms) {
                if(parms != null && parms.NeedUpdate.equals("true")){
                    LogUtils.i(TAG, "后台版本号为:" + parms.VersionNo);
                    String msg = (type.equals("1")?mContext.getResources().getString(R.string.string_app_patient):mContext.getResources().getString(R.string.string_app_doctor)) + mContext.getResources().getString(R.string.string_version_update) + parms.Url.substring(parms.Url.lastIndexOf("/")+1) + mContext.getResources().getString(R.string.string_is_update);
                    AlterDialogView.Builder builder = new AlterDialogView.Builder(mContext);
                    builder.setCancelable(false);
                    builder.setTitle(mContext.getResources().getString(R.string.string_dialog_title));
                    builder.setMessage(msg);
                    builder.setNegativeButton(mContext.getResources().getString(R.string.string_no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //不下载
                            dialogInterface.dismiss();
                            handler.sendEmptyMessage(0);
                        }
                    });
                    builder.setPositiveButton(mContext.getResources().getString(R.string.string_yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            dialog.dismiss();
                            checkDownLoadInfo(parms,mContext);
                            handler.sendEmptyMessage(0);
                        }
                    });
                    builder.create().show();
                }else{
                    handler.sendEmptyMessage(0);
                }
            }
        });
        HttpClient httpClient = new HttpClient(mContext,event);
        httpClient.start();
    }

    /**
     * 检查下载或是直接安装
     */
    private void checkDownLoadInfo(SysParms parms,Context mContext) {
        String savePath = APK_DIR + parms.Url.substring(parms.Url.lastIndexOf("/")+1);
        System.out.println("文件保存地址："+savePath);
        if (isNeedDownLoad(parms.VersionNo,savePath,mContext)) {
            downLoadApk(parms, savePath,mContext);
        } else {
            installApk(new File(savePath),mContext);
        }
    }

    /**
     * 是否需要下载
     */
    private boolean isNeedDownLoad(String versionNo,String savePath,Context mContext) {
        File file = new File(savePath);
        if (file.exists()) {
            SharedPreferences  sharedPreferences = mContext.getSharedPreferences(SP_NAME, 0);
            boolean isLoad = sharedPreferences.getBoolean(versionNo,false);
            if (isLoad) {
                //上一次下载成功，不需要再下载了
                return false;
            } else {
                //如果上一次下载失败，则重新下载
                file.delete();
                return true;
            }
        } else {
            //文件不存在，需要下载
            return true;
        }
    }

    private void installApk(File file,Context context) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    private void downLoadApk(final SysParms parms, String savePath, final Context mContext) {
        String url = parms.Url;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.download(url, savePath, new RequestCallBack<File>() {
            @Override
            public void onSuccess(ResponseInfo<File> responseInfo) {
                String fileName = parms.Url.substring(parms.Url.lastIndexOf("/")+1);
                SharedPreferences sharedPreferences = mContext.getSharedPreferences(SP_NAME,0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(parms.VersionNo, true);
                editor.commit();
                checkDownLoadInfo(parms,mContext);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                ToastUtils.showShort(mContext,mContext.getResources().getString(R.string.string_download_failed));
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                int num = (int) (current * 100 / total);
                if (mLoadingDialog != null) {
                    mLoadingDialog.setMessage(mContext.getResources().getString(R.string.string_downloading) + num + "%");
                }
            }
        });
    }
}
