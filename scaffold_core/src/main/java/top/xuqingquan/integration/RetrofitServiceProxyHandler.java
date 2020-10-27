package top.xuqingquan.integration;

import android.support.annotation.Nullable;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import retrofit2.Retrofit;

final class RetrofitServiceProxyHandler implements InvocationHandler {

    private final Retrofit mRetrofit;
    private final Class<?> mServiceClass;
    private Object mRetrofitService;

    public RetrofitServiceProxyHandler(Retrofit retrofit, Class<?> serviceClass) {
        mRetrofit = retrofit;
        mServiceClass = serviceClass;
    }

    @Override
    public Object invoke(Object proxy, Method method, @Nullable Object[] args) throws Throwable {
        return method.invoke(getRetrofitService(), args);
    }

    private Object getRetrofitService() {
        if (mRetrofitService == null) {
            mRetrofitService = mRetrofit.create(mServiceClass);
        }
        return mRetrofitService;
    }
}
