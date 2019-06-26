package top.xuqingquan.web.x5;

import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

public interface WebListenerManager {

    @SuppressWarnings("UnusedReturnValue")
    WebListenerManager setWebChromeClient(WebView webview, WebChromeClient webChromeClient);

    @SuppressWarnings("UnusedReturnValue")
    WebListenerManager setWebViewClient(WebView webView, WebViewClient webViewClient);

    WebListenerManager setDownloader(WebView webView, DownloadListener downloadListener);
}
