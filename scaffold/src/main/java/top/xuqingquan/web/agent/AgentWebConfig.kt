package top.xuqingquan.web.agent

import android.content.Context
import android.os.Build

import java.io.File

/**
 * Created by 许清泉 on 2019-06-04 23:47
 */
object AgentWebConfig {
    val FILE_CACHE_PATH = "scaffold-cache"
    private val AGENTWEB_CACHE_PATCH = File.separator + "scaffold-cache"
    /**
     * 缓存路径
     */
    var AGENTWEB_FILE_PATH: String? = null
    /**
     * DEBUG 模式 ， 如果需要查看日志请设置为 true
     */
    @JvmField
    var DEBUG = false
    /**
     * 当前操作系统是否低于 KITKAT
     */
    @JvmField
    val IS_KITKAT_OR_BELOW_KITKAT = Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT
    /**
     * 默认 WebView  类型 。
     */
    @JvmField
    val WEBVIEW_DEFAULT_TYPE = 1
    /**
     * 使用 AgentWebView
     */
    @JvmField
    val WEBVIEW_AGENTWEB_SAFE_TYPE = 2
    /**
     * 自定义 WebView
     */
    @JvmField
    val WEBVIEW_CUSTOM_TYPE = 3
    @JvmField
    var WEBVIEW_TYPE = WEBVIEW_DEFAULT_TYPE
    @Volatile
    var IS_INITIALIZED = false
    /**
     * AgentWeb 的版本
     */
    @JvmField
    val AGENTWEB_VERSION = " agentweb/5.0.0 "
    /**
     * 通过JS获取的文件大小， 这里限制最大为5MB ，太大会抛出 OutOfMemoryError
     */
    @JvmField
    var MAX_FILE_LENGTH = 1024 * 1024 * 5

    /**
     * @param context
     * @return WebView 的缓存路径
     */
    @JvmStatic
    fun getCachePath(context: Context): String {
        return context.cacheDir.absolutePath + AGENTWEB_CACHE_PATCH
    }
}
