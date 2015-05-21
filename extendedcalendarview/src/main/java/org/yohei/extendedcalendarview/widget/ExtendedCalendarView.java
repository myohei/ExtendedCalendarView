package org.yohei.extendedcalendarview.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import org.yohei.extendedcalendarview.R;
import org.yohei.extendedcalendarview.widget.util.CalendarUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Calendar;

/**
 * Created by yohei on 3/15/15.
 */
public class ExtendedCalendarView extends ExtendedBaseCalendarView implements View.OnClickListener {

    public static final int DAY_TYPE_EMPTY = -1;
    public static final int DAY_TYPE_NORMAL = 0;
    public static final int DAY_TYPE_SATURDAY = 1;
    public static final int DAY_TYPE_SUNDAY = 2;
    public static final int DAY_TYPE_TODAY = 3;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({DAY_TYPE_NORMAL, DAY_TYPE_SATURDAY, DAY_TYPE_SUNDAY, DAY_TYPE_TODAY, DAY_TYPE_EMPTY})
    public @interface DayType {
    }

    /**
     * first date.
     */
    private static final int FIRST_DATE = 1;
    /**
     * date text id.
     */
    private int mDateTextId;
    /**
     * cell click listener.
     */
    private OnCalendarCellViewClickListener mOnCalendarCellViewClickListener;
    /**
     * id prefix.
     */
    private static final String CELL_ID_PREFIX = "ecv__date";
    /**
     * current calendar object.
     */
    private Calendar mCurrentCalendar;
    /**
     * weekly color enabled.
     */
    private boolean mWeekColorEnable;
    /**
     * selected date.
     */
    private int mSelectedDate;
    /**
     * selected view.
     */
    private View mSelectedView;

    /**
     * adapter.
     */
    private ExtendedCalendarViewAdapter mAdapter;

    public ExtendedCalendarView(Context context) {
        this(context, null);
    }

