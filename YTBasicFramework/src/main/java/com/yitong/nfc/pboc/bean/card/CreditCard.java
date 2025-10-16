package com.yitong.nfc.pboc.bean.card;

import com.yitong.nfc.pboc.Constants;
import com.yitong.nfc.pboc.tech.IsoTag;

/**
 * 信用卡
 * Created by 左克飞 on 2016/11/24 13:03.
 * e-mail:zkf@yitong.com.cn
 * FilePath NFCDemo:com.yitong.nfc2_0.pboc.bean.card
 */

public class CreditCard extends BankCard {
    public CreditCard(IsoTag isoTag)  {
        super(isoTag);
    }

    @Override
    public String getAid() {
        return Constants.CREDIT_CARD_AID;
    }
}
