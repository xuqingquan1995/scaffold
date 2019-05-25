package top.xuqingquan.web;

import androidx.collection.ArrayMap;
import com.tencent.smtt.sdk.WebView;

public interface WebSecurityCheckLogic {
    void dealHoneyComb(WebView view);

    void dealJsInterface(ArrayMap<String, Object> objects, AgentWeb.SecurityType securityType);
}
