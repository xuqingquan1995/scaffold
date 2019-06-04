package top.xuqingquan.web

import android.net.Uri
import androidx.annotation.MainThread

open class DownloadListener {

    @MainThread
    open fun onStart(
        url: String,
        userAgent: String,
        contentDisposition: String,
        mimetype: String,
        contentLength: Long,
        extra: Extra
    ): Boolean {
        return false
    }

    open fun onProgress(url: String, downloaded: Long, length: Long, usedTime: Long) {}

    @MainThread
    open fun onResult(throwable: Throwable, path: Uri, url: String, extra: Extra): Boolean {
        return false
    }
}
