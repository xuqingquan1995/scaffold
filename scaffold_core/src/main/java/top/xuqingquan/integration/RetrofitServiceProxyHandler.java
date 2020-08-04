package top.xuqingquan.integration;

import androidx.annotation.Nullable;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import retrofit2.Retrofit;

class RetrofitServiceProxyHandler implements InvocationHandler {

    private Retrofit mRetrofit;
    private Class<?> mServiceClass;
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
