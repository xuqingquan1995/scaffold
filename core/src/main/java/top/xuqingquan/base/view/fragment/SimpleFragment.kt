package top.xuqingquan.base.view.fragment

import android.content.Context
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import top.xuqingquan.app.ScaffoldConfig
import top.xuqingquan.cache.Cache
import top.xuqingquan.cache.CacheType
import top.xuqingquan.delegate.IFragment
import top.xuqingquan.utils.FragmentOnKeyListener

/**
 * Created by 许清泉 on 2019-04-24 23:38
 */
abstract class SimpleFragment : Fragment(), IFragment, FragmentOnKeyListener {

    private var mCache: Cache<String, Any>? = null
    protected var mContext: Context? = null
    val launchError by lazy {
        MutableLiveData<Throwable>()
    }


    final override fun provideCache(): Cache<String, Any> {
        if (mCache == null) {
            @Suppress("UNCHECKED_CAST")
            mCache = ScaffoldConfig.getCacheFactory()
                .build(CacheType.FRAGMENT_CACHE) as Cache<String, Any>
        }
        return mCache!!
    }

    override fun onAttach(context: Context) {
        this.mContext = context
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
        mContext = null
    }

    /**
     * @return 在fragment中默认使用EventBus
     */
    override fun useEventBus(): Boolean {
        return true
    }

    /**
     * 给子类提供按键监听功能
     *
     * @return null 不拦截 其余跟Activity一致
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean? {
        return null
    }

    override fun setData(data: Any?) {}

}
