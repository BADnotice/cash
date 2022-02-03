package net.hyzenmc.cash.commands;

import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.annotation.Optional;
import me.saiintbrisson.minecraft.command.command.Context;
import net.hyzenmc.cash.user.CashUser;
import net.hyzenmc.cash.user.CashUserCache;
import net.hyzenmc.cash.user.CashUserQueue;
import net.hyzenmc.cash.util.text.TextHelper;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class CashCommand {

    @Command(
            name = "cash"
    )
    public void cashCommand(Context<CommandSender> context, @Optional Player target) {
        if (!(context.getSender() instanceof Player)) {
            return;
        }

        CashUserCache cashUserCache = CashUserCache.getInstance();

        if (target == null) {
            double balance = cashUserCache.getUser(context.getSender().getName()).getBalance();
            context.sendMessage("§aVocê possui: " + balance + " (" + TextHelper.format(balance) + ") de cash.");
            return;
        }

        double balance = cashUserCache.getUser(target.getName()).getBalance();
        context.sendMessage("§aO jogador '" + target.getName() + "' possui " + balance + " (" + TextHelper.format(balance) + ") de cash.");
    }

    @Command(
            name = "cash.help",
            permission = "cash.admin"
    )
    public void cashHelpCommand(Context<CommandSender> context) {
        context.sendMessage(new String[]{
                "",
                " §aCash comandos!",
                "",
                " §f/cash set <player> <amount> §7- defina uma quantia para alguém.",
                " §f/cash give <player> <amount> §7- adicione uma quantia para alguém.",
                " §f/cash remove <player> <amount> §7- remova uma quantia para alguém.",
                ""
        });
    }

    @Command(
            name = "cash.give",
            permission = "cash.admin",
            usage = "cash give <player> <amount>"
    )
    public void cashGiveCommand(Context<CommandSender> context, Player target, double amount) {
        applyOperation(context.getSender(), CashOperationType.ADD, target, amount);
    }

    @Command(
            name = "cash.set",
            permission = "cash.admin",
            usage = "cash set <player> <amount>"
    )
    public void cashSetCommand(Context<CommandSender> context, Player target, double amount) {
        applyOperation(context.getSender(), CashOperationType.SET, target, amount);
    }

    @Command(
            name = "cash.remove",
            permission = "cash.admin",
            usage = "cash remove <player> <amount>"
    )
    public void cashRemoveCommand(Context<CommandSender> context, Player target, double amount) {
        applyOperation(context.getSender(), CashOperationType.REMOVE, target, amount);
    }

    private boolean applyOperation(CommandSender sender, CashOperationType type, Player target, double amount) {

        CashUser cashUser = CashUserCache.getInstance().getUser(target.getName());
        if (cashUser == null) {
            sender.sendMessage("§cO jogador citado não possui uma conta em nosso banco de dados.");
            return false;
        }

        double balance = cashUser.getBalance();

        switch (type) {
            case ADD:
                balance += amount;
                break;

            case SET:
                balance = amount;
                break;

            case REMOVE:
                balance -= amount;
                if (balance < 0) {
                    balance = 0;
                }
                break;
        }

        cashUser.setBalance(balance);
        CashUserQueue.getInstance().addToQueue(target.getName());

        if (sender instanceof Player) {
            sender.sendMessage("§aVocê definiu o saldo do jogador " + target.getName() + " para " + TextHelper.format(balance));
        }

        return true;
    }

    enum CashOperationType {
        SET,
        ADD,
        REMOVE
    }

}
