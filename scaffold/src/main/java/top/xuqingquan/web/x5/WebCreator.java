package top.xuqingquan.web.x5;

import com.tencent.smtt.sdk.WebView;

public interface WebCreator extends top.xuqingquan.web.agent.WebCreator {

    WebCreator create();

    WebView getWebView();
}
