package com.demo.calendar;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;

import com.lib.calendar.CalendarLayout;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comm_calendar);
//
        final TextView date = findViewById(R.id.date);
        final CalendarLayout calendar = findViewById(R.id.calendar);
        calendar.setOnCalendarChangeListener(new CalendarLayout.OnCalendarChangeListener() {
            @Override
            public void onCalendarChange(int year, int month, int day, boolean isClick) {
                date.setText(year + "年" + month + "月");
            //    Log.e("onCalendarChange", year + " == " + month + " == " + day + " == " + isClick);
            }
        });
        calendar.setSelectDate(2018, 6, 2);
    }
}
