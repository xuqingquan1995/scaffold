package top.xuqingquan.stack

/**
 * Created by 许清泉 on 2019-04-24 22:46
 */
interface DebugStackDelegate {

    fun onPostCreate()

    fun showFragmentStackHierarchyView()

    fun logFragmentRecords(tag: String)
}