package top.xuqingquan.base.view.adapter.viewpageradapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter


/**
 * Created by 许清泉 on 2019/4/13 23:20
 */
@Deprecated("可能引起内存泄漏，可自行实现")
open class BaseViewPagerAdapter(
    fm: FragmentManager,
    private val fragments: List<Fragment>,
    private val title: List<String>? = null
) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int) = fragments[position]

    override fun getCount() = fragments.size

    override fun getPageTitle(position: Int): CharSequence? {
        return title?.get(position) ?: super.getPageTitle(position)
    }
}