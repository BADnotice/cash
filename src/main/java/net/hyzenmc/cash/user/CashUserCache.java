package net.hyzenmc.cash.user;

import lombok.Getter;
import net.hyzenmc.cash.CashPlugin;
import net.hyzenmc.cash.dao.CashUserDAO;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public final class CashUserCache {

    @Getter
    private static CashUserCache instance;

    private final Map<String, CashUser> userMap;

    public CashUserCache(CashPlugin plugin) {
        instance = this;
        this.userMap = new LinkedHashMap<>();

        Bukkit.getPluginManager().registerEvents(this.createInternalListener(), plugin);
    }

    public void registerUser(CashUser user) {
        this.userMap.put(user.getPlayerName(), user);
    }

    public void unregisterUser(CashUser user) {
        this.userMap.remove(user.getPlayerName());
    }

    public CashUser getUser(String name) {
        return this.userMap.get(name);
    }

    Listener createInternalListener() {
        return new Listener() {

            private final ExecutorService service = new ThreadPoolExecutor(
                    2, 4,
                    15, TimeUnit.SECONDS,
                    new LinkedBlockingQueue<>()
            );

            @EventHandler
            public void onPlayerJoin(PlayerJoinEvent event) {
                Player player = event.getPlayer();

                service.submit(() -> {
                    CashUser cashUser = CashUserDAO.getInstance().selectOne(player.getName());
                    if (cashUser == null) {
                        cashUser = CashUser.newCashUser(player.getName());
                        CashUserQueue.getInstance().addToQueue(player.getName());
                    }

                    registerUser(cashUser);
                });
            }

            @EventHandler
            public void onPlayerQuit(PlayerQuitEvent event) {
                Player player = event.getPlayer();

                service.submit(() -> {
                    CashUser cashUser = getUser(player.getName());
                    if (cashUser != null) {
                        CashUserDAO.getInstance().insertOne(cashUser);
                        unregisterUser(cashUser);
                    }
                });
            }
        };
    }

}
