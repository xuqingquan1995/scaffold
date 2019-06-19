package top.xuqingquan.web.nokernel;

import android.os.Build;
import com.tencent.smtt.sdk.QbSdk;
import top.xuqingquan.utils.Timber;

import java.io.File;

/**
 * Created by 许清泉 on 2019-06-19 20:00
 */
public class WebConfig {
    private WebConfig(){}
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
    static final int DISALLOW_OPEN_OTHER_APP = DERECT_OPEN_OTHER_PAGE >> 4;
    public static final String FILE_CACHE_PATH = "agentweb-cache";
    static final String AGENTWEB_CACHE_PATCH = File.separator + "agentweb-cache";
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
    public static volatile boolean IS_INITIALIZED = false;
    /**
     * AgentWeb 的版本
     */
    public static final String AGENTWEB_VERSION = " agentweb/4.0.2 ";
    /**
     * 通过JS获取的文件大小， 这里限制最大为5MB ，太大会抛出 OutOfMemoryError
     */
    public static int MAX_FILE_LENGTH = 1024 * 1024 * 5;

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
