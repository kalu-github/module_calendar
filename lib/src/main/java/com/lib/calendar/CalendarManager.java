package com.lib.calendar;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

/**
 * description: 垂直水平翻页
 * created by kalu on 2018/6/5 14:27
 */
final class CalendarManager extends LinearLayoutManager implements RecyclerView.OnChildAttachStateChangeListener {

    private final PagerSnapHelper mPagerSnapHelper = new PagerSnapHelper();
    private boolean isLast = false;
    private boolean isFirst = false;

    public CalendarManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (Exception e) {
            Log.e("onLayoutChildren", e.getMessage(), e);
        }
    }

    @Override
    public void onAttachedToWindow(RecyclerView view) {
        super.onAttachedToWindow(view);
        mPagerSnapHelper.attachToRecyclerView(view);
        view.addOnChildAttachStateChangeListener(this);
    }

    @Override
    public void onDetachedFromWindow(RecyclerView view, RecyclerView.Recycler recycler) {
        super.onDetachedFromWindow(view, recycler);
        view.removeOnChildAttachStateChangeListener(this);
    }

    @Override
    public void onScrollStateChanged(int state) {
        switch (state) {
            // 抬起
            case RecyclerView.SCROLL_STATE_IDLE:

                if (null == mOnPagerChangeListener)
                    break;

                final View view1 = mPagerSnapHelper.findSnapView(this);
                if(!(view1 instanceof BaseCalendarView))
                    return;

                final int position1 = getPosition(view1);
                final int itemCount = getItemCount();

                if (position1 == 0) {

                    if (isFirst)
                        break;

                    isFirst = true;
                    isLast = false;
                } else if (position1 + 1 == itemCount) {

                    if (isLast)
                        break;

                    isLast = true;
                    isFirst = false;
                } else {
                    isLast = false;
                    isFirst = false;
                }

                Log.e("rili", "[空闲状态] ==> itemCount = " + itemCount + ", position = " + position1 + ", isLast = " + isLast + ", isFirst = " + isFirst);
                mOnPagerChangeListener.onChange((BaseCalendarView) view1, itemCount, position1, isFirst, isLast);
                break;
//            case RecyclerView.SCROLL_STATE_DRAGGING:
//
//                if (null == mOnPagerChangeListener)
//                    break;
//
//                final View view2 = mPagerSnapHelper.findSnapView(this);
//                final int position2 = getPosition(view2);
//                Log.e("rili", "[滑动状态] ==>  = " + position2);
//
//                break;
//            case RecyclerView.SCROLL_STATE_SETTLING:
//
//                if (null == mOnPagerChangeListener)
//                    break;
//
//                final View view3 = mPagerSnapHelper.findSnapView(this);
//                final int position3 = getPosition(view3);
//                Log.e("rili", "[沉降状态] ==>  = " + position3);
//                break;
        }
    }

//    @Override
//    public void scrollToPositionWithOffset(int position, int offset) {
//        super.scrollToPositionWithOffset(position, offset);
//
//        final View child = mPagerSnapHelper.findSnapView(this);
//        if (null == child || null == child.getLayoutParams())
//            return;
//
//        final int positionIdle = getPosition(child);
//        mOnPagerChangeListener.onPageSelect(positionIdle, true, false, false);
//    }

    @Override
    public void onLayoutCompleted(RecyclerView.State state) {
        super.onLayoutCompleted(state);

        if (null == mOnPagerChangeListener)
            return;

        final View view = mPagerSnapHelper.findSnapView(this);
        if(!(view instanceof BaseCalendarView))
            return;

        final int position = getPosition(view);
        final int itemCount = getItemCount();
        if (position == 0) {
            isFirst = true;
            isLast = false;
        } else if (position + 1 == itemCount) {
            isLast = true;
            isFirst = false;
        } else {
            isLast = false;
            isFirst = false;
        }
        Log.e("rili", "[加载完成] ==> itemCount = " + itemCount + ", position = " + position + ", isLast = " + isLast + ", isFirst = " + isFirst);

        View viewIdle = mPagerSnapHelper.findSnapView(this);
        int positionIdle = getPosition(viewIdle);
        mOnPagerChangeListener.onChange((BaseCalendarView) view, itemCount, positionIdle, isFirst, isLast);
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        return super.scrollVerticallyBy(dy, recycler, state);
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        return super.scrollHorizontallyBy(dx, recycler, state);
    }

    @Override
    public void onChildViewDetachedFromWindow(View view) {
    }

    @Override
    public void onChildViewAttachedToWindow(View view) {
    }

    /************************************************************/

    private OnListAdapterChangeListener mOnPagerChangeListener;

    public interface OnListAdapterChangeListener {
        void onChange(BaseCalendarView view, int count, int position, boolean isFirst, boolean isLast);
    }

    public void setOnListAdapterChangeListener(OnListAdapterChangeListener listener) {
        this.mOnPagerChangeListener = listener;
    }
}
