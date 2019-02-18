package com.kmwlyy.patient.casehistory;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.net.bean.UserMember;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.helper.base.BaseActivity;
import com.kmwlyy.patient.helper.net.NetService;
import com.kmwlyy.patient.helper.net.bean.OptionsBean;
import com.kmwlyy.patient.helper.net.bean.UploadImageBean;
import com.kmwlyy.patient.helper.net.bean.UploadImageResp;
import com.kmwlyy.patient.helper.net.bean.UserMemberEMRsInfo;
import com.kmwlyy.patient.helper.net.event.HttpIM;
import com.kmwlyy.patient.helper.net.event.HttpUserConsults;
import com.kmwlyy.patient.helper.net.event.UserMemberEMRsEvent;
import com.kmwlyy.patient.helper.utils.EventApi;
import com.kmwlyy.usermember.UserMemberSelectActivity;
import com.ta.utdid2.android.utils.StringUtils;
import com.winson.ui.widget.CommonAdapter;
import com.winson.ui.widget.ResizeGridView;
import com.winson.ui.widget.ViewHolder;
import com.winson.ui.widget.listview.PageListView;
import com.winson.ui.widget.listview.PageListViewHelper;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import cn.qqtheme.framework.picker.DatePicker;
import me.nereo.multi_image_selector.MultiImageSelector;

/**
 * Created by xcj on 2016/12/12.
 */

public class EditCaseHistoryActivity extends BaseActivity implements View.OnClickListener {

    public static final String IS_EDIT = "IS_EDIT";
    public static final String USER_MEMBER_EMRID = "USER_MEMBER_EMRID";
    @BindView(R.id.tv_left)
    TextView tv_left;
    @BindView(R.id.tv_center)
    TextView tv_center;
    @BindView(R.id.tv_right)
    TextView mDetailsTxt;
    @BindView(R.id.grid_upload_pictures)
    ResizeGridView mGridView;
    @BindView(R.id.ll_history)
    LinearLayout mHistoryLinearLayout;
    @BindView(R.id.ll_name)
    LinearLayout mNameLinearLayout;
    @BindView(R.id.ll_data)
    LinearLayout mDataLinearLayout;
    @BindView(R.id.ll_hospital_name)
    LinearLayout mHospitalNameLinearLayout;
    @BindView(R.id.tv_date)
    TextView mDataTxt;
    @BindView(R.id.spinner)
    EditText mSpinner;
    @BindView(R.id.tv_name)
    TextView mNameTxt;
    @BindView(R.id.edit_disease_description)
    EditText mDiseaseDescription;
    @BindView(R.id.edit_hospital_name)
    EditText mHospitalName;
    @BindView(R.id.rl_option)
    RelativeLayout mOptions;

    private static final int REQUEST_IMAGE = 2;
    private ArrayList<String> mSelectPath = new ArrayList<>();
    private UserMember mUserMember = new UserMember();
    private boolean mIsEdit = false;
    private String mUserMemberId;
    private HttpClient mGetFamilyDoctorListClient;
    private PageListView mPageListview;
    private PageListViewHelper<OptionsBean> mPageListViewHelper;
    private PopupWindow popupWindow;
    private ArrayList<OptionsBean> optionData;
    /**
     * 需要上传的图片路径  控制默认图片在最后面需要用LinkedList
     */
    private LinkedList<UploadImageBean> dataList = new LinkedList<UploadImageBean>();

    private ArrayList<UploadImageBean> updateList = new ArrayList<UploadImageBean>();
    /**
     * 图片上传Adapter
     */
    private EditImageAdapter adapter;
    private ProgressDialog mOnCreateConsultDialog;

    public static void startEditCaseHistoryActivity(Context context, boolean isEdit, String id) {

        Intent intent = new Intent(context, EditCaseHistoryActivity.class);
        intent.putExtra(IS_EDIT, isEdit);
        intent.putExtra(USER_MEMBER_EMRID, id);
        context.startActivity(intent);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_case_history);
        butterknife.ButterKnife.bind(this);
        mDetailsTxt.setVisibility(View.VISIBLE);
        mDetailsTxt.setText(getString(R.string.case_save));
        dataList.addLast(null);// 初始化第一个添加按钮数据

