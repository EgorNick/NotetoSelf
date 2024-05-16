package com.example.notetoself;

// here was
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;

public class MainActivity extends AppCompatActivity {


    private JSONSerializer mSerializer;

    private List<Note> noteList;

    private RecyclerView recyclerView;

    private NoteAdapter mAdapter;

    private SharedPreferences mPrefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogNewNote dialog = new DialogNewNote();
                dialog.show(getSupportFragmentManager(), "");

            }

        });

        mSerializer = new JSONSerializer("NoteToSelf.json",
                getApplicationContext());

        try {
            noteList = mSerializer.load();
        } catch (Exception e) {
            noteList = new ArrayList<Note>();
            Log.e("Ошибка в загрузке: ", "", e);
        }


        recyclerView =
                findViewById(R.id.recyclerView);

        mAdapter = new NoteAdapter(this, noteList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(mAdapter);

        //applySavedBackgroundColor();
        // Применяем сохраненный цвет фона при создании активности
        int savedColor = AppUtils.getSavedBackgroundColor(this);
        AppUtils.setAppBackgroundColor(this, savedColor);
    }

    private void applySavedBackgroundColor() {
        int savedColor = AppUtils.getSavedBackgroundColor(this);
        AppUtils.setAppBackgroundColor(this, savedColor);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void createNewNote(Note n){

        noteList.add(n);
        mAdapter.notifyDataSetChanged();
    }
    @SuppressLint("NotifyDataSetChanged")
    public void deleteNote(Note n){

        noteList.remove(n);
        mAdapter.notifyDataSetChanged();
    }



    public void showNote(int noteToShow){
        DialogShowNote dialog = new DialogShowNote();
        dialog.sendNoteSelected(noteList.get(noteToShow));
        dialog.show(getSupportFragmentManager(), "");
    }


    @Override
    protected void onResume(){
        super.onResume();

        // Применяем сохраненный цвет фона при возобновлении активности
        int savedColor = AppUtils.getSavedBackgroundColor(this);
        AppUtils.setAppBackgroundColor(this, savedColor);
        //applySavedBackgroundColor();

        mPrefs = getSharedPreferences(
                "Note to self", MODE_PRIVATE);

        boolean mShowDividers = mPrefs.getBoolean(
                "dividers", true);

        if(mShowDividers) {
            recyclerView.addItemDecoration(
                    new DividerItemDecoration(
                            this, LinearLayoutManager.VERTICAL));
        }else{
            if(recyclerView.getItemDecorationCount() > 0) {
                recyclerView.removeItemDecorationAt(0);
            }
        }
    }



    public void saveNotes(){
        try{
            mSerializer.save(noteList);

        }catch(Exception e){
            Log.e("Ошибка в сохранении","", e);
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        saveNotes();

    }


}
