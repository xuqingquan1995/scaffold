package top.xuqingquan.sample

import android.app.Application
import android.content.Context
import com.tencent.mmkv.MMKV
import top.xuqingquan.app.ScaffoldConfig
import top.xuqingquan.delegate.AppDelegate
import top.xuqingquan.delegate.AppLifecycle

class App : Application() {

    //已经通过ContentProvider方式初始化，这边的初始化可以不用了
    private lateinit var mAppDelegate: AppLifecycle

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        mAppDelegate = AppDelegate.getInstance(base!!)
    }

    override fun onCreate() {
        super.onCreate()
        ScaffoldConfig.getInstance(this)
            .setBaseUrl("https://api.douban.com")
//            .setBaseUrl("http://so.techlz.com:9972/")
            .setRetrofitConfiguration { _, builder ->
                builder.addConverterFactory(TestConverterFactory.create(ScaffoldConfig.getGson()))
            }
            .debug(BuildConfig.DEBUG)
        mAppDelegate.onCreate(this)
//            .setBaseUrl("https://api.douban.com")
        MMKV.initialize(this)
        MMKV.defaultMMKV().encode("haha","111111")
    }

    override fun onTerminate() {
        super.onTerminate()
        mAppDelegate.onTerminate(this)
    }
}