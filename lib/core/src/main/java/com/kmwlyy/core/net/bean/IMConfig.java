package com.kmwlyy.core.net.bean;

/**
 * Created by Winson on 2016/8/13.
 */
public class IMConfig {

    /**
     * sdkAppID : 1400009922
     * userSig : eJx1jktTgzAURvf9FQxbHQcCSYg7ykuqVdFW1A1DIUhaHjEElHH871qmM7Lxbs*Z*52vhaIo6ubm8SLNsrZvZCJHTlXlUlEh0IF6-sc5Z3mSysQQ*cR1U-s9QsDcop*cCZqkhaRisix01GYGy2kjWcFOnNimYxkEIQdbpuvCpefbyMIIQaLbAM9-d-khmSr*n*-Y2wTX3osTRq7hlbdtP5Bqt6rqYPuxK3j5ui39O9qvARmcbj8GeXxVCxGFpX0ftQPizfs1Jv5mhMi3xrA*LB8qR0YrMIg8jquns*cg2*PZpGQ1PQVhDQMLaXBGByo61jaTADQd6sA4Zmvq4nvxAyg3ZbU_
     * identifier : 123
     * accountType : 5212
     */

    private int sdkAppID;
    private String userSig;
    private String identifier;
    private int accountType;

    public int getSdkAppID() {
        return sdkAppID;
    }

    public void setSdkAppID(int sdkAppID) {
        this.sdkAppID = sdkAppID;
    }

    public String getUserSig() {
        return userSig;
    }

    public void setUserSig(String userSig) {
        this.userSig = userSig;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }
}
