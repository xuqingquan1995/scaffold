package top.xuqingquan.web.x5

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.ViewGroup
import com.tencent.smtt.sdk.ValueCallback
import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebView
import top.xuqingquan.R
import top.xuqingquan.utils.FileUtils
import top.xuqingquan.utils.Timber
import top.xuqingquan.web.AbsAgentWebUIController
import top.xuqingquan.web.WebParentLayout
import top.xuqingquan.web.agent.AgentWebConfig
import top.xuqingquan.web.agent.PermissionInterceptor
import java.io.File

object X5WebUtils {

    @JvmStatic
    fun clearWebView(m: WebView?) {
        if (m == null) {
            return
        }
        if (Looper.myLooper() != Looper.getMainLooper()) {
            return
        }
        m.loadUrl("about:blank")
        m.stopLoading()
        if (m.handler != null) {
            m.handler.removeCallbacksAndMessages(null)
        }
        m.removeAllViews()
        val mViewGroup: ViewGroup = m.parent as ViewGroup
        mViewGroup.removeView(m)
        m.webChromeClient = null
        m.webViewClient = null
        m.tag = null
        m.clearHistory()
        m.destroy()
    }

    @JvmStatic
    fun clearWebViewAllCache(context: Context, webView: WebView) {
        try {
            X5WebConfig.removeAllCookies(null)
            webView.settings.cacheMode = android.webkit.WebSettings.LOAD_NO_CACHE
            context.deleteDatabase("webviewCache.db")
            context.deleteDatabase("webview.db")
            webView.clearCache(true)
            webView.clearHistory()
            webView.clearFormData()
            FileUtils.clearCacheFolder(File(AgentWebConfig.getCachePath(context)), 0)
        } catch (e: Exception) {
            Timber.e(e)
        }

    }

    @JvmStatic
    fun clearWebViewAllCache(context: Context) {
        try {
            clearWebViewAllCache(context, WebView(context.applicationContext))
        } catch (e: Exception) {
            Timber.e(e)
        }

    }


    @Deprecated("")
    @JvmStatic
    fun getUIControllerAndShowMessage(activity: Activity?, message: String, from: String) {
        if (activity == null || activity.isFinishing) {
            return
        }
        val mWebParentLayout = activity.findViewById<WebParentLayout>(R.id.web_parent_layout_id)
        val mAgentWebUIController = mWebParentLayout.provide()
        mAgentWebUIController?.onShowMessage(message, from)
    }

    @JvmStatic
    fun getAgentWebUIControllerByWebView(webView: WebView): AbsAgentWebUIController {
        val mWebParentLayout = getWebParentLayoutByWebView(webView)
        return mWebParentLayout.provide()
    }

    private fun getWebParentLayoutByWebView(webView: WebView): WebParentLayout {
        var mViewGroup: ViewGroup?
        if (webView.parent !is ViewGroup) {
            throw IllegalStateException("please check webcreator's create method was be called ?")
        }
        mViewGroup = webView.parent as ViewGroup
        while (mViewGroup != null) {

            Timber.i("ViewGroup:$mViewGroup")
            if (mViewGroup.id == R.id.web_parent_layout_id) {
                val mWebParentLayout = mViewGroup as WebParentLayout
                Timber.i("found WebParentLayout")
                return mWebParentLayout
            } else {
                val mViewParent = mViewGroup.parent
                if (mViewParent is ViewGroup) {
                    mViewGroup = mViewParent
                } else {
                    mViewGroup = null
                }
            }
        }
        throw IllegalStateException("please check webcreator's create method was be called ?")
    }

    @JvmStatic
    fun showFileChooserCompat(
        activity: Activity,
        webView: WebView,
        valueCallbacks: ValueCallback<Array<Uri>>?,
        fileChooserParams: WebChromeClient.FileChooserParams?,
        permissionInterceptor: PermissionInterceptor,
        valueCallback: ValueCallback<Uri>?,
        mimeType: String,
        jsChannelCallback: Handler.Callback?
    ): Boolean {
        try {
            val builder = FileChooser.newBuilder(activity, webView)
            if (valueCallbacks != null) {
                builder.setUriValueCallbacks(valueCallbacks)
            }
            if (fileChooserParams != null) {
                builder.setFileChooserParams(fileChooserParams)
            }
            if (valueCallback != null) {
                builder.setUriValueCallback(valueCallback)
            }
            if (!TextUtils.isEmpty(mimeType)) {
                builder.setAcceptType(mimeType)
            }
            if (jsChannelCallback != null) {
                builder.setJsChannelCallback(jsChannelCallback)
            }
            builder.setPermissionInterceptor(permissionInterceptor)
            val fileChooser = builder.build()
            fileChooser.openFileChooser()
        } catch (throwable: Throwable) {
            Timber.e(throwable)
            if (valueCallbacks != null) {
                Timber.i("onReceiveValue empty")
                return false
            }
            valueCallback?.onReceiveValue(null)
        }
        return true
    }
}
