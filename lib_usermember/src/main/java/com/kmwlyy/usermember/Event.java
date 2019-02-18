package com.kmwlyy.usermember;

/**
 * Created by Winson on 2016/8/18.
 */
public interface Event {

    class UserMemberUpdated{

    }

    class UserMemberChange{
        public String nickname;
        public String mobile;
    }

}
