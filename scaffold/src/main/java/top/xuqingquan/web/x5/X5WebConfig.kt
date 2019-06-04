package top.xuqingquan.web.x5

import android.content.Context
import android.os.AsyncTask
import android.os.Build
import com.tencent.smtt.sdk.CookieManager
import com.tencent.smtt.sdk.CookieSyncManager
import com.tencent.smtt.sdk.ValueCallback
import com.tencent.smtt.sdk.WebView
import top.xuqingquan.utils.Timber
import top.xuqingquan.web.agent.AgentWebConfig

object X5WebConfig {

    //获取Cookie
    @JvmStatic
    fun getCookiesByUrl(url: String): String? {
        return if (CookieManager.getInstance() == null) null else CookieManager.getInstance().getCookie(url)
    }

    @JvmStatic
    fun debug() {
        AgentWebConfig.DEBUG = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true)
        }
    }

    //Android  4.4  NoSuchMethodError: android.webkit.CookieManager.removeAllCookies
    @JvmStatic
    fun removeAllCookies(cb: ValueCallback<Boolean>?) {
        var callback = cb
        if (callback == null) {
            callback = getDefaultIgnoreCallback()
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().removeAllCookie()
            toSyncCookies()
            callback.onReceiveValue(!CookieManager.getInstance().hasCookies())
            return
        }
        CookieManager.getInstance().removeAllCookies(callback)
        toSyncCookies()
    }

    @Synchronized
    @JvmStatic
    fun initCookiesManager(context: Context) {
        if (!AgentWebConfig.IS_INITIALIZED) {
            createCookiesSyncInstance(context)
            AgentWebConfig.IS_INITIALIZED = true
        }
    }

    private fun createCookiesSyncInstance(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.createInstance(context)
        }
    }

    private fun toSyncCookies() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.getInstance().sync()
            return
        }
        AsyncTask.THREAD_POOL_EXECUTOR.execute { CookieManager.getInstance().flush() }
    }

    private fun getDefaultIgnoreCallback(): ValueCallback<Boolean> {
        return ValueCallback {
            Timber.i("removeExpiredCookies:$it")
        }
    }
}
