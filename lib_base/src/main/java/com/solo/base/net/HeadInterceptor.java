package com.solo.base.net;

import com.solo.base.BaseApplication;
import com.solo.base.BaseLogUtil;
import com.solo.base.utils.AesCbcUtil;
import com.solo.base.utils.ApiSignUtil;
import com.solo.base.utils.AppUtil;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Create by Totoro
 * 2019-11-11 09:30
 **/
public class HeadInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request newRequest;

        HttpUrl url = request.url().newBuilder()
                .encodedPath("/" + ApiSignUtil.makeMd5(BaseApplication.getApplication().getPackageName() + request.url().encodedPath()))
                .build();

        String md5 = ApiSignUtil.getMd5(BaseApplication.getApplication());
        BaseLogUtil.d("sign", md5);
        newRequest = request.newBuilder()
                .url(url)
                .addHeader("package-name", BaseApplication.getApplication().getPackageName())
                .addHeader("sign", md5)
                .addHeader("app_version_code", String.valueOf(AppUtil.getVersionCode()))
                .addHeader("iv", AesCbcUtil.ivParameter)
                .build();

        return chain.proceed(newRequest);
    }
}