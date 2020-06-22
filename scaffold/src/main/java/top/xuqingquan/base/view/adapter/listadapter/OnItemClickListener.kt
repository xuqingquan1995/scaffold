package top.xuqingquan.base.view.adapter.listadapter

import android.view.View

/**
 * Create by 许清泉 on 2020/6/22 23:02
 */
abstract class OnItemClickListener<T> {

    /**
     * @param view 实际点击的view
     * @param position 被点击的item
     * @param data 被点击的数据内容
     * @param viewType 被点击的viewType
     */
    abstract fun onClick(view: View, position: Int, data: T?, viewType: Int)

    /**
     * @param view 实际点击的view
     * @param position 被点击的item
     * @param data 被点击的数据内容
     * @param viewType 被点击的viewType
     * @return 长按事件常规返回
     */
    open fun onLongClick(view: View, position: Int, data: T?, viewType: Int) = true
}