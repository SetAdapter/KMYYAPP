package com.kmwlyy.patient.helper.net.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by xcj on 2016/8/20.
 */
public class DiagnoseDetails {
//    RecipeOrderID": "62e5cafc5d524357bf4b355ffecc766f",RecipeFiles
     @SerializedName("RecipeOrderID")
     public String mRecipeOrderID;

    @SerializedName("RecipeFiles")
    public ArrayList<RecipeFile> mRecipeFiles;

    public static class RecipeFile {
//        "RecipeFileID": "965dbbeb113d4a5796e3d70023541018",
//                "DoctorID": "89F9E5907FD04DBF96A9867D1FA30396",
//                "MemberID": "77C5CF07923A4E3D8121F628336527B8",
//                "OPDRegisterID": "7235d13b32ca489f8f95319c5304cb22",
//                "RecipeDate": "20160812",
//                "RecipeName": "中药处方",
//                "RecipeFileStatus": 0,
//                "RecipeType": 1,
//                "RecipeTypeName": "中药处方",
//                "TCMQuantity": 1,
//                "Usage": "",
//                "Amount": 0.26,
//                "Details": [


        @SerializedName("RecipeDate")
        public String mRecipeDate;//处方日期

        @SerializedName("RecipeName")
        public String mRecipeName;//处方名称

        @SerializedName("RecipeType")
        public int mRecipeType;//处方类型

        @SerializedName("TCMQuantity")
        public int mTCMQuantity;//剂数

        @SerializedName("Usage")
        public String mUsage;//用法

        @SerializedName("Amount")
        public double mAmount;//金额

        @SerializedName("Details")
        public ArrayList<Detail> mDetails;

        @SerializedName("Diagnoses")
        public ArrayList<Diagnose> mDiagnoses;
    }

    public static class Detail implements Serializable {
//        "Dose": 1,
//                "Quantity": 1,
//                "DrugRouteName": "",
//                "Frequency": "",
//                "Drug

        @SerializedName("Dose")
        public int mDose;//剂量

        @SerializedName("Quantity")
        public int mQuantity;//数量

        @SerializedName("DrugRouteName")
        public String mDrugRouteName;//服用方法

        @SerializedName("Frequency")
        public String mFrequency;//服用次数

        @SerializedName("Drug")
        public Drug mDrug;

    }

    public static class Diagnose implements Serializable{
        @SerializedName("Detail")
        public DiagnoseDetail mDetail;

        @SerializedName("Description")
        public String mDescription;
    }

    public static class DiagnoseDetail implements Serializable{
//        "DiseaseCode": "A01.054",
//                "DiseaseName": "肠出血性伤寒",
        @SerializedName("DiseaseCode")
        public String mDiseaseCode;

        @SerializedName("DiseaseName")
        public String mDiseaseName;

    }

    public static class Drug implements Serializable{
//        "DrugID": "0199D84E08EB47178C359343755A0BE0",
//                "DrugCode": "10601",
//                "DrugName": "炒苍耳子",
//                "Specification": "5g*200",
//                "DrugExpiryDay": "0",
//                "BatchNO": "-",
//                "FactoryName": "江西",
//                "PinYinName": "CCEZ",
//                "LicenseNo": "-",
//                "TotalDose": 1,
//                "DoseUnit": "g",
//                "Unit": "g",
//                "UnitPrice": 0.05,
//                "DrugType": 1,
//                "PharmacyID": "1",
//                "PharmacyDrugID": "2",
//                "Status": 1
        @SerializedName("DrugName")
        public String mDrugName;

        @SerializedName("Specification")
        public String mSpecification;

        @SerializedName("FactoryName")
        public String mFactoryName;

        @SerializedName("DoseUnit")
        public String mDoseUnit;//剂量单位

        @SerializedName("Unit")
        public String mUnit;//数量单位


    }
}
