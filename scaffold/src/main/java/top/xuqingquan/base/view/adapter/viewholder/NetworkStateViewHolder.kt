package top.xuqingquan.base.view.adapter.viewholder

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import top.xuqingquan.R
import top.xuqingquan.base.model.entity.NetworkStatus

/**
 * Created by 许清泉 on 2019-04-27 16:11
 */
class NetworkStateViewHolder(view: View) :
    BaseViewHolder<NetworkStatus>(view) {
    private val loading_failure = getView<TextView>(R.id.loading_failure)
    private val loading = getView<ProgressBar>(R.id.loading)
    private val loading_tv = getView<TextView>(R.id.loading_tv)

    fun bind(data: NetworkStatus, listener: View.OnClickListener) {
        loading_failure.setOnClickListener(listener)
        loading_failure.visibility = if (data == NetworkStatus.FAILED) {
            View.VISIBLE
        } else {
            View.GONE
        }
        loading.visibility = if (data == NetworkStatus.RUNNING) {
            View.VISIBLE
        } else {
            View.GONE
        }
        loading_tv.visibility = if (data == NetworkStatus.RUNNING) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}