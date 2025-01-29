package net.harupiza.ipfinder.commands;

import net.harupiza.ipfinder.Ipfinder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import java.text.MessageFormat;

public class FindPlayer implements CommandExecutor {
    private final Ipfinder plugin;

    public FindPlayer(Ipfinder plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(@Nonnull CommandSender commandSender, @Nonnull Command command,
                             @Nonnull String s, @Nonnull String[] strings) {
        var player = plugin.getServer().getPlayer(strings[0]);
        if (player == null) commandSender.sendMessage("Player not found.");
        if (player == null) {
            return false;
        }
        var ips = plugin.getIpFromPlayer(player);
        commandSender.sendMessage(MessageFormat.format("Players found: {0}", ips.size()));
        if (!ips.isEmpty()) {
            commandSender.sendMessage("! " + ips.getFirst().ip);
        }
        for (var ipplayer : ips) {
            commandSender.sendMessage("- " + ipplayer.player.getName() + " (" + ipplayer.player.getUniqueId() + ")");
        }
        return true;
    }
}
