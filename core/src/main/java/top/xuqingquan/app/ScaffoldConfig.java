package top.xuqingquan.app;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.paging.PagedList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import okhttp3.Dispatcher;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.internal.Util;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import top.xuqingquan.BuildConfig;
import top.xuqingquan.cache.Cache;
import top.xuqingquan.cache.CacheType;
import top.xuqingquan.cache.IntelligentCache;
import top.xuqingquan.cache.LruCache;
import top.xuqingquan.http.GlobalHttpHandler;
import top.xuqingquan.http.log.DefaultFormatPrinter;
import top.xuqingquan.http.log.FormatPrinter;
import top.xuqingquan.http.log.Level;
import top.xuqingquan.http.log.RequestInterceptor;
import top.xuqingquan.imageloader.BaseImageLoaderStrategy;
import top.xuqingquan.imageloader.ImageLoader;
import top.xuqingquan.imageloader.glide.GlideImageLoaderStrategy;
import top.xuqingquan.integration.ActivityLifecycle;
import top.xuqingquan.integration.AppManager;
import top.xuqingquan.integration.FragmentLifecycle;
import top.xuqingquan.integration.IRepositoryManager;
import top.xuqingquan.integration.RepositoryManager;
import top.xuqingquan.utils.FileUtils;

@SuppressWarnings({"WeakerAccess", "unused", "RedundantSuppression", "rawtypes"})
public final class ScaffoldConfig {
    private static boolean debug = BuildConfig.DEBUG;
    private static ScaffoldConfig instance;
    private static Application application;
    private static Gson gson;
    private static GsonConfiguration gsonConfiguration;
    private static HttpUrl httpUrl;
    private static Level level;
    private static PagedList.Config config;
    private static ExecutorService executorService;
    private static FormatPrinter formatPrinter;
    private static ImageLoader imageLoader;
    private static BaseImageLoaderStrategy baseImageLoaderStrategy;
    private static GlobalHttpHandler globalHttpHandler;
    private static List<Interceptor> interceptors;
    private static List<Interceptor> netInterceptors;
    private static File cacheFile;
    private static Cache.Factory cacheFactory;
    private static Cache<String, Object> extras;
    private static RetrofitConfiguration retrofitConfiguration;
    private static OkhttpConfiguration okhttpConfiguration;
    private static ComponentCallbacks2 componentCallbacks2;
    private static IRepositoryManager.ObtainServiceDelegate obtainServiceDelegate;
    private static boolean useOkHttpLoadImage;
    private static long imageCacheSize = 512 * 1024 * 1024;//512MB

    private ScaffoldConfig(@NonNull Application application) {
        ScaffoldConfig.application = application;
    }

    public static ScaffoldConfig getInstance(@NonNull Application application) {
        if (instance == null) {
            synchronized (ScaffoldConfig.class) {
                if (instance == null) {
                    instance = new ScaffoldConfig(application);
                }
            }
        }
        return instance;
    }

    public static boolean debug() {
        return debug || BuildConfig.DEBUG;
    }

    @NonNull
    public static Application getApplication() {
        return application;
    }

    @NonNull
    public static Gson getGson() {
        if (gson == null) {
            GsonBuilder builder = new GsonBuilder();
            builder.serializeNulls();//支持序列化值为 null 的参数
            builder.enableComplexMapKeySerialization();//支持将序列化 key 为 Object 的 Map, 默认只能序列化 key 为 String 的 Map
            if (gsonConfiguration != null) {
                gsonConfiguration.configGson(application, builder);
            }
            gson = builder.create();
        }
        return gson;
    }

    @NonNull
    public static HttpUrl getHttpUrl() {
        if (httpUrl == null) {
            //noinspection ConstantConditions
            return HttpUrl.parse("https://api.github.com/");
        } else {
            return httpUrl;
        }
    }

    @NonNull
    public static Level getLevel() {
        if (level == null) {
            level = debug() ? Level.ALL : Level.NONE;
        }
        return level;
    }

    @NonNull
    public static PagedList.Config getPagedListConfig() {
        if (config == null) {
            config = new PagedList.Config.Builder()
                    .setPageSize(20)
                    .setPrefetchDistance(5)
                    .setInitialLoadSizeHint(10)
                    .setEnablePlaceholders(false)
                    .build();
        }
        return config;
    }

