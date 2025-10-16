package com.yitong.mbank.util.security;

import android.app.Application;

import java.security.SecureRandom;

/**
 * JNI工具类，对重要算法及加密相关的代码进行保护
 *
 * @author yaoym
 */
public class CryptoUtil {
    private static final String TAG = "CryptoUtil";

    // 随机数种子
    private static final char[] CHARS_ALL = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G',
            'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    // 分隔符
    private static final char CHAR_SPLIT = 29;

    //
    public static String _KEY;

    static {
        System.loadLibrary("pnc-crypto");
        _KEY = CryptoUtil.getConfigKey("AES_RSP_KEY");
    }

    private static CryptoUtil instance = null;

    public static CryptoUtil getInstance() {
        if (instance == null) {
            synchronized (CryptoUtil.class) {
                if (instance == null) {
                    instance = new CryptoUtil();
                }
            }
        }
        return instance;
    }

    private CryptoUtil() {
    }

    /**
     * AES加密
     *
     * @param app
     * @param key
     * @param datas
     * @return
     */
    private native byte[] aesEncode(Application app, byte[] key, byte[] datas);

    /**
     * 响应报文解密
     *
     * @param app
     * @param keys
     * @param datas
     * @return
     */
    private native byte[] respDecode(Application app, byte[] keys, byte[] datas);

    /**
     * 国密请求报文加密
     *
     * @param app
     * @param keys
     * @param datas
     * @param macDatas 摘要结果
     * @return 对称加密结果
     */
    private native byte[] reqEncode(Application app, byte[] keys, byte[] datas, byte[] macDatas);

    /**
     * 国密响应报文解密
     *
     * @param app
     * @param keys
     * @param datas
     * @return
     */
    private native byte[] responseDecode(Application app, byte[] keys, byte[] datas);

    /**
     * 获取应用签名
     *
     * @param app
     * @param onceCode
     * @return
     */
    private native byte[] getAppSignInfo(Application app, String onceCode, byte[] key);

    /**
     * 应用签名
     *
     * @param app
     * @param keys     服务端一次性 TOKEN
     * @param codeName 应用包名称
     * @return
     */
    private native byte[] appSignure(Application app, byte[] keys, byte[] codeName);

    /**
     * 获取配置文件中的key信息
     *
     * @param key
     * @return
     */
    public static native String getConfigKey(String key);

