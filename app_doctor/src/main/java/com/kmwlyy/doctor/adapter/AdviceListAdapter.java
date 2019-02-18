package com.kmwlyy.doctor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.TextView;

import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.Utils.MyUtils;
import com.kmwlyy.doctor.model.OPDRegister;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.winson.ui.widget.CommonAdapter;


import java.util.List;

import io.agora.core.AgoraApplication;
import io.agora.core.ConsultBean;
import io.agora.core.RoomActivity1;

public class AdviceListAdapter extends CommonAdapter<OPDRegister> {

    private Context context;
    private LayoutInflater inflater;
    private List<OPDRegister> list;

    public AdviceListAdapter(Context context, List<OPDRegister> list) {
        super(context, R.layout.item_advice, list);
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list = list;
    }

    public void setData(List<OPDRegister> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void addData(List<OPDRegister> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void clear() {
        this.list.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public OPDRegister getItem(int position) {
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
            convertView = inflater.inflate(R.layout.item_advice, null);
            ViewUtils.inject(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final OPDRegister opdRegister = list.get(position);
        holder.tv_patient.setText(opdRegister.Member.MemberName);
        holder.tv_state.setText(context.getResources().getString(R.string.string_wait_doctor));
        holder.tv_appointment.setText(opdRegister.OPDDate.substring(0, 10).replace("T", " ") + " " + opdRegister.Schedule.StartTime + " - " + opdRegister.Schedule.EndTime);

        /**
         * 呼叫按钮的点击，跳转到语音或视频聊天
         * opdRegister.OPDType = 2 语音聊天
         * 			   OPDType = 3 视频聊天
         */
        holder.btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                go2agora(opdRegister);
            }
        });
        return convertView;
    }

    @Override
    public void convert(com.winson.ui.widget.ViewHolder viewHolder, OPDRegister obj, int position) {

    }

    private void go2agora(OPDRegister opdRegister) {
        ConsultBean bean = new ConsultBean();
        bean.mRegisterID = opdRegister.OPDRegisterID;
        bean.mRoomID = opdRegister.Room.ChannelID;
        if (opdRegister.OPDType.equals("2")) {
            bean.mCallType = RoomActivity1.CALLING_TYPE_VOICE;
        } else {
            bean.mCallType = RoomActivity1.CALLING_TYPE_VIDEO;
        }
        bean.mUserType = RoomActivity1.USER_TYPE_DOCTOR;
        bean.mUserFace = "";
        bean.mUserName = opdRegister.Member.MemberName;
        bean.mUserPhone = "";

        String sex = "";
        sex = MyUtils.getGendar(context,opdRegister.Member.Gender);

        bean.mUserInfo = sex + "  " + opdRegister.Member.Age + context.getResources().getString(R.string.string_age);
        AgoraApplication.loginAgora(context, bean);
    }

    private class ViewHolder {
        @ViewInject(R.id.tv_patient)
        private TextView tv_patient;

        @ViewInject(R.id.tv_state)
        private TextView tv_state;

        @ViewInject(R.id.tv_appointment)
        private TextView tv_appointment;

        @ViewInject(R.id.btn_call)
        private Button btn_call;

    }

}
