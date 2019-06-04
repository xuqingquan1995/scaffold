package top.xuqingquan.web.x5

import android.net.Uri
import androidx.annotation.MainThread
import top.xuqingquan.web.Extra
import top.xuqingquan.web.agent.DownloadListener

open class DownloadListener :DownloadListener() {

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

    @MainThread
    open fun onResult(throwable: Throwable, path: Uri, url: String, extra: Extra): Boolean {
        return false
    }
}
