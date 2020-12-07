package top.xuqingquan.sample

import android.annotation.SuppressLint
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.DiffUtil
import top.xuqingquan.base.view.adapter.listadapter.BasePagedListAdapter
import top.xuqingquan.base.view.adapter.viewholder.BaseViewHolder
import top.xuqingquan.utils.Timber
import top.xuqingquan.widget.text.MarqueeTextView

class StringAdapter(retry:()->Unit={}) : BasePagedListAdapter<String>(retry,object : DiffUtil.ItemCallback<String>(){//错误用法，请勿参考
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return false
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return false
    }
}) {

    override fun getLayoutRes(viewType: Int) = R.layout.item

    @SuppressLint("SetTextI18n")
    override fun setData(holder: BaseViewHolder<String>, data: String?,viewType: Int, position: Int) {
        holder.getView<MarqueeTextView>(R.id.text).text = "$position---${data}"
        holder.getView<AppCompatImageView>(R.id.image)
    }

    override fun onClick(view: View, position: Int, data: String?, viewType: Int) {
        super.onClick(view, position, data, viewType)
        Timber.d("data=$data")
    }

}