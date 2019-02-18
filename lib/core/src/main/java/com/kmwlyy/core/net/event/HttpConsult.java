package com.kmwlyy.core.net.event;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;

import java.util.HashMap;

/**
 * Created by Winson on 2016/8/13.
 */
public interface HttpConsult {

    class getAgoraConfig extends HttpEvent<String> {

        public getAgoraConfig(String channelid, String identifier) {
            mReqMethod = HttpClient.GET;
            mReqAction = "/IM/MediaConfig";

            mReqParams = new HashMap<>();
            mReqParams.put("ChannelID", channelid);
            mReqParams.put("Identifier", identifier);
        }
    }

    class setRoomState extends HttpEvent<String> {

        public setRoomState(String channelid, String state) {
            mReqMethod = HttpClient.PUT;
            mReqAction = "/IM/Room/State";

            mReqParams = new HashMap<>();
            mReqParams.put("ChannelID", channelid);
            mReqParams.put("State", state);
//            mReqParams.put("DisableWebSdkInteroperability", "true");
        }
    }

    class getWaitingCount extends HttpEvent<String> {

        public getWaitingCount(String channelid, String doctorid) {
            mReqMethod = HttpClient.GET;
            mReqAction = "/IM/Room/WaitingCount";

            mReqParams = new HashMap<>();
            mReqParams.put("ChannelID", channelid);
            mReqParams.put("DoctorID", doctorid);
        }
    }

    class getChannelState extends HttpEvent<String> {

        public getChannelState(String channelid) {
            mReqMethod = HttpClient.GET;
            mReqAction = "/IM/Room/State";

            mReqParams = new HashMap<>();
            mReqParams.put("ChannelID", channelid);
        }
    }
}
