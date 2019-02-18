package com.kmwlyy.doctor.model.httpEvent;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


/**
 * Created by Winson on 2016/8/18.
 */
public interface HttpUserConsults {

    class ImageFile implements Serializable{

        @SerializedName("FileName")
        public String mFileName;//:"图片名称",

        @SerializedName("FileUrl")
        public String mFileUrl;//:"/xxx/xxx.jpg",

        @SerializedName("FileType")
        public int mFileType;//:0,//文件类型 0=图片

        @SerializedName("Remark")
        public String mRemark;//:"这里是图片的说明"

    }

}
