package com.wingsquare.nice.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;

@Dao
public interface EventDao {
//    @Query("select * from event order by ttime")
//    List<Event> getAll();

    @Query("select * from event order by ttime")
    List<Event> getAllEvents();

    @Query("select * from event where ttime/(1000*60) >= :ttime1/(1000*60) and ttime/(1000*60) <= :ttime2/(1000*60) order by ttime")
    List<Event> getAllEventsByDate(long ttime1, long ttime2);

    @Query("SELECT DISTINCT(strftime('%Y-%m-%d', datetime(ttime/1000, 'unixepoch','localtime'))) FROM event where ttime/(1000*60) >= :ttime1/(1000*60) and ttime/(1000*60) <= :ttime2/(1000*60)")
    List<String> getDistinctDates(long ttime1, long ttime2);

    @Insert
    void insert(Event... events);

    @Query("delete from event where uid = :id")
    void deleteById(int id);

}
