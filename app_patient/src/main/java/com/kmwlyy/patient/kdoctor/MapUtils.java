package com.kmwlyy.patient.kdoctor;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.net.URISyntaxException;

/**
 * Created by Lixiaoge on 2016/10/25.
 */

public class MapUtils {
    /**
     * 高德地图跳转URI
     * <p>
     * androidamap://route?sourceApplication=softname&slat=36.2&slon=116.1&sname=abc&dlat=36.3&dlon=116.2&dname=def&dev=0&t=1
     */
    public static void goToGaodeNaviActivity(Context context, String startAddName, String endName, double slat, double slon, double dlat, double dlon) {
        StringBuffer stringBuffer = new StringBuffer("androidamap://route?sourceApplication=")
                .append("健康BAT")
                .append("&sname=").append(startAddName)
                .append("&dname=").append(endName)
                .append("&slat=").append(slat)
                .append("&slon=").append(slon)
                .append("&dlat=").append(dlat)
                .append("&dlon=").append(dlon)
                .append("&dev=").append(0)
                .append("&t=").append(2);
        try {
            Intent intent = new Intent("android.intent.action.VIEW", android.net.Uri.parse(stringBuffer.toString()));
            intent.setPackage("com.autonavi.minimap");
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 高德地图跳转URI
     * <p>
     * androidamap://route?sourceApplication=softname&slat=36.2&slon=116.1&sname=abc&dlat=36.3&dlon=116.2&dname=def&dev=0&t=1
     */
    public static void goToGaodeNaviActivity(Context context, String startAddName, String endName, double slat, double slon) {
        StringBuffer stringBuffer = new StringBuffer("androidamap://route?sourceApplication=")
                .append("健康BAT")
                .append("&sname=").append(startAddName)
                .append("&dname=").append(endName)
                .append("&slat=").append(slat)
                .append("&slon=").append(slon)
                .append("&dev=").append(0)
                .append("&t=").append(2);
        try {
            Intent intent = new Intent("android.intent.action.VIEW", android.net.Uri.parse(stringBuffer.toString()));
            intent.setPackage("com.autonavi.minimap");
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 百度地图跳转URI
     *
     * @param context
     * @param startAddName 导航城市名
     * @param startlat     开始经度
     * @param startlon     开始纬度
     * @param endlat       结束经度
     * @param endlon       结束纬度
     */
    public static void goToBaiduNaviActivity(Context context, String startAddName, String endName, double startlat, double startlon, double endlat, double endlon) {
        //intent = Intent.getIntent("intent://map/direction?origin=latlng:34.264642646862,108.95108518068|name:我家&destination=大雁塔&mode=driving
        // &region=西安&src=thirdapp.navi.yourCompanyName.yourAppName#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
        StringBuffer stringBuffer = new StringBuffer("intent://map/direction?origin=latlng:" + startlat + "," + startlon)
                .append("|name:" + startAddName)
                .append("&destination=latlng:" + endlat + "," + endlon)
                .append("|name:" + endName)
                .append("&mode=").append("driving")
                .append("&src=thirdapp.navi.kangmei.jkbat#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
        Intent intent = null;
        try {
            intent = Intent.getIntent(stringBuffer.toString());
            context.startActivity(intent);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

    /**
     * 百度地图跳转URI
     *
     * @param context
     * @param startAddName 导航城市名
     * @param startlat     开始经度
     * @param startlon     开始纬度
     */
    public static void goToBaiduNaviActivity(Context context, String startAddName, String endName, double startlat, double startlon) {
        //intent = Intent.getIntent("intent://map/direction?origin=latlng:34.264642646862,108.95108518068|name:我家&destination=大雁塔&mode=driving
        // &region=西安&src=thirdapp.navi.yourCompanyName.yourAppName#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
        StringBuffer stringBuffer = new StringBuffer("intent://map/direction?origin=latlng:" + startlat + "," + startlon)
                .append("|name:" + startAddName)
                .append("&destination=")
                .append("" + endName)
                .append("&mode=").append("driving")
                .append("&src=thirdapp.navi.kangmei.jkbat#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
        Intent intent = null;
        try {
            intent = Intent.getIntent(stringBuffer.toString());
            context.startActivity(intent);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

    /**
     * 腾讯地图跳转URI
     *
     * @param context
     * @param startAddName 导航城市名
     * @param startlat     开始经度
     * @param startlon     开始纬度
     * @param endlat       结束经度
     * @param endlon       结束纬度
     */
    public static void goToTencentNaviActivity(Context context, String startAddName, String endName, double startlat, double startlon, double endlat, double endlon) {
        StringBuffer stringBuffer = new StringBuffer("qqmap://map/routeplan?type=drive&from=")
                .append(startAddName)
                .append("&fromcoord=").append(startlat).append(",").append(startlon)
                .append("&to=").append(endName)
                .append("&tocoord=").append(endlat).append(",").append(endlon)
                .append("&policy=2")//0：较快捷1：无高速 2：距离
                .append("&referer=jkbat");
        Intent intent = null;
        try {
            intent = Intent.getIntent(stringBuffer.toString());
            context.startActivity(intent);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * 腾讯地图跳转URI
     *
     * @param context
     * @param startAddName 导航城市名
     * @param startlat     开始经度
     * @param startlon     开始纬度
     */
    public static void goToTencentNaviActivity(Context context, String startAddName, String endName, double startlat, double startlon) {
        StringBuffer stringBuffer = new StringBuffer("qqmap://map/routeplan?type=drive&from=")
                .append(startAddName)
                .append("&fromcoord=").append(startlat).append(",").append(startlon)
                .append("&to=").append(endName)
                .append("&policy=2")//0：较快捷1：无高速 2：距离
                .append("&referer=jkbat");
        Intent intent = null;
        try {
            intent = Intent.getIntent(stringBuffer.toString());
            context.startActivity(intent);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * 坐标转换，腾讯地图（火星坐标）转换成百度地图坐标
     *
     * @param lat 腾讯纬度
     * @param lon 腾讯经度
     * @return 返回结果：经度,纬度
     */
    public static double[] map_hx2bd(double lat, double lon) {
        double bd_lat;
        double bd_lon;
        double x_pi = 3.14159265358979324;
        double x = lon, y = lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
        bd_lon = z * Math.cos(theta) + 0.0065;
        bd_lat = z * Math.sin(theta) + 0.006;
        double[] doubles = new double[2];
        doubles[0] = bd_lat;
        doubles[1] = bd_lon;
        return doubles;
    }


    /**
     * 坐标转换，百度地图坐标转换成腾讯地图坐标
     *
     * @param lat 百度坐标纬度
     * @param lon 百度坐标经度
     * @return 返回结果：纬度,经度
     */
    public static double[] map_bd2hx(double lat, double lon) {
        double tx_lat;
        double tx_lon;
        double x_pi = 3.14159265358979324;
        double x = lon - 0.0065, y = lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
        tx_lon = z * Math.cos(theta);
        tx_lat = z * Math.sin(theta);

        double[] doubles = new double[2];
        doubles[0] = tx_lat;
        doubles[1] = tx_lon;
        return doubles;
    }

    /**
     * 判断是否安装应用
     *
     * @param packageName
     * @return
     */
    public static boolean isInstallPackage(Context context, String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager()
                    .getApplicationInfo(packageName,
                            PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
