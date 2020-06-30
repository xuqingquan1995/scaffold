@file:JvmName("FileProviderUtils")
package top.xuqingquan.utils

import android.content.Context
import android.net.Uri
import android.os.Build
import top.xuqingquan.app.ScaffoldFileProvider
import java.io.File

/**
 * Created by 许清泉 on 2019/4/14 21:49
 */
/**
 * 获取文件uri
 */
fun getUriFromFile(context: Context, file: File): Uri {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        ScaffoldFileProvider.getUriForFile(context, context.packageName + ".ScaffoldFileProvider", file)
    } else {
        Uri.fromFile(file)
    }
}