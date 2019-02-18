package com.kmwlyy.patient;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Stefan on 2017/8/8.
 */

public class RecyclerAdapter extends RecyclerView.Adapter implements View.OnClickListener {
    private Context context;
    List list;
    int checkPosition = 0;
    public void setCheckPosition(int checkPosition) {
        this.checkPosition = checkPosition;
    }

    public int getCheckPosition() {
        return checkPosition;
    }

    //ItemClick的回调接口
    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }
    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public RecyclerAdapter(Context context, List list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerViewHolder holder = new RecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false));
        holder.itemView.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        //ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
        ((RecyclerViewHolder)holder).textView.setText(list.get(position)+"");
        if(position==0){
            ((RecyclerViewHolder)holder).imageView.setVisibility(View.VISIBLE);
        }
        holder.itemView.setTag(position);
        if (position == checkPosition) {
            ((RecyclerViewHolder) holder).imageView.setVisibility(View.VISIBLE);
        } else {
            ((RecyclerViewHolder) holder).imageView.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onClick(View v) {
        //如果设置了回调，则设置点击事件
//        if (mOnItemClickLitener != null) {
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mOnItemClickLitener.onItemClick(v, (RecyclerViewHolder) holder, position);
//                }
//            });
//        }
        if (mOnItemClickLitener !=null){
            mOnItemClickLitener.onItemClick(v, (Integer) v.getTag());
        }

    }
}
