package com.kmwlyy.patient.module.doctorMessage.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


import com.kmwlyy.patient.R;

import java.util.List;

/**
 * Created by Gab on 2017/7/29 0029.
 *
 */

//public class Doctor_Message_Adapter extends CommonAdapter<Message_bean> {
public class Doctor_Message_Adapter extends BaseAdapter {

    Context context;
    List list;

    public Doctor_Message_Adapter(Context context, List list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if(view==null){
            view= LayoutInflater.from(context).inflate(R.layout.message_listview_item,null);
        }
        return view;
    }
//    public Doctor_Message_Adapter(Context context, List data) {
//        super(context, data);
//    }
//
//    @Override
//    public View getView(final int position, View arg1, ViewGroup arg2) {
//        View itemView = getViewCache().get(position);
//        if (null == itemView) {
//            Message_bean message_bean = getData().get(position);
//            itemView = LayoutInflater.from(getContext()).inflate(R.layout.message_listview_item, null);
//            TextView iv_status = (TextView) itemView.findViewById(R.id.iv_status);
//            TextView tv_title = (TextView) itemView.findViewById(R.id.tv_title);
//            TextView tv_time = (TextView) itemView.findViewById(R.id.tv_time);
//            TextView tv_content = (TextView) itemView.findViewById(R.id.tv_content);
//            switch (message_bean.isRead) {
//                case "0":
//                    iv_status.setVisibility(View.VISIBLE);
//                    break;
//                case "1":
//                    iv_status.setVisibility(View.INVISIBLE);
//                    break;
//                default:
//                    break;
//            }
//            switch (message_bean.type) {
//                case "0":
//                    tv_title.setText(message_bean.title);
//                    tv_time.setText(message_bean.createDate);
//                    tv_content.setText(message_bean.content);
//                    break;
//                default:
//                    break;
//            }
//        }
//        return null;
//    }
}
