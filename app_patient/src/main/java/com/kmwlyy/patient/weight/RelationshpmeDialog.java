package com.kmwlyy.patient.weight;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;


import com.kmwlyy.patient.R;

import java.util.ArrayList;
import java.util.List;


public class RelationshpmeDialog extends Dialog {

    private static final String[] TYPE = new String[]{
            "父亲", "母亲", "儿子", "女儿", "孙子", "孙女"
    };

    private WheelView wv_1;



    private String tagname;
    private String tagtype;
    private Context context;

    //private ArrayList<CreditCardMode> list;
    public RelationshpmeDialog(Context context) {
        super(context, R.style.CommonDialog);
        this.context = context;



        //控制高度 和位置
        Window window = getWindow();
        LayoutParams lp = window.getAttributes();
        lp.height = LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;

        //点击外部消失
        setCanceledOnTouchOutside(true);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_wheel_year, null);
        TextView cancel_tv = (TextView) dialogView.findViewById(R.id.cancel_tv);
        cancel_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        TextView confirmation_tv = (TextView) dialogView.findViewById(R.id.confirmation_tv);
        confirmation_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagname = wv_1.getSeletedItem();

                nameListener.refreshBackTypeNameUI(tagname);
                dismiss();
            }
        });
        wv_1 = (WheelView) dialogView.findViewById(R.id.wv_1);
        // wv.setOffset(0);// 偏移量
        wv_1.setOffset(2);
//        wv_1.setItems(Arrays.asList(BACK_NAME));// 实际内容
        List<String> list=new ArrayList<String>();
        for(int i=0;i<TYPE.length;i++){
            list.add(TYPE[i]);
        }
        wv_1.setItems(list);// 实际内容
        wv_1.setSeletion(0);// 设置默认被选中的项目

        wv_1.setOnWheelViewListener(new WheelView.OnWheelViewListener() {

            @Override
            public void onSelected(int selectedIndex, String item) {
                // 选中后的处理事件
                Log.d("BackTypeDailog", "[Dialog]selectedIndex: " + selectedIndex
                        + ", item: " + item);
                //名字
                //nameListener.refreshBackTypeNameUI(item);
                tagname = item;
            }

        });


        setContentView(dialogView);

    }


    public interface BackTypeNameListenre {
        /**
         * 回调函数，用于在Dialog的监听事件触发后刷新Activity的UI显示
         */
        void refreshBackTypeNameUI(String string);
    }

    private BackTypeNameListenre nameListener;

    public void setBackTypeNameListenre(BackTypeNameListenre nameListener) {
        this.nameListener = nameListener;
    }


}
