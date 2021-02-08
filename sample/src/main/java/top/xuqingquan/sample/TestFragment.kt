package top.xuqingquan.sample

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebViewClient
import kotlinx.android.synthetic.main.fragment_test.*
import top.xuqingquan.base.view.fragment.SimpleFragment
import top.xuqingquan.utils.Timber
import top.xuqingquan.utils.toast

class TestFragment : SimpleFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_test,container,false)
    }

    override fun initData(savedInstanceState: Bundle?) {
        toast("fragment")
        val cache=provideCache()
        Timber.d("cache=>${cache.size()}")
        cache.keySet().forEach {
            Timber.d("cache,$it=${cache[it]}")
        }
        webView.loadUrl("https://m.baidu.com/")
        webView.webChromeClient=WebChromeClient()
        webView.webViewClient= WebViewClient()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean? {
        toast("fragment")
        return true
    }
}