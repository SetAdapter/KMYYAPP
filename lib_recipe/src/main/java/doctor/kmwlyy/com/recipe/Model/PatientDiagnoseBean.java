package doctor.kmwlyy.com.recipe.Model;

import java.util.List;

/**
 * Created by Administrator on 2017/1/4.
 */
public class PatientDiagnoseBean {

    /**
     * OPDRegisterID : e679e1e43cfe4c52a531657c6e5f1e49
     * MedicalRecord : {"Sympton":"主诉","PastMedicalHistory":"既往病史","PresentHistoryIllness":"现病史","PreliminaryDiagnosis":"中医诊断","AllergicHistory":"过敏史","Advised":"治疗意见"}
     * Tags : [{"TagType":"ASK","Tag":"[嗜好]无特殊"},{"TagType":"ASK","Tag":"[嗜好]酸"},{"TagType":"CUT","Tag":"[脉象]缓"},{"TagType":"CUT","Tag":"[脉象]弦"},{"TagType":"CUT","Tag":"[脉象]涩"},{"TagType":"LOOK","Tag":"[神志]清楚有神"},{"TagType":"LOOK","Tag":"[神志]嗜睡"},{"TagType":"SOCIETY","Tag":"[情志]平和"},{"TagType":"SOCIETY","Tag":"[情志]恐惧"},{"TagType":"SMELLS","Tag":"[语言]清楚"},{"TagType":"SMELLS","Tag":"[语言]语蹇"}]
     */

    private String OPDRegisterID;
    /**
     * Sympton : 主诉
     * PastMedicalHistory : 既往病史
     * PresentHistoryIllness : 现病史
     * PreliminaryDiagnosis : 中医诊断
     * AllergicHistory : 过敏史
     * Advised : 治疗意见
     */

    private MedicalRecordBean MedicalRecord;
    /**
     * TagType : ASK
     * Tag : [嗜好]无特殊
     */

    private List<TagsBean> Tags;

    public String getOPDRegisterID() {
        return OPDRegisterID;
    }

    public void setOPDRegisterID(String OPDRegisterID) {
        this.OPDRegisterID = OPDRegisterID;
    }

    public MedicalRecordBean getMedicalRecord() {
        return MedicalRecord;
    }

    public void setMedicalRecord(MedicalRecordBean MedicalRecord) {
        this.MedicalRecord = MedicalRecord;
    }

    public List<TagsBean> getTags() {
        return Tags;
    }

    public void setTags(List<TagsBean> Tags) {
        this.Tags = Tags;
    }

    public static class MedicalRecordBean {
        private String Sympton;
        private String PastMedicalHistory;
        private String PresentHistoryIllness;
        private String PreliminaryDiagnosis;
        private String AllergicHistory;
        private String Advised;

        public String getSympton() {
            return Sympton;
        }

        public void setSympton(String Sympton) {
            this.Sympton = Sympton;
        }

        public String getPastMedicalHistory() {
            return PastMedicalHistory;
        }

        public void setPastMedicalHistory(String PastMedicalHistory) {
            this.PastMedicalHistory = PastMedicalHistory;
        }

        public String getPresentHistoryIllness() {
            return PresentHistoryIllness;
        }

        public void setPresentHistoryIllness(String PresentHistoryIllness) {
            this.PresentHistoryIllness = PresentHistoryIllness;
        }

        public String getPreliminaryDiagnosis() {
            return PreliminaryDiagnosis;
        }

        public void setPreliminaryDiagnosis(String PreliminaryDiagnosis) {
            this.PreliminaryDiagnosis = PreliminaryDiagnosis;
        }

        public String getAllergicHistory() {
            return AllergicHistory;
        }

        public void setAllergicHistory(String AllergicHistory) {
            this.AllergicHistory = AllergicHistory;
        }

        public String getAdvised() {
            return Advised;
        }

        public void setAdvised(String Advised) {
            this.Advised = Advised;
        }
    }

    public static class TagsBean {
        private String TagType;
        private String Tag;

        public String getTagType() {
            return TagType;
        }

        public void setTagType(String TagType) {
            this.TagType = TagType;
        }

        public String getTag() {
            return Tag;
        }

        public void setTag(String Tag) {
            this.Tag = Tag;
        }
    }
}
