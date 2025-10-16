//package com.yitong.utils.location;
//
//import android.content.Context;
//import android.location.Location;
//import android.os.Bundle;
//
//import com.amap.api.location.AMapLocation;
//import com.amap.api.location.AMapLocationListener;
//import com.amap.api.location.LocationManagerProxy;
//import com.amap.api.location.LocationProviderProxy;
//import com.yitong.logs.Logs;
//import com.yitong.mbank.app.utils.SharedPreferenceUtil;
//
///**
// * Created by zpz on 2015/9/30.
// */
//public class LocactionGdUtil implements AMapLocationListener, ILocation{
//
//    private static final String TAG = LocactionGdUtil.class.getSimpleName();
//
//    private LocationManagerProxy mLocationManagerProxy;
//    private Context context;
//
//    /** 纬度, 对应X坐标 */
//    public Double Latitude;
//    /** 经度, 对应Y坐标 */
//    public Double Longitude;
//    private static LocactionGdUtil locationUitl;
//
//    private LocactionGdUtil(Context context){
//        this.context = context;
//    }
//    public static LocactionGdUtil getInstance(Context ...context){
//        if(null == locationUitl)
//            locationUitl = new LocactionGdUtil(context[0]);
//        return locationUitl;
//    }
//
//    @Override
//    public Double getLongitude() {
//        return Longitude;
//    }
//
//    @Override
//    public Double getLatitude() {
//        return Latitude;
//    }
//
//    @Override
//    public void startLocation() {
//        mLocationManagerProxy = LocationManagerProxy.getInstance(context);
//
//        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
//        // 注意设置合适的定位时间的间隔，并且在合适时间调用removeUpdates()方法来取消定位请求
//        // 在定位结束后，在合适的生命周期调用destroy()方法
//        // 其中如果间隔时间为-1，则定位只定一次
//        mLocationManagerProxy.requestLocationData(LocationProviderProxy.AMapNetwork, 60 * 1000, 15, this);
//        mLocationManagerProxy.setGpsEnable(false);
//        getLocaiton();
//    }
//    /**
//     * 停止定位，销毁资源
//     */
//    public void stopLocaiton() {
//        if (null != mLocationManagerProxy) {
//            mLocationManagerProxy.removeUpdates(this);
//            mLocationManagerProxy.destroy();
//            mLocationManagerProxy = null;
//        }
//        Logs.i(TAG, "stopLocation-->定位资源已被销毁");
//    }
//
//    @Override
//    public void onLocationChanged(AMapLocation amapLocation) {
//        int errorCode = amapLocation.getAMapException().getErrorCode();
//        String errorDes = amapLocation.getAMapException().getErrorMessage();
//        Logs.d(TAG, "onLocationChanged-->错误描述:" + errorDes + ", 编码:[" + errorCode + "]");
//        if (amapLocation != null && errorCode == 0) {
//            // 获取位置信息
//            Latitude = amapLocation.getLatitude();
//            Longitude = amapLocation.getLongitude();
//            Logs.i(TAG, ("lbs".equals(amapLocation.getProvider()) ? "网络定位: " : "GPS定位: ") + Latitude + ", "
//                    + Longitude);
//        }
//        saveLocation();
//    }
//
//    private void saveLocation() {
//        SharedPreferenceUtil.setInfoToShared("longitude", String.valueOf(Longitude));
//        SharedPreferenceUtil.setInfoToShared("latitude", String.valueOf(Latitude));
////        SharedPreferenceUtil.setInfoToShared("address", address);
////        SharedPreferenceUtil.setInfoToShared("province", localProvince);
////        SharedPreferenceUtil.setInfoToShared("city", localCity);
//    }
//    private void getLocaiton() {
//
//        Longitude = Double.valueOf(SharedPreferenceUtil.getInfoFromShared("longitude", "0"));
//        Latitude = Double.valueOf(SharedPreferenceUtil.getInfoFromShared("latitude", "0"));
////        address = SharedPreferenceUtil.getInfoFromShared("address", "0");
////        localProvince = SharedPreferenceUtil.getInfoFromShared("province", "");
////        localCity = SharedPreferenceUtil.getInfoFromShared("city", "");
////        Logs.d("TAG", "获取位置-->" + longitude + "," + latitude + " " + address
////                + "\n省 " + localProvince + " 城市 " + localCity);
////        if (Longitude.doubleValue() == 0) {
////            isGetLocation = false;
////        } else {
////            isGetLocation = true;
////        }
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
//
//    }
//
//    @Override
//    public void onStatusChanged(String s, int i, Bundle bundle) {
//
//    }
//
//    @Override
//    public void onProviderEnabled(String s) {
//
//    }
//
//    @Override
//    public void onProviderDisabled(String s) {
//
//    }
//}
