package top.xuqingquan.web.publics;

import android.annotation.SuppressLint;
import top.xuqingquan.utils.Timber;

import java.util.Map;
import java.util.Set;

public class JsInterfaceHolderImpl extends JsBaseInterfaceHolder {

    private android.webkit.WebView mWebView;
    private com.tencent.smtt.sdk.WebView mx5WebView;

    public static JsInterfaceHolderImpl getJsInterfaceHolder(android.webkit.WebView webView) {
        return new JsInterfaceHolderImpl(webView);
    }

    public static JsInterfaceHolderImpl getJsInterfaceHolder(com.tencent.smtt.sdk.WebView webView) {
        return new JsInterfaceHolderImpl(webView);
    }

    JsInterfaceHolderImpl(android.webkit.WebView webView) {
        this.mWebView = webView;
    }

    JsInterfaceHolderImpl(com.tencent.smtt.sdk.WebView webView) {
        this.mx5WebView = webView;
    }

    @Override
    public JsInterfaceHolder addJavaObjects(Map<String, Object> maps) {
        Set<Map.Entry<String, Object>> sets = maps.entrySet();
        for (Map.Entry<String, Object> mEntry : sets) {
            Object v = mEntry.getValue();
            boolean t = checkObject(v);
            if (!t) {
                throw new JsInterfaceObjectException("This object has not offer method javascript to call ,please check addJavascriptInterface annotation was be added");
            } else {
                addJavaObjectDirect(mEntry.getKey(), v);
            }
        }
        return this;
    }

    @Override
    public JsInterfaceHolder addJavaObject(String k, Object v) {
        boolean t = checkObject(v);
        if (!t) {
            throw new JsInterfaceObjectException("this object has not offer method javascript to call , please check addJavascriptInterface annotation was be added");
        } else {
            addJavaObjectDirect(k, v);
        }
        return this;
    }

    @SuppressLint("JavascriptInterface")
    private JsInterfaceHolder addJavaObjectDirect(String k, Object v) {
        Timber.i("k:" + k + "  v:" + v);
        if (AgentWebConfig.hasX5()) {
            this.mx5WebView.addJavascriptInterface(v, k);
        } else {
            this.mWebView.addJavascriptInterface(v, k);
        }
        return this;
    }

}
