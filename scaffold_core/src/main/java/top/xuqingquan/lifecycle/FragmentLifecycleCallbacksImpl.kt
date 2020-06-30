package top.xuqingquan.lifecycle

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import top.xuqingquan.stack.StackList
import top.xuqingquan.utils.Timber

/**
 * Created by 许清泉 on 2019/4/15 00:40
 * 展示[FragmentManager.FragmentLifecycleCallbacks]用法，当前类用来记录fragment栈
 */
class FragmentLifecycleCallbacksImpl : FragmentManager.FragmentLifecycleCallbacks() {
    override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
        super.onFragmentAttached(fm, f, context)
        Timber.v("onFragmentAttached---${f.javaClass.name}")
        StackList.addFragmet(f)
    }

    override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        super.onFragmentCreated(fm, f, savedInstanceState)
        Timber.v("onFragmentCreated---${f.javaClass.name}")
    }

    override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
        super.onFragmentViewCreated(fm, f, v, savedInstanceState)
        Timber.v("onFragmentViewCreated---${f.javaClass.name}")
    }

    override fun onFragmentActivityCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        super.onFragmentActivityCreated(fm, f, savedInstanceState)
        Timber.v("onFragmentActivityCreated---${f.javaClass.name}")
    }

    override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
        super.onFragmentStarted(fm, f)
        Timber.v("onFragmentStarted---${f.javaClass.name}")
    }

    override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
        super.onFragmentResumed(fm, f)
        Timber.v("onFragmentResumed---${f.javaClass.name}")
    }

    override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
        super.onFragmentPaused(fm, f)
        Timber.v("onFragmentPaused---${f.javaClass.name}")
    }

    override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
        super.onFragmentStopped(fm, f)
        Timber.v("onFragmentStopped---${f.javaClass.name}")
    }

    override fun onFragmentSaveInstanceState(fm: FragmentManager, f: Fragment, outState: Bundle) {
        super.onFragmentSaveInstanceState(fm, f, outState)
        Timber.v("onFragmentSaveInstanceState---${f.javaClass.name}")
    }

    override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
        super.onFragmentViewDestroyed(fm, f)
        Timber.v("onFragmentViewDestroyed---${f.javaClass.name}")
    }

    override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
        super.onFragmentDestroyed(fm, f)
        Timber.v("onFragmentDestroyed---${f.javaClass.name}")
    }

    override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
        super.onFragmentDetached(fm, f)
        Timber.v("onFragmentDetached---${f.javaClass.name}")
        StackList.removeFragment(f)
    }
}
