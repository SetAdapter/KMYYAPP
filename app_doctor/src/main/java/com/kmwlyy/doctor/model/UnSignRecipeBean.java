package com.kmwlyy.doctor.model;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/18.
 */
public class UnSignRecipeBean {
    public String RecipeFileID;
    public String RecipeName;
    public String MemberName;
    public String MemberGender;
    public String MemberAge;
    public String RecipeDate;
    public String SignatureID;
    public Boolean isCheck;
    public ArrayList<String> Diagnoses;

    public UnSignRecipeBean(String recipeFileID, String recipeName, String memberName, String memberGender, String memberAge, String recipeDate, String signatureID, ArrayList<String> diagnoses) {
        isCheck = false;
        RecipeFileID = recipeFileID;
        RecipeName = recipeName;
        MemberName = memberName;
        MemberGender = memberGender;
        MemberAge = memberAge;
        RecipeDate = recipeDate;
        SignatureID = signatureID;
        Diagnoses = diagnoses;
    }
}
