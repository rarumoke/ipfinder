package net.harupiza.ipfinder.commands;

import net.harupiza.ipfinder.Ipfinder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import java.text.MessageFormat;

public class FindIP implements CommandExecutor {
    private final Ipfinder plugin;

    public FindIP(Ipfinder plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@Nonnull CommandSender commandSender, @Nonnull Command command,
                             @Nonnull String s, @Nonnull String[] strings) {
        var players = plugin.getPlayerFromIp(strings[0]);
        commandSender.sendMessage(MessageFormat.format("Players found: {0}", players.size()));
        for (var ipplayer : players) {
            commandSender.sendMessage("- " + ipplayer.player.getName() + " (" + ipplayer.ip + ")");
            commandSender.sendMessage("  - UUID: " + ipplayer.player.getUniqueId());
        }
        return true;
    }
}
