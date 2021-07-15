package com.wingsquare.nice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.wingsquare.nice.adapter.CustomAdapter;
import com.wingsquare.nice.room.AppDatabase;
import com.wingsquare.nice.room.Event;
import com.wingsquare.nice.room.EventDao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class EventsActivity extends AppCompatActivity {

    int mYear = 2021;
    int mMonth = 7;
    int mDay = 5;

    RecyclerView mRecyclerView;
    CustomAdapter mCustomAdapter;
    private static final String TAG = EventsActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        mRecyclerView = findViewById(R.id.recycler_view);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mYear = getIntent().getIntExtra("year", 2021);
        mMonth = getIntent().getIntExtra("month", 7);
        mDay = getIntent().getIntExtra("day", 5);

        getSupportActionBar().setTitle("Events on " + mDay + "-" + ( mMonth + 1 ) + "-" + mYear);

    }

    @Override
    protected void onStart() {
        super.onStart();

        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "event")
                .allowMainThreadQueries()
                .build();
        EventDao eventDao = db.eventDao();
        List<Event> events = eventDao.getAllEventsByDate(getDayStartTime(), getDayEndTime());
        // List<Event> events = eventDao.getAllEvents();
        mCustomAdapter = new CustomAdapter(events);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());

        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mCustomAdapter);

        // Log.d(TAG, " start : " + getDayStartTime() + ", end : " + getDayEndTime() + " , CurTime : " + Calendar.getInstance().getTimeInMillis());
    }

    long getDayStartTime(){

        Calendar c1 = Calendar.getInstance();
        c1.set(mYear,mMonth,mDay,0,0,0);
        long startTime = c1.getTimeInMillis();
        return startTime;
    }

    long getDayEndTime(){

        Calendar c2 = Calendar.getInstance();
        c2.set(mYear,mMonth,mDay,23,59,59);
        long endTime = c2.getTimeInMillis();
        return endTime;

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_add_event:
                Intent intent = new Intent(getApplicationContext(), AddEventActivity.class);
                intent.putExtra("year", mYear);
                intent.putExtra("month", mMonth);
                intent.putExtra("day", mDay);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}