package org.yohei.extendedcalendarview.widget;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by maeda on 15/05/21.
 */
public interface ExtendedCalendarViewAdapter {

    /**
     * create ViewCell. You must set Day at your Cell.
     *
     * @param viewGroup
     * @param month
     * @param date
     * @param type
     * @return
     */
    View getCellView(int month, int date, @ExtendedCalendarView.DayType int type, @Nullable View convertCell, @NonNull ViewGroup viewGroup);

    /**
     * reset your cell. if needed.
     *
     * @param cell
     */
    void resetCell(@NonNull View cell);
}
