@file:JvmName("SystemUtils")

package top.xuqingquan.utils

import android.app.Activity
import android.content.*
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import androidx.core.content.ContextCompat
import top.xuqingquan.app.ScaffoldFileProvider
import java.io.File

/**
 * Create by 许清泉 on 2020/6/23 22:58
 */

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
    try {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, title)
        intent.putExtra(Intent.EXTRA_TEXT, "$title $url")
        if (context !is Activity) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(Intent.createChooser(intent, null))
    } catch (e: ActivityNotFoundException) {
        e.printStackTrace()
    }
}

/**
 * 安装应用
 *
 * @param context
 * @param file
 */
fun installAPK(context: Context, file: File?) {
    if (file == null || !file.exists()) return
    val type = "application/vnd.android.package-archive"
    val intent = Intent(Intent.ACTION_VIEW)
    // 由于没有在Activity环境下启动Activity,设置下面的标签
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        val contentUri = ScaffoldFileProvider.getUriForFile(
            context, context.packageName + ".ScaffoldFileProvider", file
        )
        Timber.d("installApk: $contentUri")
        intent.setDataAndType(contentUri, type)
    } else {
        intent.setDataAndType(Uri.fromFile(file), type)
    }
    context.startActivity(intent)
}

/**
 * 打开某个App
 */
fun openApp(context: Context, packageName: String) {
    var mainIntent = context.packageManager.getLaunchIntentForPackage(packageName)
    if (mainIntent == null) {
        mainIntent = Intent(packageName)
    }
    context.startActivity(mainIntent)
}

/**
 * 打开某个App的某个Activity
 */
fun openAppActivity(context: Context, packageName: String, activityName: String): Boolean {
    val intent = Intent(Intent.ACTION_MAIN)
    intent.addCategory(Intent.CATEGORY_LAUNCHER)
    val cn = ComponentName(packageName, activityName)
    intent.component = cn
    return try {
        context.startActivity(intent)
        true
    } catch (e: Exception) {
        false
    }
}