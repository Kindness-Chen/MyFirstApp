package com.yitong.nfc.pboc.tech.ber;

import com.yitong.nfc.ByteUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * Created by 左克飞 on 2016/11/24 18:25.
 * e-mail:zkf@yitong.com.cn
 * FilePath NFCDemo:com.yitong.nfc2_0.pboc.tech.ber
 */

public final class BerTLV extends Ber {

    private final static String TAG = "BerTLV";

    private BerT berT;
    private BerL berL;
    private BerV berV;

    private ArrayList<BerTLV> child;

    public BerTLV() {
        data = new byte[0];
    }

    public BerTLV(byte[] data) {
        this.data = data;
        berT = new BerT();
        berL = new BerL();
        berV = new BerV();
        berV.read(data, berL.read(data, berT.read(data, 0)), berL.toInt());
    }


    public BerTLV(BerT berT, BerV berV) {
        this(berT, new BerL(ByteUtil.toBytes(berV.size())), berV);
    }

    private BerTLV(BerT t, BerL l) {
        super(ByteBuffer.allocate(t.data.length + l.data.length).array());
        this.berT = t;
        this.berL = l;

        System.arraycopy(t.data, 0, data, 0, t.data.length);
        System.arraycopy(l.data, 0, data, t.data.length, l.data.length);
    }

    public BerTLV(BerT berT, BerL berL, BerV berV) {
        this.berT = berT;
        this.berL = berL;
        this.berV = berV;

        ByteBuffer buffer = ByteBuffer.allocate(berT.size() + berL.size() + berV.size());
        buffer.put(berT.data);
        buffer.put(berL.data);
        buffer.put(berV.data);
        data = buffer.array();

        if (berT.hasChild())
            child = parse(berV.data);

    }

    public ArrayList<BerTLV> getChild() {
        return child;
    }

    public BerT getTag() {
        return berT;
    }

    public BerL getLen() {
        return berL;
    }

    public BerV getVal() {
        return berV;
    }

    /**
     * 解析字节中的tlv数据
     *
     * @param src
     * @return
     */
    public static ArrayList<BerTLV> parse(byte[] src) {
        ArrayList<BerTLV> tlvs = new ArrayList<BerTLV>();
        int start = 0;
        while (start < src.length) {
            BerT t = new BerT();
            BerL l = new BerL();
            BerV v = new BerV();
            start = t.read(src, start);
            start = l.read(src, start);
            start = v.read(src, start, l.toInt());
            tlvs.add(new BerTLV(t, l, v));
        }
        return tlvs;
    }

    public static ArrayList<BerTLV> readPDOL(byte[] bytes) {
        ArrayList<BerTLV> tlvs = new ArrayList<BerTLV>();
        int s = 0;
        while (s < bytes.length) {
            BerT t  = new BerT();
            BerL l  = new BerL();
            s = t.read(bytes,s);
            s = l.read(bytes,s);
            tlvs.add(new BerTLV(t, l));
        }
        return tlvs;
    }

    /**
     * 转成json格式
     * @return
     */
    public JSONObject toJson(){
        JSONObject json = new JSONObject();
        try {
            json.put("tag_"+berT.toString(),berV.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
}
