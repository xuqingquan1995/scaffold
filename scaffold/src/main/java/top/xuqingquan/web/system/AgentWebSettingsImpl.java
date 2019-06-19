package top.xuqingquan.web.system;

import android.app.Activity;
import android.app.DownloadManager;
import android.net.Uri;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import androidx.core.content.ContextCompat;
import top.xuqingquan.utils.FileUtils;
import top.xuqingquan.utils.Timber;
import top.xuqingquan.web.AgentWeb;

public class AgentWebSettingsImpl extends AbsAgentWebSettings {
    private AgentWeb mAgentWeb;

    @Override
    protected void bindAgentWebSupport(AgentWeb agentWeb) {
        this.mAgentWeb = agentWeb;
    }

    @Override
    public WebListenerManager setDownloader(WebView webView, DownloadListener downloadListener) {
        DownloadListener listener = downloadListener;
        try {
            Class.forName("com.download.library.DownloadTask");//如果有依赖下载库则使用下载库，否则使用系统的
            if (mAgentWeb != null) {
                listener = DefaultDownloadImpl.create((Activity) webView.getContext(), webView, null, mAgentWeb.getPermissionInterceptor());
            }
        } catch (Throwable t) {
            Timber.e(t);
            try {
                listener = (url, userAgent, contentDisposition, mimetype, contentLength) -> {
                    DownloadManager downloadManager = ContextCompat.getSystemService(webView.getContext(), DownloadManager.class);
                    if (downloadManager != null) {
                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        request.setDestinationInExternalPublicDir(FileUtils.getCacheFile(webView.getContext()).getAbsolutePath(), url.substring(url.lastIndexOf("/") + 1));
                        request.setVisibleInDownloadsUi(true);
                        downloadManager.enqueue(request);
                    }
                };
            } catch (Throwable tt) {
                Timber.e(tt);
            }
        }
        return super.setDownloader(webView, listener);
    }
}
