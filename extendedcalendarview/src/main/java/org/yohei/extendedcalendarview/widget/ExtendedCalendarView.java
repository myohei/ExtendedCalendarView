package org.yohei.extendedcalendarview.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.yohei.extendedcalendarview.R;
import org.yohei.extendedcalendarview.widget.util.CalendarUtils;

import java.util.Calendar;

/**
 * Created by yohei on 3/15/15.
 */
public class ExtendedCalendarView extends ExtendedBaseCalendarView implements View.OnClickListener {

    private static final int FIRST_DATE = 1;
    private int mDateTextId;
    private OnCalendarCellViewClickListener mOnCalendarCellViewClickListener;
    private static final String CELL_ID_PREFIX = "ecv__date";
    private Calendar mCurrentCalendar;
    private boolean mWeekColorEnable;
    private int mCurrentDateColor;
    private int mSelectedDay;
    private View mSelectedView;

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
        final int cellLayout = typedArray.getResourceId(R.styleable.ExtendedCalendarView_cell_layout, R.layout.ecv_view_cell);
        mDateTextId = typedArray.getResourceId(R.styleable.ExtendedCalendarView_cell_date_text_id, android.R.id.text1);
        mWeekColorEnable = typedArray.getBoolean(R.styleable.ExtendedCalendarView_week_color_enable, true);
        mCurrentDateColor = typedArray.getColor(R.styleable.ExtendedCalendarView_current_day_color, R.color.ecv__current_day);
        typedArray.recycle();
        final LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup vg = (ViewGroup) inflater.inflate(R.layout.ecv__view_calendar, this, true);
        for (int i = vg.getChildCount(); i < MUST_CHILD_COUNT; i++) {
            View v = inflater.inflate(cellLayout, this, false);
            v.setOnClickListener(this);
            this.addView(v);
        }
        if (mWeekColorEnable) {
            ECWeekItemView weekItemView = (ECWeekItemView) findViewById(R.id.ecv__week_sun);
            weekItemView.getWeekText().setTextColor(Color.RED);
            weekItemView = (ECWeekItemView) findViewById(R.id.ecv__week_sat);
            weekItemView.getWeekText().setTextColor(Color.BLUE);
        }
        Calendar calendar = Calendar.getInstance();
        setMonth(calendar.get(Calendar.MONTH));
    }

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
        resetCells();
        final Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, FIRST_DATE); // japan
        final int firstWeek = calendar.get(Calendar.DAY_OF_WEEK); // 1-7
        final int lastDate = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        mSelectedDay = CalendarUtils.getDayOfMonth(calendar);
        View cell;
        for (int i = CALENDAR_WEEK_COUNT + firstWeek - 1, size = i + lastDate; i < size; i++, calendar.add(Calendar.DAY_OF_MONTH, 1)) {
            cell = getChildAt(i);
            final int day = CalendarUtils.getDayOfMonth(calendar);
            final int id = getResources().getIdentifier(CELL_ID_PREFIX + day, "id", getContext().getPackageName());
            cell.setId(id);
            cell.setTag(day);
            cell.setEnabled(true);
            cell.setOnClickListener(this);
            cell.setVisibility(VISIBLE);
            final TextView tv = (TextView) cell.findViewById(mDateTextId);
            if (tv == null) {
                continue;
            }
            tv.setText(String.valueOf(day));
            if (DateUtils.isToday(calendar.getTimeInMillis())) {
                tv.setTextColor(getResources().getColor(mCurrentDateColor));
                mSelectedDay = day;
                cell.setSelected(true);
                continue;
            }
            if (!mWeekColorEnable || !CalendarUtils.isHoliday(calendar)) {
                continue;
            }
            if (CalendarUtils.isSaturday(calendar)) {
                setTextColorSaturday(tv);
            } else if (CalendarUtils.isSunday(calendar)) {
                setTextColorSunday(tv);
            }
        }
        mCurrentCalendar = (Calendar) calendar.clone();
    }

    private void resetCells() {
        View v;
        for (int i = CALENDAR_WEEK_COUNT, size = getChildCount(); i < size; i++) {
            v = getChildAt(i);
            v.setId(0);
            v.setTag(null);
            v.setSelected(false);
            v.setVisibility(GONE);
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
        return mSelectedDay;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == 0) {
            return;
        }
        view.setSelected(true);
        mSelectedView = view;
        mSelectedDay = (int) view.getTag();
        if (mOnCalendarCellViewClickListener != null) {
            mOnCalendarCellViewClickListener.onClick(view, CalendarUtils.getYear(mCurrentCalendar), CalendarUtils.getMonth(mCurrentCalendar) + 1, mSelectedDay);
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

    private static void setTextColorSaturday(@NonNull TextView tv) {
        tv.setTextColor(Color.BLUE);
    }

    private static void setTextColorSunday(@NonNull TextView tv) {
        tv.setTextColor(Color.RED);
    }

    public interface OnCalendarCellViewClickListener {
        void onClick(View cellView, int year, int month, int date);
    }

}
