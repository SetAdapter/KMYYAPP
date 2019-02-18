package com.kmwlyy.doctor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.model.SingedMemberBean;
import com.kmwlyy.doctor.model.SingedPeopleBean;
import com.kmwlyy.personinfo.PersonInfoActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.winson.ui.widget.CommonAdapter;
import com.winson.ui.widget.ViewHolder;

import java.util.List;

/**
 * Created by TFeng on 2017/6/30.
 */

public class SingedPeopleAdapter extends CommonAdapter<SingedMemberBean> {

    private Context mContext;
    private List<SingedMemberBean> mDataList;
    private LayoutInflater inflater;
    private SingedMemberBean singedMemberBean;





    public SingedPeopleAdapter(Context mContext, List<SingedMemberBean> mDataList) {
        super(mContext,R.layout.item_singed_people,mDataList);
        this.mContext = mContext;
        this.mDataList = mDataList;
        this.inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        if(mDataList != null || mDataList.size()>0){
            return mDataList.size();
        }
        return 0;
    }

    @Override
    public SingedMemberBean getItem(int i) {
        if(mDataList != null || mDataList.size()>0){
            return mDataList.get(i);
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        ViewHolder holder;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_singed_people,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        singedMemberBean = mDataList.get(i);
        holder.tv_item_singed_people_name.setText(singedMemberBean.getUserMember().getMemberName());

        int gender = singedMemberBean.getUserMember().getGender();
        if(gender == 0){
            holder.tv_item_singed_people_gender.setText("男");
        }else{
            holder.tv_item_singed_people_gender.setText("女");
        }

        holder.tv_item_singed_people_age.setText(singedMemberBean.getUserMember().getAge()+"岁");
        ImageLoader.getInstance().displayImage(singedMemberBean.getUserMember().getPhotoUrl(),
                holder.iv_item_user_icon, PersonInfoActivity.getCircleDisplayOptions(R.drawable.default_avatar_patient));
        return convertView;
    }



    @Override
    public void convert(com.winson.ui.widget.ViewHolder viewHolder, SingedMemberBean obj, int position) {

    }

    private static class ViewHolder{
        @ViewInject(R.id.tv_item_singed_people_name)
        TextView tv_item_singed_people_name;
        @ViewInject(R.id.tv_item_singed_people_age)
        TextView tv_item_singed_people_age;
        @ViewInject(R.id.tv_item_singed_people_gender)
        TextView tv_item_singed_people_gender;
        @ViewInject(R.id.iv_item_user_icon)
        ImageView iv_item_user_icon;

        public ViewHolder(View view) {
            ViewUtils.inject(this,view);
        }
    }

    @Override
    public void replaceData(List<SingedMemberBean> datas) {
        mDataList.clear();
        mDataList = datas;
        notifyDataSetChanged();
    }
}
