package com.kmwlyy.patient;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.kmwlyy.core.base.BaseApplication;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.core.util.SPUtils;
import com.kmwlyy.core.util.UpdateManager;
import com.kmwlyy.patient.helper.base.BaseActivity;
import com.kmwlyy.patient.helper.utils.PUtils;
import com.kmwlyy.patient.main.GuideActivity;
import com.networkbench.agent.impl.NBSAppAgent;

/**
 * Created by Winson on 2016/8/6.
 */
public class AppStart extends BaseActivity {

    Handler mHandler;
    private Runnable mStartRun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_start);

        mHandler = new Handler();

        initTinYun();

        String lastVersion = (String) SPUtils.get(this, "last_version", "");
        if(DebugUtils.debug){
            Log.d("AppStart","lastVersion : " + lastVersion+", cv : " + PUtils.getVersion(this));
        }
        if (lastVersion == null || !lastVersion.equals(PUtils.getVersion(this))) {
            Intent intent = new Intent(this, GuideActivity.class);
            startActivity(intent);
            finish();
        } else {
            UpdateManager.getUpdateManager().CheckUpdate(this, UpdateManager.USER, new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    mStartRun = new Runnable() {
                        @Override
                        public void run() {
                            startMain();
                        }
                    };
                    mHandler.postDelayed(mStartRun, 2000);
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mStartRun);
    }

    private void startMain() {
        Intent intent = new Intent(AppStart.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void initTinYun(){
        NBSAppAgent nbsAppAgent = null;
        nbsAppAgent = NBSAppAgent.setLicenseKey("fe813950fba94e3d9d2eec84bac682e2")
                .setRedirectHost("218.17.23.72:8081").setHttpEnabled(true)
                .withLocationServiceEnabled(true).setDefaultCert(false);
        nbsAppAgent.start(this.getApplicationContext());
    }

}