    /**
     * 获取应用包签名
     *
     * @param app
     * @param onceCode
     * @return
     */
    public static String getApkSignInfo(Application app, String onceCode, String key) {
        String str = null;
        try {
            if (key.length() != 16) {
                return str;
            }
            byte[] dataArr = getInstance().getAppSignInfo(app, onceCode, key.getBytes());
            str = Base64Util.encode(dataArr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String getAppSignure(Application app, String key) {
        String strEncryptSignure = "";
        try {
            byte[] keyBytes = key.getBytes();

            String resPath = app.getPackageResourcePath();
            String codePath = app.getPackageCodePath();
            int iStart = codePath.lastIndexOf("/");
            int iEnd = codePath.indexOf(".apk");
            String codeName = codePath.substring(iStart + 1, iEnd);
//			strEncryptSignure = String.format("resPath:%s \n codePath:%s", resPath, codePath);
            byte[] encryptSignureBytes = getInstance().appSignure(app, keyBytes, resPath.getBytes());
            strEncryptSignure = HexUtil.encode(encryptSignureBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return strEncryptSignure;
    }

    /**
     * 生成16位随即密钥
     *
     * @return 密钥
     */
    public static String genRandomKey() {
//        // 随机密钥
//        SecureRandom rnd = null;
//        try {
//            rnd = SecureRandom.getInstance("SHA1PRNG");
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        if (rnd == null) {
//            return null;
//        }
//        StringBuilder keyStrBui = new StringBuilder();
//        for (int i = 0; i < 16; i++) {
//            // 33到126即可实现键盘上所有单字符的随机种子
//            keyStrBui.append(String.valueOf(CHARS_ALL[rnd.nextInt(62)]));
//        }
//        return keyStrBui.toString();

        //针对安全顾问修复更改
        SecureRandom rnd = new SecureRandom();
        StringBuilder keyStrBui = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            // 33到126即可实现键盘上所有单字符的随机种子
            keyStrBui.append(String.valueOf(CHARS_ALL[rnd.nextInt(62)]));
        }
        return keyStrBui.toString();
    }


    /**
     * 数据AES加密
     *
     * @param app
     * @param dataStr
     * @param keyStr
     * @return
     */
    public static String aesEncryptData(Application app, String dataStr, String keyStr) {
        String strAESC = "";
        try {
            byte[] keyArr = keyStr.getBytes("utf-8");
            byte[] dataArr = dataStr.getBytes("utf-8");
            byte[] encryptArr = getInstance().aesEncode(app, keyArr, dataArr);
            strAESC = Base64Util.encode(encryptArr).replaceAll(" ", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strAESC;
    }


    /**
     * 数据AES解密
     *
     * @param app
     * @param dataArr
     * @param keyStr
     * @return
     */
    static String aesDecryptData(Application app, byte[] dataArr, String keyStr) {
        String decryptData = "";
        try {
            byte[] keyArr = keyStr.getBytes("utf-8");
            decryptData = new String(getInstance().respDecode(app, keyArr, dataArr));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptData;
    }

    /**
     * 数据加密， 内容为：数据摘要+加密报文+加密密钥（请求时使用）
     *
     * @param app
     * @param dataStr 明文
     * @param keyStr  密钥
     * @return
     */
    public static String encryptData(Application app, String dataStr, String keyStr) {

        String encryptData = "";
        try {
            // 数据摘要
            String strMD5 = Md5Util.encode(keyStr + dataStr).toUpperCase();
            // 加密报文
            byte[] keyArr = keyStr.getBytes("utf-8");
            byte[] dataArr = dataStr.getBytes("utf-8");
            byte[] encryptArr = getInstance().aesEncode(app, keyArr, dataArr);
            String strAESC = Base64Util.encode(encryptArr).replaceAll(" ", "");
            // 加密密钥
            String encryptKeyStr = RSACerPlus.getInstance(app).doEncrypt(keyStr).replaceAll(" ", "");
            // 内容拼接
            encryptData = strMD5 + CHAR_SPLIT + strAESC + CHAR_SPLIT + encryptKeyStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptData;
    }

    /**
     * 数据解密（响应报文使用）
     *
     * @param app
     * @param dataStr
     * @param keyStr
     * @return
     */
    public static String decryptData(Application app, String dataStr, String keyStr) {
        String decryptData = "";
        try {
            decryptData = CryptoUtil.aesDecryptData(app, Base64Util.decode(dataStr), keyStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptData;
    }

    /**
     * 数据加密， 内容为：数据摘要+加密报文+加密密钥（请求时使用）
     *
     * @param app
     * @param dataStr 明文
     * @param keyStr  密钥
     * @return
     */
    public static String encryptDataWithSM(Application app, String dataStr, String keyStr) {

        String encryptData = "";
        try {
            byte[] remarkBytes = new byte[32];
            byte[] dataBytes = dataStr.getBytes("utf-8");
            byte[] keyBytes = keyStr.getBytes("utf-8");

            byte[] dataSm4Bytes = getInstance().reqEncode(app, keyBytes, dataBytes, remarkBytes);

            // Sm3算法计算摘要
            String strRemarkSm3 = HexUtil.encode(remarkBytes).toUpperCase();
            // Sm4算法加密报文
            String strDataSm4 = HexUtil.encode(dataSm4Bytes).toUpperCase();
            // Sm2算法加密密钥
            String strKeySm2 = sm2Encrypt(app, keyStr);

            // 拼接数据
            encryptData = "#01" + strRemarkSm3 + CHAR_SPLIT + strDataSm4 + CHAR_SPLIT + strKeySm2;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptData;
    }

    /**
     * 数据解密（响应报文使用）
     *
     * @param app
     * @param dataStr
     * @param keyStr
     * @return
     */
    public static String decryptDataWithSM(Application app, String dataStr, String keyStr) {
        String decryptData = "";
        try {
            byte[] dataBytes = dataStr.getBytes("utf-8");
            byte[] keyBytes = keyStr.getBytes("utf-8");
            byte[] originBytes = getInstance().responseDecode(app, keyBytes, dataBytes);
            decryptData = new String(originBytes, "utf-8").trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptData;
    }

    /**
     * 国密加密
     *
     * @param app
     * @param data
     * @return 国密加密结果(C1C3C2)
     */
    public static String sm2Encrypt(Application app, String data) {
        String encodeMsg = null;

        byte[] hexMsgBytes = data.getBytes();
        byte[] encodes = TfbSM2Digest.getInstance().encode(hexMsgBytes);
        encodeMsg = HexUtil.encode(encodes).toUpperCase();

        return encodeMsg;
    }
}
