package com.winson.ui.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.winson.ui.R;

import java.util.List;

/**
 * @author fyales
 * @since 8/26/15.
 */
public class TagBaseAdapter extends BaseAdapter {
    public static final int WHITE = 0; //底色为白色，一次只能一个变成黄色。 评论列表时使用
    public static final int GRAY = 1; //底色为灰色，评论列表的item里使用
    public static final int YELLOW = 2; //底色为白色，支持多个变成黄色。提交评论时使用

    private Context mContext;
    private List<String> mList;
    private List<String> mHighList;
    private String ClickString = "";
    private int mType;

    public TagBaseAdapter(Context context, List<String> list,int type) {
        mContext = context;
        mList = list;
        mType = type;
    }

    public TagBaseAdapter(Context context, List<String> list,List<String> highlist ,int type) {
        mContext = context;
        mList = list;
        mHighList = highlist;
        mType = type;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public String getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            if(mType == WHITE || mType == YELLOW){
                convertView = LayoutInflater.from(mContext).inflate(R.layout.tagview, null);
            }else if(mType == GRAY){
                convertView = LayoutInflater.from(mContext).inflate(R.layout.tagview_gray, null);
            }

            holder = new ViewHolder();
            holder.tagBtn = (Button) convertView.findViewById(R.id.tag_btn);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        final String text = getItem(position);
        //点击高亮
        if(mType == WHITE){
            if(text.equals(ClickString)){
                holder.tagBtn.setTextColor(mContext.getResources().getColor(R.color.white));
                holder.tagBtn.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.tag_view_orange));
            }else{
                holder.tagBtn.setTextColor(mContext.getResources().getColor(R.color.color_tag_string));
                holder.tagBtn.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.tag_view));
            }
        }

        if(mType == YELLOW){
            if(mHighList.contains(text)){
                holder.tagBtn.setTextColor(mContext.getResources().getColor(R.color.white));
                holder.tagBtn.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.tag_view_orange));
            }else{
                holder.tagBtn.setTextColor(mContext.getResources().getColor(R.color.color_tag_string));
                holder.tagBtn.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.tag_view));
            }
        }

        holder.tagBtn.setText(text);
        return convertView;
    }

    static class ViewHolder {
        Button tagBtn;
    }

    /**
     * 点击后，一个tag高亮显示
     * @param str
     */
    public void setHighLight(String str){
        ClickString = str;
        notifyDataSetChanged();
    }

    /**
     * 点击后，一个tag高亮显示
     */
    public String getHighLight(){
        return ClickString;

    }

    /**
     * 设置多个tag 高亮显示
     * @param list
     */
    public void setMutiHighLight(List<String> list){
        mHighList.clear();
        if(null != list){
            mHighList = list;
            notifyDataSetChanged();
        }

    }

    public void setData(List<String> list){
        mList.clear();
        if(null != list){
            mList = list;
            notifyDataSetChanged();
        }

    }
}
