package top.xuqingquan.base.view.adapter.listadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import top.xuqingquan.base.view.adapter.viewholder.BaseViewHolder

/**
 * Created by 许清泉 on 2019/4/13 23:54
 * 简单的可持有数据的RecyclerViewAdapter
 */
@Suppress("NON_FINAL_MEMBER_IN_FINAL_CLASS")
open class SimpleRecyclerAdapter<T>(private val list: MutableList<T>) :
    RecyclerView.Adapter<BaseViewHolder<T>>() {

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
            onClick { view, position ->
                onClick(view, position, getItem(position), viewType)
            }

            onLongClick { view, position ->
                return@onLongClick onLongClick(view, position, getItem(position), viewType)
            }

            onClick { view, position, adapterPosition ->
                onClick(view, position, adapterPosition, getItem(adapterPosition), viewType)
            }

            onLongClick { view, position, adapterPosition ->
                onLongClick(view, position, adapterPosition, getItem(adapterPosition), viewType)
            }
        }
    }

    override fun getItemCount() = list.size

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
     * 单击回调
     */
    @Deprecated("使用五个参数的函数", replaceWith = ReplaceWith("true"))
    open fun onClick(view: View, position: Int, data: T?, viewType: Int) {
        listener?.onClick(view, position, data, viewType)
    }

    /**
     * 长按回调
     */
    @Deprecated("使用五个参数的函数", replaceWith = ReplaceWith("true"))
    open fun onLongClick(view: View, position: Int, data: T?, viewType: Int): Boolean {
        return listener?.onLongClick(view, position, data, viewType) ?: true
    }

    /**
     * 单击回调
     */
    open fun onClick(view: View, position: Int, adapterPosition: Int, data: T?, viewType: Int) {
        listener?.onClick(view, position, adapterPosition, data, viewType)
    }

    /**
     * 长按回调
     */
    open fun onLongClick(
        view: View,
        position: Int,
        adapterPosition: Int,
        data: T?,
        viewType: Int
    ): Boolean {
        return listener?.onLongClick(view, position, adapterPosition, data, viewType) ?: true
    }

    fun getBaseList() = list

    fun getItem(position: Int): T? {
        return try {
            list.getOrNull(position)
        } catch (t: Throwable) {
            null
        }
    }

    fun addList(data: List<T>) {
        list.addAll(data)
        notifyDataSetChanged()
    }

    fun addData(data: T) {
        list.add(data)
        notifyItemInserted(list.size - 1)
    }

    fun removeData(data: T) {
        list.remove(data)
        notifyDataSetChanged()
    }

    fun removeDataAt(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
    }

    fun removeAll() {
        list.clear()
        notifyDataSetChanged()
    }

    fun resetData(list: List<T>) {
        removeAll()
        addList(list)
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