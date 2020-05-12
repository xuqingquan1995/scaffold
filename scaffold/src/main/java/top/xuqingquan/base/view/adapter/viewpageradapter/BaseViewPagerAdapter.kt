package top.xuqingquan.base.view.adapter.viewpageradapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter


/**
 * Created by 许清泉 on 2019/4/13 23:20
 */
open class BaseViewPagerAdapter(
    fm: FragmentManager,
    private val fragments: List<Fragment>,
    private val title: List<String>? = null,
    private val behavior: Int? = 1//为了保证跟AndroidX版本没有差异
) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int) = fragments[position]

    override fun getCount() = fragments.size

    override fun getPageTitle(position: Int): CharSequence? {
        return title?.get(position) ?: super.getPageTitle(position)
    }
}