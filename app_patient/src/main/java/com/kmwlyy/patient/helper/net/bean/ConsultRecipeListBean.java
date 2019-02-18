package com.kmwlyy.patient.helper.net.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * @Description描述:
 * @Author作者: zhaoqile
 * @Date日期: 2017/1/6
 */
public class ConsultRecipeListBean {
    @SerializedName("RecipeFiles")
    public ArrayList<ConsultRecipeBean> ConsultRecipeList;
}
