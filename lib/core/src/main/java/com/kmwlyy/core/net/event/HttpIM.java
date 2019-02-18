package com.kmwlyy.core.net.event;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.net.bean.UploadImageResp;
import com.kmwlyy.core.util.PictureUtil;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Winson on 2016/8/30.
 */
public interface HttpIM {

    class UploadImage extends HttpEvent<UploadImageResp> {

        public UploadImage(String path, HttpListener<UploadImageResp> mListener) {
            super(mListener);
            mReqMethod = HttpClient.POST;
            mReqAction = "/Upload/Image";

            mImageFiles = new HashMap();
            String tmepName = null;
            try {
                tmepName = PictureUtil.bitmapToPath(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (tmepName != null) {
                mImageFiles.put("image", new File(path));
            }

        }

    }

}
