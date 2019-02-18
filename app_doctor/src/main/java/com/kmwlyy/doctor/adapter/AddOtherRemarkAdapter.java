package com.kmwlyy.doctor.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.model.HomeSettingBean;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by TFeng on 2017/7/17.
 */

public class AddOtherRemarkAdapter extends BaseAdapter {

    private Context mContext;
    private List<HomeSettingBean.DoctorPackageBean> mDataList;
    private HomeSettingBean.DoctorPackageBean mDoctorPackageBean;
    private int index;

    public AddOtherRemarkAdapter(Context context,List<HomeSettingBean.DoctorPackageBean> dataList){

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
    public View getView(final int postion, View convertView, ViewGroup viewGroup) {


        convertView = View.inflate(mContext, R.layout.item_add_remark,null);
        TextView tv_item_title = (TextView) convertView.findViewById(R.id.tv_item_title);
        EditText et_item_remark = (EditText)convertView.findViewById(R.id.et_item_remark);

        et_item_remark.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    index = postion;
                }
                return false;
            }
        });

        et_item_remark.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mDataList.get(postion).setRemark(editable.toString());

            }
        });

        mDoctorPackageBean = mDataList.get(postion);
        tv_item_title.setText(mDoctorPackageBean.getMonthCountName()+"服务包");
        et_item_remark.setText(mDoctorPackageBean.getRemark());

        et_item_remark.clearFocus();
        if(index != -1 && index == postion){
            et_item_remark.requestFocus();
        }
        et_item_remark.setSelection(et_item_remark.getText().length());

        return convertView;
    }

    public List<HomeSettingBean.DoctorPackageBean> getDataList() {
        return mDataList;
    }

    public class AddOtherRemarkViewHolder{
        @ViewInject(R.id.tv_item_title)
        TextView tv_item_title;
        @ViewInject(R.id.et_item_remark)
        EditText et_item_remark;

        RearkTextWatcher mTextWatcher;
        public AddOtherRemarkViewHolder(View view){
            ViewUtils.inject(this,view);
        }

        //动态更新TextWathcer的position
        public void updatePosition(int position) {
            mTextWatcher.updatePosition(position);
        }



    }
    public class RearkTextWatcher implements TextWatcher {

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
            mDataList.get(mPosition).setRemark(editable.toString());

        }
    }
}
