package com.kmwlyy.doctor.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.model.InfoEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by xcj on 2016/10/28.
 */
public class ModificationInfosActivity extends BaseActivity {

    static final String TYPE = "TYPE";
    static final String CONTENT = "CONTENT";

    private int mType = 0;
    private String mContent = "";
    private TextView mSaveTxt = null;
    private ImageView mNavigationIcon = null;
    private TextView mToolbarTitle = null;
    private EditText mContentEdit = null;
    private TextView mEdtLength = null;

    public static void startModificationInfosActivity(Context context, int type, String content) {
        Intent intent = new Intent(context, ModificationInfosActivity.class);
        intent.putExtra(TYPE, type);
        intent.putExtra(CONTENT, content);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modification_infos);
        mType = getIntent().getIntExtra(TYPE, 0);
        /*if (mType == 1){
            mContentEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
        }else{
            mContentEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(300)});
        }*/
        mContent = getIntent().getStringExtra(CONTENT);
        mSaveTxt = (TextView) findViewById(R.id.tv_save);
        mNavigationIcon = (ImageView) findViewById(R.id.navigation_icon);
        mToolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        mContentEdit = (EditText) findViewById(R.id.et_content);
        mEdtLength = (TextView) findViewById(R.id.tv_length);
        mNavigationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mSaveTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mContentEdit.getText().toString())){
                    ToastUtils.showShort(ModificationInfosActivity.this, getResources().getString(R.string.plz_input_content));
                    return;
                }
                if (mType == 1){
                    //擅长
                    if(mContentEdit.getText().toString().length() < 5){
                        ToastUtils.showShort(ModificationInfosActivity.this, getResources().getString(R.string.plz_input_at_least_five_character));
                        return;
                    }
                    InfoEvent.GoodAtDisease  goodAtDisease = new InfoEvent.GoodAtDisease();
                    goodAtDisease.goodAtDisease = mContentEdit.getText().toString();
                    EventBus.getDefault().post(goodAtDisease);
                }else if (mType == 2){
                    //个人简介
                    if(mContentEdit.getText().toString().length() < 30){
                        ToastUtils.showShort(ModificationInfosActivity.this, getResources().getString(R.string.plz_input_at_least_thirty_character));
                        return;
                    }
                    InfoEvent.Intro  intro = new InfoEvent.Intro();
                    intro.introInfo = mContentEdit.getText().toString();
                    EventBus.getDefault().post(intro);
                }
                finish();
            }
        });
        if (mType == 1){
            mToolbarTitle.setText(getResources().getString(R.string.specialty));

        }else if(mType == 2){
            mToolbarTitle.setText(getResources().getString(R.string.intro));
        }
        if (!TextUtils.isEmpty(mContent)){
            mContentEdit.setText(mContent);
        }

        mContentEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int i = s.length();
                if (mType == 1){
                    mEdtLength.setText(i + "/50");
                }else {
                    mEdtLength.setText(i + "/300");
                }
            }
        });


    }
}
