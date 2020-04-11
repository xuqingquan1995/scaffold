@file:JvmName("DeviceUtils")

package top.xuqingquan.utils

import android.Manifest
import android.content.*
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import top.xuqingquan.app.ScaffoldFileProvider
import top.xuqingquan.utils.anko.share
import java.io.File

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

//获取屏幕相关参数
fun getDisplayMetrics(context: Context): DisplayMetrics {
    val displaymetrics = DisplayMetrics()
    (context.getSystemService(
        Context.WINDOW_SERVICE
    ) as WindowManager).defaultDisplay.getMetrics(
        displaymetrics
    )
    return displaymetrics
}

/**
 * 屏幕高度
 *
 * @param context
 * @return
 */
fun getScreenHeight(context: Context): Int {
    return getDisplayMetrics(context).heightPixels
}

/**
 * 屏幕宽度
 *
 * @param context
 * @return
 */
fun getScreenWidth(context: Context): Int {
    return getDisplayMetrics(context).widthPixels
}

/**
 * 安装应用
 *
 * @param context
 * @param file
 */
fun installAPK(context: Context, file: File?) {
    if (file == null || !file.exists()) return
    val intent = Intent(Intent.ACTION_VIEW)
    // 由于没有在Activity环境下启动Activity,设置下面的标签
    // 由于没有在Activity环境下启动Activity,设置下面的标签
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        val contentUri = ScaffoldFileProvider.getUriForFile(
            context, context.packageName + ".ScaffoldFileProvider", file
        )
        Timber.d("installApk: $contentUri")
        intent.setDataAndType(contentUri, "application/vnd.android.package-archive")
    } else {
        intent.setDataAndType(
            Uri.fromFile(file),
            "application/vnd.android.package-archive"
        )
    }
    context.startActivity(intent)
}


/**
 * 获取IMEI
 */
@RequiresPermission(Manifest.permission.READ_PHONE_STATE)
fun getIMEI(context: Context): String? {
    return if (hasPermission(context, Manifest.permission.READ_PHONE_STATE)) {
        val tel = ContextCompat.getSystemService(context, TelephonyManager::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            tel?.imei
        } else {
            tel?.deviceId
        }
    } else {
        null
    }
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