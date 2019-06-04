package top.xuqingquan.web;

import android.app.Activity;
import android.webkit.JavascriptInterface;
import top.xuqingquan.utils.Timber;
import top.xuqingquan.web.x5.X5WebUtils;

import java.lang.ref.WeakReference;

public class AgentWebJsInterfaceCompat {

    private WeakReference<AgentWeb> mReference;
    private WeakReference<Activity> mActivityWeakReference;

    AgentWebJsInterfaceCompat(AgentWeb agentWeb, Activity activity) {
        mReference = new WeakReference<>(agentWeb);
        mActivityWeakReference = new WeakReference<>(activity);
    }

    @JavascriptInterface
    public void uploadFile() {
        uploadFile("*/*");
    }

    @JavascriptInterface
    public void uploadFile(String acceptType) {
        Timber.i(acceptType + "  " + mActivityWeakReference.get() + "  " + mReference.get());
        if (mActivityWeakReference.get() != null && mReference.get() != null) {
            X5WebUtils.showFileChooserCompat(mActivityWeakReference.get(),
                    mReference.get().getWebCreator().getWebView(),
                    null,
                    null,
                    mReference.get().getPermissionInterceptor(),
                    null,
                    acceptType,
                    msg -> {
                        if (mReference.get() != null) {
                            mReference.get().getJsAccessEntrace()
                                    .quickCallJs("uploadFileResult",
                                            msg.obj instanceof String ? (String) msg.obj : null);
                        }
                        return true;
                    }
            );
        }
    }
}
