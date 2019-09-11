package top.xuqingquan.webbak.publics

import android.view.KeyEvent
import top.xuqingquan.webbak.nokernel.EventInterceptor
import top.xuqingquan.webbak.nokernel.IEventHandler
import top.xuqingquan.webbak.nokernel.WebConfig

/**
 * IEventHandler 对事件的处理，主要是针对
 * 视屏状态进行了处理 ， 如果当前状态为 视频状态
 * 则先退出视频。
 */
class EventHandlerImpl : IEventHandler {
    private var mWebView: android.webkit.WebView? = null
    private var mX5WebView: com.tencent.smtt.sdk.WebView? = null
    private var mEventInterceptor: EventInterceptor? = null

    private constructor(webView: android.webkit.WebView?, eventInterceptor: EventInterceptor?) {
        this.mWebView = webView
        this.mEventInterceptor = eventInterceptor
    }

    private constructor(webView: com.tencent.smtt.sdk.WebView?, eventInterceptor: EventInterceptor?) {
        this.mX5WebView = webView
        this.mEventInterceptor = eventInterceptor
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK) {
            back()
        } else false
    }

    override fun back(): Boolean {
        if (this.mEventInterceptor != null && this.mEventInterceptor!!.event()) {
            return true
        }
        if (WebConfig.hasX5()) {
            if (mX5WebView != null && mX5WebView!!.canGoBack()) {
                mX5WebView!!.goBack()
                return true
            }
        } else {
            if (mWebView != null && mWebView!!.canGoBack()) {
                mWebView!!.goBack()
                return true
            }
        }
        return false
    }

    companion object {

        @JvmStatic
        fun getInstantce(view: android.webkit.WebView?, eventInterceptor: EventInterceptor?): EventHandlerImpl {
            return EventHandlerImpl(view, eventInterceptor)
        }

        @JvmStatic
        fun getInstantce(view: com.tencent.smtt.sdk.WebView?, eventInterceptor: EventInterceptor?): EventHandlerImpl {
            return EventHandlerImpl(view, eventInterceptor)
        }
    }

}
