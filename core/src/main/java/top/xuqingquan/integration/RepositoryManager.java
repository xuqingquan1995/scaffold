package top.xuqingquan.integration;

import androidx.annotation.NonNull;

import top.xuqingquan.utils.Preconditions;

import java.lang.reflect.Proxy;

import retrofit2.Retrofit;
import top.xuqingquan.app.ScaffoldConfig;
import top.xuqingquan.cache.Cache;
import top.xuqingquan.cache.CacheType;

/**
 * Created by 许清泉 on 2019/4/14 17:23
 */
public final class RepositoryManager implements IRepositoryManager {
    private final Retrofit mRetrofit;
    private final Cache.Factory mCacheFactory;
    private Cache<String, Object> mRetrofitServiceCache;
    private final ObtainServiceDelegate mObtainServiceDelegate;
    private static RepositoryManager instance;

    private RepositoryManager() {
        mCacheFactory = ScaffoldConfig.getCacheFactory();
        mRetrofit = ScaffoldConfig.getRetrofit();
        mObtainServiceDelegate = ScaffoldConfig.getObtainServiceDelegate();
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
    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T> T obtainRetrofitService(@NonNull Class<T> serviceClass) {
        if (mRetrofitServiceCache == null) {
            mRetrofitServiceCache = mCacheFactory.build(CacheType.getRETROFIT_SERVICE_CACHE());
        }
        Preconditions.checkNotNull(mRetrofitServiceCache,
                "Cannot return null from a Cache.Factory#build(int) method");
        //noinspection unchecked
        T retrofitService = (T) mRetrofitServiceCache.get(serviceClass.getCanonicalName());
        if (retrofitService == null) {
            if (mObtainServiceDelegate != null) {
                retrofitService = mObtainServiceDelegate.createRetrofitService(mRetrofit, serviceClass);
            }
            if (retrofitService == null) {
                retrofitService = (T) Proxy.newProxyInstance(
                        serviceClass.getClassLoader(),
                        new Class[]{serviceClass},
                        new RetrofitServiceProxyHandler(mRetrofit, serviceClass));
            }
            mRetrofitServiceCache.put(serviceClass.getCanonicalName(), retrofitService);
        }
        return retrofitService;
    }
}
