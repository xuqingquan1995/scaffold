package top.xuqingquan.web.x5;

import com.tencent.smtt.sdk.WebView;
import top.xuqingquan.web.agent.EventInterceptor;

/**
 * IEventHandler 对事件的处理，主要是针对
 * 视屏状态进行了处理 ， 如果当前状态为 视频状态
 * 则先退出视频。
 */
public class EventHandlerImpl extends top.xuqingquan.web.agent.EventHandlerImpl {
    private WebView mWebView;

    public static EventHandlerImpl getInstantce(WebView view, EventInterceptor eventInterceptor) {
        return new EventHandlerImpl(view, eventInterceptor);
    }

    public EventHandlerImpl(WebView webView, EventInterceptor eventInterceptor) {
        super(eventInterceptor);
        this.mWebView = webView;
    }

    @Override
    public boolean back() {
        if (super.back()) {
            return true;
        }
        if (mWebView != null && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return false;
    }

}