        mIsEdit = getIntent().getBooleanExtra(IS_EDIT, false);
        if (mIsEdit) {
            tv_center.setText(getString(R.string.edit_case_title));
            mUserMemberId = getIntent().getStringExtra(USER_MEMBER_EMRID);
            getInfo(mUserMemberId);
        } else {
            tv_center.setText(getString(R.string.new_case_title));
        }
        adapter = new EditImageAdapter(this, dataList);
        mGridView.setAdapter(adapter);
        mGridView.setOnItemClickListener(mItemClick);
        mHistoryLinearLayout.setOnClickListener(this);
        mNameLinearLayout.setOnClickListener(this);
        mDataLinearLayout.setOnClickListener(this);
        tv_left.setOnClickListener(this);
        mDetailsTxt.setOnClickListener(this);
        mOptions.setOnClickListener(this);
        initOption();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UserMemberSelectActivity.SELECT_USER_MEMBER_REQ && data != null) {
            mUserMember = (UserMember) data.getSerializableExtra(UserMemberSelectActivity.USER_MEMBER_INFO);
            mNameTxt.setText(mUserMember.mMemberName);
        }
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
                adapter.update(dataList); // 刷新图片

            }
        }
    }

    /**
     * 上传图片GridView Item单击监听
     */
    private AdapterView.OnItemClickListener mItemClick = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            int i = adapter.getCount() - 1;
            if (mSelectPath != null || mSelectPath.size() != 0) {
                mSelectPath.clear();
                for (int j = 0; j < i; j++) {
                    if (!adapter.getItem(j).isNet()) {
                        String path = adapter.getItem(j).getUrl();
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

            MultiImageSelector.create(EditCaseHistoryActivity.this)
                    .showCamera(true)
                    .count(9 - m)
                    .multi()
                    .origin(mSelectPath)
                    .start(EditCaseHistoryActivity.this, REQUEST_IMAGE);

        }
    };

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_left:
                finish();
                break;
            case R.id.ll_data:
                showDataPick();
                break;
            case R.id.ll_name:
                selectUserMember();
                break;
            case R.id.tv_right:
                showLoadDialog(R.string.create_electron_case);
                mDetailsTxt.setEnabled(false);
                buyConsultService();
                break;
            case R.id.rl_option:
                if (!popupWindow.isShowing()) {
                    popupWindow.showAsDropDown(mOptions, 0, 0);
                }
                break;
        }
    }

    private void showDataPick() {
        String time = mDataTxt.getText().toString();
        DatePicker picker = new DatePicker(EditCaseHistoryActivity.this);
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
                    ToastUtils.showShort(EditCaseHistoryActivity.this, "不能选择大于当前时间的值");
                    showDataPick();
                    return;
                } else if (systemYear == Integer.valueOf(year)) {
                    if (systemMonth < Integer.valueOf(month)) {
                        ToastUtils.showShort(EditCaseHistoryActivity.this, "不能选择大于当前时间的值");
                        showDataPick();
                        return;
                    } else if (systemMonth == Integer.valueOf(month)) {
                        if (systemDay < Integer.valueOf(day)) {
                            ToastUtils.showShort(EditCaseHistoryActivity.this, "不能选择大于当前时间的值");
                            showDataPick();
                            return;
                        }
                    }
                }
                mDataTxt.setText(year + "-" + month + "-" + day);
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

    private void selectUserMember() {
//        CommonUtils.startActivity(this, SelectUserMemberActivity.class);
        Intent intent = new Intent(this, UserMemberSelectActivity.class);
        startActivityForResult(intent, UserMemberSelectActivity.SELECT_USER_MEMBER_REQ);
    }

    //保存数据
    private void saveInfo(ArrayList<HttpUserConsults.ImageFile> imageFileList) {
        String codeString = mSpinner.getText().toString();
        imageFileList = addNetPic(imageFileList);
        String id = "";
        if (mIsEdit) {
            id = mUserMemberId;
        }
        UserMemberEMRsEvent.SaveInfo saveInfo = new UserMemberEMRsEvent.SaveInfo(id, mUserMember.mMemberID, codeString, mDataTxt.getText().toString(), mHospitalName.getText().toString(), mDiseaseDescription.getText().toString(), imageFileList, new HttpListener() {
            @Override
            public void onError(int code, String msg) {
                dismissLoadDialog();
                mDetailsTxt.setEnabled(true);
                if (mIsEdit) {
                    ToastUtils.showShort(EditCaseHistoryActivity.this, getString(R.string.edit_case_fail));
                } else {
                    ToastUtils.showShort(EditCaseHistoryActivity.this, getString(R.string.new_case_fail));
                }
            }

            @Override
            public void onSuccess(Object o) {
                dismissLoadDialog();
                mDetailsTxt.setEnabled(true);
                EventApi.CaseHistory caseHistory = new EventApi.CaseHistory();
                EventBus.getDefault().post(caseHistory);
                finish();
            }
        });

        HttpClient saveInfoClient = NetService.createClient(this, saveInfo);
        saveInfoClient.start();
    }

    private void buyConsultService() {
        if (TextUtils.isEmpty(mSpinner.getText().toString())) {
            dismissLoadDialog();
            mDetailsTxt.setEnabled(true);
            ToastUtils.showShort(EditCaseHistoryActivity.this, getString(R.string.plz_choose_case_name));
            return;
        }
        if (TextUtils.isEmpty(mUserMember.mMemberID)) {
            dismissLoadDialog();
            mDetailsTxt.setEnabled(true);
            ToastUtils.showShort(EditCaseHistoryActivity.this, getString(R.string.plz_choose_name_of_patient));
            return;
        }
        if (TextUtils.isEmpty(mDataTxt.getText().toString())) {
            dismissLoadDialog();
            mDetailsTxt.setEnabled(true);
            ToastUtils.showShort(EditCaseHistoryActivity.this, getString(R.string.plz_choose_time_of_see_doctor));
            return;
        }
        if (TextUtils.isEmpty(mHospitalName.getText().toString())) {
            dismissLoadDialog();
            mDetailsTxt.setEnabled(true);
            ToastUtils.showShort(EditCaseHistoryActivity.this, getString(R.string.plz_choose_hospital_of_see_doctor));
            return;
        }
        final ArrayList<HttpUserConsults.ImageFile> imageFileList = new ArrayList<>();
        dataList = adapter.getData();
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

    private void getInfo(String id) {
        UserMemberEMRsEvent.GetDetail detail = new UserMemberEMRsEvent.GetDetail(id, new HttpListener<UserMemberEMRsInfo.ListItem>() {
            @Override
            public void onError(int code, String msg) {

            }

            @Override
            public void onSuccess(UserMemberEMRsInfo.ListItem listItem) {
                mNameTxt.setText(listItem.mMemberName);
                mSpinner.setText(listItem.mEMRName);
                mDataTxt.setText(listItem.mDate.substring(0, 10));
                mHospitalName.setText(listItem.mHospitalName);
                mDiseaseDescription.setText(listItem.mRemark);
                mUserMember.mMemberID = listItem.mMemberID;
                for (UserMemberEMRsInfo.File p : listItem.mFiles) {
                    if (dataList.size() == 10) {
                        continue;
                    }
                    UploadImageBean uploadImageBean = new UploadImageBean();
                    uploadImageBean.setUrl(p.mFileUrl);
                    uploadImageBean.setUrlPrefix(p.mUrlPrefix);
                    uploadImageBean.setNet(true);
                    dataList.add(dataList.size() - 1, uploadImageBean);
                }
                adapter.update(dataList); // 刷新图片

            }
        });

        mGetFamilyDoctorListClient = NetService.createClient(this, detail);
        mGetFamilyDoctorListClient.start();
    }

    private int getLength() {
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

    private void getNetDateList() {

    }

    private void getOptions() {
        UserMemberEMRsEvent.GetOptions getOptions = new UserMemberEMRsEvent.GetOptions("emrname", new HttpListener<ArrayList<OptionsBean>>() {
            @Override
            public void onError(int code, String msg) {
                mPageListViewHelper.setRefreshing(false);
            }

            @Override
            public void onSuccess(ArrayList<OptionsBean> optionsBeen) {
                optionData = optionsBeen;
                mPageListViewHelper.setRefreshing(false);
                mPageListViewHelper.refreshData(optionsBeen);
            }
        });
        mGetFamilyDoctorListClient = NetService.createClient(this, getOptions);
        mGetFamilyDoctorListClient.start();
    }

    private void initOption() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.layout_option, null);
        mPageListview = (PageListView) view.findViewById(R.id.doctor_page_listview);
        mPageListViewHelper = new PageListViewHelper<>(mPageListview, new OptionListAdapter(EditCaseHistoryActivity.this, null));
        mPageListViewHelper.getListView().setDividerHeight(0);
        mPageListViewHelper.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    mSpinner.setText(optionData.get(position).mValue);
                }
            }
        });
        popupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.card_bg));
        popupWindow.setWidth(LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        popupWindow.update();
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        getOptions();

    }

    class OptionListAdapter extends CommonAdapter<OptionsBean> {
        public OptionListAdapter(Context context, List<OptionsBean> datas) {
            super(context, R.layout.item_options, datas);
        }

        @Override
        public void convert(ViewHolder viewHolder, final OptionsBean data, int position) {
            TextView mName = (TextView) viewHolder.findViewById(R.id.tv_name);
            mName.setText(data.mValue);
        }
    }
}
