package top.xuqingquan.web.publics;

import android.os.Handler;
import android.os.Looper;

public class JsAccessEntraceImpl extends BaseJsAccessEntrace {

    private Handler mHandler = new Handler(Looper.getMainLooper());

    public static JsAccessEntraceImpl getInstance(android.webkit.WebView webView) {
        return new JsAccessEntraceImpl(webView);
    }

    public static JsAccessEntraceImpl getInstance(com.tencent.smtt.sdk.WebView webView) {
        return new JsAccessEntraceImpl(webView);
    }

    private JsAccessEntraceImpl(android.webkit.WebView webView) {
        super(webView);
    }

    private JsAccessEntraceImpl(com.tencent.smtt.sdk.WebView webView) {
        super(webView);
    }

    private void safeCallJs(final String s, final android.webkit.ValueCallback valueCallback) {
        mHandler.post(() -> callJs(s, valueCallback));
    }

    private void safeCallJs(final String s, final com.tencent.smtt.sdk.ValueCallback valueCallback) {
        mHandler.post(() -> callJs(s, valueCallback));
    }

    @Override
    public void callJs(String params, final android.webkit.ValueCallback<String> callback) {
        if (Thread.currentThread() != Looper.getMainLooper().getThread()) {
            safeCallJs(params, callback);
            return;
        }
        super.callJs(params, callback);
    }

    @Override
    public void callJs(String params, final com.tencent.smtt.sdk.ValueCallback<String> callback) {
        if (Thread.currentThread() != Looper.getMainLooper().getThread()) {
            safeCallJs(params, callback);
            return;
        }
        super.callJs(params, callback);
    }


}
