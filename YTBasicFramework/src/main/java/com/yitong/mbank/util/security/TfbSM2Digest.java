package com.yitong.mbank.util.security;

import java.math.BigInteger;
import java.util.Random;

import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.ECCurve.Fp;
import org.bouncycastle.util.encoders.Hex;
import cn.com.yitong.util.sm.SM3Digest;

/**
 * Created by Jeremy on 2019/2/21.
 */
public class TfbSM2Digest {
    public static final int C1C2C3 = 0;
    public static final int C1C3C2 = 1;
    public static String strP = "FFFFFFFE FFFFFFFF FFFFFFFF FFFFFFFF FFFFFFFF 00000000 FFFFFFFF FFFFFFFF";
    public static String strA = "FFFFFFFE FFFFFFFF FFFFFFFF FFFFFFFF FFFFFFFF 00000000 FFFFFFFF FFFFFFFC";
    public static String strB = "28E9FA9E 9D9F5E34 4D5A9E4B CF6509A7 F39789F5 15AB8F92 DDBCBD41 4D940E93";
    public static String strN = "FFFFFFFE FFFFFFFF FFFFFFFF FFFFFFFF 7203DF6B 21C6052B 53BBF409 39D54123";
    public static String strGx;
    public static String strGy;
    private Fp b;
    private ECPoint c;
    private ECPoint d;
    private BigInteger N;
    private int type = 1;
    private static volatile TfbSM2Digest e = null;
    private Random f = new Random();

    static {
        strGx = CryptoUtil.getConfigKey("DEFAULT_PUBLIC_KEY_X");
        strGy = CryptoUtil.getConfigKey("DEFAULT_PUBLIC_KEY_Y");
    }

    private TfbSM2Digest() {
        BigInteger var1 = b(strP);
        this.b = new Fp(var1, b(strA), b(strB));
        org.bouncycastle.math.ec.ECFieldElement.Fp var2 = new org.bouncycastle.math.ec.ECFieldElement.Fp(var1, b(strGx));
        org.bouncycastle.math.ec.ECFieldElement.Fp var3 = new org.bouncycastle.math.ec.ECFieldElement.Fp(var1, b(strGy));
        this.c = new org.bouncycastle.math.ec.ECPoint.Fp(this.b, var2, var3);
        this.N = b(strN);
        String var5 = "C7191C0C324E45AE4FC7CBA56567B631038D702815BD8E4619882EA465264B5B";
        String var6 = "F5932A4180077E64AEC9E4767AE9EC97A083DF7EA7A5D41677E1ED12F520F295";
        var2 = new org.bouncycastle.math.ec.ECFieldElement.Fp(var1, b(var5));
        org.bouncycastle.math.ec.ECFieldElement.Fp var4 = new org.bouncycastle.math.ec.ECFieldElement.Fp(var1, b(var6));
        this.d = new org.bouncycastle.math.ec.ECPoint.Fp(this.b, var2, var4);
    }

    public static TfbSM2Digest getInstance() {
        if (e == null) {
            synchronized (TfbSM2Digest.class) {
                if (e == null) {
                    e = new TfbSM2Digest();
                }
            }
        }
        return e;
    }

    public TfbSM2Digest(String var1, String var2) {
        BigInteger var3 = b(strP);
        this.b = new Fp(var3, b(strA), b(strB));
        org.bouncycastle.math.ec.ECFieldElement.Fp var4 = new org.bouncycastle.math.ec.ECFieldElement.Fp(var3, b(strGx));
        org.bouncycastle.math.ec.ECFieldElement.Fp var5 = new org.bouncycastle.math.ec.ECFieldElement.Fp(var3, b(strGy));
        this.c = new org.bouncycastle.math.ec.ECPoint.Fp(this.b, var4, var5);
        this.N = b(strN);
        org.bouncycastle.math.ec.ECFieldElement.Fp var6 = new org.bouncycastle.math.ec.ECFieldElement.Fp(var3, b(var1));
        org.bouncycastle.math.ec.ECFieldElement.Fp var7 = new org.bouncycastle.math.ec.ECFieldElement.Fp(var3, b(var2));
        this.d = new org.bouncycastle.math.ec.ECPoint.Fp(this.b, var6, var7);
    }

