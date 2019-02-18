package com.kmwlyy.patient.kdoctor.bean;

/**
 * @Description描述:
 * @Author作者: hx
 * @Date日期: 2016/8/24
 */
public class SearchItemBean {
    private String resultId;
    private String resultTitle;
    private String resultDesc;
    private String resultType;
    private String deptName;
    private String pictureUrl;
    private String titleNo;
    private String address;
    private String phone;
    private String hospitalLevel;
    private String hospitalAlias;
    private String url;
    private String price;
    private String titleName;
    private String mainImage;
    private String beadHouseName ;//养老院姓名
    private String addressArea ;//养老院地址
    private String telPhone;//养老院电话
    private String category;//资讯会用到 add by liuyixin
    private String manufactorName;// 产品厂商
    /**医生性别*/
    private String sex;

    /*医生类型，"0"表示本地医生   "1"表示160的医生*/
    private String dataType ;

    /**
     * 查看更多的类型
     */
    private String moreType;

    /*是否显示更多的按钮*/
    private boolean isShowMore = true;


    //列表item的类型，疾病 disease_info，药品 drug_info，医院 hospital_info，医生 doctor_info，症状 symptom_inf,资讯news_info
    public final static String DISEASE_INFO = "disease_info";
    public final static String DRUG_INFO = "drug_info";
    public final static String HOSPITAL_INFO = "hospital_info";
    public final static String DOCTOR_INFO = "doctor_info";
    public final static String SYMPTOM_INFO = "symptom_info";
    public final static String NEWS_INFO = "news_info";
    public final static String BEADHOUSE_INFO = "beadhouse_info";

    public SearchItemBean(String resultTitle,String moreType) {
        this.moreType = moreType;
        this.resultTitle = resultTitle;
    }
    public SearchItemBean(String resultTitle,String moreType,boolean isShowMore) {
        this.moreType = moreType;
        this.resultTitle = resultTitle;
        this.isShowMore = isShowMore;
    }

    public boolean isShowMore() {
        return isShowMore;
    }

    public String getBeadHouseName() {
        return beadHouseName;
    }

    public void setBeadHouseName(String beadHouseName) {
        this.beadHouseName = beadHouseName;
    }

    public String getAddressArea() {
        return addressArea;
    }

    public void setAddressArea(String addressArea) {
        this.addressArea = addressArea;
    }

    public String getTelPhone() {
        return telPhone;
    }

    public void setTelPhone(String telPhone) {
        this.telPhone = telPhone;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public String getPrice() {
        return price;
    }

    public String getMoreType() {
        return moreType;
    }

    public void setMoreType(String moreType) {
        this.moreType = moreType;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHospitalLevel() {
        return hospitalLevel;
    }

    public void setHospitalLevel(String hospitalLevel) {
        this.hospitalLevel = hospitalLevel;
    }

    public String getHospitalAlias() {
        return hospitalAlias;
    }

    public void setHospitalAlias(String hospitalAlias) {
        this.hospitalAlias = hospitalAlias;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static String getDiseaseInfo() {
        return DISEASE_INFO;
    }

    public static String getDrugInfo() {
        return DRUG_INFO;
    }

    public static String getHospitalInfo() {
        return HOSPITAL_INFO;
    }

    public static String getDoctorInfo() {
        return DOCTOR_INFO;
    }

    public static String getSymptomInfo() {
        return SYMPTOM_INFO;
    }

    public String getResultId() {
        return resultId;
    }

    public void setResultId(String resultId) {
        this.resultId = resultId;
    }

    public String getResultTitle() {
        return resultTitle;
    }

    public void setResultTitle(String resultTitle) {
        this.resultTitle = resultTitle;
    }

    public String getResultDesc() {
        return resultDesc;
    }

    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getTitleNo() {
        return titleNo;
    }

    public void setTitleNo(String titleNo) {
        this.titleNo = titleNo;
    }

    public String getCategory() {
        return category;
    }

    public void setManufactorName(String manufactorName) {
        this.manufactorName = manufactorName;
    }

    public String getManufactorName() {
        return manufactorName;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