    public ExtendedCalendarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExtendedCalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ExtendedCalendarView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExtendedCalendarView);
        mWeekColorEnable = typedArray.getBoolean(R.styleable.ExtendedCalendarView_week_color_enable, true);
        typedArray.recycle();
        final LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.ecv__view_calendar, this, true);
        if (mWeekColorEnable) {
            ECWeekItemView weekItemView = (ECWeekItemView) findViewById(R.id.ecv__week_sun);
            weekItemView.getWeekText().setTextColor(Color.RED);
            weekItemView = (ECWeekItemView) findViewById(R.id.ecv__week_sat);
            weekItemView.getWeekText().setTextColor(Color.BLUE);
        }
    }

    /**
     * get (your custom) CellView at date.
     *
     * @param date date.
     * @return CellView
     */
    @Nullable
    public View getCellView(int date) {
        if (date < 0) {
            return null;
        }
        final int id = getResources().getIdentifier(CELL_ID_PREFIX + date, "id", getContext().getPackageName());
        return findViewById(id);
    }

    /**
     * set month.
     *
     * @param month
     */
    public void setMonth(int month) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        this.setMonth(calendar.get(Calendar.YEAR), month);
    }

    /**
     * set month.
     *
     * @param year
     * @param month
     */
    public void setMonth(int year, int month) {
        if (mAdapter == null) {
            throw new IllegalStateException("You MUST set ExtendedCalendarViewAdapter, before set month.");
        }
        resetCells();
        final Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, FIRST_DATE); // japan
        final int firstWeek = calendar.get(Calendar.DAY_OF_WEEK); // 1-7
        final int lastDate = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        mSelectedDate = CalendarUtils.getDayOfMonth(calendar);
        View cell;
        for (int i = CALENDAR_WEEK_COUNT; i < CALENDAR_WEEK_COUNT + firstWeek - 1; i++) {
            final boolean alreadyCreated = i < getChildCount();
            cell = alreadyCreated ? getChildAt(i) : null;
            cell = mAdapter.getCellView(month, -1, DAY_TYPE_EMPTY, cell, this);
            if (alreadyCreated) {
                this.removeViewAt(i);
            }
            this.addView(cell, i);
        }
        for (int i = CALENDAR_WEEK_COUNT + firstWeek - 1, size = i + lastDate; i < size; i++, calendar.add(Calendar.DAY_OF_MONTH, 1)) {
            final boolean alreadyCreated = i < getChildCount();
            cell = alreadyCreated ? getChildAt(i) : null;
            final int date = CalendarUtils.getDayOfMonth(calendar);
            final int id = getResources().getIdentifier(CELL_ID_PREFIX + date, "id", getContext().getPackageName());
            cell = mAdapter.getCellView(month, date, getDayType(calendar), cell, this);
            if (alreadyCreated) {
                this.removeViewAt(i);
            }
            this.addView(cell, i);
            cell.setId(id);
            cell.setTag(date);
            cell.setEnabled(true);
            cell.setOnClickListener(this);
            cell.setVisibility(VISIBLE);
            if (DateUtils.isToday(calendar.getTimeInMillis())) {
                mSelectedDate = date;
                cell.setSelected(true);
            }
        }
        mCurrentCalendar = (Calendar) calendar.clone();
    }

    /**
     * setAdapter.
     *
     * @param adapter:
     */
    public void setAdapter(ExtendedCalendarViewAdapter adapter) {
        if (adapter == null || adapter.equals(mAdapter)) {
            return;
        }
        this.mAdapter = adapter;
    }

    /**
     * getAdapter.
     *
     * @return ExtendedCalendarViewAdapter
     */
    public ExtendedCalendarViewAdapter getAdapter() {
        return mAdapter;
    }

    @ExtendedCalendarView.DayType
    private static int getDayType(Calendar calendar) {
        if (DateUtils.isToday(calendar.getTimeInMillis())) {
            return DAY_TYPE_TODAY;
        }
        if (CalendarUtils.isSaturday(calendar)) {
            return DAY_TYPE_SATURDAY;
        }
        if (CalendarUtils.isSunday(calendar)) {
            return DAY_TYPE_SUNDAY;
        }
        return DAY_TYPE_NORMAL;
    }

    /**
     * reset cells.
     */
    private void resetCells() {
        View v;
        for (int i = CALENDAR_WEEK_COUNT, size = getChildCount(); i < size; i++) {
            v = getChildAt(i);
            v.setId(0);
            v.setTag(null);
            v.setSelected(false);
            v.setOnClickListener(null);
            v.setVisibility(INVISIBLE);
            mAdapter.resetCell(v);
        }
    }

    /**
     * get current calendar.
     *
     * @return
     */
    public Calendar getCurrentCalendar() {
        return (Calendar) mCurrentCalendar.clone();
    }

    /**
     * get current year.
     *
     * @return
     */
    public int getCurrentYear() {
        return CalendarUtils.getYear(mCurrentCalendar);
    }

    /**
     * get current month.
     *
     * @return
     */
    public int getCurrentMonth() {
        return CalendarUtils.getMonth(mCurrentCalendar);
    }

    /**
     * get current day.
     *
     * @return
     */
    public int getSelectedDay() {
        return mSelectedDate;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == 0) {
            return;
        }
        view.setSelected(true);
        mSelectedView = view;
        mSelectedDate = (int) view.getTag();
        if (mOnCalendarCellViewClickListener != null) {
            mOnCalendarCellViewClickListener.onClick(view, CalendarUtils.getYear(mCurrentCalendar), CalendarUtils.getMonth(mCurrentCalendar) + 1, mSelectedDate);
        }
    }

    /**
     * get listener.
     *
     * @return
     */
    public OnCalendarCellViewClickListener getOnCalendarCellViewClickListener() {
        return mOnCalendarCellViewClickListener;
    }

    /**
     * set listener.
     *
     * @param onCalendarCellViewClickListener
     */
    public void setOnCalendarCellViewClickListener(OnCalendarCellViewClickListener onCalendarCellViewClickListener) {
        if (onCalendarCellViewClickListener != null) {
            mOnCalendarCellViewClickListener = onCalendarCellViewClickListener;
        }
    }

    /**
     * Cell in Calendar  Clicked Listener.
     */
    public interface OnCalendarCellViewClickListener {
        void onClick(View cellView, int year, int month, int date);
    }

}
