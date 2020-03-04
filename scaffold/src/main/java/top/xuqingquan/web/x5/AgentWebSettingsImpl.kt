package top.xuqingquan.web.x5

import android.app.DownloadManager
import android.net.Uri
import androidx.core.content.ContextCompat
import com.tencent.smtt.sdk.DownloadListener
import com.tencent.smtt.sdk.WebView
import top.xuqingquan.utils.Timber
import top.xuqingquan.utils.getActivityByContext
import top.xuqingquan.utils.getCacheFilePath
import top.xuqingquan.web.AgentWeb
import java.io.File

class AgentWebSettingsImpl : AbsAgentWebSettings() {
    private var mAgentWeb: AgentWeb? = null

    override fun bindAgentWebSupport(agentWeb: AgentWeb) {
        this.mAgentWeb = agentWeb
    }

    override fun setDownloader(
        webView: WebView?, downloadListener: DownloadListener?
    ): WebListenerManager {
        if (webView == null) {
            return super.setDownloader(webView, downloadListener)
        }
        var listener = downloadListener
        try {
            Class.forName("com.download.library.DownloadTask")//如果有依赖下载库则使用下载库，否则使用系统的
            if (mAgentWeb != null) {
                // Fix Android 5.1 crashing: ClassCastException: android.app.ContextImpl cannot be cast to android.app.Activity
                val activity = getActivityByContext(webView.context)
                listener = DefaultDownloadImpl.create(
                    activity!!, webView, mAgentWeb!!.permissionInterceptor
                )
            }
        } catch (t: Throwable) {
            Timber.e(t)
            try {
                listener = DownloadListener { url, _, _, _, _ ->
                    val fileName = if (url.contains("?")) {
                        url.substring(url.lastIndexOf("/") + 1, url.indexOf("?"))
                    } else {
                        url.substring(url.lastIndexOf("/") + 1)
                    }
                    val downloadPath = getCacheFilePath(webView.context)
                    val downloadFile = File(downloadPath, fileName)
                    if (downloadFile.exists()) {
                        return@DownloadListener
                    }
                    val downloadManager =
                        ContextCompat.getSystemService(webView.context, DownloadManager::class.java)
                    if (downloadManager != null) {
                        val request = DownloadManager.Request(Uri.parse(url))
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                        request.setDestinationInExternalFilesDir(
                            webView.context,
                            "/",
                            fileName
                        )
                        downloadManager.enqueue(request)
                    }
                }
            } catch (tt: Throwable) {
                Timber.e(tt)
            }
        }
        return super.setDownloader(webView, listener)
    }
}
