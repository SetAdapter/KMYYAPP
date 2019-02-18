package com.kmwlyy.login;

import android.widget.TextView;

import com.kmwlyy.core.base.BaseActivity;

public class MsgDetailActivity extends BaseActivity {

    private static final String TAG = MsgDetailActivity.class.getSimpleName();

    private MessageApi.Message mItem;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_msgdetail;
    }

    @Override
    protected void afterBindView() {
        mItem = (MessageApi.Message)getIntent().getSerializableExtra("item");
        setTitle(mItem.getTitle());
        ((TextView)findViewById(R.id.tv_content)).setText(mItem.getContent());
    }
}
