package cn.core.mobile.library.securepay.weixin;

import cn.core.mobile.library.remote.response.Entity;

/**
 * Created by Will.Wu on 2014/7/29.
 */
public class PrepayIdResult extends Entity {
    private String prepayId;
    private String nonceStr;
    private long timeStamp;
    private String packageValue;
    private String signParams;

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getPackageValue() {
        return packageValue;
    }

    public void setPackageValue(String packageValue) {
        this.packageValue = packageValue;
    }

    public String getSignParams() {
        return signParams;
    }

    public void setSignParams(String signParams) {
        this.signParams = signParams;
    }
}
