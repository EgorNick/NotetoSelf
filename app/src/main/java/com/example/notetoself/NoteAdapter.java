package com.example.notetoself;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import android.graphics.Color;
import android.icu.text.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.WindowDecorActionBar;
import androidx.recyclerview.widget.RecyclerView;



public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ListItemHolder> {

    private final List<Note> mNoteList;
    private final MainActivity mMainActivity;

    public NoteAdapter(MainActivity mainActivity, List<Note> noteList) {

        mMainActivity = mainActivity;
        mNoteList = noteList;
    }


    @NonNull
    @Override
    public NoteAdapter.ListItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listitem, parent, false);

        return new ListItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.ListItemHolder holder, int position) {
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedToday = sdf.format(currentDate);


        Note note = mNoteList.get(position);

        try {
            Date noteDate = sdf.parse(note.getDate());
            if (noteDate != null && noteDate.before(currentDate) || noteDate.before(currentDate)) {
                holder.mDate.setTextColor(Color.RED);
            } else {
                holder.mDate.setTextColor(Color.GREEN);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.mTitle.setText(note.getTitle());
        holder.mDate.setText(note.getDate());

        if(note.getDescription().length() < 15) {
            holder.mDescription.setText(note.getDescription());
        }
        else {
            String truncatedDescription = note.getDescription().substring(0, note.getDescription().length() / 2) + "...";
            holder.mDescription.setText(truncatedDescription);
        }

        if(note.isIdea()){
            holder.mStatus.setText(R.string.idea_text);
        }
        else if(note.isImportant()){
            holder.mStatus.setText(R.string.important_text);
        }
        else if(note.isTodo()){
            holder.mStatus.setText(R.string.todo_text);
        }


    }


    @Override
    public int getItemCount() {
        return mNoteList.size();
    }

    public class ListItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mDate;
        TextView mTitle;
        TextView mDescription;
        TextView mStatus;


        public ListItemHolder(View view) {
            super(view);

            mTitle =
                    view.findViewById(R.id.textViewTitle);
            mDate = view.findViewById(R.id.dateViewStatus);

            mDescription =
                    view.findViewById(R.id.textViewDescription);

            mStatus =
                    view.findViewById(R.id.textViewStatus);

            view.setClickable(true);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            mMainActivity.showNote(getAdapterPosition());
        }
    }

}
