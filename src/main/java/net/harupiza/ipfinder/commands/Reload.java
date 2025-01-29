package net.harupiza.ipfinder.commands;

import net.harupiza.ipfinder.Ipfinder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;

public class Reload implements CommandExecutor {
    private final Ipfinder plugin;

    public Reload(Ipfinder plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@Nonnull CommandSender commandSender, @Nonnull Command command,
                             @Nonnull String s, @Nonnull String[] strings) {
        plugin.reloadThisPlugin();
        commandSender.sendMessage("ipfinder: Reloaded.");
        return true;
    }
}
