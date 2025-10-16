package com.yitong.nfc;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.tech.IsoDep;
import android.nfc.tech.NfcF;
import android.os.Build;

/**
 * nfc管理工具类
 * Created by 左克飞 on 2016/11/24 11:47.
 * e-mail zkf@yitong.com.cn
 * FilePath NFCDemo:com.yitong.nfc2_0
 */
public final class NFCManager {

    private final Activity activity;
    private PendingIntent pendingIntent;
    private NfcAdapter nfcAdapter;

    private static NFCManager nfcManager;

    private static String[][] TECHLISTS;
    private static IntentFilter[] TAGFILTERS;

    static {
        try {
            TECHLISTS = new String[][]{{IsoDep.class.getName()},
                    {NfcF.class.getName()},};

            TAGFILTERS = new IntentFilter[]{new IntentFilter(
                    NfcAdapter.ACTION_TECH_DISCOVERED, "*/*")};
        } catch (IntentFilter.MalformedMimeTypeException e) {
            e.printStackTrace();
        }
    }


    private NFCManager(Activity activity) {
        this.activity = activity;
        pendingIntent = PendingIntent.getActivity(activity, 0, new Intent(activity, activity.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        nfcAdapter = NfcAdapter.getDefaultAdapter(activity);

    }

    public static NFCManager getInstance(Activity activity) {
        if (nfcManager == null)
            nfcManager = new NFCManager(activity);
        return nfcManager;
    }

    public void onResume() {
        if (nfcAdapter != null)
            nfcAdapter.enableForegroundDispatch(activity, pendingIntent, TAGFILTERS, TECHLISTS);
    }

    public void onPause() {
        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(activity);
            nfcManager = null;
        }
    }

    /**
     * 查询nfc状态
     *
     * @return
     */
    public NfcState checkNfcState() {
        if (Build.VERSION.SDK_INT < 10)
            return NfcState.NFC_NONE;
        if (nfcAdapter == null)
            return NfcState.NFC_NONE;
        else if (nfcAdapter.isEnabled())
            return NfcState.NFC_OPEN;
        else {
            return NfcState.NFC_CLOSE;
        }
    }


    /**
     * nfc状态
     */
    public enum NfcState {
        NFC_NONE, NFC_CLOSE, NFC_OPEN
    }
}
