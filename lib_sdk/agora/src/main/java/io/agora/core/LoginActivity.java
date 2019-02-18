package io.agora.core;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by apple on 15/9/9.
 */
public class LoginActivity extends BaseActivity {

    private EditText mVendorKey;
    private EditText mChannelID;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstance);
        super.setContentView(R.layout.activity_login);
        initViews();
    }
    
    @Override
    public void onUserInteraction(View view) {
        // Ensure inputs are valid;
        if(!validateInput()){
            return ;
        }
        if (view.getId() ==R.id.action_video_calling ){
            Intent intent = new Intent(LoginActivity.this, RoomActivity1.class);
            intent.putExtra(RoomActivity.EXTRA_CALLING_TYPE, RoomActivity.CALLING_TYPE_VIDEO);
            intent.putExtra(RoomActivity.EXTRA_VENDOR_KEY, mVendorKey.getText().toString());
            intent.putExtra(RoomActivity.EXTRA_CHANNEL_ID, mChannelID.getText().toString());
            startActivity(intent);
        }else if (view.getId() ==R.id.action_voice_calling ){
            Intent intent = new Intent(LoginActivity.this, RoomActivity1.class);
            intent.putExtra(RoomActivity.EXTRA_CALLING_TYPE, RoomActivity.CALLING_TYPE_VOICE);
            intent.putExtra(RoomActivity.EXTRA_VENDOR_KEY, mVendorKey.getText().toString());
            intent.putExtra(RoomActivity.EXTRA_CHANNEL_ID, mChannelID.getText().toString());
            startActivity(intent);
        }else{
            super.onUserInteraction(view);
        }
        // remember the vendor key and channel ID
        getSharedPreferences(getClass().getName(), MODE_PRIVATE)
                .edit()
                .putString(RoomActivity.EXTRA_VENDOR_KEY, mVendorKey.getText().toString())
                .putString(RoomActivity.EXTRA_CHANNEL_ID, mChannelID.getText().toString())
                .apply();
    }

    /********************************************/
    private void initViews() {
        // bind listeners
        findViewById(R.id.action_video_calling).setOnClickListener(getViewClickListener());
        findViewById(R.id.action_voice_calling).setOnClickListener(getViewClickListener());

        this.mVendorKey = (EditText) findViewById(R.id.input_vendor_key);
        this.mChannelID = (EditText) findViewById(R.id.input_room_number);

        // please your own key, the test key is unavailable soon.
        this.mVendorKey.setText(getSharedPreferences(getClass().getName(), MODE_PRIVATE).getString(RoomActivity.EXTRA_VENDOR_KEY, "25b77c09d6384632b4215f2d1355172a"));
        this.mChannelID.setText(getSharedPreferences(getClass().getName(), MODE_PRIVATE).getString(RoomActivity.EXTRA_CHANNEL_ID, ""));
    }

    boolean validateInput() {

        String vendorKey = mVendorKey.getText().toString();
        String roomNumber = mChannelID.getText().toString();
        // validate vendor key
        if (TextUtils.isEmpty(vendorKey)) {
            Toast.makeText(getApplicationContext(), R.string.key_required, Toast.LENGTH_SHORT).show();
            return false;
        }
        // validate room number - cannot be empty
        if (TextUtils.isEmpty(roomNumber)) {
            Toast.makeText(getApplicationContext(), R.string.room_required, Toast.LENGTH_SHORT).show();
            return false;
        }
        // validate room number - should be digits only
        if(!TextUtils.isDigitsOnly(roomNumber)){
            Toast.makeText(getApplicationContext(), R.string.room_required, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
