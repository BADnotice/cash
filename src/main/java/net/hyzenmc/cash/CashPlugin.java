package net.hyzenmc.cash;

import com.henryfabio.sqlprovider.connector.SQLConnector;
import lombok.Getter;
import me.saiintbrisson.bukkit.command.BukkitFrame;
import me.saiintbrisson.minecraft.command.message.MessageHolder;
import me.saiintbrisson.minecraft.command.message.MessageType;
import net.hyzenmc.cash.commands.CashCommand;
import net.hyzenmc.cash.dao.CashUserDAO;
import net.hyzenmc.cash.dao.factory.SQLDatabaseFactory;
import net.hyzenmc.cash.placeholders.CashUserExtension;
import net.hyzenmc.cash.user.CashUserCache;
import net.hyzenmc.cash.user.CashUserQueue;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class CashPlugin extends JavaPlugin {

    private SQLConnector sqlConnector;

    public static CashPlugin getInstance() {
        return getPlugin(CashPlugin.class);
    }

    @Override
    public void onLoad() {
        saveDefaultConfig();
    }

    @Override
    public void onEnable() {
        sqlConnector = SQLDatabaseFactory.createConnector(getConfig().getConfigurationSection("database"));

        new CashUserDAO();
        new CashUserCache(this);

        CashUserQueue cashUserQueue = new CashUserQueue();
        cashUserQueue.startQueue();

        registerCommand();

        new CashUserExtension().register();
    }

    @Override
    public void onDisable() {
        CashUserQueue.getInstance().flushAll();
    }

    private void registerCommand() {
        BukkitFrame bukkitFrame = new BukkitFrame(this);

        bukkitFrame.registerCommands(
                new CashCommand()
        );

        MessageHolder messageHolder = bukkitFrame.getMessageHolder();
        messageHolder.setMessage(MessageType.NO_PERMISSION, "§cVocê não possui permissão para executar este comando.");
        messageHolder.setMessage(MessageType.ERROR, "§cUm erro ocorreu! {error}");
        messageHolder.setMessage(MessageType.INCORRECT_USAGE, "§cUtilize /{usage}");
        messageHolder.setMessage(MessageType.INCORRECT_TARGET, "§cVocê não pode utilizar este comando pois ele é direcioado apenas para {target}.");
    }

}
