package top.xuqingquan.delegate

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import org.greenrobot.eventbus.EventBus
import top.xuqingquan.base.view.activity.SimpleActivity
import top.xuqingquan.extension.hideSoftKeyboard
import top.xuqingquan.utils.FragmentOnKeyListener
import top.xuqingquan.utils.haveAnnotation

/**
 * Created by 许清泉 on 2019/4/14 13:54
 * FragmentDelegate 默认实现类
 */
class FragmentDelegateImpl(
    private var mFragmentManager: FragmentManager?,
    private var mFragment: Fragment?
) :
    FragmentDelegate {

    private var iFragment: IFragment? = mFragment as IFragment

    override fun onAttach(f: Fragment, context: Context) {
    }

    override fun onCreate(f: Fragment, savedInstanceState: Bundle?) {
        if (iFragment?.useEventBus() == true && haveAnnotation(mFragment!!)) {
            EventBus.getDefault().register(mFragment)
        }
    }

    override fun onViewCreated(f: Fragment, view: View, savedInstanceState: Bundle?) {
    }

    override fun onActivityCreate(f: Fragment, savedInstanceState: Bundle?) {
        iFragment?.initData(savedInstanceState)
    }

    override fun onStart(f: Fragment) {
    }

    override fun onResume(f: Fragment) {
        if (f is FragmentOnKeyListener) {
            f.activity?.let {
                if (it is SimpleActivity) {
                    it.setFragmentOnKeyListener(f)
                }
            }
        }
    }

    override fun onPause(f: Fragment) {
        f.activity?.apply {
            hideSoftKeyboard()
            if (this is SimpleActivity) {
                this.setFragmentOnKeyListener(null)
            }
        }
    }

    override fun onStop(f: Fragment) {
    }

    override fun onDestroyView(f: Fragment) {
    }

    override fun onDestroy(f: Fragment) {
        if (iFragment?.useEventBus() == true && haveAnnotation(mFragment!!)) {
            EventBus.getDefault().unregister(mFragment)
        }
        iFragment = null
        mFragment = null
        mFragmentManager = null
    }

    override fun onDetach(f: Fragment) {
    }

    override fun isAdded() = mFragment?.isAdded == true

    override fun onSaveInstanceState(f: Fragment, outState: Bundle) {
    }
}