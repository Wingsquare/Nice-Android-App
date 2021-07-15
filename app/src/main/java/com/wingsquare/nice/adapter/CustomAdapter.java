package com.wingsquare.nice.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.wingsquare.nice.R;
import com.wingsquare.nice.room.AppDatabase;
import com.wingsquare.nice.room.Event;
import com.wingsquare.nice.room.EventDao;

import java.util.List;

public class CustomAdapter  extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private final List<Event> eventList;
    private final String TAG = CustomAdapter.class.getName();


    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView textView;
        private final ImageView ivDelete;
        private final String TAG = ViewHolder.class.getName();
        ViewHolder(View view){
            super(view);
            textView = view.findViewById(R.id.textView);
            ivDelete = view.findViewById(R.id.iv_delete);
        }

        TextView getTextView(){
            return textView;
        }
    }

    public CustomAdapter(List<Event> eventList){
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText("" + eventList.get(position).event);
        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppDatabase db = Room.databaseBuilder(v.getContext(), AppDatabase.class, "event")
                        .allowMainThreadQueries()
                        .build();
                EventDao eventDao = db.eventDao();
                eventDao.deleteById(eventList.get(position).uid);
                eventList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, eventList.size());

            }
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }


}
