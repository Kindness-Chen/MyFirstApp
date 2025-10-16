package com.yitong.mbank.util.security;

import android.app.Application;

/**
 * 请求返回报文自定义加密
 *
 * @author 徐明明
 *
 */
final class Encrypt {

	public static final String TAG = "Encrypt";
	
	public static final String VERSION = "1";// 自定义加密版本
	public static final String FILL_CODE = "13"; //自定义加密填充约束
	public static final Integer CONFUSE_STATUS = 2; // 混淆规则 0：无混淆；1：首尾对换；2、奇偶对换；
	
	private static Encrypt instance = new Encrypt();

	public static Encrypt getInstance() {
		return instance;
	}

	String decrypt(Application app, String data, String key) {
		try {
			String encryptData = data.substring(14); // 取得数据体

			/**
			 * 拆分控制码
			 */
			Integer confuseStartPos = Integer.parseInt(data.substring(3, 5), 16);// 混淆起始点
            Integer confuseLen = Integer.parseInt(data.substring(5, 7), 16); // 混淆长度
            Integer confuseRule = Integer.parseInt(data.substring(7, 8), 16); // 混淆规则
            Integer originalLen = Integer.parseInt(data.substring(8, 14), 16); // 数据原长度

			/**
			 * 反混淆
			 */
			StringBuffer confuseData = new StringBuffer();
			String confuseStr = encryptData.substring(confuseStartPos, confuseStartPos + confuseLen); // 混淆内容
			Integer confuseStrLen = confuseStr.length(); // 混淆内容长度

			confuseData.append(encryptData.substring(0, confuseStartPos)); // 追加未混淆部分

			switch (confuseRule) {
			case 1: // 首尾对换
				confuseData.append(confuseStr.charAt(confuseStrLen - 1));
				confuseData.append(confuseStr.substring(1, confuseStrLen - 1));
				confuseData.append(confuseStr.charAt(0));
				break;
			case 2: // 基偶对换
				for (int j = 2; j <= confuseStrLen; j++) {
					if (j % 2 == 0) {
						confuseData.append(confuseStr.charAt(j - 1));
						confuseData.append(confuseStr.charAt(j - 2));
					}
				}
				if (confuseStrLen % 2 != 0 && confuseStrLen > 0) {
					confuseData.append(confuseStr.charAt(confuseStrLen - 1));
				}
				break;
			default:
				break;
			}

			if (0 != confuseRule) {
				confuseData.append(encryptData.substring(confuseStartPos + confuseLen));
				encryptData = confuseData.toString();
			}

			/**
			 * 反填充
			 */
			encryptData = encryptData.substring(0, originalLen);

			/**
			 * 解密
			 */
			encryptData = CryptoUtil.aesDecryptData(app, HexUtil.decode(encryptData), key); // 受保护的AES加密

			return encryptData;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
}
