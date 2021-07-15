package com.wingsquare.nice.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Event {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "event")
    public String event;

    @ColumnInfo(name = "ttime")
    public long ttime;
}
