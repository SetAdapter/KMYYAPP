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

public class DayAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<Schedule> list_schedule;
    private List<DateWeek> list_week;

    public DayAdapter(Context context, List<Schedule> list_schedule, List<DateWeek> list_week) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list_schedule = list_schedule;
        this.list_week = list_week;
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
        final ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_day, null);
            ViewUtils.inject(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
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
            holder.tv_day.setText("");
            holder.tv_date.setText("");
            for (int i = 1; i < 50; i++) {
                if (position > 8 * i && position < 8 * (i + 1)) {
                    num = position - 8 - i;
                    break;
                }
            }
//			if(position > 8 && position < 16){
//				num = position - 9;
//			}else if(position > 16 && position < 24){
//				num = position - 10;
//			}else if(position > 24 && position < 32){
//				num = position - 11;
//			}else if(position > 32 && position < 40){
//				num = position - 12;
//			}else if(position > 40 && position < 48){
//				num = position - 13;
//			}else if(position > 48 && position < 56){
//				num = position - 14;
//			}else if(position > 56 && position < 64){
//				num = position - 15;
//			}else if(position > 64 && position < 72){
//				num = position - 16;
//			}else if(position > 72 && position < 80){
//				num = position - 17;
//			}

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
                    holder.ll_day.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.layout_shape_green));
                } else {
                    holder.ll_day.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.layout_shape));
                }

                //设置点击事件
                holder.ll_day.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (list_schedule.get(pos).Disable.equals("false")) {
                            if (list_schedule.get(pos).Checked.equals("true")) {
                                list_schedule.get(pos).Checked = "false";
                                holder.ll_day.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.layout_shape));
                            } else {
                                list_schedule.get(pos).Checked = "true";
                                holder.ll_day.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.layout_shape_green));
                            }
                        }
                    }
                });
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
