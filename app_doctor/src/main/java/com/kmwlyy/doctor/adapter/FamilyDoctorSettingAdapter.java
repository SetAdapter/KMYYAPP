package com.kmwlyy.doctor.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.model.HomeSettingBean;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

import cn.jpush.android.service.ServiceInterface$TagAliasOperator;

/**
 * Created by TFeng on 2017/7/17.
 */

public class FamilyDoctorSettingAdapter extends BaseAdapter{
    private int mTouchPostion = -1;
    private boolean isPrice;
    private Context mContext;
    String center;
    private List<HomeSettingBean.DoctorPackageBean> mDataList;
    private HomeSettingBean.DoctorPackageBean mDoctorPackageBean;
    private String mPrice;
    private String mCount;


    public FamilyDoctorSettingAdapter(Context context,boolean isPrice,List<HomeSettingBean.DoctorPackageBean> dataList){
        this.isPrice = isPrice;
        mContext = context;
        mDataList = dataList;
    }
    @Override
    public int getCount() {

        if(mDataList == null || mDataList.size()== 0){
            return 0;
        }else{
            return mDataList.size();
        }
    }

    @Override
    public HomeSettingBean.DoctorPackageBean getItem(int i) {
        return mDataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int postion, View convertView, ViewGroup viewGroup) {
        FamliyDoctorViewHolder holer;
        if(convertView == null){
            convertView = View.inflate(mContext, R.layout.item_familydoctor_setting,null);
            holer = new FamliyDoctorViewHolder(convertView);
            holer.et_center.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    mTouchPostion = (Integer) view.getTag();
                    return false;
                }
            });

            holer.mTextWatcher = new MyTextWatcher();
            holer.et_center.addTextChangedListener(holer.mTextWatcher);
            holer.updatePosition(postion);
            convertView.setTag(holer);
        }else{
            holer = (FamliyDoctorViewHolder) convertView.getTag();
            holer.updatePosition(postion);
        }

        mDoctorPackageBean = mDataList.get(postion);

        holer.tv_item_left.setText(mDoctorPackageBean.getMonthCountName());
        if(isPrice){
            holer.tv_item_right.setText(R.string.string_price_yuan);
            holer.et_center.setText(mDoctorPackageBean.getServicePrice());
            mPrice = mDoctorPackageBean.getServicePrice();
        }else {
            holer.tv_item_right.setText(R.string.ci);
            holer.et_center.setText(mDoctorPackageBean.getVidServiceCount());
            mCount = mDoctorPackageBean.getVidServiceCount();
        }

        holer.et_center.setTag(postion);
        if(mTouchPostion == postion){
            holer.et_center.requestFocus();
            holer.et_center.setSelection(holer.et_center.getText().length());
        }else{
            holer.et_center.clearFocus();
        }

        return convertView;
    }

    public List<HomeSettingBean.DoctorPackageBean> getDataList() {
        return mDataList;
    }

    public class FamliyDoctorViewHolder{
        @ViewInject(R.id.tv_item_left)
        TextView tv_item_left;
        @ViewInject(R.id.tv_item_right)
        TextView tv_item_right;
        @ViewInject(R.id.et_center)
        EditText et_center;
       MyTextWatcher mTextWatcher;

        //动态更新TextWathcer的position
        public void updatePosition(int position) {
            mTextWatcher.updatePosition(position);
        }


        public FamliyDoctorViewHolder(View view) {
            ViewUtils.inject(this,view);
        }
    }
    public class MyTextWatcher implements TextWatcher{

        private int mPosition;

        public void updatePosition(int position) {
            mPosition = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {


            if(isPrice){
                mDataList.get(mPosition).setServicePrice(editable.toString().trim());
            }else{

                mDataList.get(mPosition).setVidServiceCount(editable.toString().trim());
            }

        }
    }
}
