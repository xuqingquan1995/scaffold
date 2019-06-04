package top.xuqingquan.web;

import androidx.collection.ArrayMap;
import com.tencent.smtt.sdk.WebView;
import top.xuqingquan.web.agent.SecurityType;
import top.xuqingquan.web.agent.WebSecurityController;

public class WebSecurityControllerImpl implements WebSecurityController<WebSecurityCheckLogic> {

    private WebView mWebView;
    private ArrayMap<String, Object> mMap;
    private SecurityType mSecurityType;

    public WebSecurityControllerImpl(WebView view, ArrayMap<String, Object> map, SecurityType securityType) {
        this.mWebView = view;
        this.mMap = map;
        this.mSecurityType = securityType;
    }

    @Override
    public void check(WebSecurityCheckLogic webSecurityCheckLogic) {
        webSecurityCheckLogic.dealHoneyComb(mWebView);
        if (mMap != null && mSecurityType == SecurityType.STRICT_CHECK && !mMap.isEmpty()) {
            webSecurityCheckLogic.dealJsInterface(mMap, mSecurityType);
        }
    }
}
