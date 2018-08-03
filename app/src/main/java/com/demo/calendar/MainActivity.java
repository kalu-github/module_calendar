package com.demo.calendar;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.lib.calendar.CalendarLayout;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comm_calendar);

        final TextView date = findViewById(R.id.date);
        final CalendarLayout calendar = findViewById(R.id.calendar);
        calendar.setRange(2016, 5, 2018, 6);
        calendar.notifyDataSetChanged(2018, 6, 2);

        calendar.setOnCalendarChangeListener(new CalendarLayout.OnCalendarChangeListener() {
            @Override
            public void onChange(int year, int month, int day, int maxDay, boolean isClick, boolean isClickBefore) {
                Log.e("jiji", "year = " + year + ", month = " + month + ", day = " + day + ", maxDay = " + maxDay + ", isClick = " + isClick + ", isClickBefore = " + isClickBefore);

                date.setText(year + "年" + month + "月");
                if (isClick) {
                    Toast.makeText(getApplicationContext(), "点击:" + year + "-" + month + "-" + day, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "切换:" + year + "-" + month + "-" + day, Toast.LENGTH_SHORT).show();
                }
                //    Log.e("onCalendarChange", year + " == " + month + " == " + day + " == " + isClick);
            }
        });
    }
}
