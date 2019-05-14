package top.xuqingquan.base.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import top.xuqingquan.app.AppComponentUtils
import top.xuqingquan.base.view.activity.SimpleActivity
import top.xuqingquan.cache.Cache
import top.xuqingquan.cache.CacheType
import top.xuqingquan.delegate.IFragment
import top.xuqingquan.utils.FragmentOnKeyListener

/**
 * Created by 许清泉 on 2019-04-24 23:38
 * 不使用MVVM模式的时候可以使用这个类
 */
abstract class SimpleFragment : Fragment(), IFragment, FragmentOnKeyListener {

    private var mCache: Cache<String, Any>? = null
    var mContext: Context? = null

    /**
     * @return 布局id
     */
    @LayoutRes
    protected abstract fun getLayoutId(): Int

    final override fun provideCache(): Cache<String, Any> {
        if (mCache == null) {
            @Suppress("UNCHECKED_CAST")
            mCache = AppComponentUtils.obtainAppComponentFromContext(mContext!!).cacheFactory()
                .build(CacheType.FRAGMENT_CACHE) as Cache<String, Any>
        }
        return mCache!!
    }

    override fun onAttach(context: Context) {
        this.mContext = context
        super.onAttach(context)
        if (activity is SimpleActivity && activity != null) {
            (activity as SimpleActivity).setFragmentOnKeyListener(this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = initView(inflater, container)
        initView(view)
        return view
    }

    /**
     * 当离开页面当时候自动关闭软键盘
     */
    override fun onPause() {
        super.onPause()
        hideSoftKeyboard()
    }

    /**
     * 给fragment简单的初始化布局
     *
     * @param view
     */
    protected abstract fun initView(view: View)

    protected open fun initView(inflater: LayoutInflater, container: ViewGroup?): View {
        return inflater.inflate(getLayoutId(), container, false)
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

    /**
     * 隐藏软键盘
     */
    fun hideSoftKeyboard() {
        view?.let {
            val imm = ContextCompat.getSystemService(it.context, InputMethodManager::class.java)
            if (imm != null && imm.isActive) {
                imm.hideSoftInputFromWindow(it.windowToken, 0)
            }
        }
    }

    /**
     * 显示软键盘
     * @param view 需要输入的组件
     * @param time 延迟时间，默认200毫秒
     */
    fun showSoftKeyboard(view: View, time: Long = 200L) {
        activity?.let {
            val imm = ContextCompat.getSystemService(it, InputMethodManager::class.java)
            if (imm != null) {
                view.postDelayed({
                    view.requestFocus()
                    imm.showSoftInput(view, InputMethodManager.SHOW_FORCED)
                }, time)
            }
        }
    }
}
