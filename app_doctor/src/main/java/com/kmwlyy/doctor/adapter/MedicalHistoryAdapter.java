package com.kmwlyy.doctor.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kmwlyy.core.net.event.HttpUserMember;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.model.MedicalHistoryBean;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.networkbench.agent.impl.harvest.type.HarvestableObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by TFeng on 2017/7/12.
 */

public class MedicalHistoryAdapter extends BaseAdapter {
    private static final int TYPE_ADD = 0;
    private static final int TYPE_ITEM = 1;

    private List<MedicalHistoryBean> mDataList;
    private Context mContext;
    private int type;
    private String mDate;

    public MedicalHistoryAdapter(Context context,List<MedicalHistoryBean> dataList){
        mContext = context;
        mDataList = dataList;
    }

    public void update(List<MedicalHistoryBean> dataList){
        mDataList.clear();
        mDataList = dataList;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        if(mDataList != null || mDataList.size()>0 ){
            return mDataList.size()+1;
        }
        return 1;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        type = getItemViewType(i);

        if(type == TYPE_ADD){
            AddViewHolder holder = null;
            if(view == null){
                view = View.inflate(mContext, R.layout.item_add,null);
                holder = new AddViewHolder(view);
                view.setTag(holder);
            }else{
                holder = (AddViewHolder) view.getTag();
            }


        }else{
            ItemViewHolder itemHolder = null;
            if(view == null){
                view = View.inflate(mContext,R.layout.item_medical_history,null);
                itemHolder = new ItemViewHolder(view);
                view.setTag(itemHolder);
            }else{
                itemHolder = (ItemViewHolder) view.getTag();
            }
            mDate = mDataList.get(i).getDate();



            itemHolder.item_tv_date.setText(mDate.substring(0,10));


        }
        return view;
        /*switch (type){
            case TYPE_ADD:
                AddViewHolder holder = null;
                if(view == null){
                    view = View.inflate(mContext, R.layout.item_add,null);
                    holder = new AddViewHolder(view);
                    view.setTag(holder);
                }else{
                    holder = (AddViewHolder) view.getTag();
                }
                break;
            case TYPE_ITEM:
                ItemViewHolder itemHolder = null;
                if(view == null){
                    view = View.inflate(mContext,R.layout.item_medical_history,null);
                    itemHolder = new ItemViewHolder(view);
                    view.setTag(itemHolder);
                }else{
                    itemHolder = (ItemViewHolder) view.getTag();
                }

                itemHolder.item_tv_date.setText(mDataList.get(i).getDate());
                itemHolder.item_iv_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ToastUtils.showShort(mContext,"删除");
                    }
                });
                break;
        }


        return view;*/
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {

        if(mDataList.size() == 0 || mDataList == null){
            return TYPE_ADD;
        }else{
            if(position  == mDataList.size()){
                return TYPE_ADD;
            }else{
                return TYPE_ITEM;
            }
        }


    }

    class AddViewHolder{
        public AddViewHolder(View view){
            ViewUtils.inject(this,view);

        }
    }
    class ItemViewHolder{
        @ViewInject(R.id.item_tv_date)
        TextView item_tv_date;


        public ItemViewHolder(View view){
            ViewUtils.inject(this,view);
        }
    }
}
