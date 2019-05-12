package top.xuqingquan.base.view.adapter.viewpageradapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

/**
 * Created by 许清泉 on 2019/4/13 23:20
 */
open class BaseViewPagerAdapter(
    fm: FragmentManager,
    private val fragments: List<Fragment>,
    private val title: List<String>? = null
) :
    FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int) = fragments[position]

    override fun getCount() = fragments.size

    override fun getPageTitle(position: Int): CharSequence? {
        return title?.get(position) ?: super.getPageTitle(position)
    }
}