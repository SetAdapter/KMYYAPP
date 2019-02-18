package com.kmwlyy.doctor.Utils;

import com.kmwlyy.doctor.model.PatientGroup;

/**
 * Created by Winson on 2017/7/7.
 */

public interface AppEventApi {

    class UpdateGroup {
        public PatientGroup patientGroup;
    }

    class UpdateMemberGroupList {

    }
//    添加家庭成员
    class AddHomeMember{
        String MemberID;
    }
    class Delete{

    }
    class Modify{

    }
    class Add{

    }
    class Success{

        private boolean success;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }
    }

}
