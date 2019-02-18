package com.kmwlyy.doctor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.Utils.MyUtils;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.LinkedList;

/**
 * @Description描述:
 * @Author作者: zhaoqile
 * @Date日期: 2016/12/13
 */
public class UploadImageAdapter extends BaseAdapter {
    private LinkedList<String> imagePathList;
    private Context context;
    private boolean isAddData = true;
    /**
     * 控制最多上传的图片数量
     */
    private int imageNumber = 3;

    public UploadImageAdapter(Context context, LinkedList<String> imagePath) {
        this.context = context;
        this.imagePathList = imagePath;
    }

    public UploadImageAdapter(Context context, LinkedList<String> imagePath, int imageNumber) {
        this.context = context;
        this.imagePathList = imagePath;
        this.imageNumber = imageNumber;
    }

    private class ViewHolder {
        ImageView imageView;
        CheckBox checkBox;
    }

    public void update(LinkedList<String> imagePathList) {
        this.imagePathList = imagePathList;
        //这里控制选择的图片放到前面,默认的图片放到最后面,
        if (isAddData) {
            //集合中的总数量等于上传图片的数量加上默认的图片不能大于imageNumber + 1
            if (imagePathList.size() == imageNumber + 1) {
                //移除默认的图片
                imagePathList.removeLast();
                isAddData = false;
            }
        } else {
            //添加默认的图片
            imagePathList.addLast(null);
            isAddData = true;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return imagePathList == null ? 0 : imagePathList.size();
    }

    @Override
    public Object getItem(int position) {
        return imagePathList == null ? null : imagePathList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
      /*  ImageView iv_image;
        if (convertView == null) {//创建ImageView
            iv_image = new ImageView(context);
            iv_image.setLayoutParams(new AbsListView.LayoutParams(ImageUtils.getWidth(context) / 5 - 5, ImageUtils.getWidth(context) / 5 - 5));
            iv_image.setScaleType(ImageButton.ScaleType.CENTER_CROP);
            convertView = iv_image;
        } else {
            iv_image = (ImageView) convertView;
        }
        if (getItem(position) == null) {//图片地址为空时设置默认图片
//            iv_image.setBackgroundResource(R.drawable.add_square);
            ImageLoader.getInstance().displayImage(null,
                    iv_image,
                    LibUtils.getSquareDisplayOptions());
        } else {
            //获取图片缩略图，避免OOM
            ImageLoader.getInstance().displayImage("file://" + getItem(position),
                    iv_image,
                    LibUtils.getSquareDisplayOptions());

//            Bitmap bitmap = ImageUtils.getImageThumbnail((String)getItem(position), ImageUtils.getWidth(context) / 3 - 5, ImageUtils.getWidth(context) / 3 - 5);
//            iv_image.setImageBitmap(bitmap);
        }
        return convertView;*/

        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_add_photo_gv_items, parent, false);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.main_gridView_item_photo);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.main_gridView_item_cb);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (getItem(position) == null) {
            holder.checkBox.setVisibility(View.GONE);
            ImageLoader.getInstance().displayImage(null,
                    holder.imageView,
                    MyUtils.getSquareDisplayOptions());
        } else {
            holder.checkBox.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage("file://" + getItem(position),
                    holder.imageView,
                    MyUtils.getSquareDisplayOptions());
        }
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePathList.remove(position);
                update(imagePathList);
            }
        });
        return convertView;
    }
    public LinkedList<String> getData(){
        return imagePathList;
    }

}
