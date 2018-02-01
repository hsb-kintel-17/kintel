package de.kintel.ki.event;

/**
 * Created by kintel on 25.01.2018.
 */
public class MeasuredTimeEvent {
    private final long timeConsumedInSeconds;

    public MeasuredTimeEvent(long timeConsumedInSeconds) {
        this.timeConsumedInSeconds = timeConsumedInSeconds;
    }

    public long getTimeConsumedInSeconds() {
        return timeConsumedInSeconds;
    }
}
