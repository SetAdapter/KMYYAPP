package com.kmwlyy.doctor.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.model.HomeMemberBean;
import com.kmwlyy.doctor.model.SingedMemberBean;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.winson.ui.widget.CommonAdapter;
import com.winson.ui.widget.ViewHolder;

import java.util.List;

/**
 * Created by TFeng on 2017/7/3.
 */

public class HomeMemberListAdapter extends CommonAdapter<SingedMemberBean.UserMemberBean> {

    private Context mContext;

    private List<SingedMemberBean.UserMemberBean> mDataList;
    private SingedMemberBean.UserMemberBean mHomeMemberBean;

    public HomeMemberListAdapter(Context context, List<SingedMemberBean.UserMemberBean> datas) {
        super(context, R.layout.item_home_memeber, datas);
        mContext = context;

        mDataList = datas;
    }

    @Override
    public int getCount() {
        if(mDataList != null || mDataList.size()>0){
            return mDataList.size();
        }
        return 0;
    }

    @Override
    public SingedMemberBean.UserMemberBean getItem(int i) {
        return mDataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view == null){
            view = View.inflate(mContext,R.layout.item_home_memeber,null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }

        mHomeMemberBean = mDataList.get(position);
        holder.tv_item_member_name.setText(mHomeMemberBean.getMemberName());

        switch (mHomeMemberBean.getRelation()){
            case 0:
                holder.tv_item_member_relationship.setText("自己");
                break;
            case 1:
                holder.tv_item_member_relationship.setText("配偶");
                break;
            case 2:
                holder.tv_item_member_relationship.setText("父亲");
                break;
            case 3:
                holder.tv_item_member_relationship.setText("母亲");
                break;
            case 4:
                holder.tv_item_member_relationship.setText("儿子");
                break;
            case 5:
                holder.tv_item_member_relationship.setText("女儿");
                break;
            case 6:
                holder.tv_item_member_relationship.setText("其他");
                break;
        }






        return view;
    }

    @Override
    public void convert(com.winson.ui.widget.ViewHolder viewHolder, SingedMemberBean.UserMemberBean obj, int position) {

    }


    static class ViewHolder{
        @ViewInject(R.id.tv_item_member_name)
        TextView tv_item_member_name;
        @ViewInject(R.id.tv_item_member_relationship)
        TextView tv_item_member_relationship;


        public ViewHolder(View view){
            ViewUtils.inject(this,view);
        }

    }

    @Override
    public void replaceData(List<SingedMemberBean.UserMemberBean> datas) {
        super.replaceData(datas);
        mDataList.clear();
        mDataList = datas;
        notifyDataSetChanged();
    }
}
