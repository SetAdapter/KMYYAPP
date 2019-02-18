package com.kmwlyy.patient.weight;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kmwlyy.patient.R;


/**
 * Created by ljf on 2017/7/31.
 * 标题输入控件
 */

public class TitleEditText extends LinearLayout {

    TextView title;
    EditText content;

    public TitleEditText(Context context) {
        this(context, null);
    }

    public TitleEditText(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.view_titleedit, this, false);
        title = (TextView) view.findViewById(R.id.title);
        content = (EditText) view.findViewById(R.id.content);
        initView(attrs);
        addView(view);
    }

    private void initView(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TitleEditText);
        String titleTemp = typedArray.getString(R.styleable.TitleEditText_title_text);
        String contentTemp = typedArray.getString(R.styleable.TitleEditText_content_hint);
        title.setText(titleTemp);
        content.setHint(contentTemp);
        typedArray.recycle();
    }

    public String getContent() {
        return content.getText().toString().trim();
    }
}
