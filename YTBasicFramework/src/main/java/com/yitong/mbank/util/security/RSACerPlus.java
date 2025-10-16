package com.yitong.mbank.util.security;

import android.app.Application;

import com.yitong.safe.io.AssetFileInputStream;

import java.io.InputStream;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

import javax.crypto.Cipher;

/**
 * 用户客户端对数据使用证书公钥加密
 *
 * @author iven
 */
final class RSACerPlus {

    // RSA算法模式
    private static final String DEFAULT_CIPHER_ALGORITHM = "RSA/ECB/PKCS1Padding";

    // RSA加密证书名称
    private static final String RSA_FILE = "ssmHealth.cer";

    private static RSACerPlus rsaPlus = null;

    public static RSACerPlus getInstance(Application app) {
        if (null == rsaPlus) {
            rsaPlus = new RSACerPlus();
            try {
                rsaPlus.initCer(app);
            } catch (Exception e) {
                throw new IllegalArgumentException("初始化澳门健康码正扫数据加密证书失败，请求重试");
            }
        }
        return rsaPlus;
    }

    private Cipher cipher;

    private RSACerPlus() {

    }

    /**
     * 初始化加载证书
     *
     * @throws Exception
     */
    private void initCer(Application app) throws Exception {
        CertificateFactory cff = CertificateFactory.getInstance("X.509");
        InputStream in = new AssetFileInputStream(app, RSA_FILE);
        Certificate cf = cff.generateCertificate(in);
        PublicKey pk1 = cf.getPublicKey(); // 得到证书文件携带的公钥
        cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM); // 定义算法：RSA
        cipher.init(Cipher.ENCRYPT_MODE, pk1);
    }

    /**
     * 使用初始化的公钥对数据加密
     *
     * @param str
     * @return
     * @throws Exception : IllegalBlockSizeException, BadPaddingException,
     *                   UnsupportedEncodingException
     */
    public String doEncrypt(String str) throws Exception {
        // 分段加密
        byte[] data = str.getBytes("UTF-8");
        byte[] encryptdata = null;
        for (int i = 0; i < data.length; i += 100) {
            byte[] doFinal = cipher.doFinal(subarray(data, i, i + 100));
            encryptdata = addAll(encryptdata, doFinal);
        }
        return Base64Util.encode(encryptdata);
    }

    private static byte[] subarray(byte[] array, int startIndexInclusive, int endIndexExclusive) {
        if (array == null) {
            return null;
        }
        if (startIndexInclusive < 0) {
            startIndexInclusive = 0;
        }
        if (endIndexExclusive > array.length) {
            endIndexExclusive = array.length;
        }
        int newSize = endIndexExclusive - startIndexInclusive;
        if (newSize <= 0) {
            return new byte[0];
        }

        byte[] subarray = new byte[newSize];
        System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
        return subarray;
    }

    private static byte[] clone(byte[] array) {
        if (array == null) {
            return null;
        }
        return (byte[]) array.clone();
    }

    private static byte[] addAll(byte[] array1, byte[] array2) {
        if (array1 == null) {
            return clone(array2);
        } else if (array2 == null) {
            return clone(array1);
        }
        byte[] joinedArray = new byte[array1.length + array2.length];
        System.arraycopy(array1, 0, joinedArray, 0, array1.length);
        System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
        return joinedArray;
    }
}
