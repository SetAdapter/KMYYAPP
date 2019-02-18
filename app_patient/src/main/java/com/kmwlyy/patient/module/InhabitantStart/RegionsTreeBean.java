package com.kmwlyy.patient.module.InhabitantStart;

import com.kmwlyy.patient.weight.pickerview.model.IPickerViewData;

import java.util.List;

/**
 * Created by Administrator on 2017/8/23.
 */

public class RegionsTreeBean  {

    /**
     * Data : [{"ChildRegions":[{"ChildRegions":[{"ChildRegions":[{"ChildRegions":[],"Region":{"ParentID":"38","RegionID":"448","RegionLevel":3,"RegionName":"迁西县","RegionNameEN":"Qianxi Xian","RegionOrder":0,"RegionShortNameEN":"QXX"}}],"Region":{"ParentID":"4","RegionID":"47","RegionLevel":2,"RegionName":"衡水市","RegionNameEN":"Hengshui Shi","RegionOrder":0,"RegionShortNameEN":"HGS"}}],"Region":{"ParentID":"1","RegionID":"4","RegionLevel":1,"RegionName":"河北省","RegionNameEN":"Hebei Sheng","RegionOrder":0,"RegionShortNameEN":"HE"}}],"Region":{"ParentID":"0","RegionID":"1","RegionLevel":0,"RegionName":"中国","RegionNameEN":"Zhong Guo","RegionOrder":0,"RegionShortNameEN":"2"}}]
     * Msg : 操作成功
     * Result : true
     * Status : 0
     * Total : 0
     */

    private String Msg;
    private boolean Result;
    private int Status;
    private int Total;
    private List<DataBean> Data;

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String Msg) {
        this.Msg = Msg;
    }

    public boolean isResult() {
        return Result;
    }

    public void setResult(boolean Result) {
        this.Result = Result;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public int getTotal() {
        return Total;
    }

    public void setTotal(int Total) {
        this.Total = Total;
    }

    public List<DataBean> getData() {
        return Data;
    }

    public void setData(List<DataBean> Data) {
        this.Data = Data;
    }

    public static class DataBean implements IPickerViewData{
        /**
         * ChildRegions : [{"ChildRegions":[{"ChildRegions":[{"ChildRegions":[],"Region":{"ParentID":"38","RegionID":"448","RegionLevel":3,"RegionName":"迁西县","RegionNameEN":"Qianxi Xian","RegionOrder":0,"RegionShortNameEN":"QXX"}}],"Region":{"ParentID":"4","RegionID":"47","RegionLevel":2,"RegionName":"衡水市","RegionNameEN":"Hengshui Shi","RegionOrder":0,"RegionShortNameEN":"HGS"}}],"Region":{"ParentID":"1","RegionID":"4","RegionLevel":1,"RegionName":"河北省","RegionNameEN":"Hebei Sheng","RegionOrder":0,"RegionShortNameEN":"HE"}}]
         * Region : {"ParentID":"0","RegionID":"1","RegionLevel":0,"RegionName":"中国","RegionNameEN":"Zhong Guo","RegionOrder":0,"RegionShortNameEN":"2"}
         */

        private RegionBean Region;
        private List<DataBean> ChildRegions;

        public RegionBean getRegion() {
            return Region;
        }

        public void setRegion(RegionBean Region) {
            this.Region = Region;
        }

        public List<DataBean> getChildRegions() {
            return ChildRegions;
        }

        public void setChildRegions(List<DataBean> ChildRegions) {
            this.ChildRegions = ChildRegions;
        }

        @Override
        public String getPickerViewText() {
            return this.Region.getRegionName();
        }

        public static class RegionBean implements IPickerViewData{
            /**
             * ParentID : 0
             * RegionID : 1
             * RegionLevel : 0
             * RegionName : 中国
             * RegionNameEN : Zhong Guo
             * RegionOrder : 0
             * RegionShortNameEN : 2
             */

            private String ParentID;
            private String RegionID;
            private int RegionLevel;
            private String RegionName;
            private String RegionNameEN;
            private int RegionOrder;
            private String RegionShortNameEN;

            public String getParentID() {
                return ParentID;
            }

            public void setParentID(String ParentID) {
                this.ParentID = ParentID;
            }

            public String getRegionID() {
                return RegionID;
            }

            public void setRegionID(String RegionID) {
                this.RegionID = RegionID;
            }

            public int getRegionLevel() {
                return RegionLevel;
            }

            public void setRegionLevel(int RegionLevel) {
                this.RegionLevel = RegionLevel;
            }

            public String getRegionName() {
                return RegionName;
            }

            public void setRegionName(String RegionName) {
                this.RegionName = RegionName;
            }

            public String getRegionNameEN() {
                return RegionNameEN;
            }

            public void setRegionNameEN(String RegionNameEN) {
                this.RegionNameEN = RegionNameEN;
            }

            public int getRegionOrder() {
                return RegionOrder;
            }

            public void setRegionOrder(int RegionOrder) {
                this.RegionOrder = RegionOrder;
            }

            public String getRegionShortNameEN() {
                return RegionShortNameEN;
            }

            public void setRegionShortNameEN(String RegionShortNameEN) {
                this.RegionShortNameEN = RegionShortNameEN;
            }

            @Override
            public String getPickerViewText() {
                return this.RegionName;
            }
        }
    }
}
