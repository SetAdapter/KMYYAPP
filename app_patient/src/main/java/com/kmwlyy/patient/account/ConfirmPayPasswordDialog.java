package com.kmwlyy.patient.account;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.patient.R;


/**
 * Created by xcj on 2016/12/29.
 */

public class ConfirmPayPasswordDialog extends Dialog {
    /**
     * 上下文
     */
    private Context context = null;

    /**
     * 安全密码
     */
    private String secPwd = null;
    private ClickListenerInterface clickListenerInterface;
    private EditText passwordEdit;

    public interface ClickListenerInterface {

        public void doConfirm();

        public void doCancel();

        public void forget();
    }

    public ConfirmPayPasswordDialog(Context context) {
        super(context);
        this.context = context;
    }

    public ConfirmPayPasswordDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    protected ConfirmPayPasswordDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
    }

    public String getSecPwd() {
        return secPwd;
    }

    public void setSecPwd(String secPwd) {
        this.secPwd = secPwd;
    }

    public void setClicklistener(ClickListenerInterface clickListenerInterface) {
                this.clickListenerInterface = clickListenerInterface;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_pay_password, null);
        setContentView(view);
        TextView submitBtn = (TextView) view.findViewById(R.id.tv_confirm);
        passwordEdit = (EditText) view.findViewById(R.id.et_password);
        TextView negativeBtn = (TextView) view.findViewById(R.id.tv_cancel);
        TextView forgetTxt = (TextView) view.findViewById(R.id.tv_forget_password);
        submitBtn.setOnClickListener(new clickListener());
        negativeBtn.setOnClickListener(new clickListener());
        forgetTxt.setOnClickListener(new clickListener());
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.6
        dialogWindow.setAttributes(lp);
    }

    private class clickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            int id = v.getId();
            switch (id) {
                case R.id.tv_confirm:
                    if (TextUtils.isEmpty(passwordEdit.getText().toString())){
                        ToastUtils.showShort(context, R.string.plz_input_pay_password);
                        return;
                    }
                    setSecPwd(passwordEdit.getText().toString());
                    clickListenerInterface.doConfirm();
                    break;
                case R.id.tv_cancel:
                    clickListenerInterface.doCancel();
                    break;
                case R.id.tv_forget_password:
                    clickListenerInterface.forget();
                    break;
            }
        }

    }

}
