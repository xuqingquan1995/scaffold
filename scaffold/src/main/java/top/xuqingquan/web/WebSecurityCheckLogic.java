package top.xuqingquan.web;

import androidx.collection.ArrayMap;
import com.tencent.smtt.sdk.WebView;
import top.xuqingquan.web.agent.SecurityType;

public interface WebSecurityCheckLogic {
    void dealHoneyComb(WebView view);

    void dealJsInterface(ArrayMap<String, Object> objects, SecurityType securityType);
}
