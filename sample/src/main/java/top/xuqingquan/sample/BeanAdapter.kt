package top.xuqingquan.sample

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import top.xuqingquan.base.model.diffcallback.BaseDiffCallBack
import top.xuqingquan.base.view.adapter.listadapter.BasePagedListAdapter
import top.xuqingquan.base.view.adapter.listadapter.SimpleRecyclerAdapter
import top.xuqingquan.base.view.adapter.viewholder.BaseViewHolder
import top.xuqingquan.utils.Timber
import top.xuqingquan.widget.text.MarqueeTextView

class BeanAdapter(retry:()->Unit={}) : BasePagedListAdapter<Subjects>(retry,BaseDiffCallBack<Subjects>()) {

    override fun getLayoutRes(viewType: Int) = R.layout.item

    @SuppressLint("SetTextI18n")
    override fun setData(holder: BaseViewHolder<Subjects>, data: Subjects?,viewType: Int, position: Int) {
        holder.getView<MarqueeTextView>(R.id.text).text = "$position---${data?.title}"
        holder.getView<AppCompatImageView>(R.id.image)
    }

    override fun onClick(view: View, position: Int, data: Subjects?, viewType: Int) {
        super.onClick(view, position, data, viewType)
        Timber.d("data=$data")
    }

}