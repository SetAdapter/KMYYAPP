package com.kmwlyy.patient.helper.net.event;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.PictureUtil;
import com.kmwlyy.patient.helper.net.bean.UploadImageResp;

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
            String tmepName = null;
            try {
                tmepName = PictureUtil.bitmapToPath(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mImageFiles = new HashMap();
            if (tmepName != null) {
                mImageFiles.put("image", new File(tmepName));
            }

        }

    }

}
