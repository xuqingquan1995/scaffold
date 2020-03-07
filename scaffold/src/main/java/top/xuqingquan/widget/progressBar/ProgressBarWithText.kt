package top.xuqingquan.widget.progressBar

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.widget.ProgressBar
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import top.xuqingquan.R

/**
 * Created by 许清泉 on 2019-10-14 13:46
 */
class ProgressBarWithText : ProgressBar {

    private var text: String = ""
    private lateinit var mPaint: Paint
    @ColorRes
    private var textColor: Int = -1
    private var textSize: Float = 12f

    constructor(context: Context?) : super(context) {
        initText()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        val typedArray = context?.obtainStyledAttributes(attrs, R.styleable.ProgressBarWithText)
        textColor = typedArray?.getColor(R.styleable.ProgressBarWithText_textColor, -1) ?: -1
        textSize = typedArray?.getDimension(R.styleable.ProgressBarWithText_textSize, 12f) ?: 12f
        text = typedArray?.getString(R.styleable.ProgressBarWithText_text) ?: ""
        typedArray?.recycle()
        initText()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        initText()
    }

    private fun initText() {
        this.mPaint = Paint()
        this.mPaint.textSize = textSize
        this.mPaint.color = if (textColor == -1) {
            ContextCompat.getColor(context, android.R.color.black)
        } else {
            ContextCompat.getColor(context, textColor)
        }
    }

    override fun setProgress(progress: Int) {
        super.setProgress(progress)
        setText(progress)
    }

    private fun setText(progress: Int) {
        val progressText = progress * 100.0 / this.max
        this.text = String.format("%.2f%%", progressText)
    }

    fun setText(text: String) {
        this.text = text
    }

    @SuppressLint("DrawAllocation")
    @Synchronized
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val rect = Rect()
        mPaint.getTextBounds(text, 0, text.length, rect)
        val x: Int = width / 2 - rect.centerX()
        val y: Int = height / 2 - rect.centerY()
        canvas?.drawText(text, x.toFloat(), y.toFloat(), mPaint)
    }

}