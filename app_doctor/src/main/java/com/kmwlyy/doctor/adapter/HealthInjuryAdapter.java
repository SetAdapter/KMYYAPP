package com.kmwlyy.doctor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.model.SingedPeopleBean;
import com.kmwlyy.personinfo.PersonInfoActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by TFeng on 2017/6/30.
 */

public class HealthInjuryAdapter extends BaseAdapter {

    private Context mContext;
    private List<SingedPeopleBean> mDataList;
    private LayoutInflater inflater;
    private SingedPeopleBean singedPeopleBean;

    public HealthInjuryAdapter(Context mContext, List<SingedPeopleBean> mDataList) {
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
    public Object getItem(int i) {
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
            convertView = inflater.inflate(R.layout.item_health_injury,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        singedPeopleBean = mDataList.get(i);
        holder.tv_item_health_injury_name.setText(singedPeopleBean.getName());
        holder.tv_item_health_injury_gender.setText(singedPeopleBean.getGender());
        holder.tv_item_health_injury_age.setText(singedPeopleBean.getAge()+"岁");
        ImageLoader.getInstance().displayImage(singedPeopleBean.getIconUrl(),
                holder.iv_item_health_user_icon, PersonInfoActivity.getCircleDisplayOptions(R.drawable.default_avatar_patient));
        if(singedPeopleBean.isReplay()){
            holder.tv_item_health_injury_replay.setText("已回复");
        }else {
            holder.tv_item_health_injury_replay.setText("未回复");
            holder.tv_item_health_injury_replay.setTextColor(mContext.getResources().getColor(R.color.color_main_green));
        }

        holder.tv_item_health_injury_question.setText(singedPeopleBean.getQuestion());
        holder.tv_item_health_injury_time.setText(singedPeopleBean.getTime());

        return convertView;
    }
    private static class ViewHolder{
        @ViewInject(R.id.tv_item_health_injury_name)
        TextView tv_item_health_injury_name;
        @ViewInject(R.id.tv_item_health_injury_age)
        TextView tv_item_health_injury_age;
        @ViewInject(R.id.tv_item_health_injury_gender)
        TextView tv_item_health_injury_gender;
        @ViewInject(R.id.tv_item_health_injury_question)
        TextView tv_item_health_injury_question;
        @ViewInject(R.id.iv_item_health_user_icon)
        ImageView iv_item_health_user_icon;
        @ViewInject(R.id.tv_item_health_injury_replay)
        TextView tv_item_health_injury_replay;
        @ViewInject(R.id.tv_item_health_injury_time)
        TextView tv_item_health_injury_time;

        public ViewHolder(View view) {
            ViewUtils.inject(this,view);

        }
    }
}
