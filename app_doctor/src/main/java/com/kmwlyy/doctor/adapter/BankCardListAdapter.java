package com.kmwlyy.doctor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.model.BankCardBean;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;


import java.util.List;
import java.util.Map;

/**
 * @Description描述:
 * @Author作者: zhaoqile
 * @Date日期: 2016/8/19
 */
public class BankCardListAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<Map> list;
    private List<BankCardBean> dataList;


    private View.OnClickListener delete_click;

    private int flag = 0;

    public BankCardListAdapter(Context context,List<BankCardBean> dataList,View.OnClickListener delete_click){
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.dataList = dataList;
        this.delete_click = delete_click;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return dataList.size();
    }

    @Override
    public BankCardBean getItem(int position) {
        // TODO Auto-generated method stub
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public void showDelete(int flag){
        this.flag = flag;
//		notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;
        BankCardBean bean = getItem(position);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_bank_card, null);
            holder = new ViewHolder();
            ViewUtils.inject(holder, convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        if (flag == 0) {
            holder.btn_delete.setVisibility(View.GONE);
        }else{
            holder.btn_delete.setVisibility(View.VISIBLE);
        }

        holder.tv_bank.setText(bean.Bank);
        String code = bean.CardCode +"";
        holder.tv_num.setText(code.substring(0, 4)+"****"+code.substring(code.length()-3));

        holder.btn_delete.setTag(position);
        holder.btn_delete.setOnClickListener(delete_click);


        return convertView;
    }

    private class ViewHolder{
        @ViewInject(R.id.tv_bank)
        private TextView tv_bank;
        @ViewInject(R.id.tv_num)
        private TextView tv_num;

        @ViewInject(R.id.btn_delete)
        private Button btn_delete;
    }

}
