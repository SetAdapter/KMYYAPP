package com.kmwlyy.patient.module.myservice;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.kmwlyy.core.base.BaseActivity;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.net.NewHttpClient;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.module.InhabitantStart.FamilyListBean;
import com.kmwlyy.patient.module.InhabitantStart.Http_MyFamily;
import com.kmwlyy.patient.module.myservice.Adapter.VisitingPersonAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 选择就诊人
 * email：fy310518@163.com
 * Created by fangs on 2017/8/10.
 */
public class SelectVisitingPersonActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.tv_title_center)
    TextView tv_title_center;
    @BindView(R.id.lvSelectVisiting)
    ListView lvSelectVisiting;
    @BindView(R.id.iv_tools_left)
    Button iv_tools_left;
    VisitingPersonAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_selectvisiting;
    }

    @Override
    protected void afterBindView() {
        initView();
        getFamilyList();
        iv_tools_left.setOnClickListener(this);
    }

    private void initView() {
        tv_title_center.setText("选择就诊人");
        List<FamilyListBean.ResultDataBean> data = new ArrayList<>();
        adapter = new VisitingPersonAdapter(this, data);
        lvSelectVisiting.setAdapter(adapter);
        lvSelectVisiting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FamilyListBean.ResultDataBean item = (FamilyListBean.ResultDataBean) view.getTag();
                Intent intent = new Intent();
                intent.putExtra("FamilyListBeanResultDataBean", item);

                setResult(SelectVisitingPersonActivity.this.RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_tools_left:
                finish();
                break;
        }
    }

    //获取 家庭成员  列表
    private void getFamilyList() {
        Http_MyFamily.Http_FamilyList event = new Http_MyFamily.Http_FamilyList("2", new HttpListener<FamilyListBean>() {
            @Override
            public void onError(int code, String msg) {

            }

            @Override
            public void onSuccess(FamilyListBean familyListBean) {
                if (null != familyListBean && familyListBean.getResultData().size() > 0) {
                    List<FamilyListBean.ResultDataBean> resultData = familyListBean.getResultData();
                    adapter.setData(resultData);
                    adapter.clearCache();
                }

            }
        });

        new NewHttpClient(this, event).start();
    }
}
