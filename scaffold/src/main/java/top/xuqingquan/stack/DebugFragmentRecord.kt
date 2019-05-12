package top.xuqingquan.stack

/**
 * Created by 许清泉 on 2019-04-21 14:41
 * 为了调试时 查看栈视图
 */
data class DebugFragmentRecord(
    var fragmentName: String,
    var childFragmentRecord: MutableList<DebugFragmentRecord>?,
    val tag: String = ""
)
