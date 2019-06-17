package top.xuqingquan.web;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import top.xuqingquan.utils.Timber;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;

import static top.xuqingquan.web.ActionActivity.KEY_FROM_INTENTION;

public class DefaultChromeClient extends MiddlewareWebChromeBase {
    /**
     * Activity
     */
    private WeakReference<Activity> mActivityWeakReference;
    /**
     * 包装Flag
     */
    private boolean mIsWrapper;
    /**
     * Video 处理类
     */
    private IVideo mIVideo;
    /**
     * PermissionInterceptor 权限拦截器
     */
    private PermissionInterceptor mPermissionInterceptor;
    /**
     * 当前 WebView
     */
    private WebView mWebView;
    /**
     * Web端触发的定位 mOrigin
     */
    private String mOrigin = null;
    /**
     * Web 端触发的定位 Callback 回调成功，或者失败
     */
    private GeolocationPermissions.Callback mCallback = null;
    /**
     * 标志位
     */
    private static final int FROM_CODE_INTENTION = 0x18;
    /**
     * 标识当前是获取定位权限
     */
    private static final int FROM_CODE_INTENTION_LOCATION = FROM_CODE_INTENTION << 2;
    /**
     * AbsAgentWebUIController
     */
    private WeakReference<AbsAgentWebUIController> mAgentWebUIController;
    /**
     * IndicatorController 进度条控制器
     */
    private IndicatorController mIndicatorController;

    DefaultChromeClient(Activity activity,
                        IndicatorController indicatorController,
                        WebChromeClient chromeClient,
                        @Nullable IVideo iVideo,
                        PermissionInterceptor permissionInterceptor, WebView webView) {
        super(chromeClient);
        this.mIndicatorController = indicatorController;
        mIsWrapper = chromeClient != null;
        mActivityWeakReference = new WeakReference<>(activity);
        this.mIVideo = iVideo;
        this.mPermissionInterceptor = permissionInterceptor;
        this.mWebView = webView;
        mAgentWebUIController = new WeakReference<>(AgentWebUtils.getAgentWebUIControllerByWebView(webView));
    }


    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        if (mIndicatorController != null) {
            mIndicatorController.progress(view, newProgress);
        }
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        if (mIsWrapper) {
            super.onReceivedTitle(view, title);
        }
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        if (mAgentWebUIController.get() != null) {
            mAgentWebUIController.get().onJsAlert(view, url, message);
        }
        result.confirm();
        return true;
    }


    @Override
    public void onReceivedIcon(WebView view, Bitmap icon) {
        super.onReceivedIcon(view, icon);
    }

    @Override
    public void onGeolocationPermissionsHidePrompt() {
        super.onGeolocationPermissionsHidePrompt();
    }

    //location
    @Override
    public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
        onGeolocationPermissionsShowPromptInternal(origin, callback);
    }

    private void onGeolocationPermissionsShowPromptInternal(String origin, GeolocationPermissions.Callback callback) {
        if (mPermissionInterceptor != null) {
            if (mPermissionInterceptor.intercept(this.mWebView.getUrl(), AgentWebPermissions.LOCATION, "location")) {
                callback.invoke(origin, false, false);
                return;
            }
        }
        Activity mActivity = mActivityWeakReference.get();
        if (mActivity == null) {
            callback.invoke(origin, false, false);
            return;
        }
        List<String> deniedPermissions;
        if ((deniedPermissions = AgentWebUtils.getDeniedPermissions(mActivity, AgentWebPermissions.LOCATION)).isEmpty()) {
            Timber.i("onGeolocationPermissionsShowPromptInternal:" + true);
            callback.invoke(origin, true, false);
        } else {
            Action mAction = Action.createPermissionsAction(deniedPermissions.toArray(new String[]{}));
            mAction.setFromIntention(FROM_CODE_INTENTION_LOCATION);
            ActionActivity.setPermissionListener(mPermissionListener);
            this.mCallback = callback;
            this.mOrigin = origin;
            ActionActivity.start(mActivity, mAction);
        }
    }

    private ActionActivity.PermissionListener mPermissionListener = new ActionActivity.PermissionListener() {
        @Override
        public void onRequestPermissionsResult(@NonNull String[] permissions, @NonNull int[] grantResults, Bundle extras) {
            if (extras.getInt(KEY_FROM_INTENTION) == FROM_CODE_INTENTION_LOCATION) {
                boolean hasPermission = AgentWebUtils.hasPermission(mActivityWeakReference.get(), permissions);
                if (mCallback != null) {
                    if (hasPermission) {
                        mCallback.invoke(mOrigin, true, false);
                    } else {
                        mCallback.invoke(mOrigin, false, false);
                    }
                    mCallback = null;
                    mOrigin = null;
                }
                if (!hasPermission && null != mAgentWebUIController.get()) {
                    mAgentWebUIController
                            .get()
                            .onPermissionsDeny(
                                    AgentWebPermissions.LOCATION,
                                    AgentWebPermissions.ACTION_LOCATION,
                                    "Location");
                }
            }
        }
    };

    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        try {
            if (this.mAgentWebUIController.get() != null) {
                this.mAgentWebUIController.get().onJsPrompt(mWebView, url, message, defaultValue, result);
            }
        } catch (Throwable throwable) {
            Timber.e(throwable);
        }
        return true;
    }

    @Override
    public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
        if (mAgentWebUIController.get() != null) {
            mAgentWebUIController.get().onJsConfirm(view, url, message, result);
        }
        return true;
    }


    @Override
    public void onExceededDatabaseQuota(String url, String databaseIdentifier, long quota, long estimatedDatabaseSize, long totalQuota, WebStorage.QuotaUpdater quotaUpdater) {
        quotaUpdater.updateQuota(totalQuota * 2);
    }

    @Override
    public void onReachedMaxAppCacheSize(long requiredStorage, long quota, WebStorage.QuotaUpdater quotaUpdater) {
        quotaUpdater.updateQuota(requiredStorage * 2);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
        Timber.i("openFileChooser>=5.0");
        return openFileChooserAboveL(filePathCallback, fileChooserParams);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private boolean openFileChooserAboveL(ValueCallback<Uri[]> valueCallbacks, WebChromeClient.FileChooserParams fileChooserParams) {
        Timber.i("fileChooserParams:" + Arrays.toString(fileChooserParams.getAcceptTypes()) + "  getTitle:" + fileChooserParams.getTitle() + " accept:" + Arrays.toString(fileChooserParams.getAcceptTypes()) + " length:" + fileChooserParams.getAcceptTypes().length + "  :" + fileChooserParams.isCaptureEnabled() + "  " + fileChooserParams.getFilenameHint() + "  intent:" + fileChooserParams.createIntent().toString() + "   mode:" + fileChooserParams.getMode());
        Activity mActivity = this.mActivityWeakReference.get();
        if (mActivity == null || mActivity.isFinishing()) {
            return false;
        }
        return AgentWebUtils.showFileChooserCompat(mActivity,
                mWebView,
                valueCallbacks,
                fileChooserParams,
                this.mPermissionInterceptor,
                null,
                null,
                null
        );
    }

    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        super.onConsoleMessage(consoleMessage);
        return true;
    }

    @Override
    public void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback) {
        if (mIVideo != null) {
            mIVideo.onShowCustomView(view, callback);
        }
    }

    @Override
    public void onHideCustomView() {
        if (mIVideo != null) {
            mIVideo.onHideCustomView();
        }
    }
}
