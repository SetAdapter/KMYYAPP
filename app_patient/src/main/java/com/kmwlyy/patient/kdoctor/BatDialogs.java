package com.kmwlyy.patient.kdoctor;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;

/**
 * @Description描述:
 * @Author作者: hx
 * @Date日期: 2016/12/9
 */

public class BatDialogs {

    public static void showHintDialog(final Context context, String hint) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(hint)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    /**
     * 提示用户页面关闭
     * @param mActivity
     * @param hint 提示语
     */
    public static void showCloseDialog(final Activity mActivity, String hint) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setMessage(hint)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mActivity.finish();
                    }
                })
                .show();
    }

    /**
     * 是否打电话的提示
     *
     * @param context
     * @param number  电话号码
     */
    public static void isCallDialog(final Context context, final String number) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("是否发起呼叫?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Uri uri = Uri.parse("tel:" + number);
                        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                        context.startActivity(intent);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }
}
