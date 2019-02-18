package com.kmwlyy.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Winson on 2016/9/9.
 */
public class NormalActivity extends AppCompatActivity {

    protected boolean mSkipFinsih;
    private View.OnClickListener mBackPressListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EventBus.getDefault().post(new EventLoginApi.onBack());
            onBackPressed();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        View nav = findViewById(R.id.navigation_btn);
        if (nav != null) {
            nav.setOnClickListener(mBackPressListener);
        }
    }

    protected void setBarTitle(String titleStr) {
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        if (title != null) {
            title.setText(titleStr);
        }
    }

    protected void setBarTitle(int titleRes) {
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        if (title != null) {
            title.setText(titleRes);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            EventBus.getDefault().post(new EventLoginApi.onBack());
            onBackPressed();
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }

    }

}
