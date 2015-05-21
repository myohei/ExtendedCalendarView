package org.yohei.extendedcalendarview.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.yohei.extendedcalendarview.R;

/**
 * Created by yohei on 3/16/15.
 */
public class ECCalendarCell extends LinearLayout {

    private TextView mDateText;
    private TextView mTopText;
    private TextView mMiddleText;
    private TextView mBottomText;

    public ECCalendarCell(Context context) {
        super(context, null);
    }

    public ECCalendarCell(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs, 0);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public ECCalendarCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ECCalendarCell(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs, defStyleAttr);
    }

    /**
     * initialize view.
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        View view = LayoutInflater.from(context).inflate(R.layout.ecv__view_calendar_cell_pref, this, true);
        mDateText = (TextView) view.findViewById(android.R.id.text1);
        mTopText = (TextView) view.findViewById(R.id.ecv_cell_top_txt);
        mMiddleText = (TextView) view.findViewById(R.id.ecv_cell_middle_txt);
        mBottomText = (TextView) view.findViewById(R.id.ecv_cell_bottom_txt);
    }

    public TextView getDateText() {
        return mDateText;
    }

    /**
     * set date text.
     *
     * @param date
     */
    public void setDateText(String date) {
        setText(mDateText, date);
    }

    /**
     * set text at top text view.
     *
     * @param txt text
     */
    public void setTopText(String txt) {
        setText(mTopText, txt);
    }

    /**
     * set text at middle text view.
     *
     * @param txt text
     */
    public void setMiddleText(String txt) {
        setText(mMiddleText, txt);
    }

    /**
     * set text at bottom text view.
     *
     * @param txt text
     */
    public void setBottomText(String txt) {
        setText(mBottomText, txt);
    }

    /**
     * @param tv
     * @param txt
     */
    private static void setText(final @NonNull TextView tv, final String txt) {
        tv.setText(txt);
        tv.setVisibility(VISIBLE);
    }
}
