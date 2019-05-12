package top.xuqingquan.app

import android.app.Application
import android.content.Context
import androidx.fragment.app.FragmentManager
import top.xuqingquan.delegate.AppLifecycles
import top.xuqingquan.di.module.GlobalConfigModule
import top.xuqingquan.integration.ConfigModule

/**
 * Created by 许清泉 on 2019/4/15 00:33
 * 展示[ConfigModule] 用法，同时用作默认配置，避免开发者没有自定义出现崩溃
 */
class GlobalConfiguration : ConfigModule {
    override fun applyOptions(context: Context, builder: GlobalConfigModule.Builder) {
//        builder.printHttpLogLevel(BuildConfig.DEBUG ? Level.ALL : Level.NONE)//Release 时, 让框架不再打印 Http 请求和响应的信息
//        builder.baseurl(Api.APP_DOMAIN)
        //没有设置的话默认使用GlideImageLoaderStrategy
//        builder.imageLoaderStrategy(GlideImageLoaderStrategy())
        //想支持多 BaseUrl, 以及运行时动态切换任意一个 BaseUrl, 请使用 https://github.com/JessYanCoding/RetrofitUrlManager
        //如果 BaseUrl 在 App 启动时不能确定, 需要请求服务器接口动态获取, 请使用以下代码
        //以下方式是 Arms 框架自带的切换 BaseUrl 的方式, 在整个 App 生命周期内只能切换一次, 若需要无限次的切换 BaseUrl, 以及各种复杂的应用场景还是需要使用 RetrofitUrlManager 框架
        //以下代码只是配置, 还要使用 Okhttp (AppComponent 中提供) 请求服务器获取到正确的 BaseUrl 后赋值给 GlobalConfiguration.sDomain
        //切记整个过程必须在第一次调用 Retrofit 接口之前完成, 如果已经调用过 Retrofit 接口, 此种方式将不能切换 BaseUrl
//                builder.baseurl(new BaseUrl() {
//                    @Override
//                    public HttpUrl url() {
//                        return HttpUrl.parse(sDomain);
//                    }
//                })

        //可根据当前项目的情况以及环境为框架某些部件提供自定义的缓存策略, 具有强大的扩展性
//        builder.cacheFactory {
//            when (it.cacheTypeId) {
//                CacheType.EXTRAS_TYPE_ID -> IntelligentCache<Any>(500)
//                    CacheType.CACHE_SERVICE_CACHE_TYPE_ID->//自定义 Cache
//                else -> LruCache<Any, Any>(200)
//            }
//        }
        //可以自定义一个单例的线程池供全局使用
        //没有设置默认使用Executors.newCachedThreadPool()
//        builder.executorService(Executors.newCachedThreadPool())
        //这里提供一个全局处理 Http 请求和响应结果的处理类, 可以比客户端提前一步拿到服务器返回的结果, 可以做一些操作, 比如 Token 超时后, 重新获取 Token
//        builder.globalHttpHandler(GlobalHttpHandlerImpl())
//        builder.gsonConfiguration { _, gsonBuilder ->
//            gsonBuilder
//                .serializeNulls()//支持序列化值为 null 的参数
//                .enableComplexMapKeySerialization()//支持将序列化 key 为 Object 的 Map, 默认只能序列化 key 为 String 的 Map
//        }
//        builder.retrofitConfiguration { _, _ ->
            ////这里可以自己自定义配置 Retrofit 的参数, 甚至您可以替换框架配置好的 OkHttpClient 对象 (但是不建议这样做, 这样做您将损失框架提供的很多功能)
            //retrofitBuilder.addConverterFactory(FastJsonConverterFactory.create());//比如使用 FastJson 替代 Gson
//        }
//        builder.okhttpConfiguration { _, okhttpBuilder ->
//            //这里可以自己自定义配置 Okhttp 的参数
////            okhttpBuilder.sslSocketFactory() //支持 Https, 详情请百度
//            okhttpBuilder.writeTimeout(10, TimeUnit.SECONDS)
//            //使用一行代码监听 Retrofit／Okhttp 上传下载进度监听,以及 Glide 加载进度监听, 详细使用方法请查看 https://github.com/JessYanCoding/ProgressManager
//            ProgressManager.getInstance().with(okhttpBuilder)
//            //让 Retrofit 同时支持多个 BaseUrl 以及动态改变 BaseUrl, 详细使用方法请查看 https://github.com/JessYanCoding/RetrofitUrlManager
//            RetrofitUrlManager.getInstance().with(okhttpBuilder)
//        }
        //初始设置PagedList.Config
        //没有设置默认使用以下内容
//        builder.pagedListConfig(
//            PagedList.Config.Builder()
//                .setPageSize(20)
//                .setPrefetchDistance(5)
//                .setInitialLoadSizeHint(10)
//                .setEnablePlaceholders(false)
//                .build()
//        )
        //是否显示回退栈
        //没有配置默认不显示
//        builder.showStack(BuildConfig.DEBUG)
    }

    override fun injectAppLifecycle(context: Context, lifecycles: MutableList<AppLifecycles>) {
//        lifecycles.add(AppLifecyclesImpl())
    }

    override fun injectActivityLifecycle(
        context: Context,
        lifecycles: MutableList<Application.ActivityLifecycleCallbacks>
    ) {
//        lifecycles.add(ActivityLifecycleCallbacksImpl())
    }

    override fun injectFragmentLifecycle(
        context: Context,
        lifecycles: MutableList<FragmentManager.FragmentLifecycleCallbacks>
    ) {
//        lifecycles.add(FragmentLifecycleCallbacksImpl())
    }
}