package top.xuqingquan.base.model.diffcallback

import androidx.recyclerview.widget.DiffUtil

/**
 * Created by 许清泉 on 2019-04-20 17:54
 */
class BaseDiffCallBack<T> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T) = oldItem.hashCode() == newItem.hashCode()

    override fun areContentsTheSame(oldItem: T, newItem: T) = oldItem.hashCode() == newItem.hashCode()
}