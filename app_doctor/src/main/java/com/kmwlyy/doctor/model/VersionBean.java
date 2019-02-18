package com.kmwlyy.doctor.model;

/**
 * Created by KM on 2017/6/16.
 */

public class VersionBean {

        /**
         * NeedUpdate : false
         * VersionNo : 2016093001
         * VersionName :
         * Url :
         */

        private boolean NeedUpdate;
        private int VersionNo;
        private String VersionName;
        private String Url;

        public boolean isNeedUpdate() {
            return NeedUpdate;
        }

        public void setNeedUpdate(boolean NeedUpdate) {
            this.NeedUpdate = NeedUpdate;
        }

        public int getVersionNo() {
            return VersionNo;
        }

        public void setVersionNo(int VersionNo) {
            this.VersionNo = VersionNo;
        }

        public String getVersionName() {
            return VersionName;
        }

        public void setVersionName(String VersionName) {
            this.VersionName = VersionName;
        }

        public String getUrl() {
            return Url;
        }

        public void setUrl(String Url) {
            this.Url = Url;
        }

}
