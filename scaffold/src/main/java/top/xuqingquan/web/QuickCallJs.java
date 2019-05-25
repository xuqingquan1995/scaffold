package top.xuqingquan.web;

import com.tencent.smtt.sdk.ValueCallback;

public interface QuickCallJs {

    void quickCallJs(String method, ValueCallback<String> callback, String... params);

    void quickCallJs(String method, String... params);

    void quickCallJs(String method);


}
