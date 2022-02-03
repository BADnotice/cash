package net.hyzenmc.cash.util;

public final class TimeCounter {

    private long start;

    private TimeCounter(long start) {
        this.start = start;
    }

    public static TimeCounter fromNow() {
        return new TimeCounter(System.currentTimeMillis());
    }

    public void reset() {
        this.start = System.currentTimeMillis();
    }

    public double elapsedTime() {
        long now = System.currentTimeMillis();
        return (now - start) / 1000.0;
    }
}