package com.kmwlyy.address.net;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016-8-18.
 */
public interface AddressEvent {

    class GetList extends HttpEvent<ArrayList<Address>> {
        public GetList(HttpListener<ArrayList<Address>> mListener) {
            super(mListener);
            mReqMethod = HttpClient.GET;
            mReqAction = "/userAddresses/getUserAddressList";
        }
    }

    class GetDetail extends HttpEvent<Address> {
        public GetDetail(String addressId, HttpListener<Address> mListener) {
            super(mListener);
            mReqMethod = HttpClient.GET;
            mReqAction = "/userAddresses/getUserAddressList";
            mReqParams = new HashMap<>();
            mReqParams.put("ID", addressId);
        }
    }

    class Add extends HttpEvent {
        public Add(Address address) {
            mReqMethod = HttpClient.POST;
            mReqAction = "/userAddresses/addUserAddress";
            if (address != null) {
                mReqParams = new HashMap();
                mReqParams.put("UserName", address.getUserName());
                mReqParams.put("Mobile", address.getMobile());
                mReqParams.put("ProvinceName", address.getProvinceName());
                mReqParams.put("CityName", address.getCityName());
                mReqParams.put("AreaName", address.getAreaName());
                mReqParams.put("DetailAddress", address.getDetailAddress());
                mReqParams.put("Postcode", address.getPostcode());
            }
        }
    }

    class Update extends HttpEvent {
        public Update(Address address) {
            mReqMethod = HttpClient.POST;
            mReqAction = "/userAddresses/updateUserAddress";
            if (address != null) {
                mReqParams = new HashMap();
                mReqParams.put("AddressID", address.getAddressID());
                mReqParams.put("UserName", address.getUserName());
                mReqParams.put("Mobile", address.getMobile());
                mReqParams.put("ProvinceName", address.getProvinceName());
                mReqParams.put("CityName", address.getCityName());
                mReqParams.put("AreaName", address.getAreaName());
                mReqParams.put("DetailAddress", address.getDetailAddress());
                mReqParams.put("Postcode", address.getPostcode());
            }
        }
    }

    class Delete extends HttpEvent {

        public Delete(String addressId) {
            mReqMethod = HttpClient.POST;
            mReqAction = "/userAddresses/deleteUserAddress";
            mReqParams = new HashMap();
            mReqParams.put("ID", addressId);
        }
    }

    class SetDefault extends HttpEvent {
        public SetDefault(String addressId) {
            mReqMethod = HttpClient.POST;
            mReqAction = "/userAddresses/setDefaultAddress";
            mReqParams = new HashMap();
            mReqParams.put("ID", addressId);
        }
    }
}
