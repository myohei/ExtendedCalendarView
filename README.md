ExtendedCalendarView
----------------------------

## TODO



## Usage


activity_main.xml

```
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
              android:orientation="vertical"
              android:layout_width="match_parent"
              android:layout_height="match_parent">
    <org.yohei.extendedcalendarview.widget.ExtendedCalendarView
        android:id="@+id/calendar"
        android:paddingTop="16dp"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
</LinearLayout>
```

MainActivity.java


```
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView yearMonth = (TextView) findViewById(R.id.year_month);
        final ExtendedCalendarView extendedCalendarView = (ExtendedCalendarView) findViewById(R.id.calendar);
        extendedCalendarView.setMonth(2015, 4);
        extendedCalendarView.setOnCalendarCellViewClickListener(new ExtendedCalendarView.OnCalendarCellViewClickListener() {
            @Override
            public void onClick(View cellView, int year, int month, int date) {
                Log.d("Activty", cellView.getClass().getSimpleName() + year + "/" + month + "/" + date);
            }
        });
        ECCalendarCell v = (ECCalendarCell) extendedCalendarView.getCellView(12);
        v.setTopText("top text");
	}
}
```

![](https://github.com/myohei/ExtendedCalendarView/img/img01.png)

### Customize 


#### Cell Customize

activity_main.xml

```
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
              xmlns:calendar="http://schemas.android.com/apk/res-auto"
              android:orientation="vertical"
              android:layout_width="match_parent"
              android:layout_height="match_parent">
    <org.yohei.extendedcalendarview.widget.ExtendedCalendarView
        android:id="@+id/calendar"
        calendar:cell_layout="@layout/custom_cell"
        android:paddingTop="16dp"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
</LinearLayout>
```

layout/custom_cell.xml

```
<FrameLayout>

	<TextView
		android:id="@android:id/text1" // Date text Need.
		/>

</FrameLayout>

```

MainActivity.java

```
        View v = extendedCalendarView.getCellView(12); // your custom cell view.
        TextView v = v.findViewById(android.R.id.text1);
```

#### Weekly Color Enabled

