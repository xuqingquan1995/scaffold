package top.xuqingquan.web;

import androidx.collection.ArrayMap;
import com.tencent.smtt.sdk.WebView;

public class WebSecurityControllerImpl implements WebSecurityController<WebSecurityCheckLogic> {

    private WebView mWebView;
    private ArrayMap<String, Object> mMap;
    private AgentWeb.SecurityType mSecurityType;

    public WebSecurityControllerImpl(WebView view, ArrayMap<String, Object> map, AgentWeb.SecurityType securityType) {
        this.mWebView = view;
        this.mMap = map;
        this.mSecurityType = securityType;
    }

    @Override
    public void check(WebSecurityCheckLogic webSecurityCheckLogic) {
        webSecurityCheckLogic.dealHoneyComb(mWebView);
        if (mMap != null && mSecurityType == AgentWeb.SecurityType.STRICT_CHECK && !mMap.isEmpty()) {
            webSecurityCheckLogic.dealJsInterface(mMap, mSecurityType);
        }
    }
}
