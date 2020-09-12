package top.xuqingquan.integration

import android.content.Context
import android.os.Bundle
import android.view.View
import top.xuqingquan.utils.Preconditions
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import top.xuqingquan.cache.Cache
import top.xuqingquan.cache.IntelligentCache
import top.xuqingquan.delegate.FragmentDelegate
import top.xuqingquan.delegate.FragmentDelegateImpl
import top.xuqingquan.delegate.IFragment

/**
 * Created by 许清泉 on 2019/4/14 23:19
 * [FragmentManager.FragmentLifecycleCallbacks] 默认实现类
 * 通过 [FragmentDelegate] 管理 [Fragment]
 */
internal class FragmentLifecycle : FragmentManager.FragmentLifecycleCallbacks() {

    override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
        super.onFragmentAttached(fm, f, context)
        if (f is IFragment) {
            var fragmentDelegate = fetchFragmentDelegate(f)
            if (fragmentDelegate == null || !fragmentDelegate.isAdded()) {
                val cache = getCacheFromFragment(f as IFragment)
                fragmentDelegate = FragmentDelegateImpl(fm, f)
                //使用 IntelligentCache.KEY_KEEP 作为 key 的前缀, 可以使储存的数据永久存储在内存中
                //否则存储在 LRU 算法的存储空间中, 前提是 Fragment 使用的是 IntelligentCache (框架默认使用)
                cache.put(
                    IntelligentCache.getKeyOfKeep(FragmentDelegate.FRAGMENT_DELEGATE),
                    fragmentDelegate
                )
            }
            fragmentDelegate.onAttach(f, context)
        }
    }

    override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        super.onFragmentCreated(fm, f, savedInstanceState)
        val fragmentDelegate = fetchFragmentDelegate(f)
        fragmentDelegate?.onCreate(f, savedInstanceState)
    }

    override fun onFragmentActivityCreated(
        fm: FragmentManager,
        f: Fragment,
        savedInstanceState: Bundle?
    ) {
        super.onFragmentActivityCreated(fm, f, savedInstanceState)
        val fragmentDelegate = fetchFragmentDelegate(f)
        fragmentDelegate?.onActivityCreate(f, savedInstanceState)
    }

    override fun onFragmentViewCreated(
        fm: FragmentManager,
        f: Fragment,
        v: View,
        savedInstanceState: Bundle?
    ) {
        super.onFragmentViewCreated(fm, f, v, savedInstanceState)
        val fragmentDelegate = fetchFragmentDelegate(f)
        fragmentDelegate?.onViewCreated(f, v, savedInstanceState)
    }

    override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
        super.onFragmentStarted(fm, f)
        val fragmentDelegate = fetchFragmentDelegate(f)
        fragmentDelegate?.onStart(f)
    }

    override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
        super.onFragmentResumed(fm, f)
        val fragmentDelegate = fetchFragmentDelegate(f)
        fragmentDelegate?.onResume(f)
    }

    override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
        super.onFragmentPaused(fm, f)
        val fragmentDelegate = fetchFragmentDelegate(f)
        fragmentDelegate?.onPause(f)
    }

    override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
        super.onFragmentStopped(fm, f)
        val fragmentDelegate = fetchFragmentDelegate(f)
        fragmentDelegate?.onStop(f)
    }

    override fun onFragmentSaveInstanceState(fm: FragmentManager, f: Fragment, outState: Bundle) {
        super.onFragmentSaveInstanceState(fm, f, outState)
        val fragmentDelegate = fetchFragmentDelegate(f)
        fragmentDelegate?.onSaveInstanceState(f, outState)
    }

    override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
        super.onFragmentViewDestroyed(fm, f)
        val fragmentDelegate = fetchFragmentDelegate(f)
        fragmentDelegate?.onDestroyView(f)
    }

    override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
        super.onFragmentDestroyed(fm, f)
        val fragmentDelegate = fetchFragmentDelegate(f)
        fragmentDelegate?.onDestroy(f)
    }

    override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
        super.onFragmentDetached(fm, f)
        val fragmentDelegate = fetchFragmentDelegate(f)
        fragmentDelegate?.onDetach(f)
    }

    private fun fetchFragmentDelegate(fragment: Fragment): FragmentDelegate? {
        if (fragment is IFragment) {
            val cache = getCacheFromFragment(fragment as IFragment)
            return cache.get(IntelligentCache.getKeyOfKeep(FragmentDelegate.FRAGMENT_DELEGATE)) as FragmentDelegate?
        }
        return null
    }

    private fun getCacheFromFragment(fragment: IFragment): Cache<String, Any> {
        val cache = fragment.provideCache()
        Preconditions.checkNotNull(cache, Cache::class.java.name + " cannot be null on Fragment")
        return cache
    }
}