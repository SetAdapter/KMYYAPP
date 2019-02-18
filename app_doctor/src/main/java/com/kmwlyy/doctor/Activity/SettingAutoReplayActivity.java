package com.kmwlyy.doctor.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kmwlyy.core.util.ToastUtils;
import com.lidroid.xutils.ViewUtils;
import com.kmwlyy.doctor.R;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by TFeng on 2017/6/29.
 */

public class SettingAutoReplayActivity extends BaseActivity {

    @ViewInject(R.id.tv_left)
    TextView tv_left;
    @ViewInject(R.id.tv_center)
    TextView tv_center;
    @ViewInject(R.id.iv_left)
    ImageView iv_left;
    @ViewInject(R.id.tv_right)
    TextView tv_right;
    @ViewInject(R.id.et_auto_relay)
    EditText et_auto_replay;
    private String mString;
    private String mAutoReplay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_replay_setting);
        ViewUtils.inject(this);
        getIntentInfo();
        initView();
        initListener();

    }

    private void getIntentInfo() {
        mAutoReplay = getIntent().getStringExtra("AutoReplay");
    }


    private void initView() {
        tv_left.setVisibility(View.GONE);
        iv_left.setVisibility(View.VISIBLE);
        tv_right.setVisibility(View.VISIBLE);
        tv_center.setText(R.string.atuo_replay_setting);
        tv_right.setText(R.string.save);

        if(mAutoReplay == null || mAutoReplay.length() == 0){
            et_auto_replay.setText("");
        }else {
            et_auto_replay.setText(mAutoReplay);
        }


    }

    private void initListener() {

        iv_left.setOnClickListener(this);
        tv_right.setOnClickListener(this);
    }

    @Override
    public void onClick(View arg0) {
        super.onClick(arg0);
        switch (arg0.getId()){
            case R.id.iv_left:
                back();
                break;
            case R.id.tv_right:
                // TODO: 2017/6/29 检查并保存数据
                check_data();

                break;

        }
    }

    private void back() {
        Intent intentReturn = new Intent();

        intentReturn.putExtra("Back",0);

        setResult(RESULT_OK,intentReturn);
        finish();
    }

    private void check_data() {
        if(TextUtils.isEmpty(et_auto_replay.getText().toString().trim())){
            ToastUtils.showShort(mContext,"自动回复为空");
            return;
        }else{
            mString = et_auto_replay.getText().toString().trim();
        }
        Intent intent = new Intent();
        intent.putExtra("isSetting",true);
        intent.putExtra("AutoReplay",mString);
        intent.putExtra("Back",1);
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        back();
    }
}
