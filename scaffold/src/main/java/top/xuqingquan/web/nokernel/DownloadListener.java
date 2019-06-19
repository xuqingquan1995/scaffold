package top.xuqingquan.web.nokernel;

import android.net.Uri;
import androidx.annotation.MainThread;
import top.xuqingquan.web.publics.Extra;

/**
 * Created by 许清泉 on 2019-06-19 23:24
 */
public class DownloadListener {
    @MainThread
    public boolean onStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength, Extra extra) {
        return false;
    }

    public void onProgress(String url, long downloaded, long length, long usedTime) {
    }

    @MainThread
    public boolean onResult(Throwable throwable, Uri path, String url, Extra extra) {
        return false;
    }
}
