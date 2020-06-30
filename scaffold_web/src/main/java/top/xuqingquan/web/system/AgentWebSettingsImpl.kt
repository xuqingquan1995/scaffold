package top.xuqingquan.web.system

import android.os.Handler
import android.webkit.DownloadListener
import android.webkit.WebView
import top.xuqingquan.web.AgentWeb
import top.xuqingquan.web.publics.AgentWebUtils.getAgentWebUIControllerByWebView
import top.xuqingquan.web.utils.LogUtils
import top.xuqingquan.web.utils.download

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
                val activity = mAgentWeb!!.activity
                listener = DefaultDownloadImpl.create(
                    activity, webView, mAgentWeb!!.permissionInterceptor
                )
            }
        } catch (t: Throwable) {
            LogUtils.e(t)
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
                    val uiController = getAgentWebUIControllerByWebView(webView)
                    if (uiController != null) {
                        uiController.onDownloadPrompt(fileName, Handler.Callback {
                            download(mContext, fileName, url)
                            return@Callback true
                        })
                    } else {
                        download(mContext, fileName, url)
                    }
                }
            } catch (tt: Throwable) {
                LogUtils.e(tt)
            }
        }
        return super.setDownloader(webView, listener)
    }
}
