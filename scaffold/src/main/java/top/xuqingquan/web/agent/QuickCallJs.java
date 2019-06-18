package top.xuqingquan.web.agent;

public interface QuickCallJs {

    void quickCallJs(String method, android.webkit.ValueCallback<String> callback, String... params);

    void quickCallJs(String method, com.tencent.smtt.sdk.ValueCallback<String> callback, String... params);

    void quickCallJs(String method, String... params);

    void quickCallJs(String method);
}
