package top.xuqingquan.web.system;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Message;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.*;
import androidx.annotation.RequiresApi;

public class WebChromeClientDelegate extends WebChromeClient {
    private WebChromeClient mDelegate;

    protected WebChromeClient getDelegate() {
        return mDelegate;
    }

    WebChromeClientDelegate(WebChromeClient webChromeClient) {
        this.mDelegate = webChromeClient;
    }

    void setDelegate(WebChromeClient delegate) {
        this.mDelegate = delegate;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        if (this.mDelegate != null) {
            this.mDelegate.onProgressChanged(view, newProgress);
        }
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        if (this.mDelegate != null) {
            this.mDelegate.onReceivedTitle(view, title);
            return;
        }
        super.onReceivedTitle(view, title);
    }

    @Override
    public void onReceivedIcon(WebView view, Bitmap icon) {
        if (this.mDelegate != null) {
            this.mDelegate.onReceivedIcon(view, icon);
            return;
        }
        super.onReceivedIcon(view, icon);
    }

    @Override
    public void onReceivedTouchIconUrl(WebView view, String url,
                                       boolean precomposed) {
        if (this.mDelegate != null) {
            this.mDelegate.onReceivedTouchIconUrl(view, url, precomposed);
            return;
        }
        super.onReceivedTouchIconUrl(view, url, precomposed);
    }

    @Override
    public void onShowCustomView(View view, CustomViewCallback callback) {
        if (this.mDelegate != null) {
            this.mDelegate.onShowCustomView(view, callback);
            return;
        }
        super.onShowCustomView(view, callback);
    }

    @SuppressWarnings("deprecation")
    @Override
    @Deprecated
    public void onShowCustomView(View view, int requestedOrientation,
                                 CustomViewCallback callback) {
        if (this.mDelegate != null) {
            this.mDelegate.onShowCustomView(view, requestedOrientation, callback);
            return;
        }
        super.onShowCustomView(view, requestedOrientation, callback);
    }


    @Override
    public void onHideCustomView() {
        if (this.mDelegate != null) {
            this.mDelegate.onHideCustomView();
            return;
        }
        super.onHideCustomView();
    }

    @Override
    public boolean onCreateWindow(WebView view, boolean isDialog,
                                  boolean isUserGesture, Message resultMsg) {
        if (this.mDelegate != null) {
            return this.mDelegate.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
        }
        return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
    }

    @Override
    public void onRequestFocus(WebView view) {
        if (this.mDelegate != null) {
            this.mDelegate.onRequestFocus(view);
            return;
        }
        super.onRequestFocus(view);
    }

    @Override
    public void onCloseWindow(WebView window) {
        if (this.mDelegate != null) {
            this.mDelegate.onCloseWindow(window);
            return;
        }
        super.onCloseWindow(window);
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message,
                             JsResult result) {
        if (this.mDelegate != null) {
            return this.mDelegate.onJsAlert(view, url, message, result);
        }
        return super.onJsAlert(view, url, message, result);
    }

    @Override
    public boolean onJsConfirm(WebView view, String url, String message,
                               JsResult result) {
        if (this.mDelegate != null) {
            return this.mDelegate.onJsConfirm(view, url, message, result);
        }
        return super.onJsConfirm(view, url, message, result);
    }

    @Override
    public boolean onJsPrompt(WebView view, String url, String message,
                              String defaultValue, JsPromptResult result) {
        if (this.mDelegate != null) {
            return this.mDelegate.onJsPrompt(view, url, message, defaultValue, result);
        }
        return super.onJsPrompt(view, url, message, defaultValue, result);
    }

    @Override
    public boolean onJsBeforeUnload(WebView view, String url, String message,
                                    JsResult result) {
        if (this.mDelegate != null) {
            return this.mDelegate.onJsBeforeUnload(view, url, message, result);
        }
        return super.onJsBeforeUnload(view, url, message, result);
    }

    @SuppressWarnings("deprecation")
    @Override
    @Deprecated
    public void onExceededDatabaseQuota(String url, String databaseIdentifier,
                                        long quota, long estimatedDatabaseSize, long totalQuota,
                                        WebStorage.QuotaUpdater quotaUpdater) {
        if (this.mDelegate != null) {
            this.mDelegate.onExceededDatabaseQuota(url, databaseIdentifier, quota, estimatedDatabaseSize, totalQuota, quotaUpdater);
            return;
        }
        super.onExceededDatabaseQuota(url, databaseIdentifier, quota, estimatedDatabaseSize, totalQuota, quotaUpdater);

    }

    @Override
    @Deprecated
    @SuppressWarnings("deprecation")
    public void onReachedMaxAppCacheSize(long requiredStorage, long quota,
                                         WebStorage.QuotaUpdater quotaUpdater) {
        if (this.mDelegate != null) {
            this.mDelegate.onReachedMaxAppCacheSize(requiredStorage, quota, quotaUpdater);
            return;
        }
        super.onReachedMaxAppCacheSize(requiredStorage, quota, quotaUpdater);
    }

    @Override
    public void onGeolocationPermissionsShowPrompt(String origin,
                                                   GeolocationPermissions.Callback callback) {
        if (this.mDelegate != null) {
            this.mDelegate.onGeolocationPermissionsShowPrompt(origin, callback);
            return;
        }
        super.onGeolocationPermissionsShowPrompt(origin, callback);

    }

    @Override
    public void onGeolocationPermissionsHidePrompt() {
        if (this.mDelegate != null) {
            this.mDelegate.onGeolocationPermissionsHidePrompt();
            return;
        }

        super.onGeolocationPermissionsHidePrompt();
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onPermissionRequest(PermissionRequest request) {
        if (this.mDelegate != null) {
            this.mDelegate.onPermissionRequest(request);
            return;
        }
        super.onPermissionRequest(request);
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onPermissionRequestCanceled(PermissionRequest request) {

        if (this.mDelegate != null) {
            this.mDelegate.onPermissionRequestCanceled(request);
            return;
        }
        super.onPermissionRequestCanceled(request);
    }

    @Override
    @SuppressWarnings("deprecation")
    @Deprecated
    public boolean onJsTimeout() {
        if (this.mDelegate != null) {
            return this.mDelegate.onJsTimeout();
        }
        return super.onJsTimeout();
    }

    @Override
    @Deprecated
    @SuppressWarnings("deprecation")
    public void onConsoleMessage(String message, int lineNumber, String sourceID) {
        if (this.mDelegate != null) {
            this.mDelegate.onConsoleMessage(message, lineNumber, sourceID);
            return;
        }
        super.onConsoleMessage(message, lineNumber, sourceID);
    }

    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        if (this.mDelegate != null) {
            return this.mDelegate.onConsoleMessage(consoleMessage);
        }
        return super.onConsoleMessage(consoleMessage);
    }

    @Override
    public Bitmap getDefaultVideoPoster() {
        if (this.mDelegate != null) {
            return this.mDelegate.getDefaultVideoPoster();
        }
        return super.getDefaultVideoPoster();
    }

    @Override
    public View getVideoLoadingProgressView() {
        if (this.mDelegate != null) {
            return this.mDelegate.getVideoLoadingProgressView();
        }
        return super.getVideoLoadingProgressView();
    }

    @Override
    public void getVisitedHistory(ValueCallback<String[]> callback) {
        if (this.mDelegate != null) {
            this.mDelegate.getVisitedHistory(callback);
            return;
        }
        super.getVisitedHistory(callback);
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback,
                                     FileChooserParams fileChooserParams) {
        if (this.mDelegate != null) {
            return this.mDelegate.onShowFileChooser(webView, filePathCallback, fileChooserParams);
        }
        return super.onShowFileChooser(webView, filePathCallback, fileChooserParams);
    }

}
