package top.xuqingquan.delegate

import android.app.Application
import android.content.ComponentCallbacks2
import android.content.Context
import androidx.core.util.Preconditions
import top.xuqingquan.app.App
import top.xuqingquan.cache.IntelligentCache
import top.xuqingquan.di.component.AppComponent
import top.xuqingquan.di.component.DaggerAppComponent
import top.xuqingquan.di.module.GlobalConfigModule
import top.xuqingquan.integration.ConfigModule
import top.xuqingquan.lifecycle.AppLifecyclesImpl
import top.xuqingquan.utils.ManifestParser
import java.util.*
import javax.inject.Inject

/**
 * Created by 许清泉 on 2019/4/14 22:55
 */
class AppDelegate(context: Context) : App, AppLifecycles {

    private var mApplication: Application? = null
    private var mAppComponent: AppComponent? = null
    @Inject
    @JvmField
    var mActivityLifecycle: Application.ActivityLifecycleCallbacks? = null

    private var mModules: List<ConfigModule>? = null
    private var mAppLifecycles: MutableList<AppLifecycles>? = ArrayList()
    private var mActivityLifecycles: MutableList<Application.ActivityLifecycleCallbacks>? = ArrayList()
    private var mComponentCallback: ComponentCallbacks2? = null

    init {
        mAppLifecycles!!.add(AppLifecyclesImpl())
        //用反射, 将 AndroidManifest.xml 中带有 ConfigModule 标签的 class 转成对象集合（List<ConfigModule>）
        this.mModules = ManifestParser(context).parse()
        //遍历之前获得的集合, 执行每一个 ConfigModule 实现类的某些方法
        for (module in mModules!!) {
            //将框架外部, 开发者实现的 Application 的生命周期回调 (AppLifecycles) 存入 mAppLifecycles 集合 (此时还未注册回调)
            module.injectAppLifecycle(context, mAppLifecycles!!)
            //将框架外部, 开发者实现的 Activity 的生命周期回调 (ActivityLifecycleCallbacks) 存入 mActivityLifecycles 集合 (此时还未注册回调)
            module.injectActivityLifecycle(context, mActivityLifecycles!!)
        }
    }

    override fun attachBaseContext(base: Context?) {
        //遍历 mAppLifecycles, 执行所有已注册的 AppLifecycles 的 attachBaseContext() 方法 (框架外部, 开发者扩展的逻辑)
        for (lifecycle in mAppLifecycles!!) {
            lifecycle.attachBaseContext(base)
        }
    }

    override fun onCreate(application: Application) {
        this.mApplication = application
        mAppComponent = DaggerAppComponent
            .builder()
            .application(mApplication!!)//提供application
            .globalConfigModule(getGlobalConfigModule(mApplication!!, mModules!!))//全局配置
            .build()
        mAppComponent!!.inject(this)
        //将 ConfigModule 的实现类的集合存放到缓存 Cache, 可以随时获取
        //使用 IntelligentCache.KEY_KEEP 作为 key 的前缀, 可以使储存的数据永久存储在内存中
        //否则存储在 LRU 算法的存储空间中 (大于或等于缓存所能允许的最大 size, 则会根据 LRU 算法清除之前的条目)
        //前提是 extras 使用的是 IntelligentCache (框架默认使用)
        mAppComponent!!.extras().put(IntelligentCache.getKeyOfKeep(ConfigModule::class.java.name), mModules!!)
        this.mModules = null
        //注册框架内部已实现的 Activity 生命周期逻辑
        mApplication!!.registerActivityLifecycleCallbacks(mActivityLifecycle)
        //注册框架外部, 开发者扩展的 Activity 生命周期逻辑
        //每个 ConfigModule 的实现类可以声明多个 Activity 的生命周期回调
        //也可以有 N 个 ConfigModule 的实现类 (完美支持组件化项目 各个 Module 的各种独特需求)
        for (lifecycle in mActivityLifecycles!!) {
            mApplication!!.registerActivityLifecycleCallbacks(lifecycle)
        }
        mComponentCallback = AppComponentCallbacks(mApplication!!, mAppComponent!!)
        //注册回掉: 内存紧张时释放部分内存
        mApplication!!.registerComponentCallbacks(mComponentCallback)
        //执行框架外部, 开发者扩展的 App onCreate 逻辑
        for (lifecycle in mAppLifecycles!!) {
            lifecycle.onCreate(mApplication!!)
        }
    }

    override fun onTerminate(application: Application) {
        if (mActivityLifecycle != null) {
            mApplication!!.unregisterActivityLifecycleCallbacks(mActivityLifecycle)
        }
        if (mComponentCallback != null) {
            mApplication!!.unregisterComponentCallbacks(mComponentCallback)
        }
        if (mActivityLifecycles != null && mActivityLifecycles!!.size > 0) {
            for (lifecycle in mActivityLifecycles!!) {
                mApplication!!.unregisterActivityLifecycleCallbacks(lifecycle)
            }
        }
        if (mAppLifecycles != null && mAppLifecycles!!.size > 0) {
            for (lifecycle in mAppLifecycles!!) {
                lifecycle.onTerminate(mApplication!!)
            }
        }
        this.mAppComponent = null
        this.mActivityLifecycle = null
        this.mActivityLifecycles = null
        this.mComponentCallback = null
        this.mAppLifecycles = null
        this.mApplication = null
    }

    /**
     * 将app的全局配置信息封装进module(使用Dagger注入到需要配置信息的地方)
     * 需要在AndroidManifest中声明[ConfigModule]的实现类,和Glide的配置方式相似
     *
     * @return GlobalConfigModule
     */
    private fun getGlobalConfigModule(context: Context, modules: List<ConfigModule>): GlobalConfigModule {
        val builder = GlobalConfigModule
            .builder()
        //遍历 ConfigModule 集合, 给全局配置 GlobalConfigModule 添加参数
        for (module in modules) {
            module.applyOptions(context, builder)
        }

        return builder.build()
    }

    /**
     * 将 [AppComponent] 返回出去, 供其它地方使用, [AppComponent] 接口中声明的方法返回的实例, 在 [.getAppComponent] 拿到对象后都可以直接使用
     *
     * @return AppComponent
     */
    override fun getAppComponent(): AppComponent {
        Preconditions.checkNotNull(
            mAppComponent, String.format(
                "%s == null, first call %s#onCreate(Application) in %s#onCreate()",
                AppComponent::class.java.name, javaClass.name, if (mApplication == null)
                    Application::class.java.name
                else
                    mApplication!!.javaClass.name
            )
        )
        return mAppComponent!!
    }
}
