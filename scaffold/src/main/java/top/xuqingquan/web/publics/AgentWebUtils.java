package top.xuqingquan.web.publics;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import androidx.annotation.ColorInt;
import androidx.annotation.RequiresApi;
import com.google.android.material.snackbar.Snackbar;
import top.xuqingquan.R;
import top.xuqingquan.utils.FileUtils;
import top.xuqingquan.utils.Timber;
import top.xuqingquan.web.nokernel.PermissionInterceptor;
import top.xuqingquan.web.nokernel.WebConfig;
import top.xuqingquan.web.nokernel.WebUtils;

import java.io.File;
import java.lang.ref.WeakReference;

import static top.xuqingquan.web.nokernel.WebUtils.isUIThread;

public class AgentWebUtils {

    private AgentWebUtils() {
        throw new UnsupportedOperationException("u can't init me");
    }

    static void clearWebView(android.webkit.WebView m) {
        if (m == null) {
            return;
        }
        if (!isUIThread()) {
            return;
        }
        m.loadUrl("about:blank");
        m.stopLoading();
        if (m.getHandler() != null) {
            m.getHandler().removeCallbacksAndMessages(null);
        }
        m.removeAllViews();
        ViewGroup mViewGroup = ((ViewGroup) m.getParent());
        if (mViewGroup != null) {
            mViewGroup.removeView(m);
        }
        m.setWebChromeClient(null);
        m.setWebViewClient(null);
        m.setTag(null);
        m.clearHistory();
        m.destroy();
    }

    static void clearWebView(com.tencent.smtt.sdk.WebView m) {
        if (m == null) {
            return;
        }
        if (!isUIThread()) {
            return;
        }
        m.loadUrl("about:blank");
        m.stopLoading();
        if (m.getHandler() != null) {
            m.getHandler().removeCallbacksAndMessages(null);
        }
        m.removeAllViews();
        ViewGroup mViewGroup = ((ViewGroup) m.getParent());
        if (mViewGroup != null) {
            mViewGroup.removeView(m);
        }
        m.setWebChromeClient(null);
        m.setWebViewClient(null);
        m.setTag(null);
        m.clearHistory();
        m.destroy();
    }

    public static void show(View parent,
                            CharSequence text,
                            int duration,
                            @ColorInt int textColor,
                            @ColorInt int bgColor,
                            CharSequence actionText,
                            @ColorInt int actionTextColor,
                            View.OnClickListener listener) {
        SpannableString spannableString = new SpannableString(text);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(textColor);
        spannableString.setSpan(colorSpan, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        WeakReference<Snackbar> snackbarWeakReference = new WeakReference<>(Snackbar.make(parent, spannableString, duration));
        Snackbar snackbar = snackbarWeakReference.get();
        View view = snackbar.getView();
        view.setBackgroundColor(bgColor);
        if (actionText != null && actionText.length() > 0 && listener != null) {
            snackbar.setActionTextColor(actionTextColor);
            snackbar.setAction(actionText, listener);
        }
        snackbar.show();
    }

    public static void clearWebViewAllCache(Context context, android.webkit.WebView webView) {
        try {
            AgentWebConfig.removeAllCookies(null);
            webView.getSettings().setCacheMode(android.webkit.WebSettings.LOAD_NO_CACHE);
            context.deleteDatabase("webviewCache.db");
            context.deleteDatabase("webview.db");
            webView.clearCache(true);
            webView.clearHistory();
            webView.clearFormData();
            FileUtils.clearCacheFolder(new File(WebUtils.getCachePath(context)), 0);
        } catch (Throwable t) {
            Timber.e(t);
        }
    }

    public static void clearWebViewAllCache(Context context, com.tencent.smtt.sdk.WebView webView) {
        try {
            AgentWebConfig.removeAllX5Cookies(null);
            webView.getSettings().setCacheMode(android.webkit.WebSettings.LOAD_NO_CACHE);
            context.deleteDatabase("webviewCache.db");
            context.deleteDatabase("webview.db");
            webView.clearCache(true);
            webView.clearHistory();
            webView.clearFormData();
            FileUtils.clearCacheFolder(new File(WebUtils.getCachePath(context)), 0);
        } catch (Throwable t) {
            Timber.e(t);
        }
    }

    public static void clearWebViewAllCache(Context context) {
        try {
            if (WebConfig.hasX5()) {
                clearWebViewAllCache(context, new com.tencent.smtt.sdk.WebView(context.getApplicationContext()));
            } else {
                clearWebViewAllCache(context, new android.webkit.WebView(context.getApplicationContext()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static AbsAgentWebUIController getAgentWebUIControllerByWebView(android.webkit.WebView webView) {
        WebParentLayout mWebParentLayout = getWebParentLayoutByWebView(webView);
        return mWebParentLayout.provide();
    }

    public static AbsAgentWebUIController getAgentWebUIControllerByWebView(com.tencent.smtt.sdk.WebView webView) {
        WebParentLayout mWebParentLayout = getWebParentLayoutByWebView(webView);
        return mWebParentLayout.provide();
    }

    static WebParentLayout getWebParentLayoutByWebView(android.webkit.WebView webView) {
        ViewGroup mViewGroup;
        if (!(webView.getParent() instanceof ViewGroup)) {
            throw new IllegalStateException("please check webcreator's create method was be called ?");
        }
        mViewGroup = (ViewGroup) webView.getParent();
        while (mViewGroup != null) {

            Timber.i("ViewGroup:" + mViewGroup);
            if (mViewGroup.getId() == R.id.web_parent_layout_id) {
                WebParentLayout mWebParentLayout = (WebParentLayout) mViewGroup;
                Timber.i("found WebParentLayout");
                return mWebParentLayout;
            } else {
                ViewParent mViewParent = mViewGroup.getParent();
                if (mViewParent instanceof ViewGroup) {
                    mViewGroup = (ViewGroup) mViewParent;
                } else {
                    mViewGroup = null;
                }
            }
        }
        throw new IllegalStateException("please check webcreator's create method was be called ?");
    }

    static WebParentLayout getWebParentLayoutByWebView(com.tencent.smtt.sdk.WebView webView) {
        ViewGroup mViewGroup;
        if (!(webView.getParent() instanceof ViewGroup)) {
            throw new IllegalStateException("please check webcreator's create method was be called ?");
        }
        mViewGroup = (ViewGroup) webView.getParent();
        while (mViewGroup != null) {

            Timber.i("ViewGroup:" + mViewGroup);
            if (mViewGroup.getId() == R.id.web_parent_layout_id) {
                WebParentLayout mWebParentLayout = (WebParentLayout) mViewGroup;
                Timber.i("found WebParentLayout");
                return mWebParentLayout;
            } else {
                ViewParent mViewParent = mViewGroup.getParent();
                if (mViewParent instanceof ViewGroup) {
                    mViewGroup = (ViewGroup) mViewParent;
                } else {
                    mViewGroup = null;
                }
            }
        }
        throw new IllegalStateException("please check webcreator's create method was be called ?");
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static boolean showFileChooserCompat(Activity activity,
                                                android.webkit.WebView webView,
                                                android.webkit.ValueCallback valueCallbacks,
                                                android.webkit.WebChromeClient.FileChooserParams fileChooserParams,
                                                PermissionInterceptor permissionInterceptor,
                                                android.webkit.ValueCallback valueCallback,
                                                String mimeType,
                                                Handler.Callback jsChannelCallback
    ) {
        try {
            FileChooser.Builder builder = FileChooser.newBuilder(activity, webView);
            if (valueCallbacks != null) {
                builder.setUriValueCallbacks(valueCallbacks);
            }
            if (fileChooserParams != null) {
                builder.setFileChooserParams(fileChooserParams);
            }
            if (valueCallback != null) {
                builder.setUriValueCallback(valueCallback);
            }
            if (!TextUtils.isEmpty(mimeType)) {
                builder.setAcceptType(mimeType);
            }
            if (jsChannelCallback != null) {
                builder.setJsChannelCallback(jsChannelCallback);
            }
            builder.setPermissionInterceptor(permissionInterceptor);
            builder.build().openFileChooser();
        } catch (Throwable throwable) {
            Timber.e(throwable);
            if (valueCallbacks != null) {
                Timber.i("onReceiveValue empty");
                return false;
            }
            if (valueCallback != null) {
                valueCallback.onReceiveValue(null);
            }
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static boolean showFileChooserCompat(Activity activity,
                                                com.tencent.smtt.sdk.WebView webView,
                                                com.tencent.smtt.sdk.ValueCallback valueCallbacks,
                                                com.tencent.smtt.sdk.WebChromeClient.FileChooserParams fileChooserParams,
                                                PermissionInterceptor permissionInterceptor,
                                                com.tencent.smtt.sdk.ValueCallback valueCallback,
                                                String mimeType,
                                                Handler.Callback jsChannelCallback
    ) {
        try {
            FileChooser.Builder builder = FileChooser.newBuilder(activity, webView);
            if (valueCallbacks != null) {
                builder.setUriValueCallbacks(valueCallbacks);
            }
            if (fileChooserParams != null) {
                builder.setFileChooserParams(fileChooserParams);
            }
            if (valueCallback != null) {
                builder.setUriValueCallback(valueCallback);
            }
            if (!TextUtils.isEmpty(mimeType)) {
                builder.setAcceptType(mimeType);
            }
            if (jsChannelCallback != null) {
                builder.setJsChannelCallback(jsChannelCallback);
            }
            builder.setPermissionInterceptor(permissionInterceptor);
            builder.build().openFileChooser();
        } catch (Throwable throwable) {
            Timber.e(throwable);
            if (valueCallbacks != null) {
                Timber.i("onReceiveValue empty");
                return false;
            }
            if (valueCallback != null) {
                valueCallback.onReceiveValue(null);
            }
        }
        return true;
    }
}
