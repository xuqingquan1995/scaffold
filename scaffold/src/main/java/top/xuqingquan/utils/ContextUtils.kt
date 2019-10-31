@file:JvmName("ContextUtils")
package top.xuqingquan.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

/**
 * Created by 许清泉 on 2019-10-31 15:32
 */
/**
 * Copy from com.blankj.utilcode.util.ActivityUtils#getActivityByView
 */
fun getActivityByContext(ctx: Context): Activity? {
    var context = ctx
    if (context is Activity) return context
    while (context is ContextWrapper) {
        if (context is Activity) {
            return context
        }
        context = context.baseContext
    }
    return null
}