package com.kmwlyy.patient.module.teamdetail;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kmwlyy.core.base.BaseActivity;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.module.familydoctorteam.ChangeDoctorTeam;
import com.kmwlyy.patient.module.familydoctorteam.NewChangeDoctorTeam;
import com.kmwlyy.patient.weight.refresh.XListView;

import java.util.List;

import butterknife.BindView;

import static com.kmwlyy.patient.weight.refresh.XListViewFooter.mContentView;


/**
 * 团队详情
 * Created by Stefan on 2017/7/30.
 */

public class TeamDetailActivity extends BaseActivity implements XListView.IXListViewListener{

    @BindView(R.id.text_title)
    TextView text_title;
    @BindView(R.id.text_info)
    TextView text_info;
    @BindView(R.id.text_dianzan)
    TextView text_dianzan;
    @BindView(R.id.text_qianyue)
    TextView text_qianyue;
    TextView tv_title_center;
    Button iv_tools_left;
    TeamDetailAdapter adapter;
    private XListView list_item;
    String doctorGroupID;
    NewChangeDoctorTeam.DataBean dataBean;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_team_detail;
    }

    @Override
    protected void afterBindView() {
        mContentView.setVisibility(View.GONE);
        list_item = (XListView) findViewById(R.id.list_item);
        tv_title_center = (TextView) findViewById(R.id.tv_title_center);
        iv_tools_left = (Button) findViewById(R.id.iv_tools_left);
        tv_title_center.setText("团队详情");

        Intent intent = getIntent();
        doctorGroupID= intent.getStringExtra("doctorGroupID");
        dataBean= (NewChangeDoctorTeam.DataBean) intent.getSerializableExtra("DataBean");
        text_title.setText(dataBean.getGroupInfo().getGroupName());//团队名字
        text_qianyue.setText(dataBean.getGroupInfo().getSignatureCount()+"");//签约数量
        text_info.setText(dataBean.getGroupInfo().getRemark());

        adapter=new TeamDetailAdapter(this,dataBean.getGroupInfo().getDoctorGroupMembers());
        list_item.setAdapter(adapter);


        //回退
        iv_tools_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //设置XlistView 属性
        list_item.setXListViewListener(this);
        list_item.setPullRefreshEnable(true);
        list_item.setPullLoadEnable(true);

    }

//    private void getDoctorGroupInfo(String doctorgroupid) {
//        Http_DoctorTeamDetail event = new Http_DoctorTeamDetail(doctorgroupid,"1", "10", new HttpListener<List<DoctorGroupInfo.DataBean.DoctorGroupMembersBean>>() {
//
//                @Override
//                public void onError(int code, String msg) {
//                    Log.i("nihao", msg + code);
//                }
//
//                @Override
//                public void onSuccess(List<DoctorGroupInfo.DataBean.DoctorGroupMembersBean> resultDataBean) {
//                    setData(resultDataBean);
//                }
//            });
//            NetService.createClient(this, "https://tfamilyapi.kmwlyy.com:8063", event).start();
//
//    }

//    private void setData(List<DoctorGroupInfo.DataBean.DoctorGroupMembersBean> resultDataBean) {
//        adapter=new TeamDetailAdapter(this,resultDataBean);
//        list_item.setAdapter(adapter);
//    }

    @Override
    public void onRefresh() {
        adapter.notifyDataSetChanged();
        list_item.stopRefresh();
    }

    @Override
    public void onLoadMore() {

    }
}
