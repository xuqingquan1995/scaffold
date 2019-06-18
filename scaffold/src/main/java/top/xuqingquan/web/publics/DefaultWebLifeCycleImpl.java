package top.xuqingquan.web.publics;

public class DefaultWebLifeCycleImpl implements WebLifeCycle {
    private android.webkit.WebView mWebView;
    private com.tencent.smtt.sdk.WebView mX5WebView;

    public DefaultWebLifeCycleImpl(android.webkit.WebView webView) {
        this.mWebView = webView;
    }

    public DefaultWebLifeCycleImpl(com.tencent.smtt.sdk.WebView webView) {
        this.mX5WebView = webView;
    }

    @Override
    public void onResume() {
        if (AgentWebConfig.hasX5()) {
            if (this.mX5WebView != null) {
                this.mX5WebView.onResume();
                this.mX5WebView.resumeTimers();
            }
        } else {
            if (this.mWebView != null) {
                this.mWebView.onResume();
                this.mWebView.resumeTimers();
            }
        }
    }

    @Override
    public void onPause() {
        if (AgentWebConfig.hasX5()) {
            if (this.mX5WebView != null) {
                this.mX5WebView.onPause();
                this.mX5WebView.pauseTimers();
            }
        } else {
            if (this.mWebView != null) {
                this.mWebView.onPause();
                this.mWebView.pauseTimers();
            }
        }
    }

    @Override
    public void onDestroy() {
        if (AgentWebConfig.hasX5()) {
            if (this.mX5WebView != null) {
                this.mX5WebView.resumeTimers();
            }
            AgentWebUtils.clearWebView(this.mX5WebView);
        } else {
            if (this.mWebView != null) {
                this.mWebView.resumeTimers();
            }
            AgentWebUtils.clearWebView(this.mWebView);
        }
    }
}
