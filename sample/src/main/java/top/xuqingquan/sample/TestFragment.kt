package top.xuqingquan.sample

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import top.xuqingquan.base.view.fragment.SimpleFragment
import top.xuqingquan.utils.Timber
import top.xuqingquan.utils.toast

class TestFragment : SimpleFragment() {

    override fun getLayoutId()=R.layout.fragment_test

    override fun initView(view: View) {
    }

    override fun initData(savedInstanceState: Bundle?) {
        toast("fragment")
        val cache=provideCache()
        Timber.d("cache=>${cache.size()}")
        cache.keySet().forEach {
            Timber.d("cache,$it=${cache[it]}")
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean? {
        toast("fragment")
        return true
    }
}