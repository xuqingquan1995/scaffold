package top.xuqingquan.utils

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import top.xuqingquan.imageloader.GlideApp

/**
 * Created by 许清泉 on 2018/12/20 20:38
 */
@BindingAdapter("visibleGone")
fun visibleGone(view: View, show: Boolean) {
    view.visibility = if (show) View.VISIBLE else View.GONE
}

@BindingAdapter("isVisiblity")
fun isVisiblity(view: View, isVisiblity: Boolean) {
    view.visibility = if (isVisiblity) View.VISIBLE else View.INVISIBLE
}

@BindingAdapter("imageFromUrl")
fun imageFromUrl(view: ImageView, imageUrl: String) {
    GlideApp.with(view.context)
        .load(imageUrl)
        .into(view)
}