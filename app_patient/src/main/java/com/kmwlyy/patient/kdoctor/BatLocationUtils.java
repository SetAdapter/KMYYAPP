package com.kmwlyy.patient.kdoctor;

import android.app.Activity;
import android.util.Log;

import com.kmwlyy.patient.kdoctor.permission.OnPermissionCallback;
import com.kmwlyy.patient.kdoctor.permission.PermissionManager;
import com.kmwlyy.patient.kdoctor.permission.PermissionUtil;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;

/**
 * @Description描述:
 * @Author作者: hx
 * @Date日期: 2017/8/22
 */

public class BatLocationUtils {
    public static double latitude;
    public static double longitude;

    public static void location(final Activity mActivity){
        //判断是否有获取位置的权限
        if (!PermissionUtil.getInstance().checkPermission(mActivity, android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
            PermissionManager.getInstance().requestPermission(mActivity, new OnPermissionCallback() {
                @Override
                public void onSuccess(String... permissions) {
                    startLocation(mActivity);
                }

                @Override
                public void onFail(String... permissions) {

                }
            }, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        } else {
            startLocation(mActivity);
        }
    }

    private static void startLocation(Activity mActivity) {
        Log.e("aaaa","定位1");
        final TencentLocationManager locationManager = TencentLocationManager.getInstance(mActivity);
        TencentLocationRequest request = TencentLocationRequest.create();
        request.setAllowCache(true);
        request.setRequestLevel(TencentLocationRequest.REQUEST_LEVEL_ADMIN_AREA);
        locationManager.requestLocationUpdates(request, new TencentLocationListener() {
            @Override
            public void onLocationChanged(TencentLocation tencentLocation, int error, String s) {
                Log.e("aaaa","定位");

                if (TencentLocation.ERROR_OK == error) {
                    // 定位成功
                    latitude = tencentLocation.getLatitude();
                    longitude = tencentLocation.getLongitude();
                }
                locationManager.removeUpdates(this);
            }

            @Override
            public void onStatusUpdate(String s, int i, String s1) {

            }
        });
    }
}
