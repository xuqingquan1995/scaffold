package top.xuqingquan.web.system;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import top.xuqingquan.utils.Timber;


public class AgentWebView extends WebView {
    private FixedOnReceivedTitle mFixedOnReceivedTitle;
    private boolean mIsInited;

    public AgentWebView(Context context) {
        this(context, null);
    }

    public AgentWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mIsInited = true;
        mFixedOnReceivedTitle = new FixedOnReceivedTitle();
    }

    @Override
    public final void setWebChromeClient(WebChromeClient client) {
        AgentWebChrome mAgentWebChrome = new AgentWebChrome(this);
        mAgentWebChrome.setDelegate(client);
        mFixedOnReceivedTitle.setWebChromeClient(client);
        super.setWebChromeClient(mAgentWebChrome);
        setWebChromeClientSupport(mAgentWebChrome);
    }

    protected final void setWebChromeClientSupport(WebChromeClient client) {
    }

    @Override
    public final void setWebViewClient(WebViewClient client) {
        AgentWebClient mAgentWebClient = new AgentWebClient(this);
        mAgentWebClient.setDelegate(client);
        super.setWebViewClient(mAgentWebClient);
    }

    @Override
    public void destroy() {
        setVisibility(View.GONE);
        removeAllViewsInLayout();
        fixedStillAttached();
        if (mIsInited) {
            Timber.i("destroy web");
            super.destroy();
        }
    }

    @Override
    public void clearHistory() {
        if (mIsInited) {
            super.clearHistory();
        }
    }

    public static Pair<Boolean, String> isWebViewPackageException(Throwable e) {
        String messageCause = e.getCause() == null ? e.toString() : e.getCause().toString();
        String trace = Log.getStackTraceString(e);
        if (trace.contains("android.content.pm.PackageManager$NameNotFoundException")
                || trace.contains("java.lang.RuntimeException: Cannot load WebView")
                || trace.contains("android.webkit.WebViewFactory$MissingWebViewPackageException: Failed to load WebView provider: No WebView installed")) {
            return new Pair<>(true, "WebView load failed, " + messageCause);
        }
        return new Pair<>(false, messageCause);
    }

    @Override
    public void setOverScrollMode(int mode) {
        try {
            super.setOverScrollMode(mode);
        } catch (Throwable e) {
            Pair<Boolean, String> pair = isWebViewPackageException(e);
            if (pair.first) {
                Toast.makeText(getContext(), pair.second, Toast.LENGTH_SHORT).show();
                destroy();
            } else {
                throw e;
            }
        }
    }

    public static class AgentWebClient extends MiddlewareWebClientBase {

        private AgentWebView mAgentWebView;

        private AgentWebClient(AgentWebView agentWebView) {
            this.mAgentWebView = agentWebView;
        }


        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            mAgentWebView.mFixedOnReceivedTitle.onPageStarted();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mAgentWebView.mFixedOnReceivedTitle.onPageFinished(view);
            Timber.d("onPageFinished.url = " + view.getUrl());
        }


    }

    public static class AgentWebChrome extends MiddlewareWebChromeBase {

        private AgentWebView mAgentWebView;

        private AgentWebChrome(AgentWebView agentWebView) {
            this.mAgentWebView = agentWebView;
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            this.mAgentWebView.mFixedOnReceivedTitle.onReceivedTitle();
            super.onReceivedTitle(view, title);
        }
    }

    /**
     * 解决部分手机webView返回时不触发onReceivedTitle的问题（如：三星SM-G9008V 4.4.2）；
     */
    private static class FixedOnReceivedTitle {
        private WebChromeClient mWebChromeClient;
        private boolean mIsOnReceivedTitle;

        public void setWebChromeClient(WebChromeClient webChromeClient) {
            mWebChromeClient = webChromeClient;
        }

        public void onPageStarted() {
            mIsOnReceivedTitle = false;
        }

        public void onPageFinished(WebView view) {
            if (!mIsOnReceivedTitle && mWebChromeClient != null) {
                WebBackForwardList list = null;
                try {
                    list = view.copyBackForwardList();
                } catch (Throwable e) {
                    Timber.e(e);
                }
                if (list != null
                        && list.getSize() > 0
                        && list.getCurrentIndex() >= 0
                        && list.getItemAtIndex(list.getCurrentIndex()) != null) {
                    String previousTitle = list.getItemAtIndex(list.getCurrentIndex()).getTitle();
                    mWebChromeClient.onReceivedTitle(view, previousTitle);
                }
            }
        }

        public void onReceivedTitle() {
            mIsOnReceivedTitle = true;
        }
    }

    // Activity在onDestory时调用webView的destroy，可以停止播放页面中的音频
    private void fixedStillAttached() {
        // java.lang.Throwable: Error: WebView.destroy() called while still attached!
        // at android.webkit.WebViewClassic.destroy(WebViewClassic.java:4142)
        // at android.webkit.WebView.destroy(WebView.java:707)
        ViewParent parent = getParent();
        if (parent instanceof ViewGroup) { // 由于自定义webView构建时传入了该Activity的context对象，因此需要先从父容器中移除webView，然后再销毁webView；
            ViewGroup mWebViewContainer = (ViewGroup) getParent();
            mWebViewContainer.removeAllViewsInLayout();
        }
    }
}