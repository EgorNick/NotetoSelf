package com.example.notetoself;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences mPrefs;
    private SharedPreferences.Editor mEditor;

    private boolean mShowDividers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mPrefs = getSharedPreferences("Note to self", MODE_PRIVATE);
        mEditor = mPrefs.edit();

        mShowDividers = mPrefs.getBoolean("dividers", true);

        Switch switch1 = findViewById(R.id.switch1);
        // Set the switch on or off as appropriate
        switch1.setChecked(mShowDividers);

        switch1.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {

                    public void onCheckedChanged(
                            CompoundButton buttonView,
                            boolean isChecked) {

                        if (isChecked) {
                            mEditor.putBoolean(
                                    "dividers", true);

                            mShowDividers = true;

                        } else {
                            mEditor.putBoolean(
                                    "dividers", false);

                            mShowDividers = false;
                        }
                        // Анимация для визуального подтверждения действия
                        ScaleAnimation anim = new ScaleAnimation(
                                1f, 0.8f, // начальный и конечный масштабы по оси X
                                1f, 0.8f, // начальный и конечный масштабы по оси Y
                                Animation.RELATIVE_TO_SELF, 0.5f, // точка начала анимации по оси X
                                Animation.RELATIVE_TO_SELF, 0.5f // точка начала анимации по оси Y
                        );
                        anim.setDuration(200); // продолжительность анимации
                        anim.setRepeatCount(1); // количество повторений
                        anim.setRepeatMode(Animation.REVERSE); // режим повторений
                        buttonView.startAnimation(anim);
                    }
                }
        );
    }

    @Override
        protected void onPause() {
            super.onPause();

            // Save the settings here
            mEditor.commit();
        }
    }
