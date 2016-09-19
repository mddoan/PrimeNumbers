package com.dangdoan.primenumbers;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by dangdoan on 9/18/16.
 */
public class StaticMethods {
    public static final float default_ = -1.0f;
    public static float mScreenWidth = default_;
    public static float mScreenHeight = default_;
    public static float getScreenHeight(Context context) {
        if (mScreenHeight != default_) {
            return mScreenHeight;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return screenResolution(context).y;
        }
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        mScreenHeight = (metrics.heightPixels);
        return mScreenHeight;
    }

    public static float getScreenWidth(Context context) {
        if (mScreenWidth != default_) {
            return mScreenWidth;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return screenResolution(context).x;
        }


        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        mScreenWidth = metrics.widthPixels;
        return mScreenWidth;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static Point screenResolution(Context context) {
        WindowManager windowManager =
                (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point screenResolution = new Point();

        if (Build.VERSION.SDK_INT < 14)
            throw new RuntimeException("Unsupported Android version.");
        display.getRealSize(screenResolution);

        return screenResolution;
    }


}
