package liveWallpaper.myapplication;


public class UStatus {
    boolean finished = false;

    public boolean isFinished() {
        return this.finished;
    }

    public void finished() {
        this.finished = true;
    }
}
