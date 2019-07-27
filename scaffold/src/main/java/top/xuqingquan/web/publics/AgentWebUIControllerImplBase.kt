package top.xuqingquan.web.publics

import android.app.Activity
import android.os.Handler

class AgentWebUIControllerImplBase : AbsAgentWebUIController() {

    override fun onJsAlert(view: android.webkit.WebView, url: String, message: String) {
        delegate!!.onJsAlert(view, url, message)
    }

    override fun onJsAlert(view: com.tencent.smtt.sdk.WebView, url: String, message: String) {
        delegate!!.onJsAlert(view, url, message)
    }

    override fun onOpenPagePrompt(view: android.webkit.WebView, url: String, callback: Handler.Callback) {
        delegate!!.onOpenPagePrompt(view, url, callback)
    }

    override fun onOpenPagePrompt(view: com.tencent.smtt.sdk.WebView, url: String, callback: Handler.Callback) {
        delegate!!.onOpenPagePrompt(view, url, callback)
    }

    override fun onJsConfirm(
        view: android.webkit.WebView,
        url: String,
        message: String,
        jsResult: android.webkit.JsResult
    ) {
        delegate!!.onJsConfirm(view, url, message, jsResult)
    }

    override fun onJsConfirm(
        view: com.tencent.smtt.sdk.WebView,
        url: String,
        message: String,
        jsResult: com.tencent.smtt.export.external.interfaces.JsResult
    ) {
        delegate!!.onJsConfirm(view, url, message, jsResult)
    }

    override fun onSelectItemsPrompt(
        view: android.webkit.WebView,
        url: String,
        ways: Array<String>,
        callback: Handler.Callback
    ) {
        delegate!!.onSelectItemsPrompt(view, url, ways, callback)
    }

    override fun onSelectItemsPrompt(
        view: com.tencent.smtt.sdk.WebView,
        url: String,
        ways: Array<String>,
        callback: Handler.Callback
    ) {
        delegate!!.onSelectItemsPrompt(view, url, ways, callback)
    }

    override fun onForceDownloadAlert(url: String, callback: Handler.Callback) {
        delegate!!.onForceDownloadAlert(url, callback)
    }

    override fun onJsPrompt(
        view: android.webkit.WebView,
        url: String,
        message: String,
        defaultValue: String,
        jsPromptResult: android.webkit.JsPromptResult
    ) {
        delegate!!.onJsPrompt(view, url, message, defaultValue, jsPromptResult)
    }

    override fun onJsPrompt(
        view: com.tencent.smtt.sdk.WebView,
        url: String,
        message: String,
        defaultValue: String,
        jsPromptResult: com.tencent.smtt.export.external.interfaces.JsPromptResult
    ) {
        delegate!!.onJsPrompt(view, url, message, defaultValue, jsPromptResult)
    }

    override fun onMainFrameError(
        view: android.webkit.WebView,
        errorCode: Int,
        description: String,
        failingUrl: String
    ) {
        delegate!!.onMainFrameError(view, errorCode, description, failingUrl)
    }

    override fun onMainFrameError(
        view: com.tencent.smtt.sdk.WebView,
        errorCode: Int,
        description: String,
        failingUrl: String
    ) {
        delegate!!.onMainFrameError(view, errorCode, description, failingUrl)
    }

    override fun onShowMainFrame() {
        delegate!!.onShowMainFrame()
    }

    override fun onLoading(msg: String) {
        delegate!!.onLoading(msg)
    }

    override fun onCancelLoading() {
        delegate!!.onCancelLoading()
    }


    override fun onShowMessage(message: String, intent: String) {
        delegate!!.onShowMessage(message, intent)
    }

    override fun onPermissionsDeny(permissions: Array<String>, permissionType: String, action: String) {
        delegate!!.onPermissionsDeny(permissions, permissionType, action)
    }

    override fun bindSupportWebParent(webParentLayout: WebParentLayout, activity: Activity) {
        delegate!!.bindSupportWebParent(webParentLayout, activity)
    }

    companion object {

        @JvmStatic
        fun build(): AbsAgentWebUIController {
            return AgentWebUIControllerImplBase()
        }
    }


}
