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
 * Caldnear View Base.
 * Created by yohei on 3/15/15.
 */
public class ExtendedBaseCalendarView extends ViewGroup {

    /**
     * calendar column count.
     */
    protected final int CALENDAR_COLUMN_COUNT = 7;
    /**
     * calendar row count.
     */
    protected final int CALENDAR_ROW_COUNT = 6;
    /**
     * calendar week count.
     */
    protected final int CALENDAR_WEEK_COUNT = 7;
    /**
     * calendar cell total count.
     */
    protected final int CALENDAR_CELL_TOTAL_COUNT = CALENDAR_COLUMN_COUNT * CALENDAR_ROW_COUNT;
    /**
     * must child count.
     */
    protected final int MUST_CHILD_COUNT = CALENDAR_CELL_TOTAL_COUNT + CALENDAR_WEEK_COUNT;

    private int mWeekHeight = 0;

    /**
     * constructor.
     *
     * @param context
     */
    public ExtendedBaseCalendarView(Context context) {
        this(context, null);
    }

    /**
     * constructor.
     *
     * @param context
     * @param attrs
     */
    public ExtendedBaseCalendarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * constructor.
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public ExtendedBaseCalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ExtendedBaseCalendarView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * init view.
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     * @param defStyleRes
     */
    private void initView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExtendedBaseCalendarView);
        mWeekHeight = typedArray.getDimensionPixelSize(R.styleable.ExtendedBaseCalendarView_week_height, context.getResources().getDimensionPixelSize(R.dimen.ecv__week_height));
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
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
        // weekly
        for (; childIdx < CALENDAR_WEEK_COUNT; childIdx++) {
            View child = getChildAt(childIdx);
            int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(cellWidth, MeasureSpec.EXACTLY);
            int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(mWeekHeight, MeasureSpec.EXACTLY);
            child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
        }
        // cell
        final int cellTotalHeight = height - mWeekHeight;
        final int cellHeight = cellTotalHeight / CALENDAR_ROW_COUNT;
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
        int left = getPaddingLeft();
        int top = getPaddingTop();
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

    /**
     * layout childview.
     *
     * @param child
     * @param l
     * @param t
     * @param r
     * @param b
     */
    private void childLayout(View child, int l, int t, int r, int b) {
        child.layout(l, t, r, b);
    }


}
