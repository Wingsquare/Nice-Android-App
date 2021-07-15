package com.wingsquare.nice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wingsquare.nice.room.AppDatabase;
import com.wingsquare.nice.room.Event;
import com.wingsquare.nice.room.EventDao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AddEventActivity extends AppCompatActivity {

    int mYear = 2021;
    int mMonth = 7;
    int mDay = 5;

    Button mBtnSave;
    EditText mTxtEvent;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    private static final  String TAG = AddEventActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        mBtnSave = findViewById(R.id.btn_save);
        mTxtEvent = findViewById(R.id.txt_event);

        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String eventName = mTxtEvent.getText().toString().trim();
                if(eventName.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please enter event to be saved", Toast.LENGTH_LONG).show();
                    return;
                }


                AppDatabase db = Room.databaseBuilder(getApplicationContext(),AppDatabase.class,"event")
                        .allowMainThreadQueries()
                        .build();
                EventDao eventDao = db.eventDao();
                Calendar calender = Calendar.getInstance();
                calender.set(mYear, mMonth, mDay, 0, 0, 0);

                Event event = new Event();
                event.event = mTxtEvent.getText().toString();
                event.ttime = calender.getTimeInMillis();

                eventDao.insert(event);
                finish();

            }
        });



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mYear = getIntent().getIntExtra("year",2021);
        mMonth = getIntent().getIntExtra("month",7);
        mDay = getIntent().getIntExtra("day", 5);

        getSupportActionBar().setTitle("New Event for " + mDay + "-" + (mMonth+1) + "-" + mYear);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}