package top.xuqingquan.web.x5;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.view.ViewParent;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import top.xuqingquan.R;
import top.xuqingquan.utils.FileUtils;
import top.xuqingquan.utils.Timber;
import top.xuqingquan.web.AbsAgentWebUIController;
import top.xuqingquan.web.WebParentLayout;
import top.xuqingquan.web.agent.PermissionInterceptor;

import java.io.File;

public class X5WebUtils {

    public static void clearWebView(WebView m) {
        if (m == null) {
            return;
        }
        if (Looper.myLooper() != Looper.getMainLooper()) {
            return;
        }
        m.loadUrl("about:blank");
        m.stopLoading();
        if (m.getHandler() != null) {
            m.getHandler().removeCallbacksAndMessages(null);
        }
        m.removeAllViews();
        ViewGroup mViewGroup;
        if ((mViewGroup = ((ViewGroup) m.getParent())) != null) {
            mViewGroup.removeView(m);
        }
        m.setWebChromeClient(null);
        m.setWebViewClient(null);
        m.setTag(null);
        m.clearHistory();
        m.destroy();
    }

    public static void clearWebViewAllCache(Context context, WebView webView) {
        try {
            AgentWebConfig.removeAllCookies(null);
            webView.getSettings().setCacheMode(android.webkit.WebSettings.LOAD_NO_CACHE);
            context.deleteDatabase("webviewCache.db");
            context.deleteDatabase("webview.db");
            webView.clearCache(true);
            webView.clearHistory();
            webView.clearFormData();
            FileUtils.clearCacheFolder(new File(AgentWebConfig.getCachePath(context)), 0);
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    public static void clearWebViewAllCache(Context context) {
        try {
            clearWebViewAllCache(context, new WebView(context.getApplicationContext()));
        } catch (Exception e) {
            Timber.e(e);
        }
    }


    @Deprecated
    static void getUIControllerAndShowMessage(Activity activity, String message, String from) {
        if (activity == null || activity.isFinishing()) {
            return;
        }
        WebParentLayout mWebParentLayout = activity.findViewById(R.id.web_parent_layout_id);
        AbsAgentWebUIController mAgentWebUIController = mWebParentLayout.provide();
        if (mAgentWebUIController != null) {
            mAgentWebUIController.onShowMessage(message, from);
        }
    }

    public static AbsAgentWebUIController getAgentWebUIControllerByWebView(WebView webView) {
        WebParentLayout mWebParentLayout = getWebParentLayoutByWebView(webView);
        return mWebParentLayout.provide();
    }

    private static WebParentLayout getWebParentLayoutByWebView(WebView webView) {
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

    public static boolean showFileChooserCompat(Activity activity,
                                                WebView webView,
                                                ValueCallback<Uri[]> valueCallbacks,
                                                WebChromeClient.FileChooserParams fileChooserParams,
                                                PermissionInterceptor permissionInterceptor,
                                                ValueCallback<Uri> valueCallback,
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
            FileChooser fileChooser = builder.build();
            fileChooser.openFileChooser();
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
