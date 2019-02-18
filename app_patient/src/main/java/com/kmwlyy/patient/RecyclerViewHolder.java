package com.kmwlyy.patient;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/8/8.
 */

public class RecyclerViewHolder extends RecyclerView.ViewHolder{
   public ImageView imageView;
   public TextView textView;
    public RecyclerViewHolder(View itemView) {
        super(itemView);
        imageView= (ImageView) itemView.findViewById(R.id.mark);
        textView= (TextView) itemView.findViewById(R.id.btn_select);
    }
}
