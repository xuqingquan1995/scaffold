package top.xuqingquan.web;

import android.os.Handler;
import android.os.Looper;
import android.webkit.WebView;
import top.xuqingquan.utils.Timber;

import java.util.Map;

public class UrlLoaderImpl implements IUrlLoader {
	private Handler mHandler;
	private WebView mWebView;
	private HttpHeaders mHttpHeaders;
	public static final String TAG = UrlLoaderImpl.class.getSimpleName();

	UrlLoaderImpl(WebView webView, HttpHeaders httpHeaders) {
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

	@Override
	public void loadUrl(String url) {
		this.loadUrl(url, this.mHttpHeaders.getHeaders(url));
	}

	@Override
	public void loadUrl(final String url, final Map<String, String> headers) {
		if (!AgentWebUtils.isUIThread()) {
			AgentWebUtils.runInUiThread(() -> loadUrl(url, headers));
		}
		Timber.i( "loadUrl:" + url + " headers:" + headers);
		if (headers == null || headers.isEmpty()) {
			this.mWebView.loadUrl(url);
		} else {
			this.mWebView.loadUrl(url, headers);
		}
	}

	@Override
	public void reload() {
		if (!AgentWebUtils.isUIThread()) {
			mHandler.post(this::reload);
			return;
		}
		this.mWebView.reload();
	}

	@Override
	public void loadData(final String data, final String mimeType, final String encoding) {
		if (!AgentWebUtils.isUIThread()) {
			mHandler.post(() -> loadData(data, mimeType, encoding));
			return;
		}
		this.mWebView.loadData(data, mimeType, encoding);
	}

	@Override
	public void stopLoading() {
		if (!AgentWebUtils.isUIThread()) {
			mHandler.post(this::stopLoading);
			return;
		}
		this.mWebView.stopLoading();
	}

	@Override
	public void loadDataWithBaseURL(final String baseUrl, final String data, final String mimeType, final String encoding, final String historyUrl) {
		if (!AgentWebUtils.isUIThread()) {
			mHandler.post(() -> loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl));
			return;
		}
		this.mWebView.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl);
	}

	@Override
	public void postUrl(final String url, final byte[] postData) {
		if (!AgentWebUtils.isUIThread()) {
			mHandler.post(() -> postUrl(url, postData));
			return;
		}
		this.mWebView.postUrl(url, postData);
	}

	@Override
	public HttpHeaders getHttpHeaders() {
		return this.mHttpHeaders == null ? this.mHttpHeaders = HttpHeaders.create() : this.mHttpHeaders;
	}
}
