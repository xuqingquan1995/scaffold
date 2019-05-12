package top.xuqingquan.app

import android.app.Application
import android.content.Context
import top.xuqingquan.delegate.AppDelegate
import top.xuqingquan.delegate.AppLifecycles


/**
 * Created by 许清泉 on 2019/4/14 00:53
 * 开发者的[Application]可直接继承这个类或者实现App接口，
 */
open class BaseApplication : Application(), App {

    private lateinit var mAppDelegate: AppLifecycles

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        mAppDelegate = AppDelegate(base!!)
    }

    override fun onCreate() {
        super.onCreate()
        mAppDelegate.onCreate(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        mAppDelegate.onTerminate(this)
    }

    override fun getAppComponent() = (mAppDelegate as App).getAppComponent()

}