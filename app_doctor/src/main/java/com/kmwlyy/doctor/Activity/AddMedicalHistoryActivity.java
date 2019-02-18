package com.kmwlyy.doctor.Activity;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.text.TextUtils;

import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;


import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.net.bean.UploadImageResp;
import com.kmwlyy.core.net.event.HttpIM;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.doctor.NetService;
import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.Utils.AppEventApi;
import com.kmwlyy.doctor.adapter.MedicalHitoryUploadAdapter;

import com.kmwlyy.doctor.model.MedicalHistoryBean;
import com.kmwlyy.doctor.model.UploadImageBean;
import com.kmwlyy.doctor.model.httpEvent.HttpUserConsults;
import com.kmwlyy.doctor.model.httpEvent.MedicalHistoryEvent;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import com.winson.ui.widget.InsideGridView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import cn.org.bjca.signet.helper.utils.StringUtils;
import cn.qqtheme.framework.picker.DatePicker;
import me.nereo.multi_image_selector.MultiImageSelector;

/**
 * Created by TFeng on 2017/7/5.
 */

public class AddMedicalHistoryActivity extends BaseActivity {
    @ViewInject(R.id.tv_left)
    TextView tv_left;
    @ViewInject(R.id.iv_left)
    ImageView iv_left;
    @ViewInject(R.id.tv_right)
    TextView tv_right;
    @ViewInject(R.id.tv_center)
    TextView tv_center;
    @ViewInject(R.id.tv_modify)
    TextView tv_modify;
    @ViewInject(R.id.ll_check_day)
    LinearLayout ll_check_day;
    @ViewInject(R.id.tv_check_day)
    TextView tv_check_day;
    @ViewInject(R.id.et_medical_name)
    EditText et_medical_name;
    @ViewInject(R.id.et_medical_remark)
    EditText et_medical_remark;


    @ViewInject(R.id.grid_upload_pictures)
    InsideGridView grid_upload_pictures;

    /*图片上传的路径*/
    private LinkedList<UploadImageBean> dataList = new LinkedList<UploadImageBean>();
    private ArrayList<UploadImageBean> updateList = new ArrayList<UploadImageBean>();

    private static final int REQUEST_IMAGE = 2;
    private ArrayList<String> mSelectPath = new ArrayList<>();
    private MedicalHitoryUploadAdapter mAdapter;
    private String mEMRName;
    private String mDate;
    private String mRemark;

