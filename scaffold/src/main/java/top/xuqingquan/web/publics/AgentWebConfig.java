package top.xuqingquan.web.publics;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.webkit.CookieManager;
import androidx.annotation.Nullable;
import top.xuqingquan.utils.Timber;

import top.xuqingquan.web.nokernel.WebConfig;

import static top.xuqingquan.web.nokernel.WebConfig.*;

public class AgentWebConfig {

    public static void debug() {
        DEBUG = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (WebConfig.hasX5()) {
                com.tencent.smtt.sdk.WebView.setWebContentsDebuggingEnabled(true);
            } else {
                android.webkit.WebView.setWebContentsDebuggingEnabled(true);
            }
        }
    }

    //获取Cookie
    public static String getCookiesByUrl(String url) {
        if (WebConfig.hasX5()) {
            return com.tencent.smtt.sdk.CookieManager.getInstance() == null ? null : CookieManager.getInstance().getCookie(url);
        } else {
            return android.webkit.CookieManager.getInstance() == null ? null : CookieManager.getInstance().getCookie(url);
        }
    }

    static void removeAllCookies(@Nullable android.webkit.ValueCallback<Boolean> callback) {
        if (callback == null) {
            callback = getDefaultIgnoreCallback();
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            android.webkit.CookieManager.getInstance().removeAllCookie();
            toSyncCookies();
            callback.onReceiveValue(!android.webkit.CookieManager.getInstance().hasCookies());
            return;
        }
        android.webkit.CookieManager.getInstance().removeAllCookies(callback);
        toSyncCookies();
    }

    static void removeAllX5Cookies(@Nullable com.tencent.smtt.sdk.ValueCallback<Boolean> callback) {
        if (callback == null) {
            callback = getX5DefaultIgnoreCallback();
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            com.tencent.smtt.sdk.CookieManager.getInstance().removeAllCookie();
            toSyncCookies();
            callback.onReceiveValue(!com.tencent.smtt.sdk.CookieManager.getInstance().hasCookies());
            return;
        }
        com.tencent.smtt.sdk.CookieManager.getInstance().removeAllCookies(callback);
        toSyncCookies();
    }

    public static synchronized void initCookiesManager(Context context) {
        if (!IS_INITIALIZED) {
            createCookiesSyncInstance(context);
            IS_INITIALIZED = true;
        }
    }

    private static void createCookiesSyncInstance(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            if (hasX5()) {
                com.tencent.smtt.sdk.CookieSyncManager.createInstance(context);
            } else {
                android.webkit.CookieSyncManager.createInstance(context);
            }
        }
    }

    private static void toSyncCookies() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            if (hasX5()) {
                com.tencent.smtt.sdk.CookieSyncManager.getInstance().sync();
            } else {
                android.webkit.CookieSyncManager.getInstance().sync();
            }
            return;
        }
        if (hasX5()) {
            AsyncTask.THREAD_POOL_EXECUTOR.execute(() -> com.tencent.smtt.sdk.CookieManager.getInstance().flush());
        } else {
            AsyncTask.THREAD_POOL_EXECUTOR.execute(() -> android.webkit.CookieManager.getInstance().flush());
        }
    }

    private static android.webkit.ValueCallback<Boolean> getDefaultIgnoreCallback() {
        return ignore -> Timber.i("removeExpiredCookies:" + ignore);
    }

    private static com.tencent.smtt.sdk.ValueCallback<Boolean> getX5DefaultIgnoreCallback() {
        return ignore -> Timber.i("removeExpiredCookies:" + ignore);
    }
}
