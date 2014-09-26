package cn.core.mobile.library.securepay.weixin;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;

import cn.core.mobile.library.remote.response.EntityResponse;

/**
 * 微信支付
 * Created by Will.Wu on 2014/7/29.
 */
public class WxPayer {
    private Context mContext;

    public WxPayer(Context context) {
        this.mContext = context;
    }

    public void pay(IWXAPI api, String appId, String partnerId, String orderStr) {
        TypeToken<EntityResponse<PrepayIdResult>> typeToken = new TypeToken<EntityResponse<PrepayIdResult>>() {
        };
        Gson gson = new Gson();
        EntityResponse<PrepayIdResult> response = gson.fromJson(orderStr, typeToken.getType());
        PrepayIdResult result = response.getResult();
        if (result != null) {
            PayReq req = new PayReq();

            req.appId = appId;
            req.partnerId = partnerId;
            req.prepayId = result.getPrepayId();
            req.nonceStr = result.getNonceStr();
            req.timeStamp = String.valueOf(result.getTimeStamp());
            req.packageValue = "Sign=" + result.getPackageValue();
            req.sign = result.getSignParams();

            // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
            api.sendReq(req);
        }
    }
}
