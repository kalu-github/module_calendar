package com.demo.calendar;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.lib.calendar.CalendarLayout;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comm_calendar);

        final TextView date = findViewById(R.id.date);
        final CalendarLayout calendar = findViewById(R.id.calendar);
        calendar.setRange(2017, 1, 2018, 6);
        calendar.notifyDataSetChanged(2018, 6, 2);

        calendar.setOnCalendarChangeListener(new CalendarLayout.OnCalendarChangeListener() {
            @Override
            public void onCalendarChange(int year, int month, int day, int maxDay, boolean isClick, boolean isInflate) {
                date.setText(year + "年" + month + "月");
                //    Log.e("onCalendarChange", year + " == " + month + " == " + day + " == " + isClick);
            }
        });
    }
}
