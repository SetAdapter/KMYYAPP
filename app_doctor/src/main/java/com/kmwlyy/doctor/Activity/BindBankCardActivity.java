package com.kmwlyy.doctor.Activity;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.doctor.NetService;
import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.model.Constant;
import com.kmwlyy.doctor.model.httpEvent.Http_bindBankCard_Event;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;


/**
 * @Description描述: 来自康美通 KMT_Doctor_0513 项目
 * @Author作者: zhaoqile
 * @Date日期: 2016/8/19
 */
public class BindBankCardActivity extends BaseActivity {
    private final String TAG = "BindBankCardActivity";

    //返回
    @ViewInject(R.id.tv_left)
    TextView btn_left;
    @ViewInject(R.id.tv_center)
    TextView tv_title;


    @ViewInject(R.id.tv_bankName)
    private TextView tv_bankName;// 银行名称

    @ViewInject(R.id.bankCard)
    private EditText et_cardNum;// 卡号
    @ViewInject(R.id.openbank)
    private EditText et_openBank;// 开户行
    @ViewInject(R.id.userName)
    private EditText et_userName;// 持卡人姓名

    private PopupWindow bkPopupWindow;
    private ListView list_bankName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_bank_card);
        ViewUtils.inject(this);

        initView();
        initListener();
    }

    private void initView() {
        tv_title.setText(R.string.string_bind_card);
        btn_left.setVisibility(View.VISIBLE);
    }

    private void initListener() {
        btn_left.setOnClickListener(this);
    }

    @OnClick(R.id.tv_left)
    public void bindBack(View v) {
        finish();
    }

    @OnClick(R.id.btn_bind)
    public void add(View v) {
        checkMsg();
    }

    @OnClick(R.id.tv_bankName)
    public void popbankName(View v) {
        ShowPopbank();
    }


    private void checkMsg() {
        String bankName = tv_bankName.getText().toString().trim();
        String cardNum = et_cardNum.getText().toString().trim();
        String openBank = et_openBank.getText().toString().trim();
        String userName = et_userName.getText().toString().trim();
        if (bankName.equals("")) {
            ToastUtils.show(mContext, getResources().getString(R.string.string_bank_msg1), Toast.LENGTH_SHORT);
            return;
        }

        if (cardNum.equals("")||cardNum.length() <16 ||cardNum.length()>19) {
            ToastUtils.show(mContext, getResources().getString(R.string.string_bank_msg2), Toast.LENGTH_SHORT);
            return;
        }
        if (openBank.equals("")) {
            ToastUtils.show(mContext, getResources().getString(R.string.string_bank_msg3), Toast.LENGTH_SHORT);
            return;
        }
        if (userName.equals("")) {
            ToastUtils.show(mContext, getResources().getString(R.string.string_bank_msg4), Toast.LENGTH_SHORT);
            return;
        }

        showLoadDialog(R.string.string_wait_save);

        Http_bindBankCard_Event event = new Http_bindBankCard_Event(
                bankName,openBank,userName,cardNum,
                new HttpListener<String>(
        ) {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                    Log.i(TAG, "request error , code : " + code + " , msg : " + msg);
                }
                dismissLoadDialog();
                ToastUtils.showShort(mContext,getResources().getString(R.string.string_save_failed));
            }

            @Override
            public void onSuccess(String list) {

                dismissLoadDialog();
                ToastUtils.showShort(mContext,getResources().getString(R.string.string_bind_bind_success));
                setResult(1);
                finish();

            }
        });

        HttpClient httpClient = NetService.createClient(mContext, event);
        httpClient.start();

    }

    /**
     * 显示银行名称下拉菜单
     */
    public void ShowPopbank() {
        View PopuBank = LayoutInflater.from(this).inflate(R.layout.pop_bank,
                null);
        list_bankName = (ListView) PopuBank.findViewById(R.id.list_bank);
        int width = tv_bankName.getWidth();
        int heigh = ViewGroup.LayoutParams.WRAP_CONTENT;
        bkPopupWindow = new PopupWindow(PopuBank, width, heigh);
        bkPopupWindow.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(-00000);
        bkPopupWindow.setBackgroundDrawable(dw);
        bkPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        bkPopupWindow.setOutsideTouchable(false);
        bkPopupWindow.showAsDropDown(tv_bankName);
        ArrayAdapter<String> bankadapter = new ArrayAdapter<String>(this,
                R.layout.pop_bank_item, Constant.getBank(mContext));
        list_bankName.setAdapter(bankadapter);
        list_bankName.setAlpha(1);
        list_bankName.setDivider(new ColorDrawable(Color.GRAY));
        list_bankName.setDividerHeight(1);
        list_bankName.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                tv_bankName.setText(Constant.getBank(mContext)[position]);
                bkPopupWindow.dismiss();
            }
        });
    }

}
