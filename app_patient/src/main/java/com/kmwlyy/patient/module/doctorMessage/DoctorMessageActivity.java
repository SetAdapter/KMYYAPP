package com.kmwlyy.patient.module.doctorMessage;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kmwlyy.core.base.BaseActivity;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.module.doctorMessage.Adapter.Doctor_Message_Adapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Gab on 2017/7/29 0029.
 * 医生通知信息
 */

public class DoctorMessageActivity extends BaseActivity implements AdapterView.OnItemClickListener{

    Doctor_Message_Adapter mDoctor_message_adapter;

    @BindView(R.id.doctor_message_listView)
    ListView mListView;

    @BindView(R.id.rl_doctor_message)
    RelativeLayout mMessage;

    @BindView(R.id.image_record_null)
    TextView image_record_null;

    @BindView(R.id.tv_title_center)
    TextView tv_title_center;
    @BindView(R.id.iv_tools_left)
    Button iv_tools_left;

    List data;

    @Override
    protected int getLayoutId() {
        return R.layout.doctor_message_activity;
    }

    @Override
    protected void afterBindView() {
        tv_title_center.setText("医生通知");
        iv_tools_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        mDoctor_message_adapter = new Doctor_Message_Adapter(this, new ArrayList<Message_bean>());
//        mListView.setAdapter(mDoctor_message_adapter);
//        image_record_null.setVisibility(View.VISIBLE);

        getData();
        mDoctor_message_adapter = new Doctor_Message_Adapter(this, data);
        mListView.setAdapter(mDoctor_message_adapter);
        mListView.setOnItemClickListener(this);
    }

    private void getData() {
        data = new ArrayList();
        for (int i = 0; i < 8; i++) {
            data.add("");
        }
    }

    /**
     * 调接口
     */
    private void getMessageList(boolean flag) {

    }

    /**
     * 刷新消息
     */
    private void updMessageIsRead() {

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(mContext, "暂无详情", Toast.LENGTH_SHORT).show();
    }
}
