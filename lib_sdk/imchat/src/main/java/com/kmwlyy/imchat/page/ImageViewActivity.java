package com.kmwlyy.imchat.page;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kmwlyy.imchat.R;
import com.kmwlyy.imchat.utils.FileUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.winson.ui.widget.ZoomImageView;

import java.io.IOException;

public class ImageViewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_image_view);
        ViewUtils.inject(this); //注入view和事件
        String file = getIntent().getStringExtra("filename");
        ZoomImageView imageView = (ZoomImageView) findViewById(R.id.image);
        Bitmap bitmap = getImage(FileUtil.getCacheFilePath(file));
        if (bitmap != null){
            imageView.setImageBitmap(bitmap);
        }

        ((TextView)findViewById(R.id.tv_center)).setText("图片");
        findViewById(R.id.tv_left).setVisibility(View.VISIBLE);
        findViewById(R.id.tv_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private Bitmap getImage(String path){
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int reqWidth, reqHeight, width=options.outWidth, height=options.outHeight;
        if (width > height){
            reqWidth = getWindowManager().getDefaultDisplay().getWidth();
            reqHeight = (reqWidth * height)/width;
        }else{
            reqHeight = getWindowManager().getDefaultDisplay().getHeight();
            reqWidth = (width * reqHeight)/height;
        }
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        try{
            options.inSampleSize = inSampleSize;
            options.inJustDecodeBounds = false;
            Matrix mat = new Matrix();
            Bitmap bitmap = BitmapFactory.decodeFile(path, options);
            if (bitmap == null) {
                Toast.makeText(this, "正在下载，请稍候", Toast.LENGTH_SHORT).show();
                return null;
            }
            ExifInterface ei =  new ExifInterface(path);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch(orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    mat.postRotate(90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    mat.postRotate(180);
                    break;
            }
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mat, true);
        }catch (IOException e){
            return null;
        }
    }
}
