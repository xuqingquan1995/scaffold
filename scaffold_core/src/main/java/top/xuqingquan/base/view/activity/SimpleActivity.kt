package top.xuqingquan.base.view.activity

import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import top.xuqingquan.app.ScaffoldConfig
import top.xuqingquan.cache.Cache
import top.xuqingquan.cache.CacheType
import top.xuqingquan.delegate.IActivity
import top.xuqingquan.utils.FragmentOnKeyListener

/**
 * Created by 许清泉 on 2019-04-24 23:32
 */
abstract class SimpleActivity : AppCompatActivity(), IActivity {

    private var mCache: Cache<String, Any>? = null
    protected var onKeyListener: FragmentOnKeyListener? = null
    val launchError by lazy {
        MutableLiveData<Throwable>()
    }

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
