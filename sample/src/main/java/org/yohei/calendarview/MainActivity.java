package org.yohei.calendarview;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.ActionBarActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.yohei.extendedcalendarview.widget.DefaultExtendedCalendarViewAdapter;
import org.yohei.extendedcalendarview.widget.ECCalendarCell;
import org.yohei.extendedcalendarview.widget.ExtendedCalendarView;
import org.yohei.extendedcalendarview.widget.util.CalendarUtils;

import java.util.Calendar;


public class MainActivity extends ActionBarActivity {

    Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView yearMonth = (TextView) findViewById(R.id.year_month);
        final ExtendedCalendarView extendedCalendarView = (ExtendedCalendarView) findViewById(R.id.calendar);
        extendedCalendarView.setAdapter(new DefaultExtendedCalendarViewAdapter(this));
        extendedCalendarView.setMonth(2015, 4);
        yearMonth.setText(DateUtils.formatDateTime(this, extendedCalendarView.getCurrentCalendar().getTimeInMillis(), DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_NO_MONTH_DAY));
        extendedCalendarView.setOnCalendarCellViewClickListener(new ExtendedCalendarView.OnCalendarCellViewClickListener() {
            @Override
            public void onClick(View cellView, int year, int month, int date) {
                Log.d("MainActivty", cellView.getClass().getSimpleName() + year + "/" + month + "/" + date);
            }
        });
        ECCalendarCell v = (ECCalendarCell) extendedCalendarView.getCellView(12);
        v.setTopText("top text");
        final Runnable nextMonth = new Runnable() {
            private int count = 0;

            @Override
            public void run() {
                Calendar calendar = extendedCalendarView.getCurrentCalendar();
                calendar.add(Calendar.MONTH, 1);
                yearMonth.setText(DateUtils.formatDateTime(getApplicationContext(), extendedCalendarView.getCurrentCalendar().getTimeInMillis(), DateUtils.FORMAT_SHOW_YEAR));
                extendedCalendarView.setMonth(CalendarUtils.getYear(calendar), CalendarUtils.getMonth(calendar));
                if (count < 10) {
                    mHandler.postDelayed(this, 1000);
                    count++;
                }
            }
        };
        mHandler.postDelayed(nextMonth, 3000);
    }
}
