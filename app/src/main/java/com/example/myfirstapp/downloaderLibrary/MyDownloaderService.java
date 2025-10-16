package com.example.myfirstapp.downloaderLibrary;

import com.google.android.vending.expansion.downloader.impl.DownloaderService;

/**
 * Date：2023/1/5
 * Time：16:11
 * Author：chenshengrui
 */
public class MyDownloaderService extends DownloaderService {

    // stuff for LVL -- MODIFY FOR YOUR APPLICATION!
    private static final String BASE64_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAuqJYiijHNsoqmzUVvPirqgxdVd+KE61QPo1w0xbX0AmFNmoJqAkoAr8SX9+LmcZLJA6B/6ko4LroH0UNKSy9VHUQo8Ph1eD1PYgsISnuCdwdarjXh2bgFeeOlEhtLM4U95wAmC6L4FjHKxRyv7zHA7C00QuBRUSBnkptnnknemhS1efWDDs6PBsWYC7GheLU05a3KmtZJrN7wanHHUC4wp3zhZjYxToRtHAuNPMHLFMpIgYGN1bvFQp06hrZwh60Ruh/IhNJ8mdqg3nbctCwPAyXC3EAOznuWU2E7VCHm/fuD/q2s73t3NwVi3ExX2w/cvSzWEhjz3Egt8bgw5MPYQIDAQAB";

    // used by the preference obfuscater
    private static final byte[] SALT = new byte[]{
            1, 43, -12, -11, 4, 8,
            -100, -12, 43, 12, -8, -4, 9, 5, -106, -108, -33, 45, -1, 84
    };

    @Override
    public String getPublicKey() {
        return BASE64_PUBLIC_KEY;
    }

    @Override
    public byte[] getSALT() {
        return SALT;
    }

    @Override
    public String getAlarmReceiverClassName() {
        return MyAlarmReceiver.class.getName();
    }
}
