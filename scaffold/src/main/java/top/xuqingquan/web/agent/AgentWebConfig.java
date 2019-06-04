package top.xuqingquan.web.agent;

import android.content.Context;
import android.os.Build;

import java.io.File;

/**
 * Created by 许清泉 on 2019-06-04 23:47
 */
public abstract class AgentWebConfig {

    public static final String FILE_CACHE_PATH = "scaffold-cache";
    private static final String AGENTWEB_CACHE_PATCH = File.separator + "scaffold-cache";
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
    protected static volatile boolean IS_INITIALIZED = false;
    /**
     * AgentWeb 的版本
     */
    public static final String AGENTWEB_VERSION = " agentweb/5.0.0 ";
    /**
     * 通过JS获取的文件大小， 这里限制最大为5MB ，太大会抛出 OutOfMemoryError
     */
    public static int MAX_FILE_LENGTH = 1024 * 1024 * 5;

    /**
     * @param context
     * @return WebView 的缓存路径
     */
    public static String getCachePath(Context context) {
        return context.getCacheDir().getAbsolutePath() + AGENTWEB_CACHE_PATCH;
    }
}
