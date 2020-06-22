package top.xuqingquan.base.view.adapter.listadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import top.xuqingquan.base.view.adapter.viewholder.BaseViewHolder

/**
 * Created by 许清泉 on 2019/4/14 01:37
 */
open class SimplePagedListAdapter<T>(diff: DiffUtil.ItemCallback<T>) :
    PagedListAdapter<T, BaseViewHolder<T>>(diff) {

    var listener: OnItemClickListener<T>? = null
        @Deprecated("改用setOnItemClickListener")
        set

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<T> {
        val holder = getViewHolder(parent, viewType)
        setOnClickListener(holder, viewType)
        return holder
    }

    protected fun setOnClickListener(
        holder: BaseViewHolder<T>,
        viewType: Int
    ) {
        holder.setOnViewClickListener {
            onClick { view, position ->
                if (listener == null) {
                    onClick(view, position, getItem(position), viewType)
                } else {
                    listener!!.onClick(view, position, getItem(position), viewType)
                }
            }

            onLongClick { view, position ->
                return@onLongClick if (listener == null) {
                    onLongClick(view, position, getItem(position), viewType)
                } else {
                    listener!!.onLongClick(view, position, getItem(position), viewType)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        holder.setData(getItem(position), position)
        setData(holder, getItem(position), getItemViewType(position), position)
    }

    /**
     * 创建ViewHolder
     */
    open fun getViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<T> {
        return BaseViewHolder(
            LayoutInflater.from(parent.context).inflate(getLayoutRes(viewType), parent, false)
        )
    }

    /**
     * 默认设置布局的方式
     */
    @LayoutRes
    open fun getLayoutRes(viewType: Int) = 0

    open fun setData(holder: BaseViewHolder<T>, data: T?, viewType: Int, position: Int) {}

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

    override fun getItem(position: Int): T? {
        try {
            if (position >= itemCount) {
                return null
            }
            return super.getItem(position)
        } catch (t: Throwable) {
            return null
        }
    }

    fun setOnItemClickListener(init: OnItemClickListenerImpl<T>.() -> Unit) {
        val listener = OnItemClickListenerImpl<T>()
        listener.init()
        this.listener = listener
    }

}