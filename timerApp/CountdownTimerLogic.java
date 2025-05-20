package myPackage;

import java.util.Timer;
import java.util.TimerTask;

public class CountdownTimerLogic {
    private Timer timer;
    private TimerTask task;
    private int totalSeconds;
    private boolean isRunning = false;
    private boolean isPaused = false;
    private CountdownCallback callback;

    public CountdownTimerLogic(CountdownCallback callback) {
        this.callback = callback;
    }

    public void start(int seconds) {
        if (isRunning) return;
        
        if(!isPaused){
            totalSeconds = seconds;
        }

        isRunning = true;
        isPaused = false;

        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                if (totalSeconds > 0) {
                    callback.onTick(totalSeconds);
                    totalSeconds--;
                } else {
                    stop();
                    callback.onFinish();
                }
            }
        };
        timer.scheduleAtFixedRate(task, 0, 1000);
    }

    public int getTotalSeconds(){
        return totalSeconds;
    }

    public void pause() {
        if(!isRunning) return;
        stop();
        isPaused = true;
        isRunning = false;
    }

    public void reset() {
        stop();
        totalSeconds = 0;
        isPaused = false;
        isRunning = false;
    }

    private void stop() {
        if (timer != null) timer.cancel();
        if (task != null) task.cancel();
    }

    public boolean isRunning(){
        return isRunning;
    }

    public boolean isPaused(){
        return isPaused;
    }

    public interface CountdownCallback {
        void onTick(int remainingSeconds);
        void onFinish();
    }
}
