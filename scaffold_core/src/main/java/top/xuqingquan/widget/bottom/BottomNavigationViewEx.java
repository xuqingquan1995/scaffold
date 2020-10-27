package top.xuqingquan.widget.bottom;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;

/**
 * Created by 许清泉 on 2019-05-25 19:26
 * ViewPager/ViewPager2通用
 */
@SuppressWarnings({"unused", "RedundantSuppression"})
public final class BottomNavigationViewEx extends BottomNavigationViewInner {

    public BottomNavigationViewEx(Context context) {
        super(context);
    }

    public BottomNavigationViewEx(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BottomNavigationViewEx(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public BottomNavigationViewInner setIconVisibility(boolean visibility) {
        try {
            return super.setIconVisibility(visibility);
        } catch (Throwable e) {
            return this;
        }
    }

    @Override
    public BottomNavigationViewInner setTextVisibility(boolean visibility) {
        try {
            return super.setTextVisibility(visibility);
        } catch (Throwable e) {
            return this;
        }
    }

    @Override
    public BottomNavigationViewInner enableAnimation(boolean enable) {
        try {
            return super.enableAnimation(enable);
        } catch (Throwable e) {
            return this;
        }
    }

    @Override
    public BottomNavigationViewInner enableShiftingMode(boolean enable) {
        try {
            return super.enableShiftingMode(enable);
        } catch (Throwable e) {
            return this;
        }
    }

    @Override
    public BottomNavigationViewInner enableItemShiftingMode(boolean enable) {
        try {
            return super.enableItemShiftingMode(enable);
        } catch (Throwable e) {
            return this;
        }
    }

    @Override
    public int getCurrentItem() {
        try {
            return super.getCurrentItem();
        } catch (Throwable e) {
            return 0;
        }
    }

    @Override
    public int getMenuItemPosition(MenuItem item) {
        try {
            return super.getMenuItemPosition(item);
        } catch (Throwable e) {
            return 0;
        }
    }

    @Override
    public BottomNavigationViewInner setCurrentItem(int index) {
        try {
            return super.setCurrentItem(index);
        } catch (Throwable e) {
            return this;
        }
    }

    @Override
    public OnNavigationItemSelectedListener getOnNavigationItemSelectedListener() {
        try {
            return super.getOnNavigationItemSelectedListener();
        } catch (Throwable e) {
            return null;
        }
    }

    @Override
    public void setOnNavigationItemSelectedListener(OnNavigationItemSelectedListener listener) {
        try {
            super.setOnNavigationItemSelectedListener(listener);
        } catch (Throwable ignored) {
        }
    }

    @Override
    public BottomNavigationMenuView getBottomNavigationMenuView() {
        return super.getBottomNavigationMenuView();
    }

    @Override
    public BottomNavigationViewInner clearIconTintColor() {
        try {
            return super.clearIconTintColor();
        } catch (Throwable e) {
            return this;
        }
    }

    @Override
    public BottomNavigationItemView[] getBottomNavigationItemViews() {
        try {
            return super.getBottomNavigationItemViews();
        } catch (Throwable e) {
            return null;
        }
    }

    @Override
    public BottomNavigationItemView getBottomNavigationItemView(int position) {
        try {
            return super.getBottomNavigationItemView(position);
        } catch (Throwable e) {
            return null;
        }
    }

    @Override
    public ImageView getIconAt(int position) {
        try {
            return super.getIconAt(position);
        } catch (Throwable e) {
            return null;
        }
    }

    @Override
    public TextView getSmallLabelAt(int position) {
        try {
            return super.getSmallLabelAt(position);
        } catch (Throwable e) {
            return null;
        }
    }

    @Override
    public TextView getLargeLabelAt(int position) {
        try {
            return super.getLargeLabelAt(position);
        } catch (Throwable e) {
            return null;
        }
    }

    @Override
    public int getItemCount() {
        try {
            return super.getItemCount();
        } catch (Throwable e) {
            return 0;
        }
    }

    @Override
    public BottomNavigationViewInner setSmallTextSize(float sp) {
        try {
            return super.setSmallTextSize(sp);
        } catch (Throwable e) {
            return this;
        }
    }

    @Override
    public BottomNavigationViewInner setLargeTextSize(float sp) {
        try {
            return super.setLargeTextSize(sp);
        } catch (Throwable e) {
            return this;
        }
    }

    @Override
    public BottomNavigationViewInner setTextSize(float sp) {
        try {
            return super.setTextSize(sp);
        } catch (Throwable e) {
            return this;
        }
    }

    @Override
    public BottomNavigationViewInner setIconSizeAt(int position, float width, float height) {
        try {
            return super.setIconSizeAt(position, width, height);
        } catch (Throwable e) {
            return this;
        }
    }

    @Override
    public BottomNavigationViewInner setIconSize(float width, float height) {
        try {
            return super.setIconSize(width, height);
        } catch (Throwable e) {
            return this;
        }
    }

    @Override
    public BottomNavigationViewInner setIconSize(float dpSize) {
        try {
            return super.setIconSize(dpSize);
        } catch (Throwable e) {
            return this;
        }
    }

    @Override
    public BottomNavigationViewInner setItemHeight(int height) {
        try {
            return super.setItemHeight(height);
        } catch (Throwable e) {
            return this;
        }
    }

    @Override
    public int getItemHeight() {
        try {
            return super.getItemHeight();
        } catch (Throwable e) {
            return 0;
        }
    }

    @Override
    public BottomNavigationViewInner setTypeface(Typeface typeface, int style) {
        try {
            return super.setTypeface(typeface, style);
        } catch (Throwable e) {
            return this;
        }
    }

    @Override
    public BottomNavigationViewInner setTypeface(Typeface typeface) {
        try {
            return super.setTypeface(typeface);
        } catch (Throwable e) {
            return this;
        }
    }

    @Override
    public BottomNavigationViewInner setupWithViewPager(ViewPager2 viewPager2) {
        try {
            return super.setupWithViewPager(viewPager2);
        } catch (Throwable e) {
            return this;
        }
    }

    @Override
    public BottomNavigationViewInner setupWithViewPager(ViewPager viewPager) {
        try {
            return super.setupWithViewPager(viewPager);
        } catch (Throwable e) {
            return this;
        }
    }

    @Override
    public BottomNavigationViewInner setupWithViewPager(ViewPager2 viewPager2, boolean smoothScroll) {
        try {
            return super.setupWithViewPager(viewPager2, smoothScroll);
        } catch (Throwable e) {
            return this;
        }
    }

    @Override
    public BottomNavigationViewInner setupWithViewPager(ViewPager viewPager, boolean smoothScroll) {
        try {
            return super.setupWithViewPager(viewPager, smoothScroll);
        } catch (Throwable e) {
            return this;
        }
    }

    @Override
    public BottomNavigationViewInner enableShiftingMode(int position, boolean enable) {
        try {
            return super.enableShiftingMode(position, enable);
        } catch (Throwable e) {
            return this;
        }
    }

    @Override
    public BottomNavigationViewInner setItemBackground(int position, int background) {
        try {
            return super.setItemBackground(position, background);
        } catch (Throwable e) {
            return this;
        }
    }

    @Override
    public BottomNavigationViewInner setIconTintList(int position, ColorStateList tint) {
        try {
            return super.setIconTintList(position, tint);
        } catch (Throwable e) {
            return this;
        }
    }

    @Override
    public BottomNavigationViewInner setTextTintList(int position, ColorStateList tint) {
        try {
            return super.setTextTintList(position, tint);
        } catch (Throwable e) {
            return this;
        }
    }

    @Override
    public BottomNavigationViewInner setIconsMarginTop(int marginTop) {
        try {
            return super.setIconsMarginTop(marginTop);
        } catch (Throwable e) {
            return this;
        }
    }

    @Override
    public BottomNavigationViewInner setIconMarginTop(int position, int marginTop) {
        try {
            return super.setIconMarginTop(position, marginTop);
        } catch (Throwable e) {
            return this;
        }
    }
}