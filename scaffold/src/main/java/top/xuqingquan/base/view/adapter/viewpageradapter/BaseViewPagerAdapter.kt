package top.xuqingquan.base.view.adapter.viewpageradapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * Created by 许清泉 on 2019/4/13 23:20
 * @param behavior BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT 为只有当前Fragment
 * 会进入 Lifecycle.State#RESUMED ，其他的都在 Lifecycle.State#STARTED
 *
 * BEHAVIOR_SET_USER_VISIBLE_HINT 则需要使用
 * Fragment#setUserVisibleHint(boolean)进行接收可见加载状态
 */
@Deprecated("可能引起内存泄漏，可自行实现")
open class BaseViewPagerAdapter(
    fm: FragmentManager,
    private val fragments: List<Fragment>,
    private val title: List<String>? = null,
    behavior: Int = BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) :
    FragmentPagerAdapter(fm, behavior) {

    override fun getItem(position: Int) = fragments[position]

    override fun getCount() = fragments.size

    override fun getPageTitle(position: Int): CharSequence? {
        return title?.get(position) ?: super.getPageTitle(position)
    }
}