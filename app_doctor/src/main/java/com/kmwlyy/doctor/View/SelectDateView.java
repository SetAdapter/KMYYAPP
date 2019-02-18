package com.kmwlyy.doctor.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.Utils.MyUtils;
import com.kmwlyy.doctor.model.FreeCalendarBean;

import java.util.Calendar;

/**
 * Created by xcj on 2017/7/8.
 */

public class SelectDateView extends View {
    private static final int NUM_COLUMNS = 7;
    private static final int NUM_ROWS = 6;
    private Paint mPaint;
    private int mDayColor = Color.parseColor("#333333");
    private int mSelectDayColor = Color.parseColor("#ffffff");
    private int mSelectBGColor = Color.parseColor("#1FC2F3");
    private int mOverTimeColor = Color.parseColor("#eeeeee");
    private int mMainColor = R.color.color_main_green;
    //框框的颜色
    private int mLineColor = R.color.color_listview_divider;
    private int mCurrYear,mCurrMonth,mCurrDay;
    private int mSelYear,mSelMonth,mSelDay;
    private int mColumnSize,mRowSize;
    private DisplayMetrics mDisplayMetrics;
    private int mDaySize = 16;
    private TextView tv_date,tv_week;
    private int weekRow;
    private int [][] daysString;
    private MonthDateView.DateClick dateClick;
    private FreeCalendarBean bean;
    private String[] selDays;
    private int selectDay;

    public SelectDateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDisplayMetrics = getResources().getDisplayMetrics();
        Calendar calendar = Calendar.getInstance();
        mPaint = new Paint();
        mCurrYear = calendar.get(Calendar.YEAR);
        mCurrMonth = calendar.get(Calendar.MONTH);
        mCurrDay = calendar.get(Calendar.DATE);

        setSelectYearMonth(mCurrYear,mCurrMonth,mCurrDay);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        initSize();
        daysString = new int[6][7];
        mPaint.setTextSize(mDaySize*mDisplayMetrics.scaledDensity);
        String dayString;
        int mMonthDays = MyUtils.getMonthDays(mSelYear, mSelMonth);
        int weekNumber = MyUtils.getFirstDayWeek(mSelYear, mSelMonth);
        Log.d("DateView", "DateView:" + mSelMonth+"月1号周" + weekNumber);
        for(int day = 0;day < mMonthDays;day++){
            dayString = (day + 1) + "";
            int column = (day+weekNumber - 1) % 7;
            int row = (day+weekNumber - 1) / 7;
            daysString[row][column]=day + 1;
            int startX = (int) (mColumnSize * column + (mColumnSize - mPaint.measureText(dayString))/2);
            int startY = (int) (mRowSize * row + mRowSize/2 - (mPaint.ascent() + mPaint.descent())/2);

            int startRecX = mColumnSize * column;
            int startRecY = mRowSize * row;
            int endRecX = startRecX + mColumnSize;
            int endRecY = startRecY + mRowSize;

            //画背景图片
            if(!MyUtils.isFuture(mSelYear,mSelMonth,Integer.parseInt(dayString)) || MyUtils.isTwoWeekLate(mSelYear,mSelMonth,Integer.parseInt(dayString))){//小于今天，变灰色
                mPaint.setColor(mOverTimeColor);
                canvas.drawRect(startRecX, startRecY, endRecX, endRecY, mPaint);
            }else if(dayString.equals(String.valueOf(selectDay))){
                mPaint.setColor(getResources().getColor(mMainColor));
                RectF oval2 = new RectF(startRecX+(endRecX-startRecX)/4, startRecY+(endRecY-startRecY)/4, endRecX-(endRecX-startRecX)/4, endRecY-(endRecY-startRecY)/4);
                canvas.drawOval(oval2, mPaint);
            }else{
                mPaint.setColor(getResources().getColor(R.color.app_color_white));
                canvas.drawRect(startRecX, startRecY, endRecX, endRecY, mPaint);
            }
            mPaint.setStyle(Paint.Style.FILL);
            if(dayString.equals(String.valueOf(selectDay)) && MyUtils.isFuture(mSelYear,mSelMonth,Integer.parseInt(dayString)) && !MyUtils.isTwoWeekLate(mSelYear,mSelMonth,Integer.parseInt(dayString))){
                //如果设置了排班，字体为白色
                mPaint.setColor(mSelectDayColor);
            }else{
                //其它字体为黑色
                mPaint.setColor(mDayColor);
            }

            mPaint.setAntiAlias(true);
            canvas.drawText(dayString, startX, startY, mPaint);
            if(tv_date != null){
                tv_date.setText(mSelYear + "-" + (mSelMonth + 1));
            }
        }


        int mHeight = (getHeight()/6) * 5; //日历一般只有5行
        int mRow = NUM_COLUMNS - 1;
        if(daysString[5][0] > 0){
            //日历有第6排
            mHeight = getHeight();
            mRow = NUM_COLUMNS + 1;
        }
        //画格子的坚线
        for(int i=0;i<NUM_ROWS;i++){
            mPaint.setColor(getResources().getColor(mLineColor));
            mPaint.setStrokeWidth(1);
            canvas.drawLine((i+1) * (getWidth()/7), 0, (i+1) * (getWidth()/7), mHeight,mPaint);
        }

        //画格子的横线
        for(int i=0;i<(mRow);i++){
            mPaint.setColor(getResources().getColor(mLineColor));
            mPaint.setStrokeWidth(1);
            canvas.drawLine(0, i * (getHeight()/6), getWidth(),i * (getHeight()/6) , mPaint);
        }

    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    private int downX = 0,downY = 0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int eventCode=  event.getAction();
        switch(eventCode){
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getX();
                downY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                int upX = (int) event.getX();
                int upY = (int) event.getY();
                if(Math.abs(upX-downX) < 10 && Math.abs(upY - downY) < 10){//点击事件
                    performClick();
                    doClickAction((upX + downX)/2,(upY + downY)/2);
                }
                break;
        }
        return true;
    }

    /**
     * 初始化列宽行高
     */
    private void initSize(){
        mColumnSize = getWidth() / NUM_COLUMNS;
        mRowSize = getHeight() / NUM_ROWS;
    }

    /**
     * 设置年月
     * @param year
     * @param month
     */
    private void setSelectYearMonth(int year,int month,int day){
        mSelYear = year;
        mSelMonth = month;
        mSelDay = day;
    }
    /**
     * 执行点击事件
     * @param x
     * @param y
     */
    private void doClickAction(int x,int y){
        int row = y / mRowSize;
        int column = x / mColumnSize;
        setSelectYearMonth(mSelYear,mSelMonth,daysString[row][column]);
        invalidate();
        //执行activity发送过来的点击处理事件
        if(dateClick != null){
            dateClick.onClickOnDate();
        }
    }

    /**
     * 左点击，日历向后翻页
     */
    public void onLeftClick(){
        int year = mSelYear;
        int month = mSelMonth;
        int day = mSelDay;
        if(month == 0){//若果是1月份，则变成12月份
            year = mSelYear-1;
            month = 11;
        }else if(MyUtils.getMonthDays(year, month) == day){
            //如果当前日期为该月最后一点，当向前推的时候，就需要改变选中的日期
            month = month-1;
            day = MyUtils.getMonthDays(year, month);
        }else{
            month = month-1;
        }
        setSelectYearMonth(year,month,day);
        invalidate();
    }

    /**
     * 右点击，日历向前翻页
     */
    public void onRightClick(){
        int year = mSelYear;
        int month = mSelMonth;
        int day = mSelDay;
        if(month == 11){//若果是12月份，则变成1月份
            year = mSelYear+1;
            month = 0;
        }else if(MyUtils.getMonthDays(year, month) == day){
            //如果当前日期为该月最后一点，当向前推的时候，就需要改变选中的日期
            month = month + 1;
            day = MyUtils.getMonthDays(year, month);
        }else{
            month = month + 1;
        }
        setSelectYearMonth(year,month,day);
        invalidate();
    }

    /**
     * 获取选择的年份
     * @return
     */
    public int getmSelYear() {
        return mSelYear;
    }
    /**
     * 获取选择的月份
     * @return
     */
    public int getmSelMonth() {
        return mSelMonth;
    }
    /**
     * 获取选择的日期
     *
     */
    public int getmSelDay() {
        return this.mSelDay;
    }

    public int getmCurrYear() {
        return mCurrYear;
    }

    public int getmCurrMonth() {
        return mCurrMonth;
    }

    public int getmCurrDay() {
        return mCurrDay;
    }

    /**
     * 日期的大小，默认18sp
     * @param mDaySize
     */
    public void setmDaySize(int mDaySize) {
        this.mDaySize = mDaySize;
    }

    public void setTextView(TextView tv_date){
        this.tv_date = tv_date;
        invalidate();
    }

    /**
     * 设置日期的点击回调事件
     * @author shiwei.deng
     *
     */
    public interface DateClick{
        public void onClickOnDate();
    }

    /**
     * 设置日期点击事件
     * @param dateClick
     */
    public void setDateClick(MonthDateView.DateClick dateClick) {
        this.dateClick = dateClick;
    }



    public void setSelectDay(int clinicList) {
        selectDay = clinicList;
        invalidate();
    }
}
