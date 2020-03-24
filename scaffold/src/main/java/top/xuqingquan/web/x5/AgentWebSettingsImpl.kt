package top.xuqingquan.web.x5

import android.app.DownloadManager
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import androidx.core.content.ContextCompat
import com.tencent.smtt.sdk.DownloadListener
import com.tencent.smtt.sdk.WebView
import top.xuqingquan.app.ScaffoldConfig
import top.xuqingquan.utils.Timber
import top.xuqingquan.utils.getActivityByContext
import top.xuqingquan.utils.getCacheFilePath
import top.xuqingquan.web.AgentWeb
import top.xuqingquan.web.nokernel.DownLoadBroadcast
import top.xuqingquan.web.nokernel.WebUtils.getCommonFileIntentCompat
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
        val mContext = webView.context
        var listener = downloadListener
        try {
            Class.forName("com.download.library.DownloadTask")//如果有依赖下载库则使用下载库，否则使用系统的
            if (mAgentWeb != null) {
                // Fix Android 5.1 crashing: ClassCastException: android.app.ContextImpl cannot be cast to android.app.Activity
                var activity = getActivityByContext(mContext)
                if (activity == null) {
                    activity = ScaffoldConfig.getAppManager().getTopActivity()
                }
                listener = DefaultDownloadImpl.create(
                    activity!!, webView, mAgentWeb!!.permissionInterceptor
                )
            }
        } catch (t: Throwable) {
            Timber.e(t)
            try {
                listener = DownloadListener { url, _, _, _, _ ->
                    val fileName = try {
                        var lastIndexOf = url.lastIndexOf("/")
                        if (lastIndexOf != url.length - 1) {//如果已经不是最后一项了
                            lastIndexOf += 1
                        }
                        if (url.contains("?")) {
                            url.substring(lastIndexOf, url.indexOf("?"))
                        } else {
                            url.substring(lastIndexOf)
                        }
                    } catch (e: Throwable) {
                        url
                    }
                    val downloadPath = getCacheFilePath(mContext)
                    val downloadFile = File(downloadPath, fileName)
                    if (downloadFile.exists()) {
                        val mIntent = getCommonFileIntentCompat(mContext, downloadFile)
                        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        mContext.startActivity(mIntent)
                        return@DownloadListener
                    }
                    val downloadManager =
                        ContextCompat.getSystemService(mContext, DownloadManager::class.java)
                    if (downloadManager != null) {
                        val request = DownloadManager.Request(Uri.parse(url))
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                        request.setDestinationInExternalFilesDir(
                            mContext,
                            "/",
                            fileName
                        )
                        downloadManager.enqueue(request)
                        val intentFilter = IntentFilter()
                        intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
                        mContext.registerReceiver(DownLoadBroadcast(), intentFilter)
                    }
                }
            } catch (tt: Throwable) {
                Timber.e(tt)
            }
        }
        return super.setDownloader(webView, listener)
    }
}
