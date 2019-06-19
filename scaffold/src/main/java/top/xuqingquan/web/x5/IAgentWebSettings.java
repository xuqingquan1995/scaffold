package top.xuqingquan.web.x5;

import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;

public interface IAgentWebSettings<T extends WebSettings> {

    @SuppressWarnings("UnusedReturnValue")
    IAgentWebSettings toSetting(WebView webView);

    T getWebSettings();

}
