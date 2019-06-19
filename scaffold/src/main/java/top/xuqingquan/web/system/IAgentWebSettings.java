package top.xuqingquan.web.system;

import android.webkit.WebSettings;
import android.webkit.WebView;

public interface IAgentWebSettings<T extends WebSettings> {

    @SuppressWarnings("UnusedReturnValue")
    IAgentWebSettings toSetting(WebView webView);

    T getWebSettings();

}
