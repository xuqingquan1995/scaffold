package top.xuqingquan.delegate

import android.app.Application
import android.content.ComponentCallbacks2
import android.content.Context
import top.xuqingquan.app.ScaffoldConfig
import top.xuqingquan.cache.IntelligentCache
import top.xuqingquan.integration.LifecycleConfig
import top.xuqingquan.lifecycle.AppLifecyclesImpl
import top.xuqingquan.utils.ManifestParser

/**
 * Created by 许清泉 on 2019/4/14 22:55
 */
class AppDelegate(context: Context) : AppLifecycles {
    private var mApplication: Application? = null
    private var mActivityLifecycle: Application.ActivityLifecycleCallbacks? = null
    private var mModules: List<LifecycleConfig>? = null
    private var mAppLifecycles: MutableList<AppLifecycles>? = arrayListOf()
    private var mActivityLifecycles: MutableList<Application.ActivityLifecycleCallbacks>? = arrayListOf()
    private var mComponentCallback: ComponentCallbacks2? = null

    init {
        mAppLifecycles!!.add(AppLifecyclesImpl())
        //用反射, 将 AndroidManifest.xml 中带有 LifecycleConfig 标签的 class 转成对象集合（List<LifecycleConfig>）
        this.mModules = ManifestParser(context).parse()
        //遍历之前获得的集合, 执行每一个 LifecycleConfig 实现类的某些方法
        for (module in mModules!!) {
            //将框架外部, 开发者实现的 Application 的生命周期回调 (AppLifecycles) 存入 mAppLifecycles 集合 (此时还未注册回调)
            module.injectAppLifecycle(context, mAppLifecycles!!)
            //将框架外部, 开发者实现的 Activity 的生命周期回调 (ActivityLifecycleCallbacks) 存入 mActivityLifecycles 集合 (此时还未注册回调)
            module.injectActivityLifecycle(context, mActivityLifecycles!!)
        }
        attachBaseContext(context)
    }

    override fun attachBaseContext(base: Context?) {
        //遍历 mAppLifecycles, 执行所有已注册的 AppLifecycles 的 attachBaseContext() 方法 (框架外部, 开发者扩展的逻辑)
        for (lifecycle in mAppLifecycles!!) {
            lifecycle.attachBaseContext(base)
        }
    }

    override fun onCreate(application: Application) {
        this.mApplication = application
        ScaffoldConfig.getInstance(application)
        mActivityLifecycle = ScaffoldConfig.getActivityLifecycleCallbacks()
        //将 LifecycleConfig 的实现类的集合存放到缓存 Cache, 可以随时获取
        //使用 IntelligentCache.KEY_KEEP 作为 key 的前缀, 可以使储存的数据永久存储在内存中
        //否则存储在 LRU 算法的存储空间中 (大于或等于缓存所能允许的最大 size, 则会根据 LRU 算法清除之前的条目)
        //前提是 extras 使用的是 IntelligentCache (框架默认使用)
        mModules?.let {
            ScaffoldConfig.getExtras().put(IntelligentCache.getKeyOfKeep(LifecycleConfig::class.java.name), it)
        }
        this.mModules = null
        //注册框架内部已实现的 Activity 生命周期逻辑
        mApplication!!.registerActivityLifecycleCallbacks(mActivityLifecycle)
        //注册框架外部, 开发者扩展的 Activity 生命周期逻辑
        //每个 LifecycleConfig 的实现类可以声明多个 Activity 的生命周期回调
        //也可以有 N 个 LifecycleConfig 的实现类 (完美支持组件化项目 各个 Module 的各种独特需求)
        for (lifecycle in mActivityLifecycles!!) {
            mApplication!!.registerActivityLifecycleCallbacks(lifecycle)
        }
        mComponentCallback = ScaffoldConfig.getComponentCallbacks2()
        mComponentCallback?.let {
            //注册回掉: 内存紧张时释放部分内存
            mApplication!!.registerComponentCallbacks(it)
        }
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
        if (!mActivityLifecycles.isNullOrEmpty()) {
            for (lifecycle in mActivityLifecycles!!) {
                mApplication!!.unregisterActivityLifecycleCallbacks(lifecycle)
            }
        }
        if (!mAppLifecycles.isNullOrEmpty()) {
            for (lifecycle in mAppLifecycles!!) {
                lifecycle.onTerminate(mApplication!!)
            }
        }
        this.mActivityLifecycle = null
        this.mActivityLifecycles = null
        this.mComponentCallback = null
        this.mAppLifecycles = null
        this.mApplication = null
    }

}
