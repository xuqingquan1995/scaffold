package top.xuqingquan.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.text.TextUtils
import androidx.core.content.ContextCompat

/**
 * Created by 许清泉 on 2019-04-29 23:28
 */
object DeviceUtils {

    /**
     * 获取版本号
     *
     * @param context
     * @return
     */
    @JvmStatic
    fun getVersionCode(context: Context): Long {
        return try {
            context.packageManager
                .getPackageInfo(
                    context.packageName,
                    0
                ).longVersionCode
        } catch (ex: Throwable) {
            0
        }
    }

    /**
     * 复制到剪贴板
     * @param context 上下文
     * @param string 要复制到内容
     */
    @JvmStatic
    fun copyTextToBoard(context: Context, string: String, lable: String? = null) {
        if (TextUtils.isEmpty(string)) {
            return
        }
        val clip = ContextCompat.getSystemService(context.applicationContext, ClipboardManager::class.java)
        clip?.setPrimaryClip(ClipData.newPlainText(lable, string))
    }

    /**
     * 调用系统安装了的应用分享
     *
     * @param context 上下文
     * @param title 标题
     * @param url url
     */
    @JvmStatic
    fun showSystemShareOption(context: Context, title: String, url: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, title)
        intent.putExtra(Intent.EXTRA_TEXT, "$title $url")
        context.applicationContext.startActivity(Intent.createChooser(intent, "选择分享"))
    }

    //获取应用的名称
    @JvmStatic
    fun getApplicationName(context: Context): String? {
        return try {
            val packageManager = context.applicationContext.packageManager
            val applicationInfo = packageManager!!.getApplicationInfo(context.packageName, 0)
            packageManager.getApplicationLabel(applicationInfo).toString()
        } catch (e: PackageManager.NameNotFoundException) {
            ""
        }
    }

}