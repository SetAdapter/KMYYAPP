package com.kmwlyy.patient.weight;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.kmwlyy.patient.R;
import com.kmwlyy.patient.weight.doodleview.DoodleView;

/**
 * Created by Administrator on 2017/8/18.
 */

public class SignDialog extends Dialog implements View.OnClickListener {
    private Context mContext;
    private DoodleView mDoodleview;

    public SignDialog(@NonNull Context context) {
        super(context, R.style.Dialog);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_sign);
        setCanceledOnTouchOutside(true);
        initView();
    }

    private void initView() {
        TextView signed_tv = (TextView) findViewById(R.id.signed_tv);
        signed_tv.setOnClickListener(this);
        TextView clear_tv = (TextView) findViewById(R.id.clear_tv);
        clear_tv.setOnClickListener(this);
        mDoodleview = (DoodleView) findViewById(R.id.doodle_doodleview);
//        mDoodleview.setSize(dip2px(5));
        mDoodleview.setColor("#FF0A0909");
        Button confirm_btn = (Button) findViewById(R.id.confirm_btn);
        confirm_btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signed_tv:
                break;
            case R.id.clear_tv:
                mDoodleview.reset();
                break;
            case R.id.confirm_btn:
                if (mOnDoodleListener != null) {
                    String path = mDoodleview.saveBitmap(mDoodleview);
                    mOnDoodleListener.OnConfrim(path);
                }
                dismiss();
                break;
        }
    }

    private OnDoodleListener mOnDoodleListener;

   public interface OnDoodleListener {
        void OnConfrim(String path);
    }

    public void setOnDoodleListener(OnDoodleListener mOnDoodleListener) {
        this.mOnDoodleListener = mOnDoodleListener;
    }

}
