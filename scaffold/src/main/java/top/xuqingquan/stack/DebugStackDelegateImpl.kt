package top.xuqingquan.stack

import android.annotation.SuppressLint
import android.util.TypedValue
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.support.v7.app.AlertDialog
import android.support.v4.app.FragmentActivity
import top.xuqingquan.R
import top.xuqingquan.app.ScaffoldConfig
import top.xuqingquan.utils.Timber
import java.lang.ref.WeakReference
import kotlin.math.abs

/**
 * Created by 许清泉 on 2019-04-24 22:49
 */
class DebugStackDelegateImpl(private val mActivity: FragmentActivity) : DebugStackDelegate {
    private val showStack: Boolean = ScaffoldConfig.isShowStack()
    private var mStackDialog: AlertDialog? = null
    private val list = StackList.instance!!
        get() {
            return if (field.size == 1 && field[0].tag == "androidx.navigation.fragment.NavHostFragment") {
                field[0].childFragmentRecord ?: arrayListOf()
            } else {
                field
            }
        }

    @SuppressLint("ClickableViewAccessibility")
    override fun onPostCreate() {
        if (!showStack) {
            return
        }
        val root = mActivity.findViewById<View>(android.R.id.content)
        if (root is FrameLayout) {
            val stackView = ImageView(mActivity)
            stackView.setImageResource(R.drawable.fragmentation_ic_stack)
            val params =
                FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            params.gravity = Gravity.END
            val dp18 =
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 18f, mActivity.resources.displayMetrics).toInt()
            params.topMargin = dp18 * 7
            params.rightMargin = dp18
            stackView.layoutParams = params
            root.addView(stackView)
            stackView.setOnTouchListener(StackViewTouchListener(stackView, dp18 / 4))
            stackView.setOnClickListener { showFragmentStackHierarchyView() }
        }
    }

    /**
     * 调试相关:以dialog形式 显示 栈视图
     */
    override fun showFragmentStackHierarchyView() {
        logFragmentRecords("DebugStackDelegateImpl")
        if (mStackDialog != null && mStackDialog!!.isShowing) {
            return
        }
        val container = DebugHierarchyViewContainer(mActivity)
        container.bindFragmentRecords(list)
        container.layoutParams =
            ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        mStackDialog = AlertDialog.Builder(mActivity)
            .setView(container)
            .setPositiveButton(android.R.string.cancel, null)
            .setCancelable(true)
            .create()
        mStackDialog!!.show()
    }

    /**
     * 调试相关:以log形式 打印 栈视图
     */
    override fun logFragmentRecords(tag: String) {
        val sb = StringBuilder()
        sb.append(" \n\n═══════════════════════════════════════════════════════════════════════════════════\n\n")
        list.forEachIndexed { index, (name, record) ->
            when {
                list.size == 1 -> sb.append("\t根\t\t\t").append(name).append("\n\n")
                index == 0 -> sb.append("\t栈顶\t\t\t").append(name).append("\n\n")
                index == list.size - 1 -> sb.append("\t栈底\t\t\t").append(name).append("\n\n")
                else -> sb.append("\t\t\t\t").append(name).append("\n\n")
            }
            printChildFragmentRecordsLog(sb, record)
        }
        sb.append("\n═══════════════════════════════════════════════════════════════════════════════════\n")
        Timber.tag(tag).i(sb.toString())
    }

    private fun printChildFragmentRecordsLog(sb: StringBuilder, records: List<DebugFragmentRecord>?, deeps: Int = 1) {
        if (records.isNullOrEmpty()) {
            return
        }
        var deep = deeps
        records.forEachIndexed { index, (name, record) ->
            for (k in 0 until deeps) {
                sb.append("\t\t")
            }
            when {
                records.size == 1 -> sb.append("\t子根\t\t\t").append(name).append("\n\n")
                index == 0 -> sb.append("\t子栈顶\t\t").append(name).append("\n\n")
                index == records.size - 1 -> sb.append("\t子栈底\t\t").append(name).append("\n\n")
                else -> sb.append("\t↓\t\t\t").append(name).append("\n\n")
            }
            printChildFragmentRecordsLog(sb, record, ++deep)
        }
    }

    private inner class StackViewTouchListener internal constructor(stackView: View, private val clickLimitValue: Int) :
        View.OnTouchListener {
        private var dX: Float = 0.toFloat()
        private var dY = 0f
        private var downX: Float = 0.toFloat()
        private var downY = 0f
        private var isClickState: Boolean = false
        private val reference: WeakReference<View> = WeakReference(stackView)

        override fun onTouch(v: View, event: MotionEvent): Boolean {
            val stackView = reference.get() ?: return false
            val x = event.rawX
            val y = event.rawY
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    isClickState = true
                    downX = x
                    downY = y
                    dX = stackView.x - event.rawX
                    dY = stackView.y - event.rawY
                }
                MotionEvent.ACTION_MOVE -> if (abs(x - downX) < clickLimitValue && abs(y - downY) < clickLimitValue && isClickState) {
                    isClickState = true
                } else {
                    isClickState = false
                    stackView.x = event.rawX + dX
                    stackView.y = event.rawY + dY
                }
                MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> if (x - downX < clickLimitValue && isClickState) {
                    stackView.performClick()
                }
                else -> return false
            }
            return true
        }
    }
}
