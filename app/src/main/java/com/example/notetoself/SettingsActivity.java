package com.example.notetoself;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
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

        Button changeColorButton = findViewById(R.id.changeColorButton);
        changeColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Получаем корневой View вашего приложения
                View rootView = getWindow().getDecorView().getRootView();

                // Получаем следующий цвет из массива colors
                int color = colors[colorIndex];

                // Анимация плавного затухания и появления нового цвета
                rootView.animate().alpha(0f).setDuration(300).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        // Устанавливаем новый цвет фона для корневого View
                        rootView.setBackgroundColor(color);
                        rootView.animate().alpha(1f).setDuration(300).start();
                    }
                });

                // Сохраняем выбранный цвет в SharedPreferences
                mEditor.putInt("backgroundColor", color);
                mEditor.apply();

                // Увеличиваем индекс цвета для переключения на следующий цвет в массиве colors
                colorIndex = (colorIndex + 1) % colors.length;

                Toast.makeText(SettingsActivity.this, "Цвет фона изменен", Toast.LENGTH_SHORT).show();
            }
        });

        // Устанавливаем сохраненный цвет фона при загрузке активности
        int savedColor = mPrefs.getInt("backgroundColor", Color.WHITE);
        View rootView = getWindow().getDecorView().getRootView();
        rootView.setBackgroundColor(savedColor);

        // Всплывающее окно
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"your_email@example.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Обратная связь по приложению Note to Self");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Здравствуйте,\n\nЯ хотел бы предложить следующее улучшение для вашего приложения:\n\n");

        try {
            startActivity(Intent.createChooser(emailIntent, "Отправить сообщение..."));
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(this, "Нет приложения для отправки электронной почты.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
        protected void onPause() {
            super.onPause();

            // Save the settings here
            mEditor.commit();
        }
    }
