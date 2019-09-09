@file:JvmName("DeviceUtils")
package top.xuqingquan.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.text.TextUtils
import androidx.core.content.ContextCompat
import org.jetbrains.anko.share

/**
 * Created by 许清泉 on 2019-04-29 23:28
 */

/**
 * 获取版本号
 *
 * @param context
 * @return
 */
fun getVersionCode(context: Context): Long {
    return try {
        val packageInfo = context.packageManager
            .getPackageInfo(
                context.packageName,
                0
            )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageInfo.longVersionCode
        } else {
            @Suppress("DEPRECATION")
            packageInfo.versionCode.toLong()
        }
    } catch (ex: Throwable) {
        0
    }
}

/**
 * 复制到剪贴板
 * @param context 上下文
 * @param string 要复制到内容
 */
fun copyTextToBoard(context: Context, string: String, lable: String? = null) {
    if (TextUtils.isEmpty(string)) {
        return
    }
    val clip =
        ContextCompat.getSystemService(context.applicationContext, ClipboardManager::class.java)
    @Suppress("UsePropertyAccessSyntax")
    clip?.setPrimaryClip(ClipData.newPlainText(lable, string))
}

/**
 * 调用系统安装了的应用分享
 *
 * @param context 上下文
 * @param title 标题
 * @param url url
 */
fun showSystemShareOption(context: Context, title: String, url: String) {
    context.share("$title $url", title)
}

//获取应用的名称
fun getApplicationName(context: Context): String? {
    return try {
        val packageManager = context.applicationContext.packageManager
        val applicationInfo = packageManager!!.getApplicationInfo(context.packageName, 0)
        packageManager.getApplicationLabel(applicationInfo).toString()
    } catch (e: PackageManager.NameNotFoundException) {
        ""
    }
}