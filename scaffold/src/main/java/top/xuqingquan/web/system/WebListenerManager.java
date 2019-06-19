package top.xuqingquan.web.system;

import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public interface WebListenerManager {

    @SuppressWarnings("UnusedReturnValue")
    WebListenerManager setWebChromeClient(WebView webview, WebChromeClient webChromeClient);

    @SuppressWarnings("UnusedReturnValue")
    WebListenerManager setWebViewClient(WebView webView, WebViewClient webViewClient);

    WebListenerManager setDownloader(WebView webView, DownloadListener downloadListener);
}
