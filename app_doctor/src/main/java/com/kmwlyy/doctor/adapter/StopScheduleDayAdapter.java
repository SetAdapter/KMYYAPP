package com.kmwlyy.doctor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.model.DateWeek;
import com.kmwlyy.doctor.model.Schedule;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xcj on 2017/7/18.
 */

public class StopScheduleDayAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<Schedule> list_schedule;
    private List<DateWeek> list_week;
    private double oneStartTime;
    private double oneEndTime;
    private double twoStartTime;
    private double twoEndTime;
    private double startDay;
    private double endDay;

    public StopScheduleDayAdapter(Context context, List<Schedule> list_schedule, List<DateWeek> list_week,double startDay, double endDay, double oneStartTime,double oneEndTime, double twoStartTime, double twoEndTime) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list_schedule = list_schedule;
        this.list_week = list_week;
        this.oneStartTime = oneStartTime;
        this.oneEndTime = oneEndTime;
        this.twoStartTime = twoStartTime;
        this.twoEndTime = twoEndTime;
        this.startDay = startDay;
        this.endDay = endDay;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (list_week.size() == 0) {
            return 0;
        } else {
            return list_week.size() + list_schedule.size() + (list_schedule.size() / list_week.size() + 1);
        }
    }

    @Override
    public Object getItem(int position) {
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
        final StopScheduleDayAdapter.ViewHolder holder;


        if (convertView == null) {
            holder = new StopScheduleDayAdapter.ViewHolder();
            convertView = inflater.inflate(R.layout.item_day, null);
            ViewUtils.inject(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (StopScheduleDayAdapter.ViewHolder) convertView.getTag();
        }

        holder.tv_day.setTextColor(convertView.getResources().getColor(R.color.text_second));
        holder.tv_date.setTextColor(convertView.getResources().getColor(R.color.text_second));
        holder.tv_date.setVisibility(View.VISIBLE);

        holder.ll_day.setOnClickListener(null);
        //标题
        if (position == 0) {
            //do nothing
            holder.tv_day.setText("");
            holder.tv_date.setText("");
        } else if (position > 0 && position < 8) {
            //标题行
            DateWeek week = list_week.get(position - 1);
            holder.tv_day.setText(week.Day);
            holder.tv_date.setText(week.Date);

        } else if (position % 8 == 0) {
            //时间列
            Schedule schedule = list_schedule.get((position / 8 - 1) * 7);
            holder.tv_day.setText(schedule.StartTime);
            holder.tv_date.setText(schedule.EndTime);
        } else {
            //排班信息
            int num = 0;
            holder.tv_day.setTextColor(convertView.getResources().getColor(R.color.white));
            holder.tv_day.setText("");
            holder.tv_date.setVisibility(View.GONE);

            for (int i = 1; i < 50; i++) {
                if (position > 8 * i && position < 8 * (i + 1)) {
                    num = position - 8 - i;
                    break;
                }
            }

            if (num <= list_schedule.size() - 1) {
                Schedule schedule = list_schedule.get(num);

                if (list_schedule.get(num).OPDate == null) {
                    //初始化一下，避免发请求失败
                    list_schedule.get(num).OPDate = "null";
                }
                final int pos = num;

                /**
                 *    Disable：true表示不可用，false表示可用
                 *	  Checked：true表示选中，false表示未选中
                 */
                if (schedule.Disable.equals("true")) {
                    holder.ll_day.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.layout_shape_gray));
                } else if (schedule.Checked.equals("true")) {
                    if (startDay == 0|| startDay == 0){
                        holder.ll_day.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.layout_shape_green));
                        holder.tv_day.setText("" + schedule.AppointmentCounts.getCount() + "人");
                    }else {
                        String dayStart = list_week.get((position-1)%8).Date.substring(0,2)+list_week.get((position-1)%8).Date.substring(3,5);
                        String startTimeStr = schedule.StartTime.substring(0,2)+schedule.StartTime.substring(3,5);
                        String endTimeStr = schedule.EndTime.substring(0,2)+schedule.EndTime.substring(3,5);
                        if (Double.valueOf(dayStart) >= startDay && Double.valueOf(dayStart) <= endDay){
                            if (Double.valueOf(dayStart) == startDay) {
                                if (Double.valueOf(startTimeStr) >= oneStartTime && Double.valueOf(endTimeStr) <= oneEndTime) {
                                    holder.ll_day.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.layout_shape_yellow));
                                    holder.tv_day.setText("" + schedule.AppointmentCounts.getCount() + "人");
                                } else {
                                    holder.ll_day.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.layout_shape_green));
                                    holder.tv_day.setText("" + schedule.AppointmentCounts.getCount() + "人");
                                }
                            }else if (Double.valueOf(dayStart) == endDay){
                                if (Double.valueOf(startTimeStr) >= twoStartTime && Double.valueOf(endTimeStr) <= twoEndTime) {
                                    holder.ll_day.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.layout_shape_yellow));
                                    holder.tv_day.setText("" + schedule.AppointmentCounts.getCount() + "人");
                                } else {
                                    holder.ll_day.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.layout_shape_green));
                                    holder.tv_day.setText("" + schedule.AppointmentCounts.getCount() + "人");
                                }
                            }else{
                                holder.ll_day.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.layout_shape_yellow));
                                holder.tv_day.setText("" + schedule.AppointmentCounts.getCount() + "人");
                            }
                        }else {
                            holder.ll_day.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.layout_shape_green));
                            holder.tv_day.setText("" + schedule.AppointmentCounts.getCount() + "人");
                        }
                    }
                } else {
                    holder.ll_day.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.layout_shape));
                }

            }

        }

        return convertView;
    }

    private class ViewHolder {
        @ViewInject(R.id.ll_day)
        private LinearLayout ll_day;
        @ViewInject(R.id.tv_day)
        private TextView tv_day;
        @ViewInject(R.id.tv_date)
        private TextView tv_date;

    }

    public List<Schedule> getData() {
        if (list_schedule == null) {
            list_schedule = new ArrayList<>();
        }
        return this.list_schedule;
    }

}
