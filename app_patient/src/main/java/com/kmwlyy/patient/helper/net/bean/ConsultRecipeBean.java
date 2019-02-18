package com.kmwlyy.patient.helper.net.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @Description描述: 音视频会诊中的处方数据
 * @Author作者: zhaoqile
 * @Date日期: 2016/12/30
 */
public class ConsultRecipeBean implements Serializable{
    //处方编号
    @SerializedName("RecipeNo")
    public String mRecipeNo;
    //处方文件编号
    @SerializedName("RecipeFileID")
    public String mRecipeFileID;
    //处方日期
    @SerializedName("RecipeDate")
    public String mRecipeDate;
    //处方名称
    @SerializedName("RecipeName")
    public String mRecipeName;
    //处方状态
    @SerializedName("RecipeFileStatus")
    public int mRecipeFileStatus;
    //处方类型（1=中药，2=西药）
    @SerializedName("RecipeType")
    public int mRecipeType;
    //处方类型名，中药处方，西药处方
    @SerializedName("RecipeTypeName")
    public String mRecipeTypeName;
    //剂数
    @SerializedName("TCMQuantity")
    public int mTCMQuantity;
    //用法
    @SerializedName("Usage")
    public String mUsage;
    //价格
    @SerializedName("Amount")
    public float mAmount;
    //说明
    @SerializedName("Remark")
    public String mRemark;

    @SerializedName("ReplaceDose")
    public int mReplaceDose;

    @SerializedName("ReplacePrice")
    public float mReplacePrice;

    @SerializedName("DecoctNum")
    public int mDecoctNum;

    @SerializedName("DecoctTargetWater")
    public int mDecoctTargetWater;

    @SerializedName("DecoctTotalWater")
    public int mDecoctTotalWater;

    @SerializedName("Times")
    public int mTimes;

    @SerializedName("BoilWay")
    public int mBoilWay;

    @SerializedName("FreqTimes")
    public int mFreqTimes;

    @SerializedName("FreqDay")
    public int mFreqDay;

    @SerializedName("State")
    public int mState;
    @SerializedName("OrderState")
    public int mOrderState;
    @SerializedName("OrderNo")
    public String mOrderNo;
    @SerializedName("IsExpried")
    public boolean isExpried;

}
