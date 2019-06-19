package top.xuqingquan.web.publics;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;
import top.xuqingquan.R;
import top.xuqingquan.utils.DeviceUtils;
import top.xuqingquan.utils.Timber;
import top.xuqingquan.web.nokernel.WebUtils;

public class DefaultUIController extends AbsAgentWebUIController {

    private AlertDialog mAlertDialog;
    private AlertDialog mConfirmDialog;
    private android.webkit.JsPromptResult mJsPromptResult = null;
    private android.webkit.JsResult mJsResult = null;
    private com.tencent.smtt.export.external.interfaces.JsPromptResult mX5JsPromptResult = null;
    private com.tencent.smtt.export.external.interfaces.JsResult mX5JsResult = null;
    private AlertDialog mPromptDialog = null;
    private Activity mActivity;
    private WebParentLayout mWebParentLayout;
    private AlertDialog mAskOpenOtherAppDialog = null;
    private ProgressDialog mProgressDialog;
    private AlertDialog mAlertDialogForceDownload = null;
    private Resources mResources = null;

    @Override
    public void onJsAlert(android.webkit.WebView view, String url, String message) {
        WebUtils.toastShowShort(view.getContext().getApplicationContext(), message);
    }

    @Override
    public void onJsAlert(com.tencent.smtt.sdk.WebView view, String url, String message) {
        WebUtils.toastShowShort(view.getContext().getApplicationContext(), message);
    }

    @Override
    public void onOpenPagePrompt(android.webkit.WebView view, String url, final Handler.Callback callback) {
        onOpenPagePrompt(callback);
    }

    @Override
    public void onOpenPagePrompt(com.tencent.smtt.sdk.WebView view, String url, Handler.Callback callback) {
        onOpenPagePrompt(callback);
    }

