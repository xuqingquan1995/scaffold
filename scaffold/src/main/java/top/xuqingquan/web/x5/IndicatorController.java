package top.xuqingquan.web.x5;

import com.tencent.smtt.sdk.WebView;
import top.xuqingquan.web.agent.BaseIndicatorSpec;

public interface IndicatorController extends top.xuqingquan.web.agent.IndicatorController {

    void progress(WebView v, int newProgress);
}
