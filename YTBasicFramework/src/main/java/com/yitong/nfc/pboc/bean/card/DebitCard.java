package com.yitong.nfc.pboc.bean.card;

import com.yitong.nfc.pboc.Constants;
import com.yitong.nfc.pboc.tech.IsoTag;

/**
 * 借记卡
 * Created by 左克飞 on 2016/11/24 13:04.
 * e-mail:zkf@yitong.com.cn
 * FilePath NFCDemo:com.yitong.nfc2_0.pboc.bean.card
 */

public class DebitCard extends BankCard {
    public DebitCard(IsoTag isoTag) {
        super(isoTag);
    }

    @Override
    public String getAid() {
        return Constants.DEBIT_CARD_AID;
    }


}