    private void onOpenPagePrompt(Handler.Callback callback) {
        Timber.i("onOpenPagePrompt");
        if (mAskOpenOtherAppDialog == null) {
            mAskOpenOtherAppDialog = new AlertDialog
                    .Builder(mActivity)
                    .setMessage(mResources.getString(R.string.agentweb_leave_app_and_go_other_page,
                            DeviceUtils.getApplicationName(mActivity)))//
                    .setTitle(mResources.getString(R.string.agentweb_tips))
                    .setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                        if (callback != null) {
                            callback.handleMessage(Message.obtain(null, -1));
                        }
                    })//
                    .setPositiveButton(mResources.getString(R.string.agentweb_leave), (dialog, which) -> {
                        if (callback != null) {
                            callback.handleMessage(Message.obtain(null, 1));
                        }
                    })
                    .create();
        }
        mAskOpenOtherAppDialog.show();
    }

    @Override
    public void onJsConfirm(android.webkit.WebView view, String url, String message, android.webkit.JsResult jsResult) {
        onJsConfirmInternal(message, jsResult);
    }

    @Override
    public void onJsConfirm(com.tencent.smtt.sdk.WebView view, String url, String message, com.tencent.smtt.export.external.interfaces.JsResult jsResult) {
        onJsConfirmInternal(message, jsResult);
    }

    @Override
    public void onSelectItemsPrompt(android.webkit.WebView view, String url, final String[] ways, final Handler.Callback callback) {
        showChooserInternal(ways, callback);
    }

    @Override
    public void onSelectItemsPrompt(com.tencent.smtt.sdk.WebView view, String url, String[] ways, Handler.Callback callback) {
        showChooserInternal(ways, callback);
    }

    @Override
    public void onForceDownloadAlert(String url, final Handler.Callback callback) {
        onForceDownloadAlertInternal(callback);
    }

    private void onForceDownloadAlertInternal(final Handler.Callback callback) {
        Activity mActivity = this.mActivity;
        if (mActivity == null || mActivity.isFinishing()) {
            return;
        }
        if (mAlertDialogForceDownload == null) {
            mAlertDialogForceDownload = new AlertDialog.Builder(mActivity)
                    .setTitle(mResources.getString(R.string.agentweb_tips))
                    .setMessage(mResources.getString(R.string.agentweb_honeycomblow))
                    .setNegativeButton(mResources.getString(R.string.agentweb_download), (dialog, which) -> {
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                        if (callback != null) {
                            callback.handleMessage(Message.obtain());
                        }
                    })//
                    .setPositiveButton(mResources.getString(R.string.agentweb_cancel), (dialog, which) -> {

                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    }).create();
        }
        mAlertDialogForceDownload.show();
    }

    private void showChooserInternal(String[] ways, final Handler.Callback callback) {
        if (mAlertDialog == null) {
            mAlertDialog = new AlertDialog.Builder(mActivity)
                    .setSingleChoiceItems(ways, -1, (dialog, which) -> {
                        dialog.dismiss();
                        Timber.i("which:" + which);
                        if (callback != null) {
                            Message mMessage = Message.obtain();
                            mMessage.what = which;
                            callback.handleMessage(mMessage);
                        }

                    }).setOnCancelListener(dialog -> {
                        dialog.dismiss();
                        if (callback != null) {
                            callback.handleMessage(Message.obtain(null, -1));
                        }
                    }).create();
        }
        mAlertDialog.show();
    }

    private void onJsConfirmInternal(String message, android.webkit.JsResult jsResult) {
        Timber.i("activity:" + mActivity.hashCode() + "  ");
        Activity mActivity = this.mActivity;
        if (mActivity == null || mActivity.isFinishing()) {
            toCancelJsresult(jsResult);
            return;
        }
        if (mConfirmDialog == null) {
            mConfirmDialog = new AlertDialog.Builder(mActivity)
                    .setMessage(message)
                    .setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                        toDismissDialog(mConfirmDialog);
                        toCancelJsresult(mJsResult);
                    })//
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                        toDismissDialog(mConfirmDialog);
                        if (mJsResult != null) {
                            mJsResult.confirm();
                        }
                    })
                    .setOnCancelListener(dialog -> {
                        dialog.dismiss();
                        toCancelJsresult(mJsResult);
                    })
                    .create();

        }
        mConfirmDialog.setMessage(message);
        this.mJsResult = jsResult;
        mConfirmDialog.show();
    }

    private void onJsConfirmInternal(String message, com.tencent.smtt.export.external.interfaces.JsResult jsResult) {
        Timber.i("activity:" + mActivity.hashCode() + "  ");
        Activity mActivity = this.mActivity;
        if (mActivity == null || mActivity.isFinishing()) {
            toCancelJsresult(jsResult);
            return;
        }
        if (mConfirmDialog == null) {
            mConfirmDialog = new AlertDialog.Builder(mActivity)
                    .setMessage(message)
                    .setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                        toDismissDialog(mConfirmDialog);
                        toCancelJsresult(mX5JsResult);
                    })//
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                        toDismissDialog(mConfirmDialog);
                        if (mX5JsResult != null) {
                            mX5JsResult.confirm();
                        }
                    })
                    .setOnCancelListener(dialog -> {
                        dialog.dismiss();
                        toCancelJsresult(mX5JsResult);
                    })
                    .create();

        }
        mConfirmDialog.setMessage(message);
        this.mX5JsResult = jsResult;
        mConfirmDialog.show();
    }

    private void onJsPromptInternal(String message, String defaultValue, android.webkit.JsPromptResult jsPromptResult) {
        Activity mActivity = this.mActivity;
        if (mActivity == null || mActivity.isFinishing()) {
            jsPromptResult.cancel();
            return;
        }
        if (mPromptDialog == null) {
            final EditText et = new EditText(mActivity);
            et.setText(defaultValue);
            mPromptDialog = new AlertDialog.Builder(mActivity)
                    .setView(et)
                    .setTitle(message)
                    .setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                        toDismissDialog(mPromptDialog);
                        toCancelJsresult(mJsPromptResult);
                    })//
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                        toDismissDialog(mPromptDialog);
                        if (mJsPromptResult != null) {
                            mJsPromptResult.confirm(et.getText().toString());
                        }
                    })
                    .setOnCancelListener(dialog -> {
                        dialog.dismiss();
                        toCancelJsresult(mJsPromptResult);
                    })
                    .create();
        }
        this.mJsPromptResult = jsPromptResult;
        mPromptDialog.show();
    }

    private void onJsPromptInternal(String message, String defaultValue, com.tencent.smtt.export.external.interfaces.JsPromptResult jsPromptResult) {
        Activity mActivity = this.mActivity;
        if (mActivity == null || mActivity.isFinishing()) {
            jsPromptResult.cancel();
            return;
        }
        if (mPromptDialog == null) {
            final EditText et = new EditText(mActivity);
            et.setText(defaultValue);
            mPromptDialog = new AlertDialog.Builder(mActivity)
                    .setView(et)
                    .setTitle(message)
                    .setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                        toDismissDialog(mPromptDialog);
                        toCancelJsresult(mX5JsPromptResult);
                    })//
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                        toDismissDialog(mPromptDialog);
                        if (mX5JsPromptResult != null) {
                            mX5JsPromptResult.confirm(et.getText().toString());
                        }
                    })
                    .setOnCancelListener(dialog -> {
                        dialog.dismiss();
                        toCancelJsresult(mX5JsPromptResult);
                    })
                    .create();
        }
        this.mX5JsPromptResult = jsPromptResult;
        mPromptDialog.show();
    }

    @Override
    public void onJsPrompt(android.webkit.WebView view, String url, String message, String defaultValue, android.webkit.JsPromptResult jsPromptResult) {
        onJsPromptInternal(message, defaultValue, jsPromptResult);
    }

    @Override
    public void onJsPrompt(com.tencent.smtt.sdk.WebView view, String url, String message, String defaultValue, com.tencent.smtt.export.external.interfaces.JsPromptResult jsPromptResult) {
        onJsPromptInternal(message, defaultValue, jsPromptResult);
    }

    @Override
    public void onMainFrameError(android.webkit.WebView view, int errorCode, String description, String failingUrl) {
        Timber.i("mWebParentLayout onMainFrameError:" + mWebParentLayout);
        if (mWebParentLayout != null) {
            mWebParentLayout.showPageMainFrameError();
        }
    }

    @Override
    public void onMainFrameError(com.tencent.smtt.sdk.WebView view, int errorCode, String description, String failingUrl) {
        Timber.i("mWebParentLayout onMainFrameError:" + mWebParentLayout);
        if (mWebParentLayout != null) {
            mWebParentLayout.showPageMainFrameError();
        }
    }

    @Override
    public void onShowMainFrame() {
        if (mWebParentLayout != null) {
            mWebParentLayout.hideErrorLayout();
        }
    }

    @Override
    public void onLoading(String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mActivity);
        }
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setMessage(msg);
        mProgressDialog.show();

    }

    @Override
    public void onCancelLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        mProgressDialog = null;
    }

    @Override
    public void onShowMessage(String message, String from) {
        if (!TextUtils.isEmpty(from) && from.contains("performDownload")) {
            return;
        }
        WebUtils.toastShowShort(mActivity.getApplicationContext(), message);
    }

    @Override
    public void onPermissionsDeny(String[] permissions, String permissionType, String action) {
//		AgentWebUtils.toastShowShort(mActivity.getApplicationContext(), "权限被冻结");
    }

    private void toCancelJsresult(android.webkit.JsResult result) {
        if (result != null) {
            result.cancel();
        }
    }

    private void toCancelJsresult(com.tencent.smtt.export.external.interfaces.JsResult result) {
        if (result != null) {
            result.cancel();
        }
    }

    @Override
    public void bindSupportWebParent(WebParentLayout webParentLayout, Activity activity) {
        this.mActivity = activity;
        this.mWebParentLayout = webParentLayout;
        mResources = this.mActivity.getResources();
    }
}
