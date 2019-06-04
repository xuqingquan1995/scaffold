package top.xuqingquan.web.x5;

import android.app.Activity;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.WebView;
import top.xuqingquan.web.AgentWeb;
import top.xuqingquan.web.DefaultDownloadImpl;

public class AgentWebSettingsImpl extends AbsAgentWebSettings {
    private AgentWeb mAgentWeb;

    @Override
    protected void bindAgentWebSupport(AgentWeb agentWeb) {
        this.mAgentWeb = agentWeb;
    }

    @Override
    public WebListenerManager setDownloader(WebView webView, DownloadListener downloadListener) {
        DownloadListener listener = DefaultDownloadImpl.create((Activity) webView.getContext(), webView, null, mAgentWeb.getPermissionInterceptor());
        return super.setDownloader(webView, listener);
    }
}
