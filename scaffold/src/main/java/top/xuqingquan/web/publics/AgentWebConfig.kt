package top.xuqingquan.web.publics

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import android.os.Build
import top.xuqingquan.utils.Timber
import top.xuqingquan.web.nokernel.WebConfig.DEBUG
import top.xuqingquan.web.nokernel.WebConfig.IS_INITIALIZED
import top.xuqingquan.web.nokernel.WebConfig.hasX5

@Suppress("DEPRECATION")
@SuppressLint("ObsoleteSdkInt")
object AgentWebConfig {

    @JvmStatic
    fun debug() {
        DEBUG = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (hasX5()) {
                com.tencent.smtt.sdk.WebView.setWebContentsDebuggingEnabled(true)
            } else {
                android.webkit.WebView.setWebContentsDebuggingEnabled(true)
            }
        }
    }

    //获取Cookie
    @JvmStatic
    fun getCookiesByUrl(url: String): String? {
        return if (hasX5()) {
            if (com.tencent.smtt.sdk.CookieManager.getInstance() == null) {
                null
            } else {
                com.tencent.smtt.sdk.CookieManager.getInstance().getCookie(url)
            }
        } else {
            if (android.webkit.CookieManager.getInstance() == null) {
                null
            } else {
                android.webkit.CookieManager.getInstance().getCookie(url)
            }
        }
    }

    @JvmStatic
    fun removeAllCookies(callback_o: android.webkit.ValueCallback<Boolean>?) {
        var callback = callback_o
        if (callback == null) {
            callback = getDefaultIgnoreCallback()
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            android.webkit.CookieManager.getInstance().removeAllCookie()
            toSyncCookies()
            callback.onReceiveValue(!android.webkit.CookieManager.getInstance().hasCookies())
            return
        }
        android.webkit.CookieManager.getInstance().removeAllCookies(callback)
        toSyncCookies()
    }

    @JvmStatic
    fun removeAllX5Cookies(callback_o: com.tencent.smtt.sdk.ValueCallback<Boolean>?) {
        var callback = callback_o
        if (callback == null) {
            callback = getX5DefaultIgnoreCallback()
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            com.tencent.smtt.sdk.CookieManager.getInstance().removeAllCookie()
            toSyncCookies()
            callback.onReceiveValue(!com.tencent.smtt.sdk.CookieManager.getInstance().hasCookies())
            return
        }
        com.tencent.smtt.sdk.CookieManager.getInstance().removeAllCookies(callback)
        toSyncCookies()
    }

    @JvmStatic
    @Synchronized
    fun initCookiesManager(context: Context) {
        if (!IS_INITIALIZED) {
            createCookiesSyncInstance(context)
            IS_INITIALIZED = true
        }
    }

    private fun createCookiesSyncInstance(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            if (hasX5()) {
                com.tencent.smtt.sdk.CookieSyncManager.createInstance(context)
            } else {
                android.webkit.CookieSyncManager.createInstance(context)
            }
        }
    }

    private fun toSyncCookies() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            if (hasX5()) {
                com.tencent.smtt.sdk.CookieSyncManager.getInstance().sync()
            } else {
                android.webkit.CookieSyncManager.getInstance().sync()
            }
            return
        }
        if (hasX5()) {
            AsyncTask.THREAD_POOL_EXECUTOR.execute {
                com.tencent.smtt.sdk.CookieManager.getInstance().flush()
            }
        } else {
            AsyncTask.THREAD_POOL_EXECUTOR.execute {
                android.webkit.CookieManager.getInstance().flush()
            }
        }
    }

    private fun getDefaultIgnoreCallback(): android.webkit.ValueCallback<Boolean> {
        return android.webkit.ValueCallback {
            Timber.i("removeExpiredCookies:$it")
        }
    }

    private fun getX5DefaultIgnoreCallback(): com.tencent.smtt.sdk.ValueCallback<Boolean> {
        return com.tencent.smtt.sdk.ValueCallback {
            Timber.i("removeExpiredCookies:$it")
        }
    }
}
