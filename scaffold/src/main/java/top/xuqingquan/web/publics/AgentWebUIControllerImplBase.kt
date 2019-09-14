package top.xuqingquan.web.publics

import android.app.Activity
import android.os.Handler

class AgentWebUIControllerImplBase : AbsAgentWebUIController() {

    override fun onJsAlert(view: android.webkit.WebView, url: String, message: String) {
        getDelegate().onJsAlert(view, url, message)
    }

    override fun onJsAlert(view: com.tencent.smtt.sdk.WebView, url: String, message: String) {
        getDelegate().onJsAlert(view, url, message)
    }

    override fun onOpenPagePrompt(view: android.webkit.WebView, url: String, callback: Handler.Callback) {
        getDelegate().onOpenPagePrompt(view, url, callback)
    }

    override fun onOpenPagePrompt(view: com.tencent.smtt.sdk.WebView, url: String, callback: Handler.Callback) {
        getDelegate().onOpenPagePrompt(view, url, callback)
    }

    override fun onJsConfirm(
        view: android.webkit.WebView,
        url: String,
        message: String,
        jsResult: android.webkit.JsResult
    ) {
        getDelegate().onJsConfirm(view, url, message, jsResult)
    }

    override fun onJsConfirm(
        view: com.tencent.smtt.sdk.WebView,
        url: String,
        message: String,
        jsResult: com.tencent.smtt.export.external.interfaces.JsResult
    ) {
        getDelegate().onJsConfirm(view, url, message, jsResult)
    }

    override fun onSelectItemsPrompt(
        view: android.webkit.WebView,
        url: String,
        ways: Array<String>,
        callback: Handler.Callback
    ) {
        getDelegate().onSelectItemsPrompt(view, url, ways, callback)
    }

    override fun onSelectItemsPrompt(
        view: com.tencent.smtt.sdk.WebView,
        url: String,
        ways: Array<String>,
        callback: Handler.Callback
    ) {
        getDelegate().onSelectItemsPrompt(view, url, ways, callback)
    }

    override fun onForceDownloadAlert(url: String, callback: Handler.Callback) {
        getDelegate().onForceDownloadAlert(url, callback)
    }

    override fun onJsPrompt(
        view: android.webkit.WebView,
        url: String,
        message: String,
        defaultValue: String,
        jsPromptResult: android.webkit.JsPromptResult
    ) {
        getDelegate().onJsPrompt(view, url, message, defaultValue, jsPromptResult)
    }

    override fun onJsPrompt(
        view: com.tencent.smtt.sdk.WebView,
        url: String,
        message: String,
        defaultValue: String,
        jsPromptResult: com.tencent.smtt.export.external.interfaces.JsPromptResult
    ) {
        getDelegate().onJsPrompt(view, url, message, defaultValue, jsPromptResult)
    }

    override fun onMainFrameError(
        view: android.webkit.WebView,
        errorCode: Int,
        description: String,
        failingUrl: String
    ) {
        getDelegate().onMainFrameError(view, errorCode, description, failingUrl)
    }

    override fun onMainFrameError(
        view: com.tencent.smtt.sdk.WebView,
        errorCode: Int,
        description: String,
        failingUrl: String
    ) {
        getDelegate().onMainFrameError(view, errorCode, description, failingUrl)
    }

    override fun onShowMainFrame() {
        getDelegate().onShowMainFrame()
    }

    override fun onLoading(msg: String) {
        getDelegate().onLoading(msg)
    }

    override fun onCancelLoading() {
        getDelegate().onCancelLoading()
    }


    override fun onShowMessage(message: String, intent: String) {
        getDelegate().onShowMessage(message, intent)
    }

    override fun onPermissionsDeny(permissions: Array<String>, permissionType: String, action: String) {
        getDelegate().onPermissionsDeny(permissions, permissionType, action)
    }

    override fun bindSupportWebParent(webParentLayout: WebParentLayout, activity: Activity) {
        getDelegate().bindSupportWebParent(webParentLayout, activity)
    }

    companion object {

        @JvmStatic
        fun build(): AbsAgentWebUIController {
            return AgentWebUIControllerImplBase()
        }
    }


}
