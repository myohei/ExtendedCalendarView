package org.yohei.extendedcalendarview.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.yohei.extendedcalendarview.R;

/**
 * Created by yohei on 3/15/15.
 */
public class ECWeekItemView extends FrameLayout {

    private TextView mWeekText;

    public ECWeekItemView(Context context) {
        this(context, null);
    }

    public ECWeekItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ECWeekItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ECWeekItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs, defStyleAttr, defStyleRes);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ECWeekItemView);
        final String label = typedArray.getString(R.styleable.ECWeekItemView_week_value);
        typedArray.recycle();
        View view = LayoutInflater.from(context).inflate(R.layout.view_calendar_week_item, this, true);
        mWeekText = (TextView) view.findViewById(R.id.ecv_week);
        mWeekText.setText(label);
    }

    public TextView getWeekText() {
        return mWeekText;
    }
}
