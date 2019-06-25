package top.xuqingquan.integration;

import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.core.util.Preconditions;
import retrofit2.Retrofit;
import top.xuqingquan.app.ScaffoldConfig;
import top.xuqingquan.cache.Cache;
import top.xuqingquan.cache.CacheType;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by 许清泉 on 2019/4/14 17:23
 */
public class RepositoryManager implements IRepositoryManager {
    private Retrofit mRetrofit;
    private Application mApplication;
    private Cache.Factory mCachefactory;
    private Cache<String, Object> mRetrofitServiceCache;
    private static RepositoryManager instance;

    private RepositoryManager() {
        mApplication = ScaffoldConfig.getApplication();
        mCachefactory = ScaffoldConfig.getCacheFactory();
        mRetrofit = ScaffoldConfig.getRetrofit();
    }

    public static RepositoryManager getInstance() {
        if (instance == null) {
            synchronized (RepositoryManager.class) {
                if (instance == null) {
                    instance = new RepositoryManager();
                }
            }
        }
        return instance;
    }

    /**
     * 根据传入的 Class 获取对应的 Retrofit service
     *
     * @param serviceClass ApiService class
     * @param <T>          ApiService class
     * @return ApiService
     */
    @NonNull
    @Override
    public <T> T obtainRetrofitService(@NonNull Class<T> serviceClass) {
        return createWrapperService(serviceClass);
    }

    /**
     * 根据 https://zhuanlan.zhihu.com/p/40097338 对 Retrofit 进行的优化
     *
     * @param serviceClass ApiService class
     * @param <T>          ApiService class
     * @return ApiService
     */
    private <T> T createWrapperService(Class<T> serviceClass) {
        Preconditions.checkNotNull(serviceClass, "serviceClass == null");

        // 二次代理
        //noinspection unchecked
        return (T) Proxy.newProxyInstance(serviceClass.getClassLoader(),
                new Class<?>[]{serviceClass}, (proxy, method, args) -> {
                    // 此处在调用 serviceClass 中的方法时触发
                    final T service = getRetrofitService(serviceClass);
                    return getRetrofitMethod(service, method).invoke(service, args);
                });
    }

    /**
     * 根据传入的 Class 获取对应的 Retrofit service
     *
     * @param serviceClass ApiService class
     * @param <T>          ApiService class
     * @return ApiService
     */
    @SuppressWarnings("unchecked")
    private <T> T getRetrofitService(Class<T> serviceClass) {
        if (mRetrofitServiceCache == null) {
            mRetrofitServiceCache = mCachefactory.build(CacheType.getRETROFIT_SERVICE_CACHE());
        }
        Preconditions.checkNotNull(mRetrofitServiceCache,
                "Cannot return null from a Cache.Factory#build(int) method");
        T retrofitService = (T) mRetrofitServiceCache.get(serviceClass.getCanonicalName());
        if (retrofitService == null) {
            retrofitService = mRetrofit.create(serviceClass);
            mRetrofitServiceCache.put(serviceClass.getCanonicalName(), retrofitService);
        }
        return retrofitService;
    }

    private <T> Method getRetrofitMethod(T service, Method method) throws NoSuchMethodException {
        return service.getClass().getMethod(method.getName(), method.getParameterTypes());
    }

    @NonNull
    @Override
    public Context getContext() {
        return mApplication;
    }
}
