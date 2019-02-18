package com.kmwlyy.patient.module.myfamiiydoctor;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kmwlyy.core.base.BaseActivity;
import com.kmwlyy.core.base.BaseApplication;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.helper.net.event.HttpUserConsults;
import com.kmwlyy.patient.helper.net.NetService;
import com.kmwlyy.patient.helper.utils.PUtils;
import com.kmwlyy.patient.module.sign_introduce.SignIntroduceActivity;
import com.kmwlyy.patient.module.termination.TerminationActivity;
import com.kmwlyy.patient.onlinediagnose.BuyIMConsultFamilyActivity;
import com.kmwlyy.patient.weight.NoScrollListView;

import java.util.List;

import butterknife.BindView;

/**
 * 家庭医生团队
 * Created by fangs on 2017/8/8.
 */
public class FamilyDoctorActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.tv_title_center)
    TextView tv_title_center;

    @BindView(R.id.iv_tools_left)
    Button iv_tools_left;

    @BindView(R.id.text_title)
    TextView text_title;

    @BindView(R.id.text_qianyue)
    TextView text_qianyue;
    @BindView(R.id.text_info)
    TextView text_info;

    @BindView(R.id.iv_tools_right)
    Button iv_tools_right;
    @BindView(R.id.submit)
    Button submit;

    @BindView(R.id.scorollView)
    ScrollView scorollView;

    @BindView(R.id.list_item)
    NoScrollListView list_item;
    List<MyFamilyDoctorDean.DataBean> mList;
    NewTeamDetailAdapter adapter;
    List data;
    private String[] name = new String[]{"签约介绍", "解约申请"};
    private PopupWindow popupWindow;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_family_doctor;
    }

    @Override
    protected void afterBindView() {
        scorollView.smoothScrollTo(0, 20);
        tv_title_center.setText("家庭医生团队");
        iv_tools_right.setVisibility(View.VISIBLE);
        iv_tools_right.setText("");
        iv_tools_left.setOnClickListener(this);
        iv_tools_right.setOnClickListener(this);
        submit.setOnClickListener(this);
        getData();

        Drawable img_off;
        Resources res = getResources();
        img_off = res.getDrawable(R.drawable.btn_morx);
        // 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
        img_off.setBounds(0, 0, img_off.getMinimumWidth(), img_off.getMinimumHeight());
        iv_tools_right.setCompoundDrawables(null, null, img_off, null);
    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.iv_tools_left:
                finish();
                break;
            case R.id.iv_tools_right:
                getType();
                break;
            case R.id.submit:
                BuyIMConsultFamilyActivity.startBuyIMConsultFamilyActivity(FamilyDoctorActivity.this, mList.get(0).getDoctorGroupID(),mList.get(0).getGroupName(), HttpUserConsults.FAMILY_DOCTOR, true);
                break;

        }
    }

    //添加数据
    private void getData() {
        Http_GetMyDoctorGroup event=new Http_GetMyDoctorGroup(new HttpListener<List<MyFamilyDoctorDean.DataBean>>() {
            @Override
            public void onError(int code, String msg) {

            }

            @Override
            public void onSuccess(List<MyFamilyDoctorDean.DataBean> dataBeen) {
                mList = dataBeen;
                text_title.setText(dataBeen.get(0).getGroupName());
                text_qianyue.setText(dataBeen.get(0).getSignatureCount()+"");
                text_info.setText(dataBeen.get(0).getLeaderSpecialty());

                adapter = new NewTeamDetailAdapter(FamilyDoctorActivity.this, dataBeen.get(0).getDoctorGroupMembers());
                list_item.setAdapter(adapter);
            }

        });

        NetService.createClient(this, HttpClient.FAMILY_URL, event).start();
    }

    /**
     * PopupWindow
     */
    public void getType() {
        View v = LayoutInflater.from(this).inflate(R.layout.pop_window, new LinearLayout(this), false);
        ListView listView = (ListView) v.findViewById(R.id.listview);
        final TypeAdapter adapter = new TypeAdapter(this, name);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String typeone = name[position];
                    if (typeone.equals("签约介绍".trim())) {
                        startActivity(new Intent(FamilyDoctorActivity.this,SignIntroduceActivity.class));
                    } else {
                        startActivity(new Intent(FamilyDoctorActivity.this,TerminationActivity.class));
                    }

                popupWindow.dismiss();
            }

        });
        popupWindow = new PopupWindow(v, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, false);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable(this.getResources(), Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)));
        View anchorView = findViewById(R.id.layout_title);
        if (Build.VERSION.SDK_INT < 24) {
            popupWindow.showAsDropDown(anchorView, 0, 0);
        } else {
            popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY, 0, anchorView.getHeight() + PUtils.getStatusBarHeight(this));
        }
    }


}
