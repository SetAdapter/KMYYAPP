package com.kmwlyy.patient.module.myservice.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.kmwlyy.patient.R;
import com.kmwlyy.patient.helper.utils.DateUtils;
import com.kmwlyy.patient.module.InhabitantStart.FamilyListBean;

import java.util.Date;
import java.util.List;

/**
 * 就诊人列表
 * email：fy310518@163.com
 * Created by fangs on 2017/8/10.
 */
public class VisitingPersonAdapter extends AdapterBase<FamilyListBean.ResultDataBean> {

    public VisitingPersonAdapter(Context context, List<FamilyListBean.ResultDataBean> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View arg1, ViewGroup arg2) {
        View itemView = getViewCache().get(position);
        if (null == itemView) {
            FamilyListBean.ResultDataBean datas = getData().get(position);
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_visiting_person, null);
            TextView tvName = (TextView) itemView.findViewById(R.id.tvName);
            TextView tvInfor = (TextView) itemView.findViewById(R.id.tvInfor);

            tvName.setText(datas.getName());
            String sex = datas.getGender() == 0 ? "男" : "女";
            Date date = new Date(datas.getBirthday());

            int age = DateUtils.getAgeByBirth(date);
            tvInfor.setText("（" + sex + age + "岁）");

            itemView.setTag(datas);
            getViewCache().put(position, itemView);
        }

        return itemView;
    }
}
