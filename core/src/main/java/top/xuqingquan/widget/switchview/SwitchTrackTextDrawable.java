package top.xuqingquan.widget.switchview;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

public final class SwitchTrackTextDrawable extends Drawable {

    private final String mLeftText;

    private final String mRightText;

    private final Paint mTextPaint;

    private final int textSize;

    private final int switchWidth;

    private final int switchHeight;

    private final int radius;

    private final int textColor;

    private final int trackColor;

    private final int strokeColor;

    private final int borderWidth;

    private boolean setMoving;

    public SwitchTrackTextDrawable(String leftTextString, String rightTextString, int textSize, int switchWidth, int switchHeight,
                                   int borderWidth, int radius, int trackColor, int textColor, int strokeColor) {
        mLeftText = leftTextString;
        this.textColor = textColor;
        this.textSize = textSize;
        mTextPaint = createTextPaint();
        mRightText = rightTextString;
        this.switchHeight = switchHeight;
        this.switchWidth = switchWidth;
        this.radius = radius;
        this.trackColor = trackColor;
        this.strokeColor = strokeColor;
        this.borderWidth = borderWidth;
        setMoving = false;
    }

    private Paint createTextPaint() {
        Paint textPaint = new Paint();
        //noinspection
        textPaint.setColor(textColor);
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(textSize);
        // Set textSize, typeface, etc, as you wish
        return textPaint;
    }

    private Paint createBackgroundPaint() {
        Paint background = new Paint();
        background.setColor(trackColor);
        background.setAntiAlias(true);
        background.setStyle(Paint.Style.FILL);
        background.setTextAlign(Paint.Align.CENTER);
        return background;
    }

    @Override
    public void draw(Canvas canvas) {
        final Rect textBounds = new Rect();
        RectF rect = new RectF(0, 0, switchWidth, switchHeight);
        mTextPaint.getTextBounds(mRightText, 0, mRightText.length(), textBounds);
        canvas.drawRoundRect(rect, radius, radius, createBackgroundPaint());
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(strokeColor);
        paint.setStrokeWidth(borderWidth);
        canvas.drawRoundRect(rect, radius, radius, paint);
        if (setMoving)
            return;
        final int heightBaseline = canvas.getClipBounds().height() / 2 + textBounds.height() / 2;
        final int widthQuarter = canvas.getClipBounds().width() / 4;
        canvas.drawText(mLeftText, 0, mLeftText.length(),
                widthQuarter, heightBaseline,
                mTextPaint);
        canvas.drawText(mRightText, 0, mRightText.length(),
                widthQuarter * 3, heightBaseline,
                mTextPaint);
        canvas.save();

    }

    @Override
    public void setAlpha(int alpha) {
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    public void changeBackground(boolean isMoving) {
        if (isMoving) {
            setMoving = true;
        } else {
            setMoving = false;
        }
        invalidateSelf();
    }
}