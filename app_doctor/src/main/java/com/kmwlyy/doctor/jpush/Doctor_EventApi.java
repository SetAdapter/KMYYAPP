package com.kmwlyy.doctor.jpush;

public interface Doctor_EventApi {
    class updateDoctorInfo{
        /**
         * EvaluatedCount : 15
         * FollowedCount : 4
         * DiagnoseTimes : 54
         * ServiceTimes : 55
         * Income : 0.46
         */

        private int EvaluatedCount;//评论数
        private int FollowedCount; //粉丝
        private int DiagnoseTimes; //问诊量
        private int ServiceTimes;//服务次数
        private double Income;//收益
        private String UserID;
        private String DoctorID;
        private String DoctorName;

        public int getEvaluatedCount() {
            return EvaluatedCount;
        }

        public void setEvaluatedCount(int EvaluatedCount) {
            this.EvaluatedCount = EvaluatedCount;
        }

        public int getFollowedCount() {
            return FollowedCount;
        }

        public void setFollowedCount(int FollowedCount) {
            this.FollowedCount = FollowedCount;
        }

        public int getDiagnoseTimes() {
            return DiagnoseTimes;
        }

        public void setDiagnoseTimes(int DiagnoseTimes) {
            this.DiagnoseTimes = DiagnoseTimes;
        }

        public int getServiceTimes() {
            return ServiceTimes;
        }

        public void setServiceTimes(int ServiceTimes) {
            this.ServiceTimes = ServiceTimes;
        }

        public double getIncome() {
            return Income;
        }

        public void setIncome(double Income) {
            this.Income = Income;
        }

        public String getUserID() {
            return UserID;
        }

        public void setUserID(String userID) {
            UserID = userID;
        }

        public String getDoctorID() {
            return DoctorID;
        }

        public void setDoctorID(String doctorID) {
            DoctorID = doctorID;
        }

        public String getDoctorName() {
            return DoctorName;
        }

        public void setDoctorName(String doctorName) {
            DoctorName = doctorName;
        }
    }

}
