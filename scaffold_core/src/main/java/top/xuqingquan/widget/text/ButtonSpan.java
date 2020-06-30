package top.xuqingquan.widget.text;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import androidx.annotation.NonNull;

/**
 * Created by 许清泉 on 2020/4/16 15:22
 */
public class ButtonSpan extends ClickableSpan {

    View.OnClickListener onClickListener;
    private Context context;
    private int colorId;

    public ButtonSpan(Context context, View.OnClickListener onClickListener) {
        this(context, onClickListener, android.R.color.white);
    }

    public ButtonSpan(Context context, View.OnClickListener onClickListener, int colorId){
        this.onClickListener = onClickListener;
        this.context = context;
        this.colorId = colorId;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(context.getResources().getColor(colorId));
        ds.setTextSize(dip2px(14));
        ds.setUnderlineText(false);
    }

    @Override
    public void onClick(@NonNull View widget) {
        if (onClickListener != null) {
            onClickListener.onClick(widget);
        }
    }

    public static int dip2px(float dipValue)
    {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}