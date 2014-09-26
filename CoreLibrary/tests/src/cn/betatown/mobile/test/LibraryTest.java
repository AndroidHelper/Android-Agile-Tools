/**
 * 项目名称: CoreLibrary
 * 文件名称:
 * 创建时间: 2014/6/6
 * Copyright: 2014 www.fantasee.cn Inc. All rights reserved.
 */
package cn.betatown.mobile.test;

import android.test.AndroidTestCase;

import com.google.gson.reflect.TypeToken;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.betatown.mobile.library.http.HttpUtilsV2;
import cn.betatown.mobile.library.http.callback.EntityResponseCallback;
import cn.betatown.mobile.library.remote.response.EntityResponse;

/**
 * @author Will.Wu </br> Create at 2014/6/6
 * @version 1.0
 */
public class LibraryTest extends AndroidTestCase {
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testHttpPost() throws Exception {
        String url = "http://192.168.1.2:8020/product-mserver/checkAppVersion.bdo";
        List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
        postParameters.add(new BasicNameValuePair("appType", "ANDROID"));
        postParameters.add(new BasicNameValuePair("versionNum", "1.2"));
        postParameters.add(new BasicNameValuePair("configFileVersionNum", 1.0 + ""));
        Type t = new TypeToken<EntityResponse<String>>() {
        }.getType();
        HttpUtilsV2.post(getContext(), url, postParameters, t, new EntityResponseCallback<Object>() {
            @Override
            public void onCompleted(Throwable e, long updateTime, EntityResponse<Object> result) {
                if (e != null){
                    int i = 0;
                    i = 0;
                }
            }
        });

    }
}
