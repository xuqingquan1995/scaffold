package top.xuqingquan.widget.text;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * Created by 许清泉 on 2020/4/16 15:22
 */
public class ButtonSpan extends ClickableSpan {

    View.OnClickListener onClickListener;
    private Context context;
    private int color;

    public ButtonSpan(Context context, View.OnClickListener onClickListener) {
        this(context, onClickListener, ContextCompat.getColor(context, android.R.color.black));
    }

    public ButtonSpan(Context context, View.OnClickListener onClickListener, @ColorInt int color) {
        this.onClickListener = onClickListener;
        this.context = context;
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