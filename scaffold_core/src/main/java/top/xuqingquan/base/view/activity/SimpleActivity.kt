package top.xuqingquan.base.view.activity

import android.os.Bundle
import android.view.KeyEvent
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import top.xuqingquan.app.ScaffoldConfig
import top.xuqingquan.extension.hideSoftKeyboard
import top.xuqingquan.cache.Cache
import top.xuqingquan.cache.CacheType
import top.xuqingquan.delegate.IActivity
import top.xuqingquan.utils.FragmentOnKeyListener

/**
 * Created by 许清泉 on 2019-04-24 23:32
 * 不使用MVVM模式的时候可以使用这个类
 */
abstract class SimpleActivity : AppCompatActivity(), IActivity {

    private var mCache: Cache<String, Any>? = null
    protected var onKeyListener: FragmentOnKeyListener? = null

    /**
     * @return 布局id
     */
    @LayoutRes
    protected abstract fun getLayoutId(): Int

    final override fun provideCache(): Cache<String, Any> {
        if (mCache == null) {
            @Suppress("UNCHECKED_CAST")
            mCache =
                ScaffoldConfig.getCacheFactory().build(CacheType.ACTIVITY_CACHE) as Cache<String, Any>
        }
        return mCache!!
    }

    override fun useEventBus(): Boolean {
        return true
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView(savedInstanceState)
    }

    override fun onPause() {
        super.onPause()
        hideSoftKeyboard()
    }

    /**
     * 方便给子类继承选择是否使用这种方式填充布局
     * @param savedInstanceState
     */
    protected open fun initView(savedInstanceState: Bundle?) {
        setContentView(getLayoutId())
        initData(savedInstanceState)
    }

    override fun setFragmentOnKeyListener(listener: FragmentOnKeyListener?) {
        this.onKeyListener = listener
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (onKeyListener != null) {
            val onKeyDown = onKeyListener!!.onKeyDown(keyCode, event)
            if (onKeyDown != null) {
                return onKeyDown
            }
        }
        return super.onKeyDown(keyCode, event)
    }
}
