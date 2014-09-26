package cn.core.mobile.library.securepay;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;

import cn.core.mobile.library.activity.BaseActivity;
import cn.core.mobile.library.securepay.alipay.AlixControl;
import cn.core.mobile.library.securepay.weixin.WxPayer;

/**
 * 支付activity的抽象类
 * Created by Will.Wu on 2014/7/29.
 */
public abstract class PayActivity extends BaseActivity implements OnPayResultListener {
    private PayConfig mPayConfig = null;
    private IWXAPI api;

    public enum PayType {
        /**
         * 支付宝支付
         */
        ALIPAY,
        /**
         * 微信支付
         */
        WEIXIN,
        /**
         * 银联支付
         */
        UNIONPAY
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPayConfig();
        registerToWx();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 开始支付
     *
     * @param type      支付类型
     * @param orderInfo 支付订单
     */
    protected void pay(PayType type, String orderInfo) {
        if (type == PayType.ALIPAY) {
            AlixControl control = new AlixControl(this);
            control.pay(mPayConfig.AliPay_RsaPublicKey, orderInfo, this);
        } else if (type == PayType.WEIXIN) {
            WxPayer payer = new WxPayer(this);
            payer.pay(api, mPayConfig.WXPay_AppId, mPayConfig.WXPay_PartnerId, orderInfo);
        } else if (type == PayType.UNIONPAY) {

        } else {
            // 不支持的支付方式
        }
    }

    private void initPayConfig() {
        try {
            mPayConfig = new PayConfig();

            InputStream inputStream = getResources().getAssets().open("pay_config.xml");
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser pull = factory.newPullParser();
            pull.setInput(inputStream, "UTF-8");

            int eventCode = pull.getEventType();
            while (eventCode != XmlPullParser.END_DOCUMENT) {
                String name = pull.getName();
                switch (eventCode) {
                    case XmlPullParser.START_TAG:
                        // 支付宝
                        if (name.equalsIgnoreCase("AliPay")) {
                            mPayConfig.AliPay_RsaPublicKey = pull.getAttributeValue(null, "RsaPublicKey");
                        } else if (name.equalsIgnoreCase("WXPay")) { // 微信支付
                            mPayConfig.WXPay_AppId = pull.getAttributeValue(null, "AppId");
                            mPayConfig.WXPay_PartnerId = pull.getAttributeValue(null, "PartnerId");
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                    default:
                        break;
                }
                eventCode = pull.next();
            }
        } catch (IOException e) {
            e.printStackTrace();
            mPayConfig = null;
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            mPayConfig = null;
        }
        if (mPayConfig == null) {
            Toast.makeText(this, "初始化支付配置失败，请检查pay_config.xml文件!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 注册到微信
     */
    private void registerToWx() {
        if (mPayConfig != null) {
            api = WXAPIFactory.createWXAPI(this, mPayConfig.WXPay_AppId, false);
            api.registerApp(mPayConfig.WXPay_AppId);
        }
    }
}
