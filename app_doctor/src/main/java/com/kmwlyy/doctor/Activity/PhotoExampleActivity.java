package com.kmwlyy.doctor.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.kmwlyy.doctor.R;

/**
 * Created by xcj on 2016/10/28.
 */
public class PhotoExampleActivity extends BaseActivity {

    public static void startPhotoExampleActivity(Context context) {
        Intent intent = new Intent(context, PhotoExampleActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_example);
        findViewById(R.id.navigation_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
