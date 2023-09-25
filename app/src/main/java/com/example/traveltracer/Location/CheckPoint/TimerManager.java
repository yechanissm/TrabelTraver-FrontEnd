package com.example.traveltracer.Location.CheckPoint;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

public class TimerManager {

    private Handler handler;
    private Runnable LocationTimerRunnable;
    private Runnable UpdateTimerRunnable;
    private static final long TIMER_DURATION = 1 /* 분 */ * 60 * 1000; // 20분을 밀리초로 변환
    private static final long update_TIMER = 30 * 1000;
    private boolean timerResult = false;

    public TimerManager() {
        handler = new Handler(Looper.getMainLooper());
    }
    public void startTimer(Runnable callback) {
        handler.postDelayed(LocationTimerRunnable = () -> {
            if (callback != null) {
                callback.run(); // 콜백 실행
            }
        }, TIMER_DURATION);
    }
   /* public void startTimer(Runnable callback) {
        handler.postDelayed(() -> {
            if (LocationTimerRunnable != null) {
                handler.removeCallbacks(LocationTimerRunnable); // 타이머 정지
                LocationTimerRunnable = null; // 타이머 객체 초기화
                if (callback != null) {
                    callback.run(); // 콜백 실행
                }
            }
        }, TIMER_DURATION);
    }

    */

    // 타이머 초기화
    public void locationResetTimer() {
        Log.d("타이머 초기화 ", "초기화 되니?");
        if (LocationTimerRunnable != null) {
            handler.removeCallbacks(LocationTimerRunnable); // 타이머 정지
            LocationTimerRunnable = null; // 타이머 객체 초기화
        }
    }
/*
    public void updateTimer() {
        handler.postDelayed(UpdateTimerRunnable, update_TIMER);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (UpdateTimerRunnable != null) {
                    handler.removeCallbacks(UpdateTimerRunnable); // 타이머 정지
                    UpdateTimerRunnable = null; // 타이머 객체 초기화
                }
            }
        }, update_TIMER);
    }

    // 업데이트 타이머 초기화
    public void updateResetTimer() {
        if (UpdateTimerRunnable != null) {
            handler.removeCallbacks(UpdateTimerRunnable); // 타이머 정지
            UpdateTimerRunnable = null; // 타이머 객체 초기화
        }
    }

 */
}