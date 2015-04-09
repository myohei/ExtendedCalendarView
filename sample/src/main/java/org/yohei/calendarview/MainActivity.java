package org.yohei.calendarview;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;

import org.yohei.extendedcalendarview.widget.ECCalendarCell;
import org.yohei.extendedcalendarview.widget.ExtendedCalendarView;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ExtendedCalendarView extendedCalendarView = (ExtendedCalendarView) findViewById(R.id.calendar);
        extendedCalendarView.setMonth(2015, 4);
        extendedCalendarView.setOnCalendarCellViewClickListener(new ExtendedCalendarView.OnCalendarCellViewClickListener() {
            @Override
            public void onClick(View cellView, int year, int month, int date) {
                Log.d("MainActivty", cellView.getClass().getSimpleName() + year + "/" + month + "/" + date);
            }
        });
        ECCalendarCell v = (ECCalendarCell) extendedCalendarView.getCellView(12);
        Log.d("", "== " + v.getClass().getSimpleName());
        v.setMiddleText("ミドル");
        v.setTopText("とーっぷ");
        v.setBottomText("ぼっとm");
    }
}
