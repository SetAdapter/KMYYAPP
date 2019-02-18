package com.kmwlyy.patient.kdoctor;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;

import com.kmwlyy.patient.R;


/**
 * @Description： 字体颜色渐变TextView
 * 布局文件中使用start_color、end_color属性即可，需要设置checked为true属性
 * 默认checked为false，作为普通的TexView使用
 * @Author： 谭驿
 * @Date： 2017/5/4 0004
 */

public class LinearGradientTextView extends android.support.v7.widget.AppCompatTextView {
    private LinearGradient mLinearGradient;
    private Paint mPaint;
    private int mViewWidth = 0;
    private Rect mTextBound = new Rect();

    private int startColor;
    private int endColor;
    private boolean checked;

    private SpannableStringBuilder builder;

    public LinearGradientTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LinearGradient);
        startColor = typedArray.getColor(R.styleable.LinearGradient_startColor, 0xFF29CCBF);
        endColor = typedArray.getColor(R.styleable.LinearGradient_endColor, 0xFF6CCC56);
        checked = typedArray.getBoolean(R.styleable.LinearGradient_checked, false);
        typedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mViewWidth = getMeasuredWidth();
        mPaint = getPaint();
        String mTipText = getText().toString();
        mPaint.getTextBounds(mTipText, 0, mTipText.length(), mTextBound);
        mPaint.setTextAlign(Paint.Align.CENTER);
        if (!checked) {
            createLinearGradient(getCurrentTextColor(), getCurrentTextColor());
        } else {
            createLinearGradient(startColor, endColor);
        }

        mPaint.setShader(mLinearGradient);
        // TODO 字体高度适配
        canvas.drawText(mTipText, getMeasuredWidth() / 2, getMeasuredHeight() / 2 + mTextBound.height() / 2 - 5, mPaint);
    }

    private void createLinearGradient(int startColor, int endColor) {
        mLinearGradient = new LinearGradient(0, 0, mViewWidth, 0,
                new int[]{startColor, endColor},
                null, Shader.TileMode.REPEAT);
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
        invalidate();
    }

    public boolean isChecked() {
        return this.checked;
    }
}
