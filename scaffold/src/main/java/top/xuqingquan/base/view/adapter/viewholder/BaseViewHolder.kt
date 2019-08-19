package top.xuqingquan.base.view.adapter.viewholder

import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import org.jetbrains.anko.find

/**
 * Created by 许清泉 on 2019/4/13 23:28
 * 简单实现ViewHolder
 */
open class BaseViewHolder<T>(val view: View) :
    RecyclerView.ViewHolder(view) {

    var onViewClickListener: OnViewClickListener? = null

    /**
     * 设置数据
     * @param data 数据
     * @param position 在RecyclerView中的位置
     */
    open fun setData(data: T?, position: Int) {}

    inline fun <reified V : View> getView(@IdRes viewId: Int): V = view.find(viewId)

    init {
        itemView.setOnClickListener {
            onViewClickListener?.onClick(it, adapterPosition)
        }
        itemView.setOnLongClickListener {
            onViewClickListener?.onLongClick(it, adapterPosition) ?: true
        }
    }

    abstract class OnViewClickListener {
        /**
         * 点击事件
         * @param view 被点击的视图
         * @param position 在RecyclerView中的位置
         */
        abstract fun onClick(view: View, position: Int)

        /**
         * 长按事件
         * @param view 被点击的视图
         * @param position 在RecyclerView中的位置
         */
        open fun onLongClick(view: View, position: Int) = true
    }

    /**
     * 释放资源
     */
    fun onRelease() {
        onViewClickListener = null
    }
}