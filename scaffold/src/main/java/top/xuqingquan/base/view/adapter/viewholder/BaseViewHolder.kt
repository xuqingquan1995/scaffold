package top.xuqingquan.base.view.adapter.viewholder

import android.util.SparseArray
import android.view.View
import android.widget.Checkable
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView

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
            if (view !is Checkable) {//如果不是可选中的组件，默认添加点击事件
                view?.setOnClickListener {
                    onViewClickListener?.onClick(it, absoluteAdapterPosition)
                }
                view?.setOnLongClickListener {
                    onViewClickListener?.onLongClick(it, absoluteAdapterPosition) ?: true
                }
            }
        }
        return view as V
    }

    init {
        itemView.setOnClickListener {
            onViewClickListener?.onClick(it, absoluteAdapterPosition)
        }
        itemView.setOnLongClickListener {
            onViewClickListener?.onLongClick(it, absoluteAdapterPosition) ?: true
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

    fun setOnViewClickListener(onViewClickListener: OnViewClickListener) {
        this.onViewClickListener = onViewClickListener
    }

}