package top.xuqingquan.web.agent;

public interface JsAccessEntrace extends QuickCallJs {

    void callJs(String js, android.webkit.ValueCallback<String> callback);

    void callJs(String js, com.tencent.smtt.sdk.ValueCallback<String> callback);

    void callJs(String js);

}
