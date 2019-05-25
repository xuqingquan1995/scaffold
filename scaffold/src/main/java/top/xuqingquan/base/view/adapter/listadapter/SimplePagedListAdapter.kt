package top.xuqingquan.base.view.adapter.listadapter

import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import top.xuqingquan.base.view.adapter.viewholder.BaseViewHolder

/**
 * Created by 许清泉 on 2019/4/14 01:37
 */
abstract class SimplePagedListAdapter<T>(diff: DiffUtil.ItemCallback<T>) :
    PagedListAdapter<T, BaseViewHolder<T>>(diff) {

    var listener: OnViewClickListener<T>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<T> {
        val holder = getViewHolder(parent, viewType)
        setOnClickListener(holder, viewType)
        return holder
    }

    protected fun setOnClickListener(
        holder: BaseViewHolder<T>,
        viewType: Int
    ) {
        holder.onViewClickListener = object : BaseViewHolder.OnViewClickListener {
            override fun onClick(view: View, position: Int) {
                if (listener == null) {
                    onClick(view, position, getItem(position), viewType)
                } else {
                    listener!!.onClick(view, position, getItem(position), viewType)
                }
            }

            override fun onLongClick(view: View, position: Int): Boolean {
                return if (listener == null) {
                    onLongClick(view, position, getItem(position), viewType)
                } else {
                    listener!!.onLongClick(view, position, getItem(position), viewType)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        holder.setData(getItem(position)!!, position)
    }

    /**
     * 创建ViewHolder
     */
    abstract fun getViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<T>

    /**
     * 在Adapter内部实现单击回调
     */
    open fun onClick(view: View, position: Int, data: T?, viewType: Int) {

    }

    /**
     * 在Adapter内部实现长按回调
     */
    open fun onLongClick(view: View, position: Int, data: T?, viewType: Int): Boolean {
        return true
    }

    interface OnViewClickListener<T> {
        fun onClick(view: View, position: Int, data: T?, viewType: Int)

        fun onLongClick(view: View, position: Int, data: T?, viewType: Int): Boolean
    }

    companion object {
        @JvmStatic
        fun releaseAllViewHolder(recyclerView: RecyclerView) {
            for (i in recyclerView.childCount - 1 downTo 0) {
                val view = recyclerView.getChildAt(i)
                val holder = recyclerView.getChildViewHolder(view)
                if (holder != null && holder is BaseViewHolder<*>) {
                    holder.onRelease()
                }

            }
        }
    }

}