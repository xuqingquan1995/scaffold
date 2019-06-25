package top.xuqingquan.sample

import android.view.View
import android.widget.TextView
import org.jetbrains.anko.find
import top.xuqingquan.base.model.diffcallback.BaseDiffCallBack
import top.xuqingquan.base.view.adapter.listadapter.SimplePagedListAdapter
import top.xuqingquan.base.view.adapter.viewholder.BaseViewHolder
import top.xuqingquan.utils.Timber

class BeanAdapter : SimplePagedListAdapter<Subjects>(BaseDiffCallBack()) {

    override fun getLayoutRes(viewType: Int)=R.layout.item

    override fun setData(holder: BaseViewHolder<Subjects>, data: Subjects, position: Int) {
        holder.getView<TextView>(R.id.text).text = "$position---${data?.title}"
    }

    override fun onClick(view: View, position: Int, data: Subjects?, viewType: Int) {
        super.onClick(view, position, data, viewType)
        Timber.d("data=$data")
    }

}