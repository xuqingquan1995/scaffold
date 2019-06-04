package top.xuqingquan.web;

import com.tencent.smtt.sdk.WebView;
import top.xuqingquan.web.agent.WebLifeCycle;
import top.xuqingquan.web.x5.X5WebUtils;

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
        X5WebUtils.clearWebView(this.mWebView);
    }
}
