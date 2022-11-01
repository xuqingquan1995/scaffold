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
    @Deprecated("使用五个参数的函数", ReplaceWith("true"))
    open fun onClick(view: View, position: Int, data: T?, viewType: Int) {
    }

    /**
     * @param view 实际点击的view
     * @param position 被点击的item
     * @param data 被点击的数据内容
     * @param viewType 被点击的viewType
     * @return 长按事件常规返回
     */
    @Deprecated("使用五个参数的函数", ReplaceWith("true"))
    open fun onLongClick(view: View, position: Int, data: T?, viewType: Int) = true


    /**
     * @param view 实际点击的view
     * @param position 被点击的item
     * @param adapterPosition 在当前adapter中的位置
     * @param data 被点击的数据内容
     * @param viewType 被点击的viewType
     */
    abstract fun onClick(view: View, position: Int, adapterPosition: Int, data: T?, viewType: Int)

    /**
     * @param view 实际点击的view
     * @param position 被点击的item
     * @param adapterPosition 在当前adapter中的位置
     * @param data 被点击的数据内容
     * @param viewType 被点击的viewType
     * @return 长按事件常规返回
     */
    open fun onLongClick(view: View, position: Int, adapterPosition: Int, data: T?, viewType: Int) =
        true
}

class OnItemClickListenerImpl<T> : OnItemClickListener<T>() {

    @Deprecated("使用onAdapterClick替代")
    private var onClick: ((view: View, position: Int, data: T?, viewType: Int) -> Unit)? = null

    @Deprecated("使用onAdapterLongClick替代")
    private var onLongClick: ((view: View, position: Int, data: T?, viewType: Int) -> Boolean)? =
        null
    private var onAdapterClick: ((view: View, position: Int, adapterPosition: Int, data: T?, viewType: Int) -> Unit)? =
        null
    private var onAdapterLongClick: ((view: View, position: Int, adapterPosition: Int, data: T?, viewType: Int) -> Boolean)? =
        null

    override fun onClick(view: View, position: Int, data: T?, viewType: Int) {
        onClick?.invoke(view, position, data, viewType)
    }

    override fun onLongClick(view: View, position: Int, data: T?, viewType: Int): Boolean {
        return onLongClick?.invoke(view, position, data, viewType) ?: true
    }

    override fun onClick(view: View, position: Int, adapterPosition: Int, data: T?, viewType: Int) {
        onAdapterClick?.invoke(view, position, adapterPosition, data, viewType)
    }

    override fun onLongClick(
        view: View, position: Int, adapterPosition: Int, data: T?, viewType: Int
    ): Boolean {
        return onAdapterLongClick?.invoke(view, position, adapterPosition, data, viewType) ?: true
    }

    @Deprecated("使用五个参数的函数替代")
    fun onClick(l: (view: View, position: Int, data: T?, viewType: Int) -> Unit) {
        onClick = l
    }

    @Deprecated("使用五个参数的函数替代")
    fun onLongClick(l: ((view: View, position: Int, data: T?, viewType: Int) -> Boolean)) {
        onLongClick = l
    }

    fun onClick(l: (view: View, position: Int, adapterPosition: Int, data: T?, viewType: Int) -> Unit) {
        onAdapterClick = l
    }

    fun onLongClick(l: ((view: View, position: Int, adapterPosition: Int, data: T?, viewType: Int) -> Boolean)) {
        onAdapterLongClick = l
    }
}

abstract class OnViewClickListener {
    /**
     * 点击事件
     * @param view 被点击的视图
     * @param position 在RecyclerView中的位置
     */
    @Deprecated("替换为三个参数的函数", ReplaceWith("true"))
    open fun onClick(view: View, position: Int) {
    }

    /**
     * 长按事件
     * @param view 被点击的视图
     * @param position 在RecyclerView中的位置
     */
    @Deprecated("替换为三个参数的函数", ReplaceWith("true"))
    open fun onLongClick(view: View, position: Int) = true

    /**
     * 点击事件
     * @param view 被点击的视图
     * @param adapterPosition 在当前adapter中的位置
     * @param position 在RecyclerView中的位置
     */
    abstract fun onClick(view: View, position: Int, adapterPosition: Int)

    /**
     * 长按事件
     * @param view 被点击的视图
     * @param adapterPosition 在当前adapter中的位置
     * @param position 在RecyclerView中的位置
     */
    open fun onLongClick(view: View, position: Int, adapterPosition: Int) = true
}

class OnViewClickListenerImpl : OnViewClickListener() {

    @Deprecated("使用onAdapterClick替换")
    private var onClick: ((view: View, position: Int) -> Unit)? = null

    @Deprecated("使用onAdapterLongClick替换")
    private var onLongClick: ((view: View, position: Int) -> Boolean)? = null
    private var onAdapterClick: ((view: View, position: Int, adapterPosition: Int) -> Unit)? = null
    private var onAdapterLongClick: ((view: View, position: Int, adapterPosition: Int) -> Boolean)? =
        null

    override fun onClick(view: View, position: Int) {
        onClick?.invoke(view, position)
    }

    override fun onLongClick(view: View, position: Int): Boolean {
        return onLongClick?.invoke(view, position) ?: true
    }

    override fun onClick(view: View, position: Int, adapterPosition: Int) {
        onAdapterClick?.invoke(view, position, adapterPosition)
    }

    override fun onLongClick(view: View, position: Int, adapterPosition: Int): Boolean {
        return onAdapterLongClick?.invoke(view, position, adapterPosition) ?: true
    }

    @Deprecated("使用三个参数的函数替代")
    fun onClick(l: (view: View, position: Int) -> Unit) {
        onClick = l
    }

    @Deprecated("使用三个参数的函数替代")
    fun onLongClick(l: ((view: View, position: Int) -> Boolean)) {
        onLongClick = l
    }

    fun onClick(l: (view: View, position: Int, adapterPosition: Int) -> Unit) {
        onAdapterClick = l
    }

    fun onLongClick(l: ((view: View, position: Int, adapterPosition: Int) -> Boolean)) {
        onAdapterLongClick = l
    }
}
