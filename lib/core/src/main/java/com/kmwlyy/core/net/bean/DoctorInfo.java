package com.kmwlyy.core.net.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by xcj on 2016/10/23.
 */
public class DoctorInfo implements Serializable {
/*    "Data": {
        "DoctorID": "89F9E5907FD04DBF96A9867D1FA30396",
                "DoctorName": "邱浩强",
                "UserID": "B04D4AE28F994AE2AACBB456D7E0647B",
                "Gender": 1,
                "Marriage": 0,
                "Birthday": "19850808",
                "IDType": 4,
                "IDNumber": "123",
                "Address": "2131",
                "PostCode": "231",
                "Intro": "广东省医师协会神经病学分会委员，中华医学会揭阳内科分会副主任委员。揭阳市优秀专家和拔尖人才，普宁市优秀专业技术人才。政协揭阳市第三、第四、第五届委员。

        \n\n从事临床及医院管理工作30多年，曾于广州市第一人民医院进修，具有丰富的临床经验。专业特长为神经内科、心血管内科，特别在应用“微创”手术治疗高血压性脑出血、颅内血肿等疾病领域有着独特造诣。

        \n\n主持科研项目5项，获揭阳市科技进步一等奖2项、三等奖2项。主编出版专著1部，在国家级医学杂志发表论文6篇，省级医学杂志发表论文17篇。

        ",
        "IsConsultation": false,
                "IsExpert": false,
                "Specialty": "高血压 糖尿病 恶性肿瘤 其他",
                "areaCode": "",
                "HospitalID": "42FF1C61132E443F862510FF3BC3B03A",
                "HospitalName": "",
                "Grade": "0",
                "DepartmentID": "A8064D2DAE3542B18CBD64F467828F57",
                "DepartmentName": "",
                "Education": "",
                "Title": "",
                "Duties": "",
                "CheckState": 0,
                "SignatureURL": "",
                "Sort": 0
    },
            "Total": 0,
            "Status": 0,
            "Msg": ""*/

    @SerializedName("DoctorName")
    public String mDoctorName;
    @SerializedName("Intro")
    public String mIntro;
    @SerializedName("Specialty")
    public String mSpecialty;
    @SerializedName("User")
    public User mUser;

    public class User implements Serializable{
        @SerializedName("Mobile")
        public String mMobile;
        @SerializedName("PhotoUrl")
        public String mPhotoUrl;
    }
}
