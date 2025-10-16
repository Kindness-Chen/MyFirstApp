package com.example.myfirstapp.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

import androidx.core.view.ViewCompat;

import com.example.myfirstapp.R;
import com.yitong.utils.ScreenUtils;

/**
 * Date：2024/6/9
 * Time：14:34
 * Author：chenshengrui
 */
public class SlideMenuView extends HorizontalScrollView {

    private int mMenuMarginRight = 50;
    private int mMenuWidth;

    private View mMenuView;
    private View mContentView;

    //判断当前菜单栏是否打开
    private boolean isMenuOpen = false;

    //处理手势快速滑动
    private GestureDetector mGestureDetector;

    //判断当前事件是否被拦截
    private boolean isIntercept = false;

    public SlideMenuView(Context context) {
        this(context, null);
    }

    public SlideMenuView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SlideMenuView);
        mMenuMarginRight = (int) typedArray.getDimension(R.styleable.SlideMenuView_menuMarginRight, dp2px(mMenuMarginRight));

        //侧边菜单栏的宽度为屏幕宽度-居右边距离
        mMenuWidth = ScreenUtils.getScreenWidth(context) - mMenuMarginRight;

        typedArray.recycle();
        mGestureDetector = new GestureDetector(context, new GestureDetectorListener());
    }

    /**
     * 指定menuView和contentView的宽度
     * <p>
     * 该方法在布局解析完成之后便会调用
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //获取父LinerLayout
        ViewGroup container = (ViewGroup) getChildAt(0);

        int childCount = container.getChildCount();
        //子view只能有两个，否则抛异常
        if (childCount != 2) {
            throw new RuntimeException("必须有两个子视图");
        }

        //设置MenuView宽度
        mMenuView = container.getChildAt(0);
        ViewGroup.LayoutParams menuView = mMenuView.getLayoutParams();
        menuView.width = mMenuWidth;
        mMenuView.setLayoutParams(menuView);

        //设置主视图宽度
        mContentView = container.getChildAt(1);
        ViewGroup.LayoutParams contentParams = mContentView.getLayoutParams();
        contentParams.width = ScreenUtils.getScreenWidth(getContext());
        mContentView.setLayoutParams(contentParams);
    }

    /**
     * 随着滑动距离的变化，来缩放以及渐变 平移menuView 和contentView
     * 来达到视觉上的效果
     *
     * @param l
     * @param t
     * @param oldl
     * @param oldt
     */
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        Log.i("SlideMenuView", l + "");
        //以0.7为基数 缩放contentView
        float contentScale = (float) (0.7 + 0.3 * l * 1f / mMenuView.getMeasuredWidth());
        mContentView.setPivotX(0);
        mContentView.setPivotY(mContentView.getMeasuredHeight() / 2);
        mContentView.setScaleX(contentScale);
        mContentView.setScaleY(contentScale);

        //给MenuView设置渐变 基数为0.5
        float menuAlpha = (float) (1 - 0.5 * l * 1f / mMenuView.getMeasuredWidth());
        Log.i("SlideMenuView", "ALPHA=" + menuAlpha);
        mMenuView.setAlpha(menuAlpha);

        //给MenuView设置缩放
        float menuScale = (float) (1 - 0.3 * l * 1f / mMenuView.getMeasuredWidth());
        mMenuView.setScaleX(menuScale);
        mMenuView.setScaleY(menuScale);

        //设置menuView的抽屉效果
        mMenuView.setTranslationX(0.25f * l);
    }

    /**
     * 当在滑动的过程中还没关闭的时候抬起手，判断当前应该关闭menuView还是打开menuView
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //已被事件拦截
        if (isIntercept) {
            return true;
        }

        //代表当前为手势快速滑动了
        if (mGestureDetector.onTouchEvent(ev)) {
            return true;
        }
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            int scrollX = getScrollX();
            if (scrollX > mMenuView.getMeasuredWidth() / 2) {
                closeMenu();
            } else {
                openMenu();
            }
            //一定要消费事件
            return true;
        }
        return super.onTouchEvent(ev);
    }

    private void closeMenu() {
        smoothScrollTo(mMenuWidth, 0);
        isMenuOpen = false;
    }

    private void openMenu() {
        smoothScrollTo(0, 0);
        isMenuOpen = true;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        scrollTo(mMenuWidth, 0);
    }

    //添加手势事件处理
    private class GestureDetectorListener extends GestureDetector.SimpleOnGestureListener {

        //处理手势快速滑动的方法
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            //当手势为左右快速滑动是，则切换状态
            if (Math.abs(velocityX) < Math.abs(velocityY)) {
                //上下滑动的距离大于左右滑动的距离 代表当前为上下滑动
                //此时不处理
                return super.onFling(e1, e2, velocityX, velocityY);
            }
            //当velocityX > 0  代表右滑  velocityX < 0 代表左滑
            if (isMenuOpen) {
                //当菜单栏打开的时候，快速左滑，关闭菜单栏
                if (velocityX < 0) {
                    closeMenu();
                    return true;
                }
            } else {
                if (velocityX > 0) {
                    openMenu();
                    return true;
                }
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }

    //处理事件拦截   当菜单栏打开的时候，点击右侧内容页面 关闭菜单栏  且拦截内容页面的所有事件
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        isIntercept = false;
        if (isMenuOpen) {
            int x = (int) ev.getX();
            if (x > mMenuView.getMeasuredWidth()) {
                closeMenu();

                isIntercept = true;
                return true;
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    //dip转px
    private float dp2px(int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
