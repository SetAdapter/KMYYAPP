package com.kmwlyy.doctor.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.model.InfoEvent;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by xcj on 2017/6/20.
 */

public class VideoInquiryActivity extends BaseActivity {

    static final String TYPE = "TYPE";

    @ViewInject(R.id.tv_left)
    TextView btn_left;
    @ViewInject(R.id.tv_center)
    TextView tv_title;
    @ViewInject(R.id.btn_service_activation)
    Button btn_service_activation;
    @ViewInject(R.id.tv_head_desc)
    TextView tv_head_desc;
    @ViewInject(R.id.tv_head)
    TextView tv_head;
    @ViewInject(R.id.iv_info)
    ImageView iv_info;
    @ViewInject(R.id.tv_info1)
    TextView tv_info1;
    @ViewInject(R.id.tv_info2)
    TextView tv_info2;
    @ViewInject(R.id.tv_info3)
    TextView tv_info3;
    private int mType = 0;

    public static void startVideoInquiryActivity(Context context, int type) {
        Intent intent = new Intent(context, VideoInquiryActivity.class);
        intent.putExtra(TYPE, type);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_inquiry);
        ViewUtils.inject(this);
        mType = getIntent().getIntExtra(TYPE, 0);
        if(mType == 1){
            tv_title.setText("视话问诊");
            tv_head.setText("视话问诊");
            tv_head_desc.setText("通过视频、语音、预约接诊、开处方，高效解决患者问题");
            iv_info.setImageResource(R.mipmap.img_shwz);
            tv_info1.setText("A、医生设定可被预约时间，以及每个时间段能够接诊的最大人数上限；");
            tv_info2.setText("B、患者预约问诊时必须提交个人基础信息和简单病历信息，否则医生可以拒绝接诊；");
            tv_info3.setText("C、医生自主设定价格，患者付费购买。");
        }else if(mType == 2){
            tv_title.setText("图文咨询");
            tv_head.setText("图文咨询");
            tv_head_desc.setText("通过文字、图片，解答患者健康咨询和疑问");
            iv_info.setImageResource(R.mipmap.img_twzx);
            tv_info1.setText("A、医生可以利用业余时间，充分发挥专业才能，回答患者的健康咨询；");
            tv_info2.setText("B、医生通过与患者沟通交流，给予合适的健康建议；");
            tv_info3.setText("C、医生自主设定价格，患者付费购买。");
        }else if(mType == 3){
            tv_title.setText("图文义诊");
            tv_head.setText("图文义诊");
            tv_head_desc.setText("通过文字、图片，免费解答患者健康咨询和疑问");
            iv_info.setImageResource(R.mipmap.img_twyz);
            tv_info1.setText("A、康美平台常设有义诊窗口，向患者推荐平台上的医生和专家，由您自由选择是否参加；");
            tv_info2.setText("B、义诊以‘天’为单位，当天你将可以设置免费图文咨询给患者，免费次数由您设置；");
            tv_info3.setText("C、参加义诊会大大增加您的曝光量，提高知名度，为您带来更多的粉丝患者。");
        }
        btn_left.setVisibility(View.VISIBLE);
        btn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoInquiryActivity.this.finish();
            }
        });
        btn_service_activation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转页面
                if(mType == 3) {
                    Intent intent = new Intent(VideoInquiryActivity.this, FreeClinicSettingActivity.class);
                    startActivity(intent);
                }else {
                    VideoInquirySettingActivity.startVideoInquirySettingActivity(VideoInquiryActivity.this, mType, false);
                }

            }
        });
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshData(InfoEvent.SettingSuc refreshData) {
       finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
