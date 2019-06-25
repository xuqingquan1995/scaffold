package top.xuqingquan.sample

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.jetbrains.anko.find
import top.xuqingquan.base.model.diffcallback.BaseDiffCallBack
import top.xuqingquan.base.view.adapter.listadapter.SimplePagedListAdapter
import top.xuqingquan.base.view.adapter.listadapter.SimpleRecyclerAdapter
import top.xuqingquan.base.view.adapter.viewholder.BaseViewHolder

class BeanAdapter : SimplePagedListAdapter<Subjects>(BaseDiffCallBack()) {

    override fun getViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Subjects> {
        return BeanViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false))
    }

    private class BeanViewHolder(view: View) : BaseViewHolder<Subjects>(view) {
        val text = view.find<TextView>(R.id.text)

        override fun setData(data: Subjects, position: Int) {
            text.text = data.title
        }
    }

}