package top.xuqingquan.base.view.adapter.viewholder

import android.view.View
import top.xuqingquan.base.model.entity.NetworkStatus
import top.xuqingquan.databinding.NetworkStateBinding

/**
 * Created by 许清泉 on 2019-04-27 16:11
 */
class NetworkStateViewHolder(val binding: NetworkStateBinding) :
    BaseViewHolder<NetworkStatus>(binding) {
    override fun setData(data: NetworkStatus, position: Int) {
    }

    fun bind(data: NetworkStatus,listener: View.OnClickListener) {
        binding.apply {
            clickListener = listener
            loadingVisible = data == NetworkStatus.RUNNING
            errorVisible = data == NetworkStatus.FAILED
            executePendingBindings()
        }
    }
}