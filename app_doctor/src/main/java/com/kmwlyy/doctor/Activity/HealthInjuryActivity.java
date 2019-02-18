package com.kmwlyy.doctor.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kmwlyy.doctor.Fragment.DoctorChatFragment;
import com.kmwlyy.doctor.Fragment.QueryChatFragment;
import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.model.Constant;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by TFeng on 2017/6/30.
 */

public class HealthInjuryActivity extends BaseActivity {
    @ViewInject(R.id.tv_left)
    TextView tv_left;
    @ViewInject(R.id.tv_center)
    TextView tv_center;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_injury);
        ViewUtils.inject(this);
        initView();
    }

    private void initView() {
        tv_left.setVisibility(View.VISIBLE);
        tv_left.setOnClickListener(this);
        tv_center.setText(getResources().getString(R.string.health_inqury));

        DoctorChatFragment fragment = new DoctorChatFragment();
        //图文咨询列表 参数
        Bundle bundle1 = new Bundle();
        bundle1.putString("OPDType", Constant.OPDTYPE_FAMILY);
        fragment.setArguments(bundle1);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.ll_consult, fragment)
                .commit();
    }

    @Override
    public void onClick(View arg0) {
        super.onClick(arg0);
        switch (arg0.getId()) {
            case R.id.tv_left:
                finish();
                break;
        }
    }
}
