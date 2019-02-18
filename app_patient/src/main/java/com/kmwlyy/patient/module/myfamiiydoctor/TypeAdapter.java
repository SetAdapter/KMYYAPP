package com.kmwlyy.patient.module.myfamiiydoctor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kmwlyy.patient.R;


public class TypeAdapter extends BaseAdapter {

    private String[] name;
    private Context context;

    public TypeAdapter(Context context, String[] name) {
        this.context = context;
        this.name = name;
    }

    @Override
    public int getCount() {
        return name.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHodler holder;
        if (convertView == null) {
            holder = new ViewHodler();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_type, parent, false);
            holder.type = (TextView) convertView.findViewById(R.id.type);
            convertView.setTag(holder);
        } else {
            holder = (ViewHodler) convertView.getTag();
        }
        holder.type.setText(name[position]);

        return convertView;
    }

    class ViewHodler {
        TextView type;
    }

//    public String setTypeText(int position, )
}
