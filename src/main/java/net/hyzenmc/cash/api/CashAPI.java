package net.hyzenmc.cash.api;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.hyzenmc.cash.user.CashUser;
import net.hyzenmc.cash.user.CashUserCache;
import org.bukkit.entity.Player;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CashAPI {

    @Getter
    private static final CashAPI instance = new CashAPI();

    public CashUser getUser(Player player) {
        return getUser(player.getName());
    }

    public CashUser getUser(String name) {
        return CashUserCache.getInstance().getUser(name);
    }

}
