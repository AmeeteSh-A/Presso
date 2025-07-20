package com.example.attendanceappdraft0.utils;

import android.view.MotionEvent;
import android.view.View;

public class UIUtils {

    public static void applyShrinkEffect(View view) {
        view.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100).start();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    v.animate().scaleX(1f).scaleY(1f).setDuration(100).start();
                    break;
            }
            return false;
        });
    }

    public static void applyRotateEffect(View view) {
        view.setOnClickListener(v -> {
            v.animate()
                    .rotationBy(360f)
                    .setDuration(500)
                    .start();
        });
    }
}