    private String mUserEMRID;
    private String mMemberID;
    private MedicalHistoryBean mMedicalHistory;
    private boolean mIsEdit;
    private List<MedicalHistoryBean.Files> mFiles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medical_history);
        ViewUtils.inject(this);

        getIntentInfo();
        initView();
        initListener();

    }
    private void getIntentInfo(){
        mMedicalHistory = (MedicalHistoryBean) getIntent().getSerializableExtra("MedicalHistory");
        mMemberID = getIntent().getStringExtra("MemberID");
        mIsEdit = getIntent().getBooleanExtra("IsEdit", false);
    }

    private void initView() {
        tv_left.setVisibility(View.GONE);
        iv_left.setVisibility(View.VISIBLE);
        tv_right.setVisibility(View.VISIBLE);
        tv_center.setText(R.string.add_medical_history);
        dataList.addLast(null);
        mAdapter = new MedicalHitoryUploadAdapter(mContext,dataList,9);
        grid_upload_pictures.setAdapter(mAdapter);

        if(mIsEdit){
            tv_center.setText("修改病历");
            tv_modify.setVisibility(View.VISIBLE);
            tv_modify.setText("删除");
            tv_right.setText("修改");
            et_medical_name.setText(mMedicalHistory.getEMRName());
            tv_check_day.setText(mMedicalHistory.getDate().substring(0,10));
            et_medical_remark.setText(mMedicalHistory.getRemark());
            mMemberID = mMedicalHistory.getMemberID();
            mFiles = mMedicalHistory.getFiles();
            mUserEMRID = mMedicalHistory.getUserMemberEMRID();
           refeshpic();
        }else{
            tv_right.setText(R.string.upload);
        }



    }

    private void refeshpic() {
        if(mFiles != null && mFiles.size()>0) {
            for (MedicalHistoryBean.Files p : mFiles) {
                if (dataList.size() == 10) {
                    continue;
                }
                UploadImageBean uploadImageBean = new UploadImageBean();
                uploadImageBean.setUrl(p.getFileUrl());
                uploadImageBean.setUrlPrefix(p.getUrlPrefix());
                uploadImageBean.setNet(true);
                dataList.add(dataList.size() - 1, uploadImageBean);

            }
            mAdapter.update(dataList); // 刷新图片
        }else {
            return;
        }
    }

    private void initListener() {
        tv_modify.setOnClickListener(this);
        iv_left.setOnClickListener(this);
        ll_check_day.setOnClickListener(this);
        grid_upload_pictures.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int postion, long l) {
                int i = mAdapter.getCount() - 1;
                if (mSelectPath != null || mSelectPath.size() != 0) {
                    mSelectPath.clear();
                    for (int j = 0; j < i; j++) {
                        if (!mAdapter.getItem(j).isNet()) {
                            String path = mAdapter.getItem(j).getUrl();
                            if (!TextUtils.isEmpty(path)) {
                                mSelectPath.add(path);
                            }
                        }
                    }
                }
                int m = 0;
                for (int k = 0; k < dataList.size() - 1; k++) {
                    if (dataList.get(k).isNet()) {
                        m++;
                    }
                }
                MultiImageSelector.create(AddMedicalHistoryActivity.this)
                        .showCamera(true)
                        .count(9-m)
                        .multi()
                        .origin(mSelectPath)
                        .start(AddMedicalHistoryActivity.this, REQUEST_IMAGE);

            }
        });
        tv_right.setOnClickListener(this);
        tv_modify.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                mSelectPath = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                if (mSelectPath.size() == 0) {
                    return;
                }
                if (dataList.getLast() == null) {
                    dataList.removeLast();
                }
                if (dataList.size() != 0) {
                    for (int i = dataList.size() - 1; i >= 0; i--) {
                        if (!dataList.get(i).isNet()) {
                            dataList.remove(i);
                        }
                    }
                }
                dataList.addLast(null);
                for (String p : mSelectPath) {
                    if (dataList.size() == 10) {
                        continue;
                    }
                    UploadImageBean uploadImageBean = new UploadImageBean();
                    uploadImageBean.setUrl(p);
                    uploadImageBean.setNet(false);
                    dataList.add(dataList.size() - 1, uploadImageBean);
                }
                mAdapter.update(dataList); // 刷新图片

            }
        }
    }

    @Override
    public void onClick(View arg0) {
        super.onClick(arg0);
        switch (arg0.getId()){
            case R.id.iv_left:
                finish();
                break;
            case R.id.tv_right:
                // TODO: 2017/7/5 上传数据处理
                showLoadDialog(R.string.upload_emr);
//                tv_right.setEnabled(false);
                check_upload();
                break;
            case R.id.ll_check_day:
                showSelectDay();
                break;
            case R.id.tv_modify:
                // TODO: 2017/7/13 删除
                delete();
                break;
        }
    }

    private void delete() {
        showLoadDialog(R.string.string_wait);
        MedicalHistoryEvent.DeleteMedicalHistory event = new MedicalHistoryEvent.DeleteMedicalHistory(mUserEMRID, new HttpListener() {
            @Override
            public void onError(int code, String msg) {
                dismissLoadDialog();
                ToastUtils.showShort(mContext,msg);
            }

            @Override
            public void onSuccess(Object o) {

                dismissLoadDialog();

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(o.toString());
                    boolean suceess = jsonObject.getBoolean("Data");
                    if(suceess){
                        ToastUtils.showShort(mContext,"删除成功");
                        EventBus.getDefault().post(new AppEventApi.Delete());
                        finish();
                    }else {
                        ToastUtils.showShort(mContext,"删除失败");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
        new HttpClient(mContext,event).startNewApi();

    }

    private void check_upload() {

        if(TextUtils.isEmpty(et_medical_name.getText().toString().trim())){
            dismissLoadDialog();
            tv_right.setEnabled(true);
            ToastUtils.showShort(mContext,"请填写病历名称");
            return;
        }else{
            mEMRName = et_medical_name.getText().toString().trim();
        }

        if(TextUtils.isEmpty(tv_check_day.getText().toString().trim())){
            dismissLoadDialog();
            tv_right.setEnabled(true);
            ToastUtils.showShort(mContext,"请填写病历日期");
            return;
        }else{
            mDate = tv_check_day.getText().toString().trim();
        }

        if(TextUtils.isEmpty(et_medical_remark.getText().toString().trim())){
            dismissLoadDialog();
            ToastUtils.showShort(mContext,"请填写病历说明");
            tv_right.setEnabled(true);
            return;
        }else{
            mRemark = et_medical_remark.getText().toString().trim();
        }



        final ArrayList<HttpUserConsults.ImageFile> imageFileList = new ArrayList<>();
        dataList = mAdapter.getData();
        if (!dataList.isEmpty() && !(dataList.get(0) == null)) {
            final int length = getLength();
            if (length == 0) {
                saveInfo(imageFileList);
            }
            getUpdateList();
            final Point point = new Point();
            for (final UploadImageBean bean : updateList) {
                HttpIM.UploadImage uploadImage = new HttpIM.UploadImage(bean.getUrl(), new HttpListener<UploadImageResp>() {
                    @Override
                    public void onError(int code, String msg) {
                        point.x++;
                        if (point.x >= length) {
                            // complete
                            saveInfo(imageFileList);
                        }
                    }
                    @Override
                    public void onSuccess(UploadImageResp data) {
                        point.x++;
                        HttpUserConsults.ImageFile imageFile = new HttpUserConsults.ImageFile();
                        imageFile.mFileUrl = data.mFileName;
                        imageFile.mFileType = 0;
                        imageFile.mFileName = data.mFileName;
                        imageFile.mRemark = data.mFileName;
                        imageFileList.add(imageFile);

                        if (point.x >= length) {
                            // complete
                            saveInfo(imageFileList);
                        }
                    }
                });

                HttpClient uploadImageClient = NetService.createClient(this, uploadImage);
//                uploadImageClient.start();
                uploadImageClient.imageStart();

            }
        } else {
            saveInfo(imageFileList);
        }


    }

    private void saveInfo(ArrayList<HttpUserConsults.ImageFile> imgFiles){
        imgFiles = addNetPic(imgFiles);
        String id = "";
        if(mIsEdit){
            id = mMedicalHistory.getUserMemberEMRID();
        }

        MedicalHistoryEvent.ModifyInfo event = new MedicalHistoryEvent.ModifyInfo(id, mMemberID,"", mEMRName, mDate, mRemark, imgFiles, new HttpListener() {
            @Override
            public void onError(int code, String msg) {
                dismissLoadDialog();
                tv_right.setEnabled(true);
                if (mIsEdit) {
                    ToastUtils.showShort(mContext,"编辑失败");
                } else {
                    ToastUtils.showShort(mContext, "添加失败");
                }
            }

            @Override
            public void onSuccess(Object o) {
                dismissLoadDialog();
                ToastUtils.showShort(mContext,"保存成功");
                EventBus.getDefault().post(new AppEventApi.Modify());
                finish();
            }
        });

        new HttpClient(mContext,event).startNewApi();


    }

    private ArrayList<HttpUserConsults.ImageFile> addNetPic(ArrayList<HttpUserConsults.ImageFile> imageFileList) {
        if (dataList.getLast() == null) {
            dataList.removeLast();
        }
        for (UploadImageBean bean : dataList) {
            if (bean.isNet()) {
                HttpUserConsults.ImageFile imageFile = new HttpUserConsults.ImageFile();
                imageFile.mFileUrl = bean.getUrl();
                imageFile.mFileType = 0;
                imageFile.mFileName = bean.getUrl();
                imageFile.mRemark = bean.getUrl();
                imageFileList.add(imageFile);
            }
        }

        return imageFileList;
    }

    private void getUpdateList() {
        if (dataList.getLast() == null) {
            dataList.removeLast();
        }
        for (UploadImageBean bean : dataList) {
            if (!bean.isNet()) {
                updateList.add(bean);
            }
        }
    }

    private void showSelectDay() {
        String time = tv_check_day.getText().toString();
        DatePicker picker = new DatePicker(AddMedicalHistoryActivity.this);
        picker.setRange(2000, 2030);
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                Calendar c = Calendar.getInstance();
                int systemYear = c.get(Calendar.YEAR);
                int systemMonth = c.get(Calendar.MONTH);
                int systemDay = c.get(Calendar.DAY_OF_MONTH);
                systemMonth = systemMonth + 1;
                if (systemYear < Integer.valueOf(year)) {
                    ToastUtils.showShort(AddMedicalHistoryActivity.this, "不能选择大于当前时间的值");
                    showSelectDay();
                    return;
                } else if (systemYear == Integer.valueOf(year)) {
                    if (systemMonth < Integer.valueOf(month)) {
                        ToastUtils.showShort(AddMedicalHistoryActivity.this, "不能选择大于当前时间的值");
                        showSelectDay();
                        return;
                    } else if (systemMonth == Integer.valueOf(month)) {
                        if (systemDay < Integer.valueOf(day)) {
                            ToastUtils.showShort(AddMedicalHistoryActivity.this, "不能选择大于当前时间的值");
                            showSelectDay();
                            return;
                        }
                    }
                }
                tv_check_day.setText(year + "-" + month + "-" + day);
            }
        });
        if (StringUtils.isEmpty(time)) {
            Calendar calendar = Calendar.getInstance();  //获取当前时间，作为图标的名字
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            picker.setSelectedItem(year, month, day);
        } else {
            String year = time.substring(0, 4);
            String month = time.substring(5, 7);
            String day = time.substring(8, 10);
            picker.setSelectedItem(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
        }
        picker.show();
    }

    private int getLength(){
        int i = 0;
        if (dataList.getLast() == null) {
            dataList.removeLast();
        }
        for (UploadImageBean bean : dataList) {
            if (!bean.isNet()) {
                ++i;
            }
        }
        return i;
    }


}
