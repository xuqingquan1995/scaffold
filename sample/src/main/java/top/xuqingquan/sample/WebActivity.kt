package top.xuqingquan.sample

import android.os.Bundle
import android.view.KeyEvent
import kotlinx.android.synthetic.main.activity_web.*
import top.xuqingquan.base.view.activity.SimpleActivity

class WebActivity : SimpleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        initData(savedInstanceState)
    }

    private fun initData(savedInstanceState: Bundle?) {
        val url = "https://tv.cctv.com/2021/07/07/VIDENGCCNuuthbOvE29cn212210707.shtml"
        webview.loadUrl(url)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (webview.onKeyDown(keyCode, event)) {
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

}
