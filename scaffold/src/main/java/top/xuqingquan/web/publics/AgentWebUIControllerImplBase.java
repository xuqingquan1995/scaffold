package top.xuqingquan.web.publics;

import android.app.Activity;
import android.os.Handler;

public class AgentWebUIControllerImplBase extends AbsAgentWebUIController {

    public static AbsAgentWebUIController build() {
        return new AgentWebUIControllerImplBase();
    }

    @Override
    public void onJsAlert(android.webkit.WebView view, String url, String message) {
        getDelegate().onJsAlert(view, url, message);
    }

    @Override
    public void onJsAlert(com.tencent.smtt.sdk.WebView view, String url, String message) {
        getDelegate().onJsAlert(view, url, message);
    }

    @Override
    public void onOpenPagePrompt(android.webkit.WebView view, String url, Handler.Callback callback) {
        getDelegate().onOpenPagePrompt(view, url, callback);
    }

    @Override
    public void onOpenPagePrompt(com.tencent.smtt.sdk.WebView view, String url, Handler.Callback callback) {
        getDelegate().onOpenPagePrompt(view, url, callback);
    }

    @Override
    public void onJsConfirm(android.webkit.WebView view, String url, String message, android.webkit.JsResult jsResult) {
        getDelegate().onJsConfirm(view, url, message, jsResult);
    }

    @Override
    public void onJsConfirm(com.tencent.smtt.sdk.WebView view, String url, String message, com.tencent.smtt.export.external.interfaces.JsResult jsResult) {
        getDelegate().onJsConfirm(view, url, message, jsResult);
    }

    @Override
    public void onSelectItemsPrompt(android.webkit.WebView view, String url, String[] ways, Handler.Callback callback) {
        getDelegate().onSelectItemsPrompt(view, url, ways, callback);
    }

    @Override
    public void onSelectItemsPrompt(com.tencent.smtt.sdk.WebView view, String url, String[] ways, Handler.Callback callback) {
        getDelegate().onSelectItemsPrompt(view, url, ways, callback);
    }

    @Override
    public void onForceDownloadAlert(String url, Handler.Callback callback) {
        getDelegate().onForceDownloadAlert(url, callback);
    }

    @Override
    public void onJsPrompt(android.webkit.WebView view, String url, String message, String defaultValue, android.webkit.JsPromptResult jsPromptResult) {
        getDelegate().onJsPrompt(view, url, message, defaultValue, jsPromptResult);
    }

    @Override
    public void onJsPrompt(com.tencent.smtt.sdk.WebView view, String url, String message, String defaultValue, com.tencent.smtt.export.external.interfaces.JsPromptResult jsPromptResult) {
        getDelegate().onJsPrompt(view, url, message, defaultValue, jsPromptResult);
    }

    @Override
    public void onMainFrameError(android.webkit.WebView view, int errorCode, String description, String failingUrl) {
        getDelegate().onMainFrameError(view, errorCode, description, failingUrl);
    }

    @Override
    public void onMainFrameError(com.tencent.smtt.sdk.WebView view, int errorCode, String description, String failingUrl) {
        getDelegate().onMainFrameError(view, errorCode, description, failingUrl);
    }

    @Override
    public void onShowMainFrame() {
        getDelegate().onShowMainFrame();
    }

    @Override
    public void onLoading(String msg) {
        getDelegate().onLoading(msg);
    }

    @Override
    public void onCancelLoading() {
        getDelegate().onCancelLoading();
    }


    @Override
    public void onShowMessage(String message, String from) {
        getDelegate().onShowMessage(message, from);
    }

    @Override
    public void onPermissionsDeny(String[] permissions, String permissionType, String action) {
        getDelegate().onPermissionsDeny(permissions, permissionType, action);
    }

    @Override
    public void bindSupportWebParent(WebParentLayout webParentLayout, Activity activity) {
        getDelegate().bindSupportWebParent(webParentLayout, activity);
    }


}
