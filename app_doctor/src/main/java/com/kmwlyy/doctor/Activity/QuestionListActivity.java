package com.kmwlyy.doctor.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.kmwlyy.doctor.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class QuestionListActivity extends BaseActivity {
    //返回
    @ViewInject(R.id.tv_left)
    TextView btn_left;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_list);
        ViewUtils.inject(this);

        ((TextView)findViewById(R.id.tv_center)).setText(getResources().getString(R.string.string_tab_query));


        btn_left.setVisibility(View.VISIBLE);
        btn_left.setOnClickListener(this);
    }

    @Override
    public void onClick(View arg0) {
        super.onClick(arg0);
        switch (arg0.getId()){
            case R.id.tv_left:
                finish();
                break;
        }
    }
}
