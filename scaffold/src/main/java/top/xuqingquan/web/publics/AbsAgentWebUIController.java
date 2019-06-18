package top.xuqingquan.web.publics;

import android.app.Activity;
import android.app.Dialog;
import android.os.Handler;

public abstract class AbsAgentWebUIController {

    private static boolean HAS_DESIGN_LIB;
    private volatile boolean mIsBindWebParent = false;
    private AbsAgentWebUIController mAgentWebUIControllerDelegate;

    static {
        try {
            Class.forName("com.google.android.material.snackbar.Snackbar");
            Class.forName("com.google.android.material.bottomsheet.BottomSheetDialog");
            HAS_DESIGN_LIB = true;
        } catch (Throwable ignore) {
            HAS_DESIGN_LIB = false;
        }
    }

    protected AbsAgentWebUIController create() {
        return HAS_DESIGN_LIB ? new DefaultDesignUIController() : new DefaultUIController();
    }

    protected AbsAgentWebUIController getDelegate() {
        AbsAgentWebUIController mAgentWebUIController = this.mAgentWebUIControllerDelegate;
        if (mAgentWebUIController == null) {
            this.mAgentWebUIControllerDelegate = mAgentWebUIController = create();
        }
        return mAgentWebUIController;
    }

    public final synchronized void bindWebParent(WebParentLayout webParentLayout, Activity activity) {
        if (!mIsBindWebParent) {
            mIsBindWebParent = true;
            bindSupportWebParent(webParentLayout, activity);
        }
    }

    protected void toDismissDialog(Dialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    protected void toShowDialog(Dialog dialog) {
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
    }

    public abstract void bindSupportWebParent(WebParentLayout webParentLayout, Activity activity);

    /**
     * WebChromeClient#onJsAlert
     *
     * @param view
     * @param url
     * @param message
     */
    public abstract void onJsAlert(android.webkit.WebView view, String url, String message);

    public abstract void onJsAlert(com.tencent.smtt.sdk.WebView view, String url, String message);

    /**
     * 咨询用户是否前往其他页面
     *
     * @param view
     * @param url
     * @param callback
     */
    public abstract void onOpenPagePrompt(android.webkit.WebView view, String url, Handler.Callback callback);

    public abstract void onOpenPagePrompt(com.tencent.smtt.sdk.WebView view, String url, Handler.Callback callback);

    /**
     * WebChromeClient#onJsConfirm
     *
     * @param view
     * @param url
     * @param message
     * @param jsResult
     */
    public abstract void onJsConfirm(android.webkit.WebView view, String url, String message, android.webkit.JsResult jsResult);

    public abstract void onJsConfirm(com.tencent.smtt.sdk.WebView view, String url, String message, com.tencent.smtt.export.external.interfaces.JsResult jsResult);

    public abstract void onSelectItemsPrompt(android.webkit.WebView view, String url, String[] ways, Handler.Callback callback);

    public abstract void onSelectItemsPrompt(com.tencent.smtt.sdk.WebView view, String url, String[] ways, Handler.Callback callback);

    /**
     * 强制下载弹窗
     *
     * @param url      当前下载地址。
     * @param callback 用户操作回调回调
     */
    public abstract void onForceDownloadAlert(String url, Handler.Callback callback);

    /**
     * WebChromeClient#onJsPrompt
     *
     * @param view
     * @param url
     * @param message
     * @param defaultValue
     * @param jsPromptResult
     */
    public abstract void onJsPrompt(android.webkit.WebView view, String url, String message, String defaultValue, android.webkit.JsPromptResult jsPromptResult);

    public abstract void onJsPrompt(com.tencent.smtt.sdk.WebView view, String url, String message, String defaultValue, com.tencent.smtt.export.external.interfaces.JsPromptResult jsPromptResult);

    /**
     * 显示错误页
     *
     * @param view
     * @param errorCode
     * @param description
     * @param failingUrl
     */
    public abstract void onMainFrameError(android.webkit.WebView view, int errorCode, String description, String failingUrl);

    public abstract void onMainFrameError(com.tencent.smtt.sdk.WebView view, int errorCode, String description, String failingUrl);

    /**
     * 隐藏错误页
     */
    public abstract void onShowMainFrame();

    /**
     * 正在加载...
     *
     * @param msg
     */
    public abstract void onLoading(String msg);

    /**
     * 取消正在加载...
     */
    public abstract void onCancelLoading();

    /**
     * @param message 消息
     * @param intent  说明message的来源，意图
     */
    public abstract void onShowMessage(String message, String intent);

    /**
     * 当权限被拒回调该方法
     *
     * @param permissions
     * @param permissionType
     * @param action
     */
    public abstract void onPermissionsDeny(String[] permissions, String permissionType, String action);

}
