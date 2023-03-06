package top.xuqingquan.base.view.adapter.listadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import top.xuqingquan.base.view.adapter.viewholder.BaseViewHolder

/**
 * Created by 许清泉 on 2019/4/14 01:37
 */
open class SimplePagedListAdapter<T : Any>(diff: DiffUtil.ItemCallback<T>) :
    PagingDataAdapter<T, BaseViewHolder<T>>(diff) {

    private var listener: OnItemClickListener<T>? = null

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
            onClick { view, position, adapterPosition ->
                onClick(view, position, adapterPosition, getItem(adapterPosition), viewType)
            }

            onLongClick { view, position, adapterPosition ->
                return@onLongClick onLongClick(
                    view,
                    position,
                    adapterPosition,
                    getItem(adapterPosition),
                    viewType
                )
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
    open fun onClick(view: View, position: Int, adapterPosition: Int, data: T?, viewType: Int) {
        listener?.onClick(view, position, adapterPosition, data, viewType)
    }

    /**
     * 在Adapter内部实现长按回调
     */
    open fun onLongClick(
        view: View,
        position: Int,
        adapterPosition: Int,
        data: T?,
        viewType: Int
    ): Boolean {
        return listener?.onLongClick(
            view,
            position,
            adapterPosition,
            data,
            viewType
        ) ?: true
    }

    fun setOnItemClickListener(init: OnItemClickListenerImpl<T>.() -> Unit) {
        val listener = OnItemClickListenerImpl<T>()
        listener.init()
        setOnItemClickListener(listener)
    }

    fun setOnItemClickListener(listener: OnItemClickListener<T>){
        this.listener = listener
    }

}