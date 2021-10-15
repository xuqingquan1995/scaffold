package top.xuqingquan.app

import android.app.Application
import android.content.Context
import me.jessyan.retrofiturlmanager.RetrofitUrlManager
import top.xuqingquan.delegate.AppDelegate
import top.xuqingquan.delegate.AppLifecycle

/**
 *  @author : 许清泉 xuqingquan1995@gmail.com
 *  @since   : 2021-10-15
 */
class App : Application() {
    private lateinit var mAppDelegate: AppLifecycle

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        mAppDelegate = AppDelegate.getInstance(base!!)
        RetrofitUrlManager.getInstance().putDomain("URL_1", "xxxx")
        RetrofitUrlManager.getInstance().putDomain("URL_2", "xxxx")
        HttpsUtils.initSslSocketFactory(this)
        ScaffoldConfig.getInstance(this)
            .setOkhttpConfiguration { _, builder ->
                RetrofitUrlManager.getInstance().with(builder)
            }
            .setGsonConfiguration { _, builder ->
                builder.setLenient()
            }
            .debug(true)
    }

    override fun onCreate() {
        super.onCreate()
        mAppDelegate.onCreate(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        mAppDelegate.onTerminate(this)
    }

}