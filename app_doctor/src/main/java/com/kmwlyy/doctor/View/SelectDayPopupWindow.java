package com.kmwlyy.doctor.View;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.kmwlyy.core.util.CommonUtils;
import com.kmwlyy.core.util.LogUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.Utils.MyUtils;

/**
 * Created by xcj on 2017/6/30.
 */

public class SelectDayPopupWindow extends PopupWindow {
    private TextView tv_cancel;
    private TextView tv_confirm;
    private View mMenuView;
    private SelectDateView mDateView;
    private TextView tv_date;
    private ImageView iv_left;
    private ImageView iv_right;
    private String day;
    public int Month = 0;
    private Context mContext;
    private String monthStr;
    private String dayStr;
    private String yearStr;
    public static final int LAST_MONTH = -1,NEXT_MONTH = 1;
    private View.OnClickListener confirmClickListener = null;

    public SelectDayPopupWindow(Activity context) {
        super(context);
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.layout_select_day, null);
        tv_cancel = (TextView) mMenuView.findViewById(R.id.tv_cancel);
        tv_confirm = (TextView) mMenuView.findViewById(R.id.tv_confirm);
        tv_date = (TextView) mMenuView.findViewById(R.id.tv_date);
        iv_left = (ImageView) mMenuView.findViewById(R.id.iv_left);
        iv_right = (ImageView) mMenuView.findViewById(R.id.iv_right);
        mDateView = (SelectDateView) mMenuView.findViewById(R.id.monthDateView);
        mDateView.setTextView(tv_date);
        mDateView.setDateClick(new MonthDateView.DateClick() {

            @Override
            public void onClickOnDate() {
//               Toast.makeText(getApplication(), "点击了：" + monthDateView.getmSelDay(), Toast.LENGTH_SHORT).show();
                mDateView.setSelectDay(mDateView.getmSelDay());
            }
        });
        //取消按钮
        tv_cancel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                //销毁弹出框
                dismiss();
            }
        });
        //设置按钮监听
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(String.valueOf(mDateView.getmSelDay()))){
                    ToastUtils.showShort(mContext,"请选择日期");
                    return;
                }
//               验证是否为0
                if(mDateView.getmSelDay() == 0){
                    ToastUtils.showShort(mContext,"当前日期不能选择");
                    return;
                }
                //验证点击的日期是否合法
                if(CommonUtils.compareTime(""+mDateView.getmSelYear() + ((mDateView.getmSelMonth()+1)<10?"0"+(mDateView.getmSelMonth()+1):(mDateView.getmSelMonth()+1)) + (mDateView.getmSelDay()<10?"0"+mDateView.getmSelDay():mDateView.getmSelDay()),CommonUtils.getFutureDate(CommonUtils.getCurrentDate("yyyy-MM-dd"),"yyyyMMdd",14),14)){
                    ToastUtils.showShort(mContext,"请选择日期");
                    return;
                }
                day = ""+mDateView.getmSelYear()+"-"+(mDateView.getmSelMonth()+1)+"-"+mDateView.getmSelDay();
                monthStr = (mDateView.getmSelMonth()+1)+"";
                dayStr = mDateView.getmSelDay()+"";
                yearStr = mDateView.getmSelYear()+"";
                confirmClickListener.onClick(v);
                dismiss();
            }
        });
        iv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Month = Month - 1;
                loadData(MyUtils.getYearMonth(Month),LAST_MONTH);

            }
        });
        iv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Month = Month + 1;
                loadData(MyUtils.getYearMonth(Month),NEXT_MONTH);
            }
        });
        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        this.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.FILL_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(com.winson.ui.R.style.popupAnimation);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x80000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        this.setOutsideTouchable(false);

    }
    public void confirmDay(View.OnClickListener listener) {
        this.confirmClickListener = listener;
    }

    private void loadData(String monthStr,final int type){
        if(type == LAST_MONTH){
            mDateView.onLeftClick();
        }
        else if(type == NEXT_MONTH){
            mDateView.onRightClick();
        }
    }

    public String getTime(){
        return day;
    }

    public String getMonthStr(){
        return monthStr;
    }
    public String getDayStr(){
        return dayStr;
    }
    public String getYearStr(){
        return yearStr;
    }

}
