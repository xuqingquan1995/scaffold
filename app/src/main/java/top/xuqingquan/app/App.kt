package top.xuqingquan.app

import android.app.Application
import android.content.Context
import me.jessyan.retrofiturlmanager.RetrofitUrlManager
import okhttp3.OkHttpClient
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
        val scaffold = ScaffoldConfig.getInstance(this)
            .setOkhttpConfiguration { _, builder ->
                RetrofitUrlManager.getInstance().with(builder)
            }
            .setGsonConfiguration { _, builder ->
                builder.setLenient()
            }
            .debug(true)
        val builder = ScaffoldConfig.getOkHttpClientBuilder(OkHttpClient.Builder())
        RetrofitUrlManager.getInstance().with(builder)
        builder.sslSocketFactory(HttpsUtils.sSLSocketFactory, HttpsUtils.trustManager)
        val retrofit = ScaffoldConfig.getNewRetrofit(builder.build())
        scaffold.addRetrofit("https", retrofit)
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