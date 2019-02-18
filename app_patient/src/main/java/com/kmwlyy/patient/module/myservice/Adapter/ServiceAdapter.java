package com.kmwlyy.patient.module.myservice.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kmwlyy.patient.R;
import com.kmwlyy.patient.helper.utils.LibUtils;
import com.kmwlyy.patient.module.myservice.Bean.Doorservoce;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by ljf on 2017/7/31.
 * 上门服务适配器
 */

public class ServiceAdapter extends AdapterBase<Doorservoce.DataBean> {

    public ServiceAdapter(Context context, List<Doorservoce.DataBean> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        View itemView = getViewCache().get(position);
        if (null == itemView) {
            final Doorservoce.DataBean dataBean = getData().get(position);
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_service, null);
            TextView title = (TextView) itemView.findViewById(R.id.title);
            TextView detail = (TextView) itemView.findViewById(R.id.detail);
            TextView text_price = (TextView) itemView.findViewById(R.id.text_price);
            TextView text_buy = (TextView) itemView.findViewById(R.id.text_buy);
            ImageView image_pic = (ImageView) itemView.findViewById(R.id.image_pic);

            title.setText(dataBean.getPackageName());//名称
            detail.setText(dataBean.getRemark());//说明详情
            text_price.setText((dataBean.getPrice() + "元")); // 价格
            text_buy.setText(dataBean.getBuyUserNum() + "人已购"); //购买人数
//            ImageLoader.getInstance().displayImage(datas.getPhotoUrl() ,image_pic, LibUtils.getSquareDisplayOptions());//图片
            if (position == 1) {
                image_pic.setImageResource(R.drawable.vcgq);
            }
            itemView.setTag(dataBean);
            getViewCache().put(position, itemView);
        }
        return itemView;
    }

}
