package com.kmwlyy.patient.casehistory;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.kmwlyy.core.util.ImageUtils;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.helper.base.BaseActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import uk.co.senab.photoview.PhotoView;


/**
 * Created by Winson on 2016/10/29.
 */
public class PhotoViewActivity extends BaseActivity {

    public static final String TAG = PhotoViewActivity.class.getSimpleName();

    static final String PHOTOS = "PHOTOS";
    static final String INDEX = "INDEX";

    public static void startPhotoViewActivity(Context context, String[] photos, int index) {
        Intent intent = new Intent(context, PhotoViewActivity.class);
        intent.putExtra(PHOTOS, photos);
        intent.putExtra(INDEX, index);
        context.startActivity(intent);
    }

    ViewPager mPhotoViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_view_layout);
        View tv_left = findViewById(R.id.tv_left);
        View tv_center = findViewById(R.id.tv_center);
        tv_center.setVisibility(View.GONE);
        tv_left.setVisibility(View.VISIBLE);
        tv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        int index = getIntent().getIntExtra(INDEX, 0);
        String[] photos = getIntent().getStringArrayExtra(PHOTOS);
        mPhotoViewPager = (ViewPager) findViewById(R.id.view_pager);
        mPhotoViewPager.setAdapter(new PhotoPageAdapter(photos));
        mPhotoViewPager.setCurrentItem(index);
    }

    class PhotoPageAdapter extends PagerAdapter {

        private String[] mPhotos;

        public PhotoPageAdapter(String[] photos) {
            this.mPhotos = photos;
        }

        @Override
        public int getCount() {
            return mPhotos.length;
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {

            PhotoView photoView = new PhotoView(container.getContext());
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            photoView.setLayoutParams(lp);
            ImageLoader.getInstance().displayImage(mPhotos[position],
                    photoView, ImageUtils.getRectDisplayOptions(R.drawable.default_res));

            container.addView(photoView);

            return photoView;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

}