    @NonNull
    public static ExecutorService getExecutorService() {
        if (executorService == null) {
            executorService = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS,
                    new SynchronousQueue<>(), Util.threadFactory("AppExecutor", false));
        }
        return executorService;
    }

    @NonNull
    public static FormatPrinter getFormatPrinter() {
        if (formatPrinter == null) {
            formatPrinter = new DefaultFormatPrinter();
        }
        return formatPrinter;
    }

    @NonNull
    public static ImageLoader getImageLoader() {
        if (imageLoader == null) {
            imageLoader = ImageLoader.getInstance();
        }
        return imageLoader;
    }

    @NonNull
    public static BaseImageLoaderStrategy getLoaderStrategy() {
        if (baseImageLoaderStrategy == null) {//设置默认图片加载器
            baseImageLoaderStrategy = new GlideImageLoaderStrategy();
        }
        return baseImageLoaderStrategy;
    }

    @NonNull
    public static GlobalHttpHandler getGlobalHttpHandler() {
        if (globalHttpHandler == null) {
            globalHttpHandler = GlobalHttpHandler.EMPTY;
        }
        return globalHttpHandler;
    }

    @Nullable
    public static List<Interceptor> getInterceptors() {
        return interceptors;
    }

    @Nullable
    public static List<Interceptor> getNetInterceptors() {
        return netInterceptors;
    }

    @NonNull
    public static File getCacheFile() {
        if (cacheFile == null) {
            cacheFile = FileUtils.getCacheFile(application);
        }
        return cacheFile;
    }

    @NonNull
    public static Cache.Factory getCacheFactory() {
        if (cacheFactory == null) {
            cacheFactory = type -> {
                //若想自定义 LruCache 的 size, 或者不想使用 LruCache, 想使用自己自定义的策略
                //使用 GlobalConfigModule.Builder#cacheFactory() 即可扩展
                switch (type.getCacheTypeId()) {
                    //Activity、Fragment 以及 Extras 使用 IntelligentCache (具有 LruCache 和 可永久存储数据的 Map)
                    case CacheType.EXTRAS_TYPE_ID:
                    case CacheType.ACTIVITY_CACHE_TYPE_ID:
                    case CacheType.FRAGMENT_CACHE_TYPE_ID:
                        return new IntelligentCache(type.calculateCacheSize(application));
                    //其余使用 LruCache (当达到最大容量时可根据 LRU 算法抛弃不合规数据)
                    default:
                        return new LruCache(type.calculateCacheSize(application));
                }
            };
        }
        return cacheFactory;
    }

    @NonNull
    public static Cache<String, Object> getExtras() {
        if (extras == null) {
            //noinspection unchecked
            extras = getCacheFactory().build(CacheType.getEXTRAS());
        }
        return extras;
    }

    @Nullable
    public static RetrofitConfiguration getRetrofitConfiguration() {
        return retrofitConfiguration;
    }

    @Nullable
    public static IRepositoryManager.ObtainServiceDelegate getObtainServiceDelegate() {
        return obtainServiceDelegate;
    }

    @Nullable
    public static OkhttpConfiguration getOkhttpConfiguration() {
        return okhttpConfiguration;
    }

    @Nullable
    public static ComponentCallbacks2 getComponentCallbacks2() {
        return componentCallbacks2;
    }

    public static boolean isUseOkHttpLoadImage() {
        return useOkHttpLoadImage;
    }

    public ScaffoldConfig debug(boolean debug) {
        ScaffoldConfig.debug = debug;
        return this;
    }

    public ScaffoldConfig setUseOkHttpLoadImage(boolean useOkHttpLoadImage) {
        ScaffoldConfig.useOkHttpLoadImage = useOkHttpLoadImage;
        return this;
    }

    public static long getImageCacheSize() {
        return imageCacheSize;
    }

    public ScaffoldConfig setImageCacheSize(long imageCacheSize) {
        ScaffoldConfig.imageCacheSize = imageCacheSize;
        return this;
    }

    @NonNull
    public ScaffoldConfig setGsonConfiguration(@Nullable GsonConfiguration gsonConfiguration) {
        ScaffoldConfig.gsonConfiguration = gsonConfiguration;
        return this;
    }

    @NonNull
    public ScaffoldConfig setBaseUrl(@NonNull String baseUrl) {
        ScaffoldConfig.httpUrl = HttpUrl.parse(baseUrl);
        return this;
    }

    @NonNull
    public ScaffoldConfig setLevel(@Nullable Level level) {
        ScaffoldConfig.level = level;
        return this;
    }

    @NonNull
    public ScaffoldConfig setPagedListConfig(@Nullable PagedList.Config config) {
        ScaffoldConfig.config = config;
        return this;
    }

    @NonNull
    public ScaffoldConfig setExecutorService(@Nullable ExecutorService executorService) {
        ScaffoldConfig.executorService = executorService;
        return this;
    }

    @NonNull
    public ScaffoldConfig setFormatPrinter(@Nullable FormatPrinter formatPrinter) {
        ScaffoldConfig.formatPrinter = formatPrinter;
        return this;
    }

    @NonNull
    public ScaffoldConfig setLoaderStrategy(@Nullable BaseImageLoaderStrategy loaderStrategy) {
        ScaffoldConfig.baseImageLoaderStrategy = loaderStrategy;
        return this;
    }

    @NonNull
    public ScaffoldConfig setGlobalHttpHandler(@Nullable GlobalHttpHandler globalHttpHandler) {
        ScaffoldConfig.globalHttpHandler = globalHttpHandler;
        return this;
    }

    @NonNull
    public ScaffoldConfig addInterceptor(@NonNull Interceptor interceptor) {
        if (interceptors == null) {
            interceptors = new ArrayList<>();
        }
        interceptors.add(interceptor);
        return this;
    }

    @NonNull
    public ScaffoldConfig addNetInterceptor(@NonNull Interceptor interceptor) {
        if (netInterceptors == null) {
            netInterceptors = new ArrayList<>();
        }
        netInterceptors.add(interceptor);
        return this;
    }

    @NonNull
    public ScaffoldConfig setCacheFile(@Nullable File cacheFile) {
        ScaffoldConfig.cacheFile = cacheFile;
        return this;
    }

    @NonNull
    public ScaffoldConfig setCacheFactory(@Nullable Cache.Factory cacheFactory) {
        ScaffoldConfig.cacheFactory = cacheFactory;
        return this;
    }

    @NonNull
    public ScaffoldConfig setRetrofitConfiguration(@Nullable RetrofitConfiguration retrofitConfiguration) {
        ScaffoldConfig.retrofitConfiguration = retrofitConfiguration;
        return this;
    }

    public ScaffoldConfig setObtainServiceDelegate(IRepositoryManager.ObtainServiceDelegate obtainServiceDelegate) {
        ScaffoldConfig.obtainServiceDelegate = obtainServiceDelegate;
        return this;
    }

    @NonNull
    public ScaffoldConfig setOkhttpConfiguration(@Nullable OkhttpConfiguration okhttpConfiguration) {
        ScaffoldConfig.okhttpConfiguration = okhttpConfiguration;
        return this;
    }

    @NonNull
    public ScaffoldConfig setComponentCallbacks2(@Nullable ComponentCallbacks2 componentCallbacks2) {
        ScaffoldConfig.componentCallbacks2 = componentCallbacks2;
        return this;
    }

    /*********************************************************************/

    private static OkHttpClient.Builder okHttpClientBuilder;
    private static OkHttpClient okHttpClient;
    private static Retrofit.Builder retrofitBuilder;
    private static Retrofit retrofit;
    private static IRepositoryManager repositoryManager;
    private static Map<String, Retrofit> retrofitMap;
    private static final int TIME_OUT = 10;

    @NonNull
    public static OkHttpClient.Builder getOkHttpClientBuilder() {
        if (okHttpClientBuilder == null) {
            okHttpClientBuilder = new OkHttpClient.Builder();
        }
        return okHttpClientBuilder;
    }

    public static OkHttpClient.Builder getOkHttpClientBuilder(OkHttpClient.Builder builder) {
        builder
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .addNetworkInterceptor(RequestInterceptor.getInstance())
                .addInterceptor(chain -> chain.proceed(getGlobalHttpHandler().onHttpRequestBefore(chain, chain.request())));
        List<Interceptor> netInterceptors = getNetInterceptors();
        //如果外部提供了 Interceptor 的集合则遍历添加
        if (netInterceptors != null) {
            for (Interceptor interceptor : netInterceptors) {
                if (interceptor != null) {
                    builder.addNetworkInterceptor(interceptor);
                }
            }
        }
        List<Interceptor> interceptors = getInterceptors();
        //如果外部提供了 Interceptor 的集合则遍历添加
        if (interceptors != null) {
            for (Interceptor interceptor : interceptors) {
                if (interceptor != null) {
                    builder.addInterceptor(interceptor);
                }
            }
        }
        //为 OkHttp 设置默认的线程池
        builder.dispatcher(new Dispatcher(getExecutorService()));
        return builder;
    }

    @NonNull
    public static OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            OkHttpClient.Builder builder = getOkHttpClientBuilder(getOkHttpClientBuilder());
            OkhttpConfiguration configuration = getOkhttpConfiguration();
            if (configuration != null) {
                configuration.configOkhttp(application, builder);
            }
            okHttpClient = builder.build();
        }
        return okHttpClient;
    }

    @NonNull
    public static Retrofit.Builder getRetrofitBuilder() {
        if (retrofitBuilder == null) {
            retrofitBuilder = new Retrofit.Builder();
        }
        return retrofitBuilder;
    }

    @NonNull
    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            retrofit = getNewRetrofit(getOkHttpClient());
            instance.addRetrofit("default", retrofit);
        }
        return retrofit;
    }

    public static Retrofit getNewRetrofit(OkHttpClient client) {
        Retrofit.Builder builder = getRetrofitBuilder();
        builder.baseUrl(ScaffoldConfig.getHttpUrl())//域名
                .client(client);//设置 OkHttp
        ScaffoldConfig.RetrofitConfiguration configuration = ScaffoldConfig.getRetrofitConfiguration();
        if (configuration != null) {
            configuration.configRetrofit(application, builder);
        }
        builder.addConverterFactory(GsonConverterFactory.create(ScaffoldConfig.getGson()));//使用 Gson
        return builder.build();
    }

    @NonNull
    public static IRepositoryManager getRepositoryManager() {
        if (repositoryManager == null) {
            repositoryManager = RepositoryManager.getInstance();
        }
        return repositoryManager;
    }

    public static Retrofit getRetrofit(String name) {
        if (retrofitMap == null) {
            retrofitMap = new HashMap<>();
        }
        Retrofit retrofit = retrofitMap.get(name);
        if (retrofit == null) {
            retrofit = getRetrofit();
        }
        return retrofit;
    }

    public ScaffoldConfig addRetrofit(String name, Retrofit retrofit) {
        if (retrofitMap == null) {
            retrofitMap = new HashMap<>();
        }
        retrofitMap.put(name, retrofit);
        return this;
    }

    /*********************************************************************/

    @SuppressLint("StaticFieldLeak")
    private static AppManager appManager;
    private static Application.ActivityLifecycleCallbacks activityLifecycleCallbacks;
    private static FragmentManager.FragmentLifecycleCallbacks fragmentLifecycleCallbacks;
    private static List<FragmentManager.FragmentLifecycleCallbacks> fragmentLifecycleCallbacksList;

    @NonNull
    public static AppManager getAppManager() {
        if (appManager == null) {
            appManager = AppManager.getAppManager().init(application);
        }
        return appManager;
    }

    @NonNull
    public static Application.ActivityLifecycleCallbacks getActivityLifecycleCallbacks() {
        if (activityLifecycleCallbacks == null) {
            activityLifecycleCallbacks = new ActivityLifecycle();
        }
        return activityLifecycleCallbacks;
    }

    @NonNull
    public static FragmentManager.FragmentLifecycleCallbacks getFragmentLifecycleCallbacks() {
        if (fragmentLifecycleCallbacks == null) {
            fragmentLifecycleCallbacks = new FragmentLifecycle();
        }
        return fragmentLifecycleCallbacks;
    }

    @NonNull
    public static List<FragmentManager.FragmentLifecycleCallbacks> getFragmentLifecycleCallbacksList() {
        if (fragmentLifecycleCallbacksList == null) {
            fragmentLifecycleCallbacksList = new ArrayList<>();
        }
        return fragmentLifecycleCallbacksList;
    }

    /*********************************************************************/

    public interface GsonConfiguration {
        void configGson(@NonNull Context context, @NonNull GsonBuilder builder);
    }

    /**
     * {@link Retrofit} 自定义配置接口
     */
    public interface RetrofitConfiguration {
        void configRetrofit(@NonNull Context context, @NonNull Retrofit.Builder builder);
    }

    /**
     * {@link OkHttpClient} 自定义配置接口
     */
    public interface OkhttpConfiguration {
        void configOkhttp(@NonNull Context context, @NonNull OkHttpClient.Builder builder);
    }
}
