package top.xuqingquan.webbak.publics

import top.xuqingquan.webbak.nokernel.WebConfig
import top.xuqingquan.webbak.nokernel.WebLifeCycle

class DefaultWebLifeCycleImpl : WebLifeCycle {
    private var mWebView: android.webkit.WebView? = null
    private var mX5WebView: com.tencent.smtt.sdk.WebView? = null

    constructor(webView: android.webkit.WebView?) {
        this.mWebView = webView
    }

    constructor(webView: com.tencent.smtt.sdk.WebView?) {
        this.mX5WebView = webView
    }

    override fun onResume() {
        if (WebConfig.hasX5()) {
            mX5WebView?.let {
                it.onResume()
                it.resumeTimers()
            }
        } else {
            mWebView?.let {
                it.onResume()
                it.resumeTimers()
            }
        }
    }

    override fun onPause() {
        if (WebConfig.hasX5()) {
            mX5WebView?.let {
                it.onPause()
                it.pauseTimers()
            }
        } else {
            mWebView?.let {
                it.onPause()
                it.pauseTimers()
            }
        }
    }

    override fun onDestroy() {
        if (WebConfig.hasX5()) {
            this.mX5WebView?.resumeTimers()
            AgentWebUtils.clearWebView(this.mX5WebView)
        } else {
            this.mWebView?.resumeTimers()
            AgentWebUtils.clearWebView(this.mWebView)
        }
    }
}
