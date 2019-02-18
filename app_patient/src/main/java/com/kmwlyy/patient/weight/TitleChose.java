package com.kmwlyy.patient.weight;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kmwlyy.patient.R;


/**
 * Created by ljf on 2017/7/31.
 * 标题选择控件
 */

public class TitleChose extends LinearLayout {

    TextView title;
    LinearLayout linearLayout;
    TextView hint;

    public TitleChose(Context context) {
        this(context, null);
    }

    public TitleChose(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleChose(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.view_titlechose, this, false);
        title = (TextView) view.findViewById(R.id.title);
        hint = (TextView) view.findViewById(R.id.content);
        linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);
        initView(attrs);
        addView(view);
    }

    private void initView(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TitleChose);
        String titleString = typedArray.getString(R.styleable.TitleChose_title_title);
        String content = typedArray.getString(R.styleable.TitleChose_hint_text);
        title.setText(titleString);
        hint.setText(content);
        typedArray.recycle();
    }

    public void setOnSelfClickListener(OnClickListener onClickListener) {
        linearLayout.setOnClickListener(onClickListener);
    }

    public void setHint(String string) {
        hint.setText(string);
    }

    public String getHint(){
        return (String) hint.getText();
    }
}