    public byte[] encode(byte[] var1) {
        byte[] var4 = var1;
        ECPoint var3 = this.d;
        ECPoint var2 = this.c;
        TfbSM2Digest var9 = this;
        ECPoint var5 = null;
        ECPoint var6 = null;
        boolean var7 = false;

        while (!var7) {
            BigInteger var8 = (new BigInteger(256, var9.f)).mod(var9.N.subtract(BigInteger.ONE)).add(BigInteger.ONE);
            if (var7 = a(var5 = var2.multiply(var8))) {
                var7 = a(var6 = var3.multiply(var8));
            }
        }

        byte[] var13 = b(var5);
        byte[] var10 = b(var6);
        int var11 = var4.length;
        byte[] var12 = a(var10, var11);
        var12 = a(var4, var12);
        var10 = b(var10, var4);
        var4 = new byte[var11 + 64 + var10.length];
        if (1 == var9.type) {
            System.arraycopy(var13, 0, var4, 0, 64);
            System.arraycopy(var10, 0, var4, 64, 32);
            System.arraycopy(var12, 0, var4, 96, var11);
        } else {
            System.arraycopy(var13, 0, var4, 0, 64);
            System.arraycopy(var12, 0, var4, 64, var11);
            System.arraycopy(var10, 0, var4, var11 + 64, 32);
        }

        return var4;
    }

    private static byte[] b(byte[] var0, byte[] var1) {
        byte[] var2 = new byte[var0.length + var1.length];
        int var3 = var0.length / 2;
        System.arraycopy(var0, 0, var2, 0, var3);
        System.arraycopy(var1, 0, var2, var3, var1.length);
        System.arraycopy(var0, var3, var2, var3 + var1.length, var3);

        try {
            return SM3Digest.getInstance().hash(var2);
        } catch (Exception var4) {
            return null;
        }
    }

    private static byte[] a(byte[] var0, byte[] var1) {
        byte[] var2 = new byte[var0.length];

        for (int var3 = 0; var3 < var0.length; ++var3) {
            var2[var3] = (byte) (var0[var3] ^ var1[var3]);
        }

        return var2;
    }

    private static String a(String var0) {
        StringBuilder var1 = new StringBuilder();

        for (int var2 = var0.length(); var2 < 64; ++var2) {
            var1.append("0");
        }

        var1.append(var0);
        return var1.toString();
    }

    private static boolean a(ECPoint var0) {
        String var1 = var0.getX().toBigInteger().toString(16);
        String var2 = var0.getY().toBigInteger().toString(16);
        var1 = a(var1);
        var2 = a(var2);
        byte[] var3 = Hex.decode(var1);
        byte[] var6 = Hex.decode(var2);
        boolean var4;
        if (!(var4 = var3.length == 32 && var6.length == 32)) {
            int var5 = var1.length();
            String[] var10000 = new String[]{String.format("field size:%d", var5)};
            if (var5 > 64) {
                var0.multiply(b("343035333638363136453637343836313639353936393534364636453637"));
            }

            var10000 = new String[]{String.format("x.size:%d,y.size:%d", var3.length, var6.length)};
        }

        return var4;
    }

    private static byte[] b(ECPoint var0) {
        byte[] var1 = new byte[64];
        String var2 = var0.getX().toBigInteger().toString(16);
        String var3 = var0.getY().toBigInteger().toString(16);
        var2 = a(var2);
        var3 = a(var3);
        byte[] var5 = Hex.decode(var2);
        byte[] var4 = Hex.decode(var3);
        System.arraycopy(var5, 0, var1, 0, 32);
        System.arraycopy(var4, 0, var1, 32, 32);
        return var1;
    }

    private static byte[] a(byte[] var0, int var1) {
        SM3Digest var2 = SM3Digest.getInstance();
        int var3 = var1 / 32;
        int var4 = var1 % 32;
        byte[] var12 = new byte[var1];
        int var5 = var0.length;
        int var6 = 1;

        int var7;
        byte[] var8;
        byte[] var9;
        for (var7 = 0; var7 < var3; ++var7) {
            var8 = a(var6);
            var9 = new byte[var5 + 4];
            System.arraycopy(var0, 0, var9, 0, var5);
            System.arraycopy(var8, 0, var9, var5, 4);

            try {
                System.arraycopy(var2.hash(var9), 0, var12, var7 << 5, 32);
            } catch (Exception var11) {
                var11.printStackTrace();
            }

            ++var6;
        }

        if (var4 > 0) {
            var8 = a(var6);
            var9 = new byte[var5 + 4];
            System.arraycopy(var0, 0, var9, 0, var5);
            System.arraycopy(var8, 0, var9, var5, 4);

            try {
                System.arraycopy(var2.hash(var9), 0, var12, var7 << 5, var4);
            } catch (Exception var10) {
                var10.printStackTrace();
            }
        }

        return var12;
    }

    private static byte[] a(int var0) {
        byte[] var1;
        (var1 = new byte[4])[0] = (byte) (var0 >> 24);
        var1[1] = (byte) (var0 >> 16);
        var1[2] = (byte) (var0 >> 8);
        var1[3] = (byte) var0;
        return var1;
    }

    private static BigInteger b(String var0) {
        return new BigInteger(var0.replaceAll(" ", ""), 16);
    }

    public int getType() {
        return this.type;
    }

    public void setType(int var1) {
        this.type = var1;
    }
}
