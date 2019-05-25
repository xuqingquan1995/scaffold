package top.xuqingquan.web;

import android.os.Build;
import androidx.collection.ArrayMap;
import com.tencent.smtt.sdk.WebView;
import top.xuqingquan.utils.Timber;


public class WebSecurityLogicImpl implements WebSecurityCheckLogic {

    public static WebSecurityLogicImpl getInstance() {
        return new WebSecurityLogicImpl();
    }

    public WebSecurityLogicImpl() {
    }

    @Override
    public void dealHoneyComb(WebView view) {
        if (Build.VERSION_CODES.HONEYCOMB > Build.VERSION.SDK_INT || Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return;
        }
        view.removeJavascriptInterface("searchBoxJavaBridge_");
        view.removeJavascriptInterface("accessibility");
        view.removeJavascriptInterface("accessibilityTraversal");
    }

    @Override
    public void dealJsInterface(ArrayMap<String, Object> objects, AgentWeb.SecurityType securityType) {
        if (securityType == AgentWeb.SecurityType.STRICT_CHECK
                && AgentWebConfig.WEBVIEW_TYPE != AgentWebConfig.WEBVIEW_AGENTWEB_SAFE_TYPE
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Timber.e("Give up all inject objects");
            objects.clear();
            System.gc();
        }
    }
}
