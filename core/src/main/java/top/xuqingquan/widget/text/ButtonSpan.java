package top.xuqingquan.widget.text;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import top.xuqingquan.R;

/**
 * Created by 许清泉 on 2020/4/16 15:22
 */
public final class ButtonSpan extends ClickableSpan {

    private final View.OnClickListener onClickListener;
    private final int color;

    public ButtonSpan(Context context, View.OnClickListener onClickListener) {
        this(onClickListener, ContextCompat.getColor(context, R.color.black));
    }

    public ButtonSpan(View.OnClickListener onClickListener, @ColorInt int color) {
        this.onClickListener = onClickListener;
        this.color = color;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(color);
        ds.setTextSize(dip2px(14));
        ds.setUnderlineText(false);
    }

    @Override
    public void onClick(@NonNull View widget) {
        if (onClickListener != null) {
            onClickListener.onClick(widget);
        }
    }

    public static int dip2px(float dipValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}