package top.xuqingquan.widget.switchview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.Dimension;
import android.support.annotation.IntDef;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import top.xuqingquan.R;
import top.xuqingquan.utils.DimensionsKt;

public final class SmoothSwitch extends SwitchCompat {//平滑

    public static final int SMALL = 0;
    public static final int LARGE = 1;

    @IntDef({SMALL, LARGE})
    @Retention(RetentionPolicy.SOURCE)
    @interface Type {
    }

    private Context mContext;
    @Type
    private int type = SMALL;
    public String leftString = "";
    public String rightString = "";
    @ColorInt
    public int borderColor = -1;
    @ColorInt
    public int trackColor = -1;
    @ColorInt
    public int thumbColor = -1;
    @ColorInt
    public int trackTextColor = -1;
    @Dimension
    public float borderWidth = 0;
    private SwitchTrackTextDrawable switchTrackTextDrawable;

    public SmoothSwitch(Context context) {
        super(context);
    }

    @SuppressLint("CustomViewStyleable")
    public SmoothSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
        setShowText(true);
        mContext = context;
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.scaffold_SmoothSwitch);
        if (a.getString(R.styleable.scaffold_SmoothSwitch_scaffold_size) != null) {
            type = a.getInteger(R.styleable.scaffold_SmoothSwitch_scaffold_size, 0);
        }
        if (a.getString(R.styleable.scaffold_SmoothSwitch_scaffold_leftTrackText) != null) {
            leftString = a.getString(R.styleable.scaffold_SmoothSwitch_scaffold_leftTrackText);
        }
        if (a.getString(R.styleable.scaffold_SmoothSwitch_scaffold_rightTrackText) != null) {
            rightString = a.getString(R.styleable.scaffold_SmoothSwitch_scaffold_rightTrackText);
        }
        if (a.getString(R.styleable.scaffold_SmoothSwitch_scaffold_borderColor) != null) {
            borderColor = a.getColor(R.styleable.scaffold_SmoothSwitch_scaffold_borderColor, borderColor);
        }
        if (a.getString(R.styleable.scaffold_SmoothSwitch_scaffold_trackColor) != null) {
            trackColor = a.getColor(R.styleable.scaffold_SmoothSwitch_scaffold_trackColor, trackColor);
        }
        if (a.getString(R.styleable.scaffold_SmoothSwitch_scaffold_thumbColor) != null) {
            thumbColor = a.getColor(R.styleable.scaffold_SmoothSwitch_scaffold_thumbColor, thumbColor);
        }
        if (a.getString(R.styleable.scaffold_SmoothSwitch_scaffold_trackTextColor) != null) {
            trackTextColor = a.getColor(R.styleable.scaffold_SmoothSwitch_scaffold_trackTextColor, trackTextColor);
        }
        if (a.getString(R.styleable.scaffold_SmoothSwitch_scaffold_borderWidth) != null) {
            borderWidth = a.getDimension(R.styleable.scaffold_SmoothSwitch_scaffold_borderWidth, 0);
        }
        a.recycle();
        setSwitch();
    }

    @SuppressLint("ClickableViewAccessibility")
    public void setSwitch() {
        if (type == LARGE) {
            switchTrackTextDrawable = new SwitchTrackTextDrawable(leftString, rightString, (int) dp2px(15), (int) dp2px(220), (int) dp2px(38), (int) dp2px(borderWidth), (int) dp2px(19), trackColor, trackTextColor, borderColor);
            this.setTrackDrawable(switchTrackTextDrawable);
            this.setThumbDrawable(addSelector(LARGE));
        } else if (type == SMALL) {
            switchTrackTextDrawable = new SwitchTrackTextDrawable(leftString, rightString, (int) dp2px(11), (int) dp2px(162), (int) dp2px(28), (int) dp2px(borderWidth), (int) dp2px(19), trackColor, trackTextColor, borderColor);
            this.setTrackDrawable(switchTrackTextDrawable);
            this.setThumbDrawable(addSelector(SMALL));
        }
        this.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                changeBackground(true);
            } else {
                changeBackground(false);
            }
            return false;
        });

    }

    public StateListDrawable addSelector(@Type int type) {
        StateListDrawable res = new StateListDrawable();
        if (type == LARGE) {
            this.setSwitchTextAppearance(mContext, R.style.scaffold_LargeText);
            res.addState(new int[]{android.R.attr.state_checked}, getResources().getDrawable(R.drawable.scaffold_large_switch));
            res.addState(new int[]{-android.R.attr.state_checked}, getResources().getDrawable(R.drawable.scaffold_large_switch));
        } else if (type == SMALL) {
            this.setSwitchTextAppearance(mContext, R.style.scaffold_SmallText);
            res.addState(new int[]{android.R.attr.state_checked}, getResources().getDrawable(R.drawable.scaffold_small_switch));
            res.addState(new int[]{-android.R.attr.state_checked}, getResources().getDrawable(R.drawable.scaffold_small_switch));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            res.setTint(thumbColor);
        }
        return res;
    }

    public void changeBackground(boolean isMoving) {
        switchTrackTextDrawable.changeBackground(isMoving);
    }

    private float dp2px(float dp) {
        return DimensionsKt.dip(getContext(),dp);
    }
}