package top.xuqingquan.sample

import android.support.v7.util.DiffUtil


data class Bean(
    val count: Int, val start: Int, val total: Int, val title: String, val subjects: List<Subjects>
)

data class Subjects(val title: String)

class SubjectsDiffCallBack : DiffUtil.ItemCallback<Subjects>() {
    override fun areItemsTheSame(oldItem: Subjects, newItem: Subjects) =
        oldItem.title == newItem.title

    override fun areContentsTheSame(oldItem: Subjects, newItem: Subjects) =
        oldItem.title == newItem.title
}