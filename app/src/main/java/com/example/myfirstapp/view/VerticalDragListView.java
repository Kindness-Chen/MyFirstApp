package com.example.myfirstapp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.ListViewCompat;
import androidx.customview.widget.ViewDragHelper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/**
 * Date：2024/6/9
 * Time：16:22
 * Author：chenshengrui
 */
public class VerticalDragListView extends FrameLayout {

    private ViewDragHelper mViewDragHelper;

    private View mDragView;

    private int mMenuHeight;

    private boolean isMenuModel = false;

    public VerticalDragListView(@NonNull Context context) {
        this(context, null);
    }

    public VerticalDragListView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalDragListView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mViewDragHelper = ViewDragHelper.create(this, callback);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        int childCount = getChildCount();
        if (childCount != 2) {
            throw new RuntimeException("只能包含两个子布局");
        }

        mDragView = getChildAt(1);
        mMenuHeight = getChildAt(0).getLayoutParams().height;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    private ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(@NonNull View child, int pointerId) {
            return child == mDragView;
        }

        @Override
        public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
            if (top <= 0) {
                top = 0;
            }
            if (top > mMenuHeight) {
                top = mMenuHeight;
            }
            return top;
        }

//        @Override
//        public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
//            return left;
//        }


        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            if (mDragView.getTop() >= mMenuHeight / 2) {
//                super.clampViewPositionVertical(mDragView, mMenuHeight, 0);
                mViewDragHelper.settleCapturedViewAt(0, mMenuHeight);
                isMenuModel = true;
            } else {
//                super.clampViewPositionVertical(mDragView, 0, 0);
                mViewDragHelper.settleCapturedViewAt(0, 0);
                isMenuModel = false;
            }
            invalidate();
        }
    };

    //listView可以滑动，菜单没有了
    private float downY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }



    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isMenuModel) {
            return true;
        }
        //向下滑动拦截，不给listview做处理
        //请求父类不要拦截
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = ev.getY();
                mViewDragHelper.processTouchEvent(ev);
//                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                float moveY = ev.getY();
                if (downY - moveY < 0 && !canChildScrollUp()) {
//                    getParent().requestDisallowInterceptTouchEvent(true);
                    return true;
                }
                break;
//            case MotionEvent.ACTION_UP:
//                break;
            case MotionEvent.ACTION_CANCEL:
        }

        return super.onInterceptTouchEvent(ev);
    }

    public boolean canChildScrollUp() {
        if (mDragView instanceof ListView) {
            return ListViewCompat.canScrollList((ListView) mDragView, -1);
        }
        return mDragView.canScrollVertically(-1);
    }

    @Override
    public void computeScroll() {
        if (mViewDragHelper.continueSettling(true)) {
            invalidate();
        }
    }
}
