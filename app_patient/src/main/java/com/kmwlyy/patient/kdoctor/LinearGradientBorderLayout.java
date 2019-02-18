package com.kmwlyy.patient.kdoctor;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import com.kmwlyy.patient.R;

/**
 * @Description： 渐变的边框布局
 * 支持设置渐变的开始颜色：startColor和结束颜色：endColor
 * 支持设置不渐变边框颜色：defaultColor
 * 支持设置边框的宽度：strokeWidth
 * 支持设置边框角度的弧度：radius
 * ***注意***
 * 必须设置子View的背景颜色：childBackground
 * 一个LinearGradientBorderLayout只能包裹一个子View
 * 渐变边框设置checked属性为true，默认为false不渐变
 * <p>
 * 可以跟LinearGradientTextView配套使用
 * 默认如果子View为LinearGradientTextView，子View的checked属性会与LinearGradientBorderLayout同步
 * 否则，子View当作普通的View处理
 * 如不需要同步，可以设置属性：synchronize="false"
 * <p>
 * @Author： 谭驿
 * @Date： 2017/5/10 0010
 */

public class LinearGradientBorderLayout extends RelativeLayout {

    private int startColor;
    private int endColor;
    private int defaultColor;
    private int radius;
    private int childBackground;
    private boolean checked;
    private boolean synchronize;
    private LinearGradientTextView mChildView;

    public LinearGradientBorderLayout(Context context) {
        this(context, null);
    }

    public LinearGradientBorderLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LinearGradientBorderLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LinearGradient);
        startColor = typedArray.getColor(R.styleable.LinearGradient_startColor, 0xFF29CCBF);
        endColor = typedArray.getColor(R.styleable.LinearGradient_endColor, 0xFF6CCC56);
        defaultColor = typedArray.getColor(R.styleable.LinearGradient_defaultColor, Color.BLACK);
        radius = typedArray.getInteger(R.styleable.LinearGradient_radius1, 0);
        childBackground = typedArray.getColor(R.styleable.LinearGradient_childBackground, 0);
        checked = typedArray.getBoolean(R.styleable.LinearGradient_checked, false);
        synchronize = typedArray.getBoolean(R.styleable.LinearGradient_synchronize, true);

        int strokeWidth = typedArray.getInteger(R.styleable.LinearGradient_strokeWidth1, 0);
        typedArray.recycle();

        setPadding(strokeWidth, strokeWidth, strokeWidth, strokeWidth);
        initLinearGradient();
    }

    private GradientDrawable getGroupDrawable() {
        int checkedColors[] = {startColor, endColor};
        int defauleColors[] = {defaultColor, defaultColor};
        GradientDrawable drawable;
        if (checked) {
            drawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, checkedColors);
        } else {
            drawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, defauleColors);
        }
        drawable.setCornerRadius(radius);
        return drawable;
    }

    private GradientDrawable getChildDrawable() {
        GradientDrawable drawable = new GradientDrawable();
        if (radius > 2) {
            drawable.setCornerRadius(radius - 2);
        } else if (radius > 1) {
            drawable.setCornerRadius(radius - 1);
        } else if (radius != 0) {
            drawable.setCornerRadius(radius);
        }
        drawable.setColor(childBackground);
        return drawable;
    }

    private void initLinearGradient() {
        setBackgroundDrawable(getGroupDrawable());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        try {
            mChildView = (LinearGradientTextView) getChildAt(0);
            mChildView.setBackgroundDrawable(getChildDrawable());
            if (synchronize) {
                mChildView.setChecked(checked);
            }
        } catch (ClassCastException e) {
            mChildView = null;
            View childView = getChildAt(0);
            childView.setBackgroundDrawable(getChildDrawable());
        }
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
        initLinearGradient();
        if (mChildView != null && synchronize) {
            mChildView.setChecked(checked);
        }
    }
}