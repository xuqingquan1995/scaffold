package top.xuqingquan.web;

import com.tencent.smtt.sdk.WebView;
import top.xuqingquan.utils.Timber;
import top.xuqingquan.web.agent.JsBaseInterfaceHolder;
import top.xuqingquan.web.agent.JsInterfaceHolder;
import top.xuqingquan.web.agent.JsInterfaceObjectException;
import top.xuqingquan.web.agent.SecurityType;

import java.util.Map;
import java.util.Set;

public class JsInterfaceHolderImpl extends JsBaseInterfaceHolder {

    private WebView mWebView;

    static JsInterfaceHolderImpl getJsInterfaceHolder(WebView webView, SecurityType securityType) {
        return new JsInterfaceHolderImpl(webView, securityType);
    }

    JsInterfaceHolderImpl(WebView webView, SecurityType securityType) {
        super(securityType);
        this.mWebView = webView;
    }

    @Override
    public JsInterfaceHolder addJavaObjects(Map<String, Object> maps) {
        if (!checkSecurity()) {
            Timber.e("The injected object is not safe, give up injection");
            return this;
        }
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
        if (!checkSecurity()) {
            return this;
        }
        boolean t = checkObject(v);
        if (!t) {
            throw new JsInterfaceObjectException("this object has not offer method javascript to call , please check addJavascriptInterface annotation was be added");
        } else {
            addJavaObjectDirect(k, v);
        }
        return this;
    }

    private JsInterfaceHolder addJavaObjectDirect(String k, Object v) {
        Timber.i("k:" + k + "  v:" + v);
        this.mWebView.addJavascriptInterface(v, k);
        return this;
    }

}
