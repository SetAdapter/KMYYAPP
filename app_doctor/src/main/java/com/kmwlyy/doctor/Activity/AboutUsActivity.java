package com.kmwlyy.doctor.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.kmwlyy.core.util.CommonUtils;
import com.kmwlyy.doctor.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * @Description描述:
 * @Author作者: zhaoqile
 * @Date日期: 2016/12/12
 */
public class AboutUsActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG = AboutUsActivity.class.getSimpleName();

    /* 标题栏 */
    @ViewInject(R.id.tv_left)
    TextView btn_left;
    @ViewInject(R.id.tv_center)
    TextView tv_title;

    @ViewInject(R.id.soft_introduce)
    TextView soft_introduce;
    @ViewInject(R.id.version_info)
    TextView mVersionInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ViewUtils.inject(this);

        initView();
        initListener();
    }

    private void initView() {
        tv_title.setText(R.string.about_us);
        btn_left.setVisibility(View.VISIBLE);

        mVersionInfo.setText("v" + CommonUtils.getVersion(this));

    }

    private void initListener() {
        btn_left.setOnClickListener(this);
        soft_introduce.setOnClickListener(this);
    }


    @Override
    public void onClick(View arg0) {
        super.onClick(arg0);
        switch (arg0.getId()) {
            case R.id.tv_left:
                finish();
                break;
            case R.id.soft_introduce:
                CommonUtils.startActivity(this, SoftIntroduceActivity.class);
                break;
        }
    }
}
