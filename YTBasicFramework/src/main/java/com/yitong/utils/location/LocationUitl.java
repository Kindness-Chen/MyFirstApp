//package com.yitong.utils.location;
//
//import android.app.Service;
//import android.content.Context;
//import android.os.Vibrator;
//import com.baidu.location.BDLocation;
//import com.baidu.location.BDLocationListener;
//import com.baidu.location.LocationClient;
//import com.baidu.location.LocationClientOption;
//import com.baidu.location.LocationClientOption.LocationMode;
//import com.yitong.logs.Logs;
//import com.yitong.utils.SharedPreferenceUtil;
//
///**
// * 百度地图获取定位信息
// *
// * @author flueky zkf@yitong.com.cn
// * @date 2015-3-24 下午3:57:32
// */
//public class LocationUitl implements BDLocationListener, ILocation{
//
//	private LocationClient mLocationClient;
//	private LocationClientOption option;
//	private static LocationUitl locationUitl;
//	public Vibrator mVibrator;
//	private boolean isGetLocation = false;
//
//	private static Double longitude, latitude;// 经度，纬度
//	private static String address;// 地理位置信息
//	private static String localProvince;
//	private static String localCity;
//
//	public LocationUitl(Context context) {
//		mLocationClient = new LocationClient(context);
//		mLocationClient.registerLocationListener(this);
//		mVibrator = (Vibrator) context
//				.getSystemService(Service.VIBRATOR_SERVICE);
//		initLocOption();
//	}
//
//	public static LocationUitl getInstance(Context ...context) {
//		if (locationUitl == null) {
//			locationUitl = new LocationUitl(context[0]);
//		}
//		return locationUitl;
//	}
//
//	private void initLocOption() {
//		option = new LocationClientOption();
//		option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
//															// Hight_Accuracy（高精度）、Battery_Saving（低功耗）、Device_Sensors（仅设备）
//		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度，默认值gcj02（国测局加密）
//										// bd09ll（百度加密经纬度）bd09（百度加密墨卡托）
//		option.setScanSpan(1000 * 60);// 设置发起定位请求的间隔时间为1分钟
//		option.setIsNeedAddress(true);// 不要地址信息
//		mLocationClient.setLocOption(option);
//	}
//
//	/**
//	 * 开始定位
//	 *
//	 * @author flueky zkf@yitong.com.cn
//	 * @date 2015-3-24 下午12:45:54
//	 */
//	public void startLocation() {
////		SDKInitializer.initialize(getApplicationContext());
//		getLocaiton();
//		Logs.d("TAG", "开始定位");
//		mLocationClient.start();
//	}
//
//	/**
//	 * 结束定位
//	 *
//	 * @author flueky zkf@yitong.com.cn
//	 * @date 2015-3-24 下午12:42:18
//	 */
//	public void stopLocaiton() {
//		mLocationClient.stop();
//		Logs.d("TAG", "结束定位");
//	}
//
//	@Override
//	public void onReceiveLocation(BDLocation arg0) {
//		isGetLocation = true;
//		// 获取经纬度坐标
//		longitude = arg0.getLongitude();
//		latitude = arg0.getLatitude();
//		address = arg0.getAddrStr();
//		localCity = arg0.getCity();
//		localProvince = arg0.getProvince();
//		Logs.d("TAG", "定位成功-->" + longitude + "," + latitude + " " + address
//				+ "\n省 " + localProvince + " 城市 " + localCity);
//		saveLocation();
//		// 定位成功后，停止定位
//		// stopLocaiton();
//	}
//
//	public Double getLongitude() {
//		return longitude;
//	}
//
//	public Double getLatitude() {
//		return latitude;
//	}
//
//	public static String getAddress() {
//		return address;
//	}
//
//	public static String getLocalProvince() {
//		return localProvince;
//	}
//
//	public static String getLocalCity() {
//		return localCity;
//	}
//
//	/**
//	 * 从缓存中取位置信息
//	 *
//	 * @author flueky zkf@yitong.com.cn
//	 * @date 2015-3-24 下午4:39:06
//	 */
//	private void getLocaiton() {
//
//		longitude = Double.valueOf(SharedPreferenceUtil.getInfoFromShared("longitude", "0"));
//		latitude = Double.valueOf(SharedPreferenceUtil.getInfoFromShared("latitude", "0"));
//		address = SharedPreferenceUtil.getInfoFromShared("address", "0");
//		localProvince = SharedPreferenceUtil.getInfoFromShared("province", "");
//		localCity = SharedPreferenceUtil.getInfoFromShared("city", "");
//		Logs.d("TAG", "获取位置-->" + longitude + "," + latitude + " " + address
//				+ "\n省 " + localProvince + " 城市 " + localCity);
//		if (longitude.equals("0")) {
//			isGetLocation = false;
//		} else {
//			isGetLocation = true;
//		}
//	}
//
//	/**
//	 * 存信息到缓存
//	 *
//	 * @author flueky zkf@yitong.com.cn
//	 * @date 2015-3-24 下午4:40:03
//	 */
//	private void saveLocation() {
//		SharedPreferenceUtil.setInfoToShared("longitude", String.valueOf(longitude) );
//		SharedPreferenceUtil.setInfoToShared("latitude", String.valueOf(latitude));
//		SharedPreferenceUtil.setInfoToShared("address", address);
//		SharedPreferenceUtil.setInfoToShared("province", localProvince);
//		SharedPreferenceUtil.setInfoToShared("city", localCity);
//	}
//
//	public boolean isGetLocation() {
//		return isGetLocation;
//	}
//
//}
