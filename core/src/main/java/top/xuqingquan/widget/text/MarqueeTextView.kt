package top.xuqingquan.widget.text

import android.content.Context
import android.graphics.Rect
import android.text.TextUtils
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/**
 * Created by 许清泉 on 2018/12/28 23:51
 * 跑马灯效果
 */
class MarqueeTextView constructor(context: Context, attrs: AttributeSet? = null) :
    AppCompatTextView(context, attrs) {

    init {
        //设置单行
        setSingleLine()
        //设置Ellipsize
        ellipsize = TextUtils.TruncateAt.MARQUEE
        //获取焦点
        isFocusable = true
        //走马灯的重复次数，-1代表无限重复
        marqueeRepeatLimit = -1
        //强制获得焦点
        isFocusableInTouchMode = true
    }

    /*
    *这个属性这个View得到焦点,在这里我们设置为true,这个View就永远是有焦点的
    */
    override fun isFocused() = true

    /*
    * 用于EditText抢注焦点的问题
    * */
    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        if (focused) {
            super.onFocusChanged(true, direction, previouslyFocusedRect)
        }
    }

    /*
    * Window与Window间焦点发生改变时的回调
    * */
    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        if (hasWindowFocus) {
            super.onWindowFocusChanged(true)
        }
    }
}
