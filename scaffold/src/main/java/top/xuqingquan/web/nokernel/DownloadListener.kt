package top.xuqingquan.web.nokernel

import android.net.Uri
import androidx.annotation.MainThread
import top.xuqingquan.web.publics.Extra

/**
 * Created by 许清泉 on 2019-06-19 23:24
 */
class DownloadListener {
    @MainThread
    fun onStart(
        url: String,
        userAgent: String,
        contentDisposition: String,
        mimetype: String,
        contentLength: Long,
        extra: Extra
    ) = false

    fun onProgress(url: String, downloaded: Long, length: Long, usedTime: Long) {}

    @MainThread
    fun onResult(throwable: Throwable, path: Uri, url: String, extra: Extra) = false
}