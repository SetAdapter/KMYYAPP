package com.kmwlyy.doctor.View;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by TFeng on 2017/7/2.
 */

public class NoScrollViewPager extends ViewPager {
    private boolean isScroll;

    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    /*一般不做处理，处理孩子都无法接受事件*/

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    /*

     是否拦截
   * 拦截:会走到自己的onTouchEvent方法里面来
   * 不拦截:事件传递给子孩子
    *return false;//可行,不拦截事件,
     return true;//不行,孩子无法处理事件
    * */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isScroll) {
            return super.onInterceptTouchEvent(ev);
        } else {
            return false;
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if(isScroll){
            return super.onTouchEvent(ev);

        }else {
            return true;
        }
    }

    public void setScroll(boolean scroll) {
        isScroll = scroll;
    }
}
