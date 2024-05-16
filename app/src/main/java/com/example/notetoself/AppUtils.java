package com.example.notetoself;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.View;

public class AppUtils {

    private static final int[] COLORS = {Color.WHITE, Color.GRAY, Color.CYAN, Color.BLACK};
    private static final String PREFS_NAME = "AppPreferences";
    private static final String KEY_COLOR_INDEX = "colorIndex";

    public static void setAppBackgroundColor(Context context, int color) {
        View rootView = ((Activity) context).getWindow().getDecorView();
        rootView.setBackgroundColor(color);
        saveBackgroundColor(context, color);
    }

    public static void saveBackgroundColor(Context context, int color) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("backgroundColor", color);
        editor.apply();
    }

    public static int getSavedBackgroundColor(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getInt("backgroundColor", Color.WHITE);
    }

    public static int getNextColor(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int currentIndex = prefs.getInt(KEY_COLOR_INDEX, 0); // Default index is 0
        int nextIndex = (currentIndex + 1) % COLORS.length; // Get next index, wrap around if needed
        prefs.edit().putInt(KEY_COLOR_INDEX, nextIndex).apply(); // Save next index
        return COLORS[nextIndex]; // Return next color
    }

}
