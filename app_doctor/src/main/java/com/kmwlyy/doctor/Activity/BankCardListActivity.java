package com.kmwlyy.doctor.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.doctor.NetService;
import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.adapter.BankCardListAdapter;
import com.kmwlyy.doctor.model.BankCardBean;
import com.kmwlyy.doctor.model.httpEvent.Http_getBankCardList_Event;
import com.kmwlyy.doctor.model.httpEvent.Http_unbindBankCard_Event;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.winson.ui.widget.AlterDialogView;

import java.util.ArrayList;
import java.util.List;


/**
 * @Description描述: 来自康美通 KMT_Doctor_0513 项目
 * @Author作者: zhaoqile
 * @Date日期: 2016/8/19
 */
public class BankCardListActivity  extends BaseActivity {
    private final String TAG = "BankCardListActivity";

    //返回
    @ViewInject(R.id.tv_left)
    TextView btn_left;
    @ViewInject(R.id.tv_right)
    TextView tv_right;
    @ViewInject(R.id.tv_center)
    TextView tv_title;
    @ViewInject(R.id.listView)
    ListView listView;
    @ViewInject(R.id.btn_addCard)
    Button btn_addCard;

    private boolean flag = false;

    private int mflag = 0;
    private final int CODE = 1;

    private List<BankCardBean> dataList = new ArrayList<>();

    private BankCardListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_card_list);
        ViewUtils.inject(this);

        initView();
        initListener();
        getData();
    }

    private void initView()
    {
        tv_title.setText(R.string.string_my_card);
        tv_right.setText(R.string.string_unbind);
        btn_left.setVisibility(View.VISIBLE);
        tv_right.setVisibility(View.VISIBLE);
        adapter = new BankCardListAdapter(this, dataList, delete_click);
        listView.setAdapter(adapter);
    }

    private void initListener()
    {
        btn_left.setOnClickListener(this);
        tv_right.setOnClickListener(this);
        btn_addCard.setOnClickListener(this);
    }


    @Override
    public void onClick(View arg0) {
        super.onClick(arg0);
        switch (arg0.getId()){
            case R.id.tv_left:
                finish();
                break;
            case R.id.tv_right:
                if (!flag) {
                    adapter.showDelete(1);
                    adapter.notifyDataSetChanged();
                    tv_right.setText(getResources().getString(R.string.string_cancel));
                    flag = true;
                } else {
                    adapter.showDelete(0);
                    adapter.notifyDataSetChanged();
                    tv_right.setText(getResources().getString(R.string.string_bind_cancel));
                    flag = false;
                }
                break;
            case R.id.btn_addCard:
                Intent intent = new Intent(this, BindBankCardActivity.class);
                startActivityForResult(intent, CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_CANCELED)
            return;
        if (requestCode == CODE) {
            getData();
            mflag = 1;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    private void getData()
    {
        showLoadDialog(R.string.string_wait);

        Http_getBankCardList_Event event = new Http_getBankCardList_Event(new HttpListener<List<BankCardBean>>(
        ) {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                    Log.i(TAG, "request error , code : " + code + " , msg : " + msg);
                }
                dismissLoadDialog();
                ToastUtils.showShort(mContext,getResources().getString(R.string.string_apptoken));
            }

            @Override
            public void onSuccess(List<BankCardBean> list) {
                dismissLoadDialog();

                //更新数据
                if(null != list){
                    dataList.clear();
                    dataList.addAll(list);
                    if (dataList.size() == 0) {
                        tv_right.setVisibility(View.GONE);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });

        HttpClient httpClient = NetService.createClient(mContext, event);
        httpClient.start();

    }


    private View.OnClickListener delete_click = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            final int index = (Integer) v.getTag();

            final String CARD_ID = dataList.get(index).BankCardID;

            AlterDialogView.Builder builder = new AlterDialogView.Builder(BankCardListActivity.this);
            builder.setTitle(getResources().getString(R.string.string_dialog));
            builder.setMessage(getResources().getString(R.string.string_bind_cancel_msg));
            builder.setNegativeButton(getResources().getString(R.string.string_cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton(getResources().getString(R.string.string_yes),
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {

                            dialog.dismiss();

                            showLoadDialog(R.string.string_wait_save);

                            Http_unbindBankCard_Event event = new Http_unbindBankCard_Event(
                                    CARD_ID,
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
                                            ToastUtils.showShort(mContext,getResources().getString(R.string.string_bind_cancel_success));
                                            dataList.remove(index);
                                            adapter.notifyDataSetChanged();

                                        }
                                    });

                            HttpClient httpClient = NetService.createClient(mContext, event);
                            httpClient.start();

                        }
                    });
            builder.create().show();

        }
    };
}
