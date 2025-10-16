package com.yitong.nfc.pboc.tech.ber;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by 左克飞 on 16/12/9 上午11:55.
 * e-mail:zkf@yitong.com.cn
 * FilePath NFCDemo:com.yitong.nfc2_0.pboc.tech.ber
 */

public class BerHouse {

    private static  final String  TAG = "BerHouse";

    /**
     * 根据BerT 知道指定的tlv数据
     *
     * @param tlvs
     * @param t
     * @return
     */
    public static BerTLV findTlvByTag(ArrayList<BerTLV> tlvs, BerT t) {
        int i = 0;
        BerTLV berTLV =null;
        while (tlvs != null && i < tlvs.size()) {
            BerTLV tlv = tlvs.get(i++);
            if (tlv.getTag().equals(t))
                berTLV = tlv;
            if (tlv.getChild() != null)
                berTLV= findTlvByTag(tlv.getChild(), t);
            if(berTLV!=null)
                break;
        }
        return berTLV;
    }

    /**
     * 根据BerT 知道指定的tlv所有数据
     *
     * @param tlvs
     * @param t
     * @return
     */
    public static ArrayList<BerTLV> findAllTlvByTag(ArrayList<BerTLV> tlvs, BerT t) {
        int i = 0;
        ArrayList<BerTLV> result = new ArrayList<BerTLV>();
        while (tlvs != null && i < tlvs.size()) {
            BerTLV tlv = tlvs.get(i++);
            if (tlv.getTag().equals(t))
                result.add(tlv);
            if (tlv.getChild() != null)
                result.addAll(findAllTlvByTag(tlv.getChild(), t));
        }
        return result;
    }

    public static void printfTLV(ArrayList<BerTLV> tlvs) {
        int i = 0;
        while (tlvs != null && i < tlvs.size()) {
            BerTLV tlv = tlvs.get(i++);
            if (tlv.getChild() != null)
                printfTLV(tlv.getChild());
            else
                Log.d(TAG, "printfTLV: " + tlv);
        }
    }
}
