package codewithcal.au.calendarappexample;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class SwipeGestureListener extends GestureDetector.SimpleOnGestureListener {
    private static final String TAG = "SwipeGestureListener";
    private static final int SWIPE_THRESHOLD = 100;
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;
    private final OnSwipeListener listener;

    public SwipeGestureListener(OnSwipeListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1 == null || e2 == null) {
            Log.d(TAG, "e1 or e2 is null");
            return false;
        }

        float diffX = e2.getX() - e1.getX();
        Log.d(TAG, "diffX: " + diffX + ", velocityX: " + velocityX);

        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
            if (diffX > 0) {
                Log.d(TAG, "Swipe Right Detected");
                listener.onSwipeRight();
            } else {
                Log.d(TAG, "Swipe Left Detected");
                listener.onSwipeLeft();
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    public interface OnSwipeListener {
        void onSwipeLeft();
        void onSwipeRight();
    }
}
