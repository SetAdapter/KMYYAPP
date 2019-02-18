package com.kmwlyy.doctor.model;

import java.util.List;

/**
 * Created by xcj on 2016/10/28.
 */
public interface InfoEvent {
    class GoodAtDisease{
        public String goodAtDisease;
    }

    class Intro{
        public String introInfo;
    }

    class FreeCalendar{
        public List<Integer> calendarlist;
        public String currentDate;
    }

    class SettingSuc{

    }

    class IssueStopSuc{

    }
}
