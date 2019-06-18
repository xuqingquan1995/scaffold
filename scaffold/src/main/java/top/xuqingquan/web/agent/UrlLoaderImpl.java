package top.xuqingquan.web.agent;

import android.os.Handler;
import android.os.Looper;
import top.xuqingquan.utils.Timber;

import java.util.Map;

public class UrlLoaderImpl implements IUrlLoader {
    private Handler mHandler;
    private android.webkit.WebView mWebView;
    private com.tencent.smtt.sdk.WebView mx5WebView;
    private HttpHeaders mHttpHeaders;

    public UrlLoaderImpl(android.webkit.WebView webView, HttpHeaders httpHeaders) {
        this.mWebView = webView;
        if (this.mWebView == null) {
            throw new NullPointerException("webview cannot be null .");
        }
        this.mHttpHeaders = httpHeaders;
        if (this.mHttpHeaders == null) {
            this.mHttpHeaders = HttpHeaders.create();
        }
        mHandler = new Handler(Looper.getMainLooper());
    }

    UrlLoaderImpl(com.tencent.smtt.sdk.WebView webView, HttpHeaders httpHeaders) {
        this.mx5WebView = webView;
        if (this.mx5WebView == null) {
            throw new NullPointerException("webview cannot be null .");
        }
        this.mHttpHeaders = httpHeaders;
        if (this.mHttpHeaders == null) {
            this.mHttpHeaders = HttpHeaders.create();
        }
        mHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void loadUrl(String url) {
        this.loadUrl(url, this.mHttpHeaders.getHeaders(url));
    }

    @Override
    public void loadUrl(final String url, final Map<String, String> headers) {
        if (!AgentWebUtils.isUIThread()) {
            AgentWebUtils.runInUiThread(() -> loadUrl(url, headers));
        }
        Timber.i("loadUrl:" + url + " headers:" + headers);
        if (AgentWebConfig.hasX5()) {
            if (headers == null || headers.isEmpty()) {
                this.mx5WebView.loadUrl(url);
            } else {
                this.mx5WebView.loadUrl(url, headers);
            }
        } else {
            if (headers == null || headers.isEmpty()) {
                this.mWebView.loadUrl(url);
            } else {
                this.mWebView.loadUrl(url, headers);
            }
        }
    }

    @Override
    public void reload() {
        if (!AgentWebUtils.isUIThread()) {
            mHandler.post(this::reload);
            return;
        }
        if (AgentWebConfig.hasX5()) {
            this.mx5WebView.reload();
        } else {
            this.mWebView.reload();
        }
    }

    @Override
    public void loadData(final String data, final String mimeType, final String encoding) {
        if (!AgentWebUtils.isUIThread()) {
            mHandler.post(() -> loadData(data, mimeType, encoding));
            return;
        }
        if (AgentWebConfig.hasX5()) {
            this.mx5WebView.loadData(data, mimeType, encoding);
        } else {
            this.mWebView.loadData(data, mimeType, encoding);
        }
    }

    @Override
    public void stopLoading() {
        if (!AgentWebUtils.isUIThread()) {
            mHandler.post(this::stopLoading);
            return;
        }
        if (AgentWebConfig.hasX5()) {
            this.mx5WebView.stopLoading();
        } else {
            this.mWebView.stopLoading();
        }
    }

    @Override
    public void loadDataWithBaseURL(final String baseUrl, final String data, final String mimeType, final String encoding, final String historyUrl) {
        if (!AgentWebUtils.isUIThread()) {
            mHandler.post(() -> loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl));
            return;
        }
        if (AgentWebConfig.hasX5()) {
            this.mx5WebView.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl);
        } else {
            this.mWebView.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl);
        }
    }

    @Override
    public void postUrl(final String url, final byte[] postData) {
        if (!AgentWebUtils.isUIThread()) {
            mHandler.post(() -> postUrl(url, postData));
            return;
        }
        if (AgentWebConfig.hasX5()) {
            this.mx5WebView.postUrl(url, postData);
        } else {
            this.mWebView.postUrl(url, postData);
        }
    }

    @Override
    public HttpHeaders getHttpHeaders() {
        return this.mHttpHeaders == null ? this.mHttpHeaders = HttpHeaders.create() : this.mHttpHeaders;
    }
}
