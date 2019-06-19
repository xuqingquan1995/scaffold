package top.xuqingquan.web.publics;

import android.os.Build;
import top.xuqingquan.utils.CharacterUtils;
import top.xuqingquan.web.nokernel.WebConfig;

public abstract class BaseJsAccessEntrace implements JsAccessEntrace {

    private android.webkit.WebView mWebView;
    private com.tencent.smtt.sdk.WebView mx5WebView;

    BaseJsAccessEntrace(android.webkit.WebView webView) {
        this.mWebView = webView;
    }

    BaseJsAccessEntrace(com.tencent.smtt.sdk.WebView webView) {
        this.mx5WebView = webView;
    }

    @Override
    public void callJs(String js, final android.webkit.ValueCallback<String> callback) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            this.evaluateJs(js, callback);
        } else {
            this.loadJs(js);
        }
    }

    @Override
    public void callJs(String js, final com.tencent.smtt.sdk.ValueCallback<String> callback) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            this.evaluateJs(js, callback);
        } else {
            this.loadJs(js);
        }
    }

    @Override
    public void callJs(String js) {
        if (WebConfig.hasX5()) {
            //noinspection RedundantCast
            this.callJs(js, (com.tencent.smtt.sdk.ValueCallback<String>) null);
        } else {
            this.callJs(js, (android.webkit.ValueCallback<String>) null);
        }
    }

    private void loadJs(String js) {
        if (WebConfig.hasX5()) {
            mx5WebView.loadUrl(js);
        } else {
            mWebView.loadUrl(js);
        }
    }

    private void evaluateJs(String js, final android.webkit.ValueCallback<String> callback) {
        mWebView.evaluateJavascript(js, value -> {
            if (callback != null) {
                callback.onReceiveValue(value);
            }
        });
    }

    private void evaluateJs(String js, final com.tencent.smtt.sdk.ValueCallback<String> callback) {
        mx5WebView.evaluateJavascript(js, value -> {
            if (callback != null) {
                callback.onReceiveValue(value);
            }
        });
    }

    @Override
    public void quickCallJs(String method, android.webkit.ValueCallback<String> callback, String... params) {
        StringBuilder sb = new StringBuilder();
        sb.append("javascript:" + method);
        if (params == null || params.length == 0) {
            sb.append("()");
        } else {
            sb.append("(").append(concat(params)).append(")");
        }
        callJs(sb.toString(), callback);
    }

    @Override
    public void quickCallJs(String method, com.tencent.smtt.sdk.ValueCallback<String> callback, String... params) {
        StringBuilder sb = new StringBuilder();
        sb.append("javascript:" + method);
        if (params == null || params.length == 0) {
            sb.append("()");
        } else {
            sb.append("(").append(concat(params)).append(")");
        }
        callJs(sb.toString(), callback);
    }

    private String concat(String... params) {
        StringBuilder mStringBuilder = new StringBuilder();
        for (int i = 0; i < params.length; i++) {
            String param = params[i];
            if (!CharacterUtils.isJson(param)) {
                mStringBuilder.append("\"").append(param).append("\"");
            } else {
                mStringBuilder.append(param);
            }
            if (i != params.length - 1) {
                mStringBuilder.append(" , ");
            }
        }
        return mStringBuilder.toString();
    }

    @Override
    public void quickCallJs(String method, String... params) {
        if (WebConfig.hasX5()) {
            //noinspection RedundantCast
            this.quickCallJs(method, (com.tencent.smtt.sdk.ValueCallback<String>) null, params);
        } else {
            this.quickCallJs(method, (android.webkit.ValueCallback<String>) null, params);
        }
    }

    @Override
    public void quickCallJs(String method) {
        this.quickCallJs(method, (String[]) null);
    }
}
