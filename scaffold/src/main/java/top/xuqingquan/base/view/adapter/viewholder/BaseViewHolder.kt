package top.xuqingquan.base.view.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by 许清泉 on 2019/4/13 23:28
 * 简单实现ViewHolder
 */
abstract class BaseViewHolder<T>(view: View) :
    RecyclerView.ViewHolder(view) {

    var onViewClickListener: OnViewClickListener? = null

    /**
     * 设置数据
     * @param data 数据
     * @param position 在RecyclerView中的位置
     */
    abstract fun setData(data: T, position: Int)

    init {
        itemView.setOnClickListener {
            onViewClickListener?.onClick(it, adapterPosition)
        }
        itemView.setOnLongClickListener {
            onViewClickListener?.onLongClick(it, adapterPosition) ?: true
        }
    }

    interface OnViewClickListener {
        /**
         * 点击事件
         * @param view 被点击的视图
         * @param position 在RecyclerView中的位置
         */
        fun onClick(view: View, position: Int)

        /**
         * 长按事件
         * @param view 被点击的视图
         * @param position 在RecyclerView中的位置
         */
        fun onLongClick(view: View, position: Int): Boolean
    }

    /**
     * 释放资源
     */
    fun onRelease() {
        onViewClickListener = null
    }
}