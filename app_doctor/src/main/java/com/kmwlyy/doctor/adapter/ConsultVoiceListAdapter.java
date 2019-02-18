package com.kmwlyy.doctor.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kmwlyy.core.util.ImageUtils;
import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.Utils.MyUtils;
import com.kmwlyy.doctor.model.ConsultBeanNew;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.winson.ui.widget.CommonAdapter;
import com.winson.ui.widget.ViewHolder;

import java.util.List;

import doctor.kmwlyy.com.recipe.RecipeActivity;
import io.agora.core.AgoraApplication;
import io.agora.core.ConsultBean;
import io.agora.core.RoomActivity1;

/**
 * Created by Winson on 2017/7/10.
 */

public class ConsultVoiceListAdapter extends CommonAdapter<ConsultBeanNew> {


    private String timeFormatStr;
    private String ageFormatStr;

    public ConsultVoiceListAdapter(Context context, List<ConsultBeanNew> datas) {
        super(context, R.layout.item_advice_new, datas);
        timeFormatStr = context.getString(R.string.string_query_time_format);
        ageFormatStr = context.getString(R.string.age_format);
    }

    @Override
    public void convert(ViewHolder viewHolder, final ConsultBeanNew data, int position) {
        TextView patientName = (TextView) viewHolder.findViewById(R.id.tv_name);
        ImageView avatarIV = (ImageView) viewHolder.findViewById(R.id.iv_patient_portrait);

        if (data.Member != null) {
            patientName.setText(data.Member.MemberName);
            ImageLoader.getInstance().displayImage(data.Member.PhotoUrl, avatarIV, ImageUtils.getCircleDisplayOptions());
            ((TextView) viewHolder.findViewById(R.id.gender)).setText(MyUtils.getGendar(context, "" + data.Member.Gender));
            ((TextView) viewHolder.findViewById(R.id.age)).setText(String.format(ageFormatStr, data.Member.Age));

        } else {
            patientName.setText("");
            ImageLoader.getInstance().displayImage("", avatarIV, ImageUtils.getCacheOptions());
            ((TextView) viewHolder.findViewById(R.id.gender)).setText(MyUtils.getGendar(context, "" + 0));
            ((TextView) viewHolder.findViewById(R.id.age)).setText(String.format(ageFormatStr, 0));

        }

        if (data.Schedule != null) {
            ((TextView) viewHolder.findViewById(R.id.consult_date)).setText(String.format(timeFormatStr, data.Schedule.StartTime, data.Schedule.EndTime));
        } else {
            ((TextView) viewHolder.findViewById(R.id.consult_date)).setText(String.format(timeFormatStr, "", ""));
        }

        TextView replyFlag = (TextView) viewHolder.findViewById(R.id.reply_flag);
        replyFlag.setTextColor(replyFlag.getResources().getColor(R.color.app_color_main));
        //0 未就诊，1 候诊中，2 就诊中，3 已就诊，4 呼叫中，5 离开中，6 断开连接
        int roomState = Integer.parseInt(data.Room.RoomState);
        switch (roomState) {
            case -1:
                replyFlag.setText(R.string.other);
                break;
            case 0:
                replyFlag.setText(R.string.no_visit);
                replyFlag.setTextColor(replyFlag.getResources().getColor(R.color.app_color_string));
                break;
            case 2:
                replyFlag.setText(R.string.on_visit);
                break;
            case 3:
                replyFlag.setText(R.string.visited);
                break;
            case 4:
                replyFlag.setText(R.string.on_call);
                break;
            case 5:
                replyFlag.setText(R.string.no_leave);
                break;
            case 6:
                replyFlag.setText(R.string.break_connect);
                break;
            case 1:
            case 7:
            default:
                replyFlag.setText(R.string.wait_visit);
                break;
        }

        TextView action = (TextView) viewHolder.findViewById(R.id.action);

        //“未就诊”，“候诊中”的时候按钮是“呼叫”
        //“已就诊”，“离开”，“断开连接”对应“查看病情”
        //“呼叫中”，“就诊中”对应“诊断”
        if (roomState == 3 || roomState == 5 || roomState == 6) {
            // “查看病情”
            //查看病情进入处方界面
            action.setCompoundDrawables(null, null, null, null);
            action.setBackgroundResource(R.drawable.btn_green);
            action.setTextColor(viewHolder.getConvertView().getResources().getColor(R.color.color_button_green));
            action.setText(R.string.look_illness_state);
            action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (data.Room != null) {
                        Intent recipeIntent = new Intent(v.getContext(), RecipeActivity.class);
                        recipeIntent.putExtra("id", data.Room.ServiceID);
                        v.getContext().startActivity(recipeIntent);
                    }
                }
            });
        } else if (roomState == 4 || roomState == 2) {
            // “诊断”
            //诊断进入开处方界面
            action.setCompoundDrawables(null, null, null, null);
            action.setBackgroundResource(R.drawable.btn_green);
            action.setTextColor(viewHolder.getConvertView().getResources().getColor(R.color.color_button_green));
            action.setText(R.string.string_diagnose2);
            action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (data.Room != null) {
                        Intent recipeIntent = new Intent(v.getContext(), RecipeActivity.class);
                        recipeIntent.putExtra("id", data.Room.ServiceID);
                        v.getContext().startActivity(recipeIntent);
                    }
                }
            });
        } else {
            // “呼叫”
            //呼叫进去呼叫界面
            action.setBackgroundResource(R.drawable.btn_dark);
            action.setTextColor(viewHolder.getConvertView().getResources().getColor(R.color.text_second));
            action.setText(R.string.string_call);
            if (data.Room.ServiceType == "2") {
//                RoomActivity1.CALLING_TYPE_VOICE;
                Drawable drawable = context.getResources().getDrawable(R.mipmap.call_voice);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                action.setCompoundDrawables(drawable, null, null, null);
            } else {
//                RoomActivity1.CALLING_TYPE_VIDEO;
                Drawable drawable = context.getResources().getDrawable(R.mipmap.call_video);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                action.setCompoundDrawables(drawable, null, null, null);
            }

            action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    go2agora(data);
                }
            });
        }

    }

    private void go2agora(ConsultBeanNew consultBeanNew) {
        ConsultBean bean = new ConsultBean();
        bean.mRegisterID = consultBeanNew.Room.ServiceID;
        bean.mRoomID = consultBeanNew.Room.ChannelID;
        if (consultBeanNew.Room.ServiceType == "2") {
            bean.mCallType = RoomActivity1.CALLING_TYPE_VOICE;
        } else {
            bean.mCallType = RoomActivity1.CALLING_TYPE_VIDEO;
        }
        bean.mUserType = RoomActivity1.USER_TYPE_DOCTOR;
        bean.mUserFace = "";
        bean.mUserName = consultBeanNew.Member.MemberName;
        bean.mUserPhone = "";

        String sex = "";
        sex = MyUtils.getGendar(context, consultBeanNew.Member.Gender);

        bean.mUserInfo = sex + "  " + consultBeanNew.Member.Age + context.getResources().getString(R.string.string_age);
        AgoraApplication.loginAgora(context, bean);
    }


}
