package net.hyzenmc.cash.user;

import lombok.Getter;
import net.hyzenmc.cash.dao.CashUserDAO;

import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author https://github.com/kauepalota
 */
public class CashUserQueue {

    @Getter
    private static CashUserQueue instance;

    private final Queue<String> queue;

    public CashUserQueue() {
        instance = this;
        queue = new LinkedBlockingQueue<>();
    }

    public void addToQueue(String playerName) {
        if (!queue.contains(playerName)) {
            queue.add(playerName);
        }
    }

    public void flush() {
        String name = queue.poll();

        CashUser cashUser = CashUserCache.getInstance().getUser(name);
        if (cashUser != null) {
            CashUserDAO.getInstance().insertOne(cashUser);
        }
    }

    public void flushAll() {
        while (!queue.isEmpty()) {
            flush();
        }
    }

    public void startQueue() {
        Executors.newSingleThreadScheduledExecutor()
                .scheduleWithFixedDelay(this::flushAll, 1L, 1L, TimeUnit.MINUTES);
    }

}
