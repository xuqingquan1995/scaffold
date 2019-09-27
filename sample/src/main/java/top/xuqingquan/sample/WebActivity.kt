package top.xuqingquan.sample

import android.os.Bundle
import android.view.KeyEvent
import kotlinx.android.synthetic.main.activity_web.*
import top.xuqingquan.base.view.activity.SimpleActivity

class WebActivity : SimpleActivity() {

    override fun getLayoutId() = R.layout.activity_web

    override fun initData(savedInstanceState: Bundle?) {
        val url = intent.getStringExtra("url")
        if (url.isNullOrEmpty()) {
            webview.loadUrl()
        } else {
            webview.loadUrl(url)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (webview.onKeyDown(keyCode, event)) {
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

}
