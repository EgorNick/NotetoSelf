package com.example.notetoself;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;
import android.view.ViewGroup;

public class AppUtils {

    private static final int[] COLORS = {Color.WHITE, Color.GRAY, Color.CYAN, Color.BLACK};
    private static final String PREFS_NAME = "AppPreferences";
    private static final String KEY_COLOR_INDEX = "colorIndex";

    public static void setAppBackgroundColor(Context context, int color) {
        View rootView = ((Activity) context).getWindow().getDecorView();
        rootView.setBackgroundColor(color);
        changeTextColor(rootView, color);
        saveBackgroundColor(context, color);
    }

    private static void changeTextColor(View rootView, int backgroundColor) {
        int textColor = (backgroundColor == Color.BLACK) ? Color.WHITE : Color.BLACK;
        if (rootView instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) rootView;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                if (child instanceof TextView) {
                    ((TextView) child).setTextColor(textColor);
                } else if (child instanceof ViewGroup) {
                    changeTextColor(child, backgroundColor); // recursive call
                }
            }
        }
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
