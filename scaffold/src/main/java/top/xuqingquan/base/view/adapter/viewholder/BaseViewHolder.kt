package top.xuqingquan.base.view.adapter.viewholder

import android.util.SparseArray
import android.support.annotation.IdRes
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by 许清泉 on 2019/4/13 23:28
 * 简单实现ViewHolder
 */
open class BaseViewHolder<T>(_view: View) :
    RecyclerView.ViewHolder(_view) {

    /**
     * Views indexed with their IDs
     */
    val views = SparseArray<View>()

    var onViewClickListener: OnViewClickListener? = null

    /**
     * 设置数据
     * @param data 数据
     * @param position 在RecyclerView中的位置
     */
    open fun setData(data: T?, position: Int) {}

    inline fun <reified V : View> getView(@IdRes viewId: Int): V {
        var view = views.get(viewId)
        if (view == null) {
            view = itemView.findViewById(viewId)
            views.put(viewId, view)
        }
        return view as V
    }

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