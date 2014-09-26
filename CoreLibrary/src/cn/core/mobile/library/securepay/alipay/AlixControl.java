/*
 * Copyright (C) 2010 The MobileSecurePay Project
 * All right reserved.
 * author: shiqun.shi@alipay.com
 */

package cn.core.mobile.library.securepay.alipay;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.alipay.android.app.sdk.AliPay;

import cn.core.library.mobile.R;
import cn.core.mobile.library.securepay.OnPayResultListener;

/**
 * 模拟商户应用的商品列表，交易步骤。
 * <p/>
 * 1. 将商户ID，收款帐号，外部订单号，商品名称，商品介绍，价格，通知地址封装成订单信息
 * 2. 对订单信息进行签名
 * 3. 将订单信息，签名，签名方式封装成请求参数
 * 4. 调用pay方法
 *
 * @version v4_0413 2012-03-02
 */
public class AlixControl {
    static String TAG = "AlixControl";
    private Context mContext;
    private OnPayResultListener mListener;
    private String mSignKey;

    private static final int RQF_PAY_RESULT = 100;

    public AlixControl(Context context) {
        this.mContext = context;
    }

    /**
     * 开始支付
     *
     * @param signKey      算签名sign时使用
     * @param strOrderInfo 支付订单信息
     * @param listener     支付结果监听器
     * @return true
     */
    public boolean pay(final String signKey, final String strOrderInfo, OnPayResultListener listener) {
        mListener = listener;
        mSignKey = signKey;

        new Thread() {
            public void run() {
                AliPay aliPay = new AliPay((Activity) mContext, mHandler);

                //设置为沙箱模式，不设置默认为线上环境,(目前没有开放)
                // aliPay.setSandBox(true);

                String result = aliPay.pay(strOrderInfo);

                Message msg = new Message();
                msg.what = RQF_PAY_RESULT;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        }.start();
        return true;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RQF_PAY_RESULT:
                    Result result = new Result((String) msg.obj);
                    // 先判断是否操作成功。
                    if ("9000".equals(result.getResultCode())) {
                        // 操作成功后在判断签名是否通过
                        result.parseResult(mSignKey);
                        if (result.isSignOk()) {
                            if (mListener != null) {
                                mListener.onPayResult(null);
                            }
                        } else {
                            if (mListener != null) {
                                String message = mContext.getString(R.string.alipay_check_sign_failed);
                                Throwable e = new Throwable(message);
                                mListener.onPayResult(e);
                            }
                        }
                    } else {
                        if (mListener != null) {
                            Throwable e = new Throwable(result.getResultStatus());
                            mListener.onPayResult(e);
                        }
                    }
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

}