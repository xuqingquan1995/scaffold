package top.xuqingquan.web.utils

import android.util.Log

/**
 * Create by 许清泉 on 2020/6/30 21:12
 */
object LogUtils {
    private const val TAG = "Scaffold_Web"

    @JvmStatic
    fun v(msg: String) {
        Log.v(TAG, msg)
    }

    @JvmStatic
    fun d(msg: String) {
        Log.d(TAG, msg)
    }

    @JvmStatic
    fun i(msg: String) {
        Log.i(TAG, msg)
    }

    @JvmStatic
    fun w(msg: String) {
        Log.w(TAG, msg)
    }

    @JvmStatic
    fun e(msg: String) {
        Log.e(TAG, msg)
    }

    @JvmStatic
    fun e(t: Throwable) {
        Log.e(TAG, "", t)
    }

    @JvmStatic
    fun e(t: Throwable, msg: String? = "", vararg args: Any?) {
        Log.e(TAG, String.format(msg ?: "", args), t)
    }

}