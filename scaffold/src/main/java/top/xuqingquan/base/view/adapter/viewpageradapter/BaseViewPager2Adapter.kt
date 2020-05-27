package top.xuqingquan.base.view.adapter.viewpageradapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * Created by 许清泉 on 2019-05-25 18:44
 */
@Deprecated("可能引起内存泄漏，可自行实现")
open class BaseViewPager2Adapter : FragmentStateAdapter {

    private val fragments: List<Fragment>

    constructor(fragmentActivity: FragmentActivity, fragments: List<Fragment>) : super(fragmentActivity) {
        this.fragments = fragments
    }

    constructor(fragment: Fragment, fragments: List<Fragment>) : super(fragment) {
        this.fragments = fragments
    }

    constructor(fm: FragmentManager, lifecycle: Lifecycle, fragments: List<Fragment>) : super(fm, lifecycle) {
        this.fragments = fragments
    }

    override fun createFragment(position: Int) = fragments[position]

    override fun getItemCount() = fragments.size


}