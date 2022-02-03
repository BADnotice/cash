package net.hyzenmc.cash.placeholders;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.hyzenmc.cash.user.CashUser;
import net.hyzenmc.cash.user.CashUserCache;
import net.hyzenmc.cash.util.text.TextHelper;
import org.bukkit.entity.Player;

public class CashUserExtension extends PlaceholderExpansion {

    @Override
    public String getIdentifier() {
        return "cash";
    }

    @Override
    public String getAuthor() {
        return "badnotice";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public String onPlaceholderRequest(Player player, String params) {
        CashUser cashUser = CashUserCache.getInstance().getUser(player.getName());
        if (cashUser == null) {
            return null;
        }

        if (params.equalsIgnoreCase("balance")) {
            return TextHelper.format(cashUser.getBalance());
        }

        return super.onPlaceholderRequest(player, params);
    }

}
