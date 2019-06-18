package top.xuqingquan.web;

import android.webkit.WebView;
import top.xuqingquan.web.agent.AgentWebUtils;
import top.xuqingquan.web.agent.WebLifeCycle;

public class DefaultWebLifeCycleImpl implements WebLifeCycle {
    private WebView mWebView;

    DefaultWebLifeCycleImpl(WebView webView) {
        this.mWebView = webView;
    }

    @Override
    public void onResume() {
        if (this.mWebView != null) {
            this.mWebView.onResume();
            this.mWebView.resumeTimers();
        }
    }

    @Override
    public void onPause() {
        if (this.mWebView != null) {
            this.mWebView.onPause();
            this.mWebView.pauseTimers();
        }
    }

    @Override
    public void onDestroy() {
        if (this.mWebView != null) {
            this.mWebView.resumeTimers();
        }
        AgentWebUtils.clearWebView(this.mWebView);
    }
}
