package com.kmwlyy.doctor.model;

import java.util.List;

/**
 * Created by Administrator on 2016/12/23.
 */
public class SignRecipeBean {

    /**
     * RecipeFileID : CW2017062010380003
     * MemberID : c153ee3d70f343458faea2137fed55f2
     * MemberName : 陆正顺
     * MemberGender : 0
     * MemberBirthday : 1994-10-14
     * RecipeName : 西药处方
     * RecipeDate : 2017-06-20
     * OPDRegisterID : ff842b653c034bd8bf01d54735756615
     * SignatureID : ff85a092dfab4bf09ef8ade91865591f
     * MemberAge : 23
     * RecipeFileStatus : 2
     * Diagnoses : ["贫血"]
     */

    private String RecipeFileID;
    private String MemberID;
    private String MemberName;
    private int MemberGender;
    private String MemberBirthday;
    private String RecipeName;
    private String RecipeDate;
    private String OPDRegisterID;
    private String SignatureID;
    private int MemberAge;
    private int RecipeFileStatus;
    private List<String> Diagnoses;
    public Boolean isCheck = false;
    public String getRecipeFileID() {
        return RecipeFileID;
    }

    public void setRecipeFileID(String RecipeFileID) {
        this.RecipeFileID = RecipeFileID;
    }

    public String getMemberID() {
        return MemberID;
    }

    public void setMemberID(String MemberID) {
        this.MemberID = MemberID;
    }

    public String getMemberName() {
        return MemberName;
    }

    public void setMemberName(String MemberName) {
        this.MemberName = MemberName;
    }

    public int getMemberGender() {
        return MemberGender;
    }

    public void setMemberGender(int MemberGender) {
        this.MemberGender = MemberGender;
    }

    public String getMemberBirthday() {
        return MemberBirthday;
    }

    public void setMemberBirthday(String MemberBirthday) {
        this.MemberBirthday = MemberBirthday;
    }

    public String getRecipeName() {
        return RecipeName;
    }

    public void setRecipeName(String RecipeName) {
        this.RecipeName = RecipeName;
    }

    public String getRecipeDate() {
        return RecipeDate;
    }

    public void setRecipeDate(String RecipeDate) {
        this.RecipeDate = RecipeDate;
    }

    public String getOPDRegisterID() {
        return OPDRegisterID;
    }

    public void setOPDRegisterID(String OPDRegisterID) {
        this.OPDRegisterID = OPDRegisterID;
    }

    public String getSignatureID() {
        return SignatureID;
    }

    public void setSignatureID(String SignatureID) {
        this.SignatureID = SignatureID;
    }

    public int getMemberAge() {
        return MemberAge;
    }

    public void setMemberAge(int MemberAge) {
        this.MemberAge = MemberAge;
    }

    public int getRecipeFileStatus() {
        return RecipeFileStatus;
    }

    public void setRecipeFileStatus(int RecipeFileStatus) {
        this.RecipeFileStatus = RecipeFileStatus;
    }

    public List<String> getDiagnoses() {
        return Diagnoses;
    }

    public void setDiagnoses(List<String> Diagnoses) {
        this.Diagnoses = Diagnoses;
    }
}
