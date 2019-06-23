package top.xuqingquan.delegate

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import org.greenrobot.eventbus.EventBus
import top.xuqingquan.utils.EventBusHelper

/**
 * Created by 许清泉 on 2019/4/14 13:54
 * FragmentDelegate 默认实现类
 */
class FragmentDelegateImpl(private var mFragmentManager: FragmentManager?, private var mFragment: Fragment?) :
    FragmentDelegate {

    private var iFragment: IFragment? = mFragment as IFragment

    override fun onAttach(context: Context) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (iFragment?.useEventBus() == true && EventBusHelper.haveAnnotation(mFragment!!)) {
            EventBus.getDefault().register(mFragment)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) {
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    }

    override fun onActivityCreate(savedInstanceState: Bundle?) {
        iFragment?.initData(savedInstanceState)
    }

    override fun onStart() {
    }

    override fun onResume() {
    }

    override fun onPause() {
    }

    override fun onStop() {
    }

    override fun onDestroyView() {
    }

    override fun onDestroy() {
        if (iFragment?.useEventBus() == true) {
            EventBus.getDefault().unregister(mFragment)
        }
        iFragment = null
        mFragment = null
        mFragmentManager = null
    }

    override fun onDetach() {
    }

    override fun isAdded() = mFragment?.isAdded == true

    override fun onSaveInstanceState(outState: Bundle) {
    }
}