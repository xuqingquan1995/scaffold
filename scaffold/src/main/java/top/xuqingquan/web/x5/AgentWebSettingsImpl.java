package top.xuqingquan.web.x5;

import android.app.Activity;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.WebView;
import top.xuqingquan.utils.Timber;
import top.xuqingquan.web.AgentWeb;
import top.xuqingquan.web.publics.PermissionInterceptor;

public class AgentWebSettingsImpl extends AbsAgentWebSettings {
    private AgentWeb mAgentWeb;

    @Override
    protected void bindAgentWebSupport(AgentWeb agentWeb) {
        this.mAgentWeb = agentWeb;
    }

    @Override
    public WebListenerManager setDownloader(WebView webView, DownloadListener downloadListener) {
        //TODO 改用系统下载
        Class<?> clazz;
        Object mDefaultDownloadImpl$Extra = null;
        try {
            clazz = Class.forName("top.xuqingquan.web.download.DefaultDownloadImpl");
            mDefaultDownloadImpl$Extra =
                    clazz.getDeclaredMethod("create", Activity.class, WebView.class,
                            Class.forName("top.xuqingquan.web.download.DownloadListener"),
                            PermissionInterceptor.class)
                            .invoke(mDefaultDownloadImpl$Extra, webView.getContext()
                                    , webView, null, mAgentWeb.getPermissionInterceptor());

        } catch (Throwable t) {
            Timber.e(t);
        }
        return super.setDownloader(webView, mDefaultDownloadImpl$Extra == null ? downloadListener : (DownloadListener) mDefaultDownloadImpl$Extra);
    }
}
