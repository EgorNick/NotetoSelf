package com.example.notetoself;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences mPrefs;
    private SharedPreferences.Editor mEditor;
    private boolean mShowDividers;
    private int[] colors = {Color.WHITE, Color.BLACK, Color.GRAY, Color.CYAN};
    private int colorIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mPrefs = getSharedPreferences("Note to self", MODE_PRIVATE);
        mEditor = mPrefs.edit();

        mShowDividers = mPrefs.getBoolean("dividers", true);

        Switch switch1 = findViewById(R.id.switch1);
        switch1.setChecked(mShowDividers);

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mEditor.putBoolean("dividers", true);
                    mShowDividers = true;
                } else {
                    mEditor.putBoolean("dividers", false);
                    mShowDividers = false;
                }
                mEditor.apply();
            }
        });

        Button changeColorButton = findViewById(R.id.changeColorButton);
        changeColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nextColor = AppUtils.getNextColor(SettingsActivity.this);
                AppUtils.setAppBackgroundColor(SettingsActivity.this, nextColor);
                Toast.makeText(SettingsActivity.this, "Цвет фона изменен", Toast.LENGTH_SHORT).show();
            }
        });

        applySavedBackgroundColor();
    }

    private void applySavedBackgroundColor() {
        int savedColor = AppUtils.getSavedBackgroundColor(this);
        AppUtils.setAppBackgroundColor(this, savedColor);
    }

    @Override
    protected void onResume() {
        super.onResume();
        applySavedBackgroundColor();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mEditor.commit();
    }
}
