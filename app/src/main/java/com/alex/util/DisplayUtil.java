package com.alex.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

/**
 * Created by alex on 15-11-18.
 * Calculate scale parameters depend on different devices
 */
public class DisplayUtil {

    public static void init(Context context) {

        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);

        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;

        if (screenWidth > screenHeight) {
            scale = screenHeight / (BASE_HEIGHT * 1.0f);
        } else {
            scale = screenWidth / (BASE_WIDTH * 1.0f);
        }
        Log.d("Debug-" + TAG, screenWidth + ", " + screenHeight + ", " + scale);
    }

    public static void scaleView(View view, int resId) {
        try {
            FrameLayout layout = (FrameLayout) view.findViewById(resId);
            layout.setScaleX(scale);
            layout.setScaleY(scale);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    public static void scaleView(View view, int resId, float pivotX, float pivotY) {
        try {
            FrameLayout layout = (FrameLayout) view.findViewById(resId);
            layout.setPivotX(pivotX);
            layout.setPivotY(pivotY);
            layout.setScaleX(scale);
            layout.setScaleY(scale);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    public static float scale;

    private static int screenWidth;
    private static int screenHeight;

    private static final int BASE_WIDTH = 1024;
    private static final int BASE_HEIGHT = 768;
    private static final String TAG = DisplayUtil.class.getName();

}
