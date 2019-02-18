package com.winson.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by Winson on 2016/11/1.
 */
public class LineCheckTextView extends TextView {

    public static final String TAG = LineCheckTextView.class.getSimpleName();

    public interface OnLineCountChangeListener {

        void onLineCountChange(int realCount);

    }

    private int mLastLineCount;
    private boolean mIsShowAll;
    private boolean mIsOver;

    public LineCheckTextView(Context context) {
        super(context);
    }

    public LineCheckTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LineCheckTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LineCheckTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public boolean checkIsOver() {
        int lineCount = getLineCount();
        return lineCount > 3;
    }

    public boolean isShowAll() {
        return mIsShowAll;
    }

    public void showAll() {
        mIsShowAll = true;
        setMaxLines(Integer.MAX_VALUE);
    }

    public void hiddenPart() {
        mIsShowAll = false;
        setMaxLines(3);
    }

}
