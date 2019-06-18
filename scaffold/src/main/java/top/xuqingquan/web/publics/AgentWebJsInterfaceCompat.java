package top.xuqingquan.web.publics;

import android.app.Activity;
import android.os.Build;
import android.webkit.JavascriptInterface;
import androidx.annotation.RequiresApi;
import top.xuqingquan.utils.Timber;
import top.xuqingquan.web.AgentWeb;

import java.lang.ref.WeakReference;

public class AgentWebJsInterfaceCompat {

    private WeakReference<AgentWeb> mReference;
    private WeakReference<Activity> mActivityWeakReference;

    public AgentWebJsInterfaceCompat(AgentWeb agentWeb, Activity activity) {
        mReference = new WeakReference<>(agentWeb);
        mActivityWeakReference = new WeakReference<>(activity);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @JavascriptInterface
    public void uploadFile() {
        uploadFile("*/*");
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @JavascriptInterface
    public void uploadFile(String acceptType) {
        Timber.i(acceptType + "  " + mActivityWeakReference.get() + "  " + mReference.get());
        if (mActivityWeakReference.get() != null && mReference.get() != null) {
            AgentWebUtils.showFileChooserCompat(mActivityWeakReference.get(),
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
