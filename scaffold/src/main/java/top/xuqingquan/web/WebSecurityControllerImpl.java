package top.xuqingquan.web;

import com.tencent.smtt.sdk.WebView;
import top.xuqingquan.web.agent.WebSecurityController;

public class WebSecurityControllerImpl implements WebSecurityController<WebSecurityCheckLogic> {

    private WebView mWebView;

    public WebSecurityControllerImpl(WebView view) {
        this.mWebView = view;
    }

    @Override
    public void check(WebSecurityCheckLogic webSecurityCheckLogic) {
        webSecurityCheckLogic.dealHoneyComb(mWebView);
    }
}
