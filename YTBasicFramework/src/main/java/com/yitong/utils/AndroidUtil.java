package com.yitong.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.yitong.logs.Logs;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * android客户端工具类
 *
 * @author 刘国山 lgs@yitong.com.cn
 * @version 1.0 2013年7月23日
 * @Description
 * @class com.yitong.zjrc.mbank.utils.yitong.AndroidUtil
 */
public class AndroidUtil {

    public static final String TAG = "AndroidUtil";

    /**
     * 获取手机唯一序列号
     */
    public static String getMyDeviceId(Context context) {
        return BuildDeviceIdUtil.id(context);
    }

    /**
     * 获取手机唯一序列号
     * 注：如取不到设备号，则取UUID作为手机唯一序列号
     */
    public static String getDeviceUUID(Context context) {
        return Installation.id(context);
    }

    /**
     * 获取手机序列号
     */
    public static String getDeviceId(Context context) {
        String deviceID = null;

        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (!StringTools.isEmpty(tm.getDeviceId())) {
            deviceID = tm.getDeviceId();
        }

        return deviceID;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取应用的ApplicationId
     *
     * @param context
     * @return
     */
    public static String getApplicationId(Context context) {
        try {
            String pkName = context.getPackageName();
            return pkName;
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 取得操作系统版本号
     */
    public static String getOSVersion(Context context) {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取应用版本号
     */
    public static String getAppVersion(Context context) {
        String strVersion = null;

        try {
            PackageInfo pi = null;
            pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            if (pi != null) {
                strVersion = pi.versionName;
            }
        } catch (NameNotFoundException e) {
            Logs.e(TAG, e.getMessage(), e);
        }

        return strVersion;
    }

    /**
     * 获取自己应用内部的版本号
     */
    public static int getVersionCode(Context context) {
        PackageManager manager = context.getPackageManager();
        int code = 0;
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            code = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return code;
    }

    /**
     * 获取签名摘要
     */
    public static String getSign(Context context) {
        String strSign = null;

        try {
            int flag = PackageManager.GET_SIGNATURES;
            PackageManager pm = context.getPackageManager();
            List<PackageInfo> apps = pm.getInstalledPackages(flag);
            Object[] objs = apps.toArray();
            for (int i = 0, j = objs.length; i < j; i++) {
                PackageInfo packageinfo = (PackageInfo) objs[i];
                String packageName = packageinfo.packageName;
                if (packageName.equals(context.getPackageName())) {
                    Signature[] temps = packageinfo.signatures;
                    Signature tmpSign = temps[0];
                    strSign = tmpSign.toCharsString();
                }
            }
        } catch (Exception e) {
        }
        return strSign;
    }

    /**
     * 判断手机是否ROOT
     */
    public static boolean isSystemRoot() {
        boolean isRoot = false;
        try {
            if ((!new File("/system/bin/su").exists())
                    && (!new File("/system/xbin/su").exists())) {
                isRoot = false;
            } else {
                isRoot = true;
            }
            Logs.d("TAG", "isRoot  = " + isRoot);
        } catch (Exception e) {

        }
        return isRoot;
    }

    /**
     * 检查手机上是否安装了指定的软件
     *
     * @param context
     * @param packageName：应用包名
     * @return
     */
    public static boolean isAppAvilibleByPkgName(Context context, String packageName) {
        // 获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        // 获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        // 用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<>();
        // 从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        // 判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }

    /**
     * 真实设备检测
     *
     * @return true:真机,false:模拟器
     */
    public static boolean isTrulyDevice() {
        //ro.radio.use-ppp—>yes or ro.product.cpu.abi—>x86 一定是模拟器
        //ro.radio.use-ppp—>null or init.svc.console->null 一定是真机
        String abi = properties("ro.product.cpu.abi");
        String usePPP = properties("ro.radio.use-ppp");
        String console = properties("init.svc.console");

        boolean emulator1 = "x86".equals(abi);
        boolean emulator2 = "yes".equals(usePPP);

        boolean device1 = TextUtils.isEmpty(usePPP);
        boolean device2 = TextUtils.isEmpty(console);

        return !(emulator1 || emulator2) && (device1 || device2);
    }

    private static String properties(String key) {
        try {
            Process process = Runtime.getRuntime().exec("getprop " + key);
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer buffer = new StringBuffer();
            String line = null;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            return buffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
