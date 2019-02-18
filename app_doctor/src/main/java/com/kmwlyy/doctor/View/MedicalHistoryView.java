package com.kmwlyy.doctor.View;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kmwlyy.doctor.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by TFeng on 2017/7/5.
 */

public class MedicalHistoryView extends FrameLayout {
    @ViewInject(R.id.iv_medical_history)
    ImageView iv_medical_history;
    @ViewInject(R.id.tv_name_time)
    TextView tv_name_time;

    private View mView;

    public MedicalHistoryView(@NonNull Context context) {
        this(context,null);
    }

    public MedicalHistoryView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mView = LayoutInflater.from(context).inflate(R.layout.view_medical_history, this, true);
        ViewUtils.inject(this,mView);

    }
    public void setMedicalHidtory(String uri){


    }
    public void setNameTime(String str){
        tv_name_time.setText(str);
    }
}
