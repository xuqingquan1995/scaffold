package top.xuqingquan.web.publics;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import androidx.annotation.Nullable;
import com.tencent.smtt.sdk.QbSdk;
import top.xuqingquan.utils.Timber;

import java.io.File;

public class AgentWebConfig {
    /**
     * 直接打开其他页面
     */
    public static final int DERECT_OPEN_OTHER_PAGE = 1001;
    /**
     * 弹窗咨询用户是否前往其他页面
     */
    public static final int ASK_USER_OPEN_OTHER_PAGE = DERECT_OPEN_OTHER_PAGE >> 2;
    /**
     * 不允许打开其他页面
     */
    public static final int DISALLOW_OPEN_OTHER_APP = DERECT_OPEN_OTHER_PAGE >> 4;

    public static final String FILE_CACHE_PATH = "agentweb-cache";
    private static final String AGENTWEB_CACHE_PATCH = File.separator + "agentweb-cache";
    /**
     * 缓存路径
     */
    public static String AGENTWEB_FILE_PATH;
    /**
     * DEBUG 模式 ， 如果需要查看日志请设置为 true
     */
    public static boolean DEBUG = false;
    /**
     * 当前操作系统是否低于 KITKAT
     */
    public static final boolean IS_KITKAT_OR_BELOW_KITKAT = Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT;
    /**
     * 默认 WebView  类型 。
     */
    public static final int WEBVIEW_DEFAULT_TYPE = 1;
    /**
     * 使用 AgentWebView
     */
    public static final int WEBVIEW_AGENTWEB_SAFE_TYPE = 2;
    /**
     * 自定义 WebView
     */
    public static final int WEBVIEW_CUSTOM_TYPE = 3;
    public static int WEBVIEW_TYPE = WEBVIEW_DEFAULT_TYPE;
    private static volatile boolean IS_INITIALIZED = false;
    /**
     * AgentWeb 的版本
     */
    public static final String AGENTWEB_VERSION = " agentweb/4.0.2 ";
    /**
     * 通过JS获取的文件大小， 这里限制最大为5MB ，太大会抛出 OutOfMemoryError
     */
    public static int MAX_FILE_LENGTH = 1024 * 1024 * 5;

    public static void debug() {
        DEBUG = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (hasX5()) {
                com.tencent.smtt.sdk.WebView.setWebContentsDebuggingEnabled(true);
            } else {
                android.webkit.WebView.setWebContentsDebuggingEnabled(true);
            }
        }
    }

    /**
     * @param context
     * @return WebView 的缓存路径
     */
    public static String getCachePath(Context context) {
        return context.getCacheDir().getAbsolutePath() + AGENTWEB_CACHE_PATCH;
    }

    public static void removeAllCookies(@Nullable android.webkit.ValueCallback<Boolean> callback) {
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

    public static void removeAllCookies(@Nullable com.tencent.smtt.sdk.ValueCallback<Boolean> callback) {
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

    public static Boolean x5 = null;

    public static boolean hasX5() {
        if (x5 != null) {
            return x5;
        }
        try {
            Class.forName("com.tencent.smtt.sdk.WebView");
            x5 = QbSdk.isTbsCoreInited();
        } catch (Exception e) {
            Timber.e(e);
            x5 = false;
        }
        return x5;
    }
}
