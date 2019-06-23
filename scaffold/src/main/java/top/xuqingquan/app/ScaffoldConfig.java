package top.xuqingquan.app;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Preconditions;
import androidx.fragment.app.FragmentManager;
import androidx.paging.PagedList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import top.xuqingquan.http.BaseUrl;
import top.xuqingquan.http.GlobalHttpHandler;
import top.xuqingquan.http.log.DefaultFormatPrinter;
import top.xuqingquan.http.log.FormatPrinter;
import top.xuqingquan.http.log.Level;
import top.xuqingquan.http.log.RequestInterceptor;
import top.xuqingquan.imageloader.BaseImageLoaderStrategy;
import top.xuqingquan.imageloader.GlideImageLoaderStrategy;
import top.xuqingquan.imageloader.ImageLoader;
import top.xuqingquan.integration.AppManager;
import top.xuqingquan.integration.IRepositoryManager;
import top.xuqingquan.integration.RepositoryManager;
import top.xuqingquan.lifecycle.FragmentLifecycleCallbacksImpl;
import top.xuqingquan.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ScaffoldConfig {
    private static ScaffoldConfig instance;
    private static Application application;
    private static Gson gson;
    private static GsonConfiguration gsonConfiguration;
    private static HttpUrl httpUrl;
    private static BaseUrl baseUrl;
    private static boolean showStack = BuildConfig.DEBUG;
    private static Level level;
    private static PagedList.Config config;
    private static ExecutorService executorService;
    private static FormatPrinter formatPrinter;
    private static ImageLoader imageLoader;
    private static BaseImageLoaderStrategy baseImageLoaderStrategy;
    private static GlobalHttpHandler globalHttpHandler;
    private static List<Interceptor> interceptors;
    private static File cacheFile;
    private static Cache.Factory cacheFactory;
    private static Cache<String, Object> extras;
    private static RetrofitConfiguration retrofitConfiguration;
    private static OkhttpConfiguration okhttpConfiguration;

    private ScaffoldConfig(Application application) {
        ScaffoldConfig.application = application;
    }

    public static ScaffoldConfig getInstance(Application application) {
        if (instance == null) {
            synchronized (ScaffoldConfig.class) {
                if (instance == null) {
                    instance = new ScaffoldConfig(application);
                }
            }
        }
        return instance;
    }

    public static Application getApplication() {
        return application;
    }

    public static Gson getGson() {
        if (gson == null) {
            GsonBuilder builder = new GsonBuilder();
            if (gsonConfiguration != null) {
                gsonConfiguration.configGson(application, builder);
            }
        }
        return gson;
    }

    public static HttpUrl getHttpUrl() {
        if (baseUrl != null) {
            HttpUrl httpUrl = baseUrl.url();
            if (httpUrl != null) {
                return httpUrl;
            }
        }
        if (httpUrl == null) {
            return HttpUrl.parse("https://api.github.com/");
        } else {
            return httpUrl;
        }
    }

    public static boolean isShowStack() {
        return showStack;
    }

    public static Level getLevel() {
        if (level == null) {
            level = BuildConfig.DEBUG ? Level.ALL : Level.NONE;
        }
        return level;
    }

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

    public static ExecutorService getExecutorService() {
        if (executorService == null) {
            executorService = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS,
                    new SynchronousQueue<>(), Util.threadFactory("AppExecutor", false));
        }
        return executorService;
    }

    public static FormatPrinter getFormatPrinter() {
        if (formatPrinter == null) {
            formatPrinter = new DefaultFormatPrinter();
        }
        return formatPrinter;
    }

    public static ImageLoader getImageLoader() {
        if (imageLoader == null) {
            imageLoader = ImageLoader.getInstance();
        }
        return imageLoader;
    }

    @Nullable
    public static BaseImageLoaderStrategy getLoaderStrategy() {
        return baseImageLoaderStrategy;
    }

    public static GlobalHttpHandler getGlobalHttpHandler() {
        if (globalHttpHandler == null) {
            globalHttpHandler = GlobalHttpHandler.EMPTY;
        }
        return globalHttpHandler;
    }

    public static List<Interceptor> getInterceptors() {
        return interceptors;
    }

    public static File getCacheFile() {
        if (cacheFile == null) {
            cacheFile = FileUtils.getCacheFile(application);
        }
        return cacheFile;
    }

    public static Cache.Factory getCacheFactory() {
        return cacheFactory == null ?
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
                } : cacheFactory;
    }

    public static Cache<String, Object> getExtras() {
        if (extras == null) {
            extras = getCacheFactory().build(CacheType.getEXTRAS());
        }
        return extras;
    }

    public static RetrofitConfiguration getRetrofitConfiguration() {
        return retrofitConfiguration;
    }

    public static OkhttpConfiguration getOkhttpConfiguration() {
        return okhttpConfiguration;
    }

    public ScaffoldConfig setGsonConfiguration(GsonConfiguration gsonConfiguration) {
        ScaffoldConfig.gsonConfiguration = gsonConfiguration;
        return this;
    }

    public ScaffoldConfig setBaseUrl(BaseUrl baseUrl) {
        ScaffoldConfig.baseUrl = Preconditions.checkNotNull(baseUrl, BaseUrl.class.getCanonicalName() + "can not be null.");
        ;
        return this;
    }

    public ScaffoldConfig setBaseUrl(String baseUrl) {
        if (TextUtils.isEmpty(baseUrl)) {
            throw new NullPointerException("BaseUrl can not be empty");
        }
        ScaffoldConfig.httpUrl = HttpUrl.parse(baseUrl);
        return this;
    }

    public ScaffoldConfig setShowStack(boolean showStack) {
        ScaffoldConfig.showStack = showStack;
        return this;
    }

    public ScaffoldConfig setLevel(Level level) {
        if (level == null) {
            level = BuildConfig.DEBUG ? Level.ALL : Level.NONE;
        }
        ScaffoldConfig.level = level;
        return this;
    }

    public ScaffoldConfig sePagedListConfig(PagedList.Config config) {
        ScaffoldConfig.config = config;
        return this;
    }

    public ScaffoldConfig setExecutorService(ExecutorService executorService) {
        if (executorService == null) {
            executorService = Executors.newCachedThreadPool();
        }
        ScaffoldConfig.executorService = executorService;
        return this;
    }

    public ScaffoldConfig setFormatPrinter(FormatPrinter formatPrinter) {
        ScaffoldConfig.formatPrinter = Preconditions.checkNotNull(formatPrinter, FormatPrinter.class.getCanonicalName() + "can not be null.");
        return this;
    }

    public ScaffoldConfig setLoaderStrategy(BaseImageLoaderStrategy loaderStrategy) {
        if (loaderStrategy == null) {//设置默认图片加载器
            loaderStrategy = new GlideImageLoaderStrategy();
        }
        ScaffoldConfig.baseImageLoaderStrategy = loaderStrategy;
        return this;
    }

    public ScaffoldConfig setGlobalHttpHandler(GlobalHttpHandler globalHttpHandler) {
        ScaffoldConfig.globalHttpHandler = globalHttpHandler;
        return this;
    }

    public ScaffoldConfig addInterceptor(Interceptor interceptor) {
        if (interceptors == null) {
            interceptors = new ArrayList<>();
        }
        interceptors.add(interceptor);
        return this;
    }

    public ScaffoldConfig setCacheFile(File cacheFile) {
        ScaffoldConfig.cacheFile = cacheFile;
        return this;
    }

    public ScaffoldConfig setCacheFactory(Cache.Factory cacheFactory) {
        ScaffoldConfig.cacheFactory = cacheFactory;
        return this;
    }

    public ScaffoldConfig setRetrofitConfiguration(RetrofitConfiguration retrofitConfiguration) {
        ScaffoldConfig.retrofitConfiguration = retrofitConfiguration;
        return this;
    }

    public ScaffoldConfig setOkhttpConfiguration(OkhttpConfiguration okhttpConfiguration) {
        ScaffoldConfig.okhttpConfiguration = okhttpConfiguration;
        return this;
    }

    /*********************************************************************/

    private static OkHttpClient.Builder okHttpClientBuilder;
    private static OkHttpClient okHttpClient;
    private static Retrofit.Builder retrofitBuilder;
    private static Retrofit retrofit;
    private static IRepositoryManager repositoryManager;
    private static final int TIME_OUT = 10;

    public static OkHttpClient.Builder getOkHttpClientBuilder() {
        if (okHttpClientBuilder == null) {
            okHttpClientBuilder = new OkHttpClient.Builder();
        }
        return okHttpClientBuilder;
    }

    public static OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            OkHttpClient.Builder builder = getOkHttpClientBuilder();
            builder
                    .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                    .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                    .addNetworkInterceptor(RequestInterceptor.getInstance());
            builder.addInterceptor(chain -> chain.proceed(getGlobalHttpHandler().onHttpRequestBefore(chain, chain.request())));
            List<Interceptor> interceptors = getInterceptors();
            //如果外部提供了 Interceptor 的集合则遍历添加
            if (interceptors != null) {
                for (Interceptor interceptor : interceptors) {
                    builder.addInterceptor(interceptor);
                }
            }
            //为 OkHttp 设置默认的线程池
            builder.dispatcher(new Dispatcher(getExecutorService()));
            OkhttpConfiguration configuration = getOkhttpConfiguration();
            if (configuration != null) {
                configuration.configOkhttp(application, builder);
            }
            okHttpClient = builder.build();
        }
        return okHttpClient;
    }

    public static Retrofit.Builder getRetrofitBuilder() {
        if (retrofitBuilder == null) {
            retrofitBuilder = new Retrofit.Builder();
        }
        return retrofitBuilder;
    }

    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            Retrofit.Builder builder = getRetrofitBuilder();
            OkHttpClient client = getOkHttpClient();
            builder.baseUrl(ScaffoldConfig.getHttpUrl())//域名
                    .client(client);//设置 OkHttp
            ScaffoldConfig.RetrofitConfiguration configuration = ScaffoldConfig.getRetrofitConfiguration();
            if (configuration != null) {
                configuration.configRetrofit(application, builder);
            }
            builder
                    .addConverterFactory(GsonConverterFactory.create(ScaffoldConfig.getGson()));//使用 Gson
            retrofit = builder.build();
        }
        return retrofit;
    }

    public static IRepositoryManager getRepositoryManager() {
        if (repositoryManager == null) {
            repositoryManager = RepositoryManager.getInstance();
        }
        return repositoryManager;
    }

    /*********************************************************************/

    @SuppressLint("StaticFieldLeak")
    private static AppManager appManager;
    private static Application.ActivityLifecycleCallbacks activityLifecycleCallbacks;
    private static FragmentManager.FragmentLifecycleCallbacks fragmentLifecycleCallbacks;
    private static List<FragmentManager.FragmentLifecycleCallbacks> fragmentLifecycleCallbacksList;

    public static AppManager getAppManager() {
        if (appManager == null) {
            appManager = AppManager.getAppManager().init(application);
        }
        return appManager;
    }

    public static Application.ActivityLifecycleCallbacks getActivityLifecycleCallbacks() {
        return activityLifecycleCallbacks;
    }

    public static FragmentManager.FragmentLifecycleCallbacks getFragmentLifecycleCallbacks() {
        return fragmentLifecycleCallbacks;
    }

    public static List<FragmentManager.FragmentLifecycleCallbacks> getFragmentLifecycleCallbacksList() {
        if (fragmentLifecycleCallbacksList == null) {
            fragmentLifecycleCallbacksList = new ArrayList<>();
            fragmentLifecycleCallbacksList.add(new FragmentLifecycleCallbacksImpl());
        }
        return fragmentLifecycleCallbacksList;
    }

    /*********************************************************************/

    public interface GsonConfiguration {
        void configGson(@NonNull Context context, GsonBuilder builder);
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
