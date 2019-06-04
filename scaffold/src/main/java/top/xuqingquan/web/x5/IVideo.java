package top.xuqingquan.web.x5;

import android.view.View;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;

public interface IVideo extends top.xuqingquan.web.agent.IVideo {

    void onShowCustomView(View view, IX5WebChromeClient.CustomViewCallback callback);

}
