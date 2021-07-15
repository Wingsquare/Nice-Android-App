package com.wingsquare.nice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;

import com.wingsquare.nice.room.AppDatabase;
import com.wingsquare.nice.room.EventDao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import sun.bob.mcalendarview.MCalendarView;
import sun.bob.mcalendarview.MarkStyle;
import sun.bob.mcalendarview.listeners.OnDateClickListener;
import sun.bob.mcalendarview.listeners.OnMonthChangeListener;
import sun.bob.mcalendarview.vo.DateData;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();

    MCalendarView mCalendarView;
    TextView mTvCalendarViewHeader;
    List<String> mMarkedDates;
    int mYear = 2021;
    int mMonth = 7;
    String[] months = {  "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCalendarView = findViewById(R.id.calendarView);
        mTvCalendarViewHeader = findViewById(R.id.tvCalendarHeader);

        mCalendarView.hasTitle(false);
        mMarkedDates = new ArrayList<String>();

        // Click Event Listener
        mCalendarView.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onDateClick(View view, DateData date) {
                Intent intent = new Intent(getApplicationContext(),EventsActivity.class);
                intent.putExtra("year",date.getYear());
                intent.putExtra("month", date.getMonth()-1);
                intent.putExtra("day", date.getDay());

                startActivity(intent);
            }
        });


        // Month changed Listener
        mCalendarView.setOnMonthChangeListener(new OnMonthChangeListener() {
            @Override
            public void onMonthChange(int year, int month) {
                mYear = year;
                mMonth = month;
                markEventDates(year, month);
            }
        });

        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH)+1;
    }

    @Override
    protected void onResume() {
        super.onResume();
        markEventDates(mYear, mMonth);
    }


    void markEventDates(int year, int month){
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),AppDatabase.class,"event")
                .allowMainThreadQueries()
                .build();
        EventDao eventDao = db.eventDao();
        long startTime = getStartTime(year, month);
        long endTime = getEndTime(year, month);
        List<String> dates = eventDao.getDistinctDates(startTime, endTime);

        // Unmark dates
        //mCalendarView.getMarkedDates().getAll().clear();
        for (String d : mMarkedDates){
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal  = Calendar.getInstance();
            try {
                cal.setTime(df.parse(d));
            }catch(Exception e){
                Log.d(TAG, "Current Month : Exception : " + e.toString());
            }
            mCalendarView.unMarkDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
        }



        // Mark Dates
        for (String d : dates){
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal  = Calendar.getInstance();
            try {
                cal.setTime(df.parse(d));
            }catch(Exception e){
                Log.d(TAG, "Current Month : Exception : " + e.toString());
            }


            DateData dateData = new DateData(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH))
                    .setMarkStyle(MarkStyle.DOT, Color.BLUE);
            mCalendarView.markDate(dateData);

        }

        // Update Calendar Header
        mTvCalendarViewHeader.setText(months[mMonth-1] + " - " + mYear);

        mMarkedDates = dates;
    }

    long getStartTime(int year, int month){
        long startTimeInMillis = 0;
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month-1, 1, 0, 0, 0);
        startTimeInMillis = calendar.getTimeInMillis();
        return startTimeInMillis;
    }

    long getEndTime(int year, int month){
        long endTimeInMillis = 0;
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1, 23, 59, 59);   // First day of the next month
        calendar.add(Calendar.DATE, -1);    // Get Previous Day
        endTimeInMillis = calendar.getTimeInMillis();
        return endTimeInMillis;
    }

}