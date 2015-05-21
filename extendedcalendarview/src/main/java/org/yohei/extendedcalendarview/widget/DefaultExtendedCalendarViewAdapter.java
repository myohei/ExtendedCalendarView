package org.yohei.extendedcalendarview.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.yohei.extendedcalendarview.R;

/**
 * Created by maeda on 15/05/21.
 */
public class DefaultExtendedCalendarViewAdapter implements ExtendedCalendarViewAdapter {

    private final Context mContext;

    public DefaultExtendedCalendarViewAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public View getCellView(int month, int date, @ExtendedCalendarView.DayType int type, @Nullable View convertCell, @NonNull ViewGroup viewGroup) {
        ECCalendarCell v;
        if (convertCell == null) {
            v = (ECCalendarCell) LayoutInflater.from(mContext).inflate(R.layout.ecv_view_cell, viewGroup, false);
        } else {
            v = (ECCalendarCell) convertCell;
        }
        if (type != ExtendedCalendarView.DAY_TYPE_EMPTY) {
            v.getDateText().setText(String.valueOf(date));
        }
        switch (type) {
            case ExtendedCalendarView.DAY_TYPE_SATURDAY:
                v.getDateText().setTextColor(Color.BLUE);
                break;
            case ExtendedCalendarView.DAY_TYPE_SUNDAY:
                v.getDateText().setTextColor(Color.RED);
                break;
            case ExtendedCalendarView.DAY_TYPE_TODAY:
                break;
        }
        return v;
    }

    @Override
    public void resetCell(View cell) {
        ECCalendarCell v = (ECCalendarCell) cell;
        v.setDateText("");
        v.setTopText("");
        v.setMiddleText("");
        v.setBottomText("");
    }
}
