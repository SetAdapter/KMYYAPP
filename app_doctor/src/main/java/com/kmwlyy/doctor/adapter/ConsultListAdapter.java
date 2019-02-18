package com.kmwlyy.doctor.adapter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.Utils.MyUtils;
import com.kmwlyy.doctor.model.ConsultBean;
import com.kmwlyy.doctor.model.Content;
import com.kmwlyy.imchat.TimApplication;
import com.winson.ui.widget.CommonAdapter;


import java.util.List;

public class ConsultListAdapter extends CommonAdapter<ConsultBean> {

    private Context context;
    private LayoutInflater inflater;
    private List<ConsultBean> list;
    public ConsultListAdapter(Context context, List<ConsultBean> datas) {
        super(context, R.layout.item_consult, datas);
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list = datas;
    }

    public void setData(List<ConsultBean> list){
        this.list.clear();
        this.list.addAll(list);
    }

    public void addData(List<ConsultBean> list){
        this.list.addAll(list);
    }

    public void clear(){
        this.list.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list == null ? 0 : list.size();
    }

    @Override
    public ConsultBean getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_consult, null);
            holder.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
            holder.tv_state = (TextView) convertView.findViewById(R.id.tv_state);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            holder.ll_consult = (LinearLayout) convertView.findViewById(R.id.ll_consult);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ConsultBean consultBean = list.get(position);
        //咨询类型（0-付费、1-免费、2-义诊、3-套餐、4-会员）
        holder.tv_type.setText(MyUtils.getConsultType(context,consultBean.ConsultType,consultBean.InquiryType));
        holder.tv_state.setText(MyUtils.getConsultState(context,""+consultBean.ConsultState,holder.tv_state));
		holder.tv_time.setText(consultBean.ConsultTime.substring(0,16).replace("T"," "));
		holder.tv_name.setText(consultBean.UserMember.MemberName);
//		holder.tv_content.setText(consultBean.ConsultContent);

        try {
            if(consultBean.Messages == null){
                holder.tv_time.setText(consultBean.ConsultTime.substring(0,16).replace("T"," "));
                holder.tv_content.setText(consultBean.ConsultContent);
            }else{
                holder.tv_time.setText(consultBean.Messages.get(0).MessageTime.substring(0,16).replace("T"," "));

                String type = consultBean.Messages.get(0).MessageType;
                Content content = JSON.parseObject(consultBean.Messages.get(0).MessageContent,Content.class);
                if( type.equals("TIMCustomElem")){
                    holder.tv_content.setText(context.getResources().getString(R.string.string_type_custom));}
                else if(type.equals("TIMFaceElem")){//表情
                    holder.tv_content.setText(content.MsgContent.Data);
                }
                else if(type.equals("TIMFileElem")){//文件
                    holder.tv_content.setText(context.getResources().getString(R.string.string_type_file));
                }
                else if(type.equals("TIMImageElem")){//图片
                    holder.tv_content.setText(context.getResources().getString(R.string.string_type_pic));
                }
                else if(type.equals("TIMSoundElem")){//语音
                    holder.tv_content.setText(context.getResources().getString(R.string.string_type_voice));
                }
                else if(type.equals("TIMTextElem")){
                    holder.tv_content.setText(content.MsgContent.Text == null?"":content.MsgContent.Text);
                }
                else{

                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //图文咨询点击事件，跳转到图文聊天界面
        holder.ll_consult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //0-未筛选、1-未领取、2-未回复、4-已回复、5-已完成
                //如果是已完成，就不让回复。隐藏发送栏
                Boolean isChat = true;
                //ConsultState = 0,1,5 这三种的时候,不让发送内容
                if(consultBean.ConsultState.equals("5") || consultBean.ConsultState.equals("0") || consultBean.ConsultState.equals("1")){
                    isChat = false;
                }
                //还有ChannelId空或者0的时候。
                if(consultBean.Room.ChannelID.equals("0") || consultBean.Room.ChannelID.equals("")){
                    isChat = false;
                }
                TimApplication.enterTimchat((AppCompatActivity)context,consultBean.Room.ChannelID,consultBean.UserMember.MemberName,isChat);
            }
        });
        return convertView;
    }

    @Override
    public void convert(com.winson.ui.widget.ViewHolder viewHolder, ConsultBean obj, int position) {
    }

    private class ViewHolder {
        private TextView tv_type;
        private TextView tv_state;
        private TextView tv_time;
        private TextView tv_name;
        private TextView tv_content;
        private LinearLayout ll_consult;
    }

}
