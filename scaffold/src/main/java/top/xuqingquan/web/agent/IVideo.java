package top.xuqingquan.web.agent;

import android.view.View;

public interface IVideo {

    void onShowCustomView(View view, android.webkit.WebChromeClient.CustomViewCallback callback);

    void onShowCustomView(View view, com.tencent.smtt.export.external.interfaces.IX5WebChromeClient.CustomViewCallback callback);

    void onHideCustomView();


    boolean isVideoState();

}
