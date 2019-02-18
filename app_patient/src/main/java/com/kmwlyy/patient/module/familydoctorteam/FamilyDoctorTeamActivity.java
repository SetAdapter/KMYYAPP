package com.kmwlyy.patient.module.familydoctorteam;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.thirdparty.P;
import com.kmwlyy.core.base.BaseActivity;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.net.NewHttpClient;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.helper.net.NetService;
import com.kmwlyy.patient.module.InhabitantStart.InhabitanStartActivity;
import com.winson.ui.widget.EmptyViewUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 家庭医生团队   更换医生团队
 * Created by Stefan on 2017/7/30.
 */

public class FamilyDoctorTeamActivity extends BaseActivity {
    TextView tv_title_center;
    Button iv_tools_left;
    EditText edit_search;
    ListView listView_doctor;
    DoctorTeamAdapter adapter;
    ViewGroup mContent;
    //NewChangeDoctorTeam DataBean;
    String OrgnazitionID; //"医生团队所属机构ID（甲方）
    private String searchText;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_family_doctor_team;
    }

    @Override
    protected void afterBindView() {
        OrgnazitionID = getIntent().getStringExtra("mOrgnazitionID");
        initView();
        initData();
    }


    private void initView() {
        listView_doctor = (ListView) findViewById(R.id.listView_doctor);
        mContent = (ViewGroup) findViewById(R.id.liner);
        tv_title_center = (TextView) findViewById(R.id.tv_title_center);
        iv_tools_left = (Button) findViewById(R.id.iv_tools_left);
        edit_search = (EditText) findViewById(R.id.edit_search);
        tv_title_center.setText("家庭医生团队");
        //回退
        iv_tools_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //搜索
        edit_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchText = edit_search.getText().toString();
                    ((InputMethodManager) edit_search.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                                            .hideSoftInputFromWindow(FamilyDoctorTeamActivity.this
                                            .getCurrentFocus()
                                            .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    if (adapter.getCount() < 1) {
                        EmptyViewUtils.showEmptyView(mContent, "没有找到相关医生", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                EmptyViewUtils.removeEmptyView(mContent);
                            }
                        });
                    }
                    getDoctorSearch(searchText);
                    adapter.notifyDataSetChanged();
                    return true;
                }
                return false;
            }
        });

//        edit_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
//                    searchText = edit_search.getText().toString();
//                    //强行弹出键盘
////                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
////                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//                    if (adapter.getCount() < 1) {
//                        EmptyViewUtils.showEmptyView(mContent, "没有找到相关医生", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                EmptyViewUtils.removeEmptyView(mContent);
//                            }
//                        });
//                    }
//                    getDoctorSearch(searchText);
//                    adapter.notifyDataSetChanged();
//                    return true;
//                }
//                return false;
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    //医生搜索
    private void getDoctorSearch(String searchText) {
        showDialog(getResources().getString(R.string.patient_loading));
        Http_SearchDoctorTeam event = new Http_SearchDoctorTeam("1", "10", OrgnazitionID, searchText, new HttpListener<List<NewChangeDoctorTeam.DataBean>>() {
            @Override
            public void onError(int code, String msg) {
                hideDialog();
                Log.i("nihao", msg + code);
            }

            @Override
            public void onSuccess(List<NewChangeDoctorTeam.DataBean> resultDataBean) {
                hideDialog();
                adapter = new DoctorTeamAdapter(FamilyDoctorTeamActivity.this, resultDataBean);
                listView_doctor.setAdapter(adapter);
            }
        });
        NetService.createClient(this, HttpClient.FAMILY_URL, event).start();
    }

    private void initData() {
        Http_ChangeDoctorTeam event = new Http_ChangeDoctorTeam("1", "10", OrgnazitionID, new HttpListener<List<NewChangeDoctorTeam.DataBean>>() {
            @Override
            public void onError(int code, String msg) {
                Log.i("nihao", msg + code);
            }

            @Override
            public void onSuccess(List<NewChangeDoctorTeam.DataBean> resultDataBean) {
                setData(resultDataBean);
            }
        });
        NetService.createClient(this, HttpClient.FAMILY_URL, event).start();
    }

    public void setData(final List<NewChangeDoctorTeam.DataBean> data) {
        adapter = new DoctorTeamAdapter(FamilyDoctorTeamActivity.this, data);
        listView_doctor.setAdapter(adapter);
        listView_doctor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String orgnazitionID = data.get(position).getGroupInfo().getOrgnazitionID();//机构ID
                String doctorGroupID = data.get(position).getGroupInfo().getDoctorGroupID();//团队ID
                String leaderName = data.get(position).getGroupInfo().getLeaderName();//队长名字
                int size = data.size();//团队成员数量
                String groupName = data.get(position).getGroupInfo().getGroupName();//团队名字
                String hospitalName = data.get(position).getHospitalName();
                Intent intent = new Intent(FamilyDoctorTeamActivity.this, InhabitanStartActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("orgnazitionID", orgnazitionID);
                bundle.putString("doctorGroupID", doctorGroupID);
                bundle.putString("leaderName", leaderName);
                bundle.putInt("size", size);
                bundle.putString("groupName", groupName);
                bundle.putString("hospitalName", hospitalName);
                bundle.putSerializable("DataBean", data.get(position));
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
