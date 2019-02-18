package com.kmwlyy.doctor.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kmwlyy.doctor.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * @Description描述:
 * @Author作者: zhaoqile
 * @Date日期: 2016/12/12
 */
public class SoftIntroduceActivity extends BaseActivity{
    public static final String TAG = SoftIntroduceActivity.class.getSimpleName();

    /* 标题栏 */
    @ViewInject(R.id.tv_left)
    TextView btn_left;
    @ViewInject(R.id.tv_center)
    TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soft_introduce);
        ViewUtils.inject(this);

        initView();
        initListener();
    }

    private void initView(){
        tv_title.setText(R.string.soft_introduce);
        btn_left.setVisibility(View.VISIBLE);
    }

    private void initListener(){
        btn_left.setOnClickListener(this);
    }


    @Override
    public void onClick(View arg0) {
        super.onClick(arg0);
        switch (arg0.getId()){
            case R.id.tv_left:
                finish();
                break;
        }
    }
}
