package top.xuqingquan.base.view.adapter.viewholder

import android.util.SparseArray
import android.view.View
import android.widget.Checkable
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import top.xuqingquan.base.view.adapter.listadapter.OnViewClickListener
import top.xuqingquan.base.view.adapter.listadapter.OnViewClickListenerImpl

/**
 * Created by 许清泉 on 2019/4/13 23:28
 * 简单实现ViewHolder
 */
open class BaseViewHolder<T>(_view: View) :
    RecyclerView.ViewHolder(_view) {

    /**
     * Views indexed with their IDs
     */
    private val views = SparseArray<View>()

    private var onViewClickListener: OnViewClickListener? = null

    /**
     * 设置数据
     * @param data 数据
     * @param position 在RecyclerView中的位置
     */
    open fun setData(data: T?, position: Int) {}

    @Suppress("UNCHECKED_CAST")
    fun <V : View> getView(@IdRes viewId: Int): V {
        var view = views.get(viewId)
        if (view == null) {
            view = itemView.findViewById(viewId)
            views.put(viewId, view)
        }
        return view as V
    }

    /**
     * 获取view并设置点击事件
     */
    fun <V : View> getClickedView(@IdRes viewId: Int): V {
        val view = getView<V>(viewId)
        if (view !is Checkable) {//如果不是可选中的组件，默认添加点击事件
            view.setOnClickListener {
                onViewClickListener?.onClick(it, absoluteAdapterPosition, bindingAdapterPosition)
            }
            view.setOnLongClickListener {
                return@setOnLongClickListener onViewClickListener?.onLongClick(
                    it,
                    absoluteAdapterPosition,
                    bindingAdapterPosition
                ) ?: true
            }
        }
        return view
    }

    init {
        itemView.setOnClickListener {
            onViewClickListener?.onClick(it, absoluteAdapterPosition, bindingAdapterPosition)
        }
        itemView.setOnLongClickListener {
            return@setOnLongClickListener onViewClickListener?.onLongClick(
                it,
                absoluteAdapterPosition,
                bindingAdapterPosition
            ) ?: true
        }
    }

    fun setOnViewClickListener(init: OnViewClickListenerImpl.() -> Unit) {
        val listener = OnViewClickListenerImpl()
        listener.init()
        this.onViewClickListener = listener
    }

}