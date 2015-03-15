package org.yohei.extendedcalendarview.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import org.yohei.extendedcalendarview.R;

/**
 * Created by yohei on 3/15/15.
 */
public class ExtendedBaseCalendarView extends ViewGroup {

    private static final String TAG = ExtendedBaseCalendarView.class.getSimpleName();
    protected final int CALENDAR_COLUMN_COUNT = 7;
    protected final int CALNEDAR_ROW_COUNT = 5;
    protected final int CALENDAR_WEEK_COUNT = 7;
    protected final int CALENDAR_CELL_TOTAL_COUNT = CALENDAR_COLUMN_COUNT * CALNEDAR_ROW_COUNT;
    protected final int MUST_CHILD_COUNT = CALENDAR_CELL_TOTAL_COUNT + CALENDAR_WEEK_COUNT;

    private int mWeekHeight = 0;

    public ExtendedBaseCalendarView(Context context) {
        this(context, null);
    }

    public ExtendedBaseCalendarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExtendedBaseCalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ExtendedBaseCalendarView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs, defStyleAttr, defStyleRes);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExtendedBaseCalendarView);
        mWeekHeight = typedArray.getDimensionPixelSize(R.styleable.ExtendedBaseCalendarView_week_height, context.getResources().getDimensionPixelSize(R.dimen.ecv__week_height));
        typedArray.recycle();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 小ビューのサイズを決定する
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode != MeasureSpec.EXACTLY && heightMode != MeasureSpec.EXACTLY) {
            throw new IllegalStateException("Must measure with an exact.");
        }
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        final int width = widthSize - getPaddingLeft() - getPaddingRight();
        final int height = heightSize - getPaddingTop() - getPaddingBottom();
        final int cellWidth = width / CALENDAR_COLUMN_COUNT;
        int childIdx = 0;
        for (; childIdx < CALENDAR_WEEK_COUNT; childIdx++) {
            View child = getChildAt(childIdx);
            int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(cellWidth, MeasureSpec.EXACTLY);
            int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(mWeekHeight, MeasureSpec.EXACTLY);
            child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
        }
        // 次にcell
        final int cellTotalHeight = height - mWeekHeight;
        final int cellHeight = cellTotalHeight / CALNEDAR_ROW_COUNT;
        final int cellCount = getChildCount() >= MUST_CHILD_COUNT ? MUST_CHILD_COUNT : getChildCount();
        for (; childIdx < cellCount; childIdx++) {
            View child = getChildAt(childIdx);
            int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(cellWidth, MeasureSpec.EXACTLY);
            int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(cellHeight, MeasureSpec.EXACTLY);
            child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int width = (r - l) - getPaddingLeft() - getPaddingRight();
        int height = (b - t) - getPaddingTop() - getPaddingBottom();
        int left = getPaddingLeft();
        int top = getPaddingTop();
        int right = left + width;
        int bottom = top + height;
        int nextLeft = left;
        int nextTop = top;
        final int columnLastIdx = 6;
        for (int i = 0, size = getChildCount(); i < size; i++) {
            // week or cell
            final View v = getChildAt(i);
            final int columnPosition = i % CALENDAR_COLUMN_COUNT; // 0-6
            final int childLeft = nextLeft;
            final int childTop = nextTop;
            final int childRight = childLeft + v.getMeasuredWidth();
            final int childBottom = childTop + v.getMeasuredHeight();
            childLayout(v, childLeft, childTop, childRight, childBottom);
            nextLeft = columnPosition == columnLastIdx ? left : childRight;
            nextTop = columnPosition == columnLastIdx ? childBottom : childTop;
        }
    }

    private void childLayout(View child, int l, int t, int r, int b) {
        child.layout(l, t, r, b);
    }


}
