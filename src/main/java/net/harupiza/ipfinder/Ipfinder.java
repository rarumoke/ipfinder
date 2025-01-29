package net.harupiza.ipfinder;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;

public final class Ipfinder extends JavaPlugin implements Listener {
    public Boolean doesEnabledThis = false;
    public ArrayList<Ipplayer> ipplayer = new ArrayList<>();
    public Datamanager datamanager = new Datamanager(this);
    public final boolean isDebug = true;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadThisPlugin();
        getServer().getPluginManager().registerEvents(this, this);

        Objects.requireNonNull(getServer().getPluginCommand("ipfinder_reload"))
                .setExecutor(new net.harupiza.ipfinder.commands.Reload(this));
        Objects.requireNonNull(getServer().getPluginCommand("ipfinder_findip"))
                .setExecutor(new net.harupiza.ipfinder.commands.FindIP(this));
        Objects.requireNonNull(getServer().getPluginCommand("ipfinder_findplayer"))
                .setExecutor(new net.harupiza.ipfinder.commands.FindPlayer(this));
    }

    @Override
    public void onDisable() {
        try {
            datamanager.push(ipplayer);
        } catch (IOException e) {
            getLogger().warning("Failed to push data.");
        }
    }

    public void reloadThisPlugin() {
        reloadConfig();
        doesEnabledThis = true;
    }

    public ArrayList<Ipplayer> getIpFromPlayer(Player player) {
        try {
            ipplayer = datamanager.pull();
        } catch (IOException e) {
            getLogger().warning("Failed to pull data.");
            return new ArrayList<>();
        }

        return new ArrayList<>(ipplayer.stream().filter(
                ip2 -> ip2.player.getUniqueId().equals(player.getUniqueId())).toList());
    }

    public ArrayList<Ipplayer> getPlayerFromIp(String ip) {
        try {
            ipplayer = datamanager.pull();
        } catch (IOException e) {
            getLogger().warning("Failed to pull data.");
            return new ArrayList<>();
        }

        return new ArrayList<>(ipplayer.stream().filter(
                ipplayer -> ipplayer.ip.equals(ip)).toList());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!doesEnabledThis) return;
        var player = event.getPlayer();
        var address = player.getAddress();
        var ip2 = new Ipplayer(Objects.requireNonNull(address).getHostName(), player, this);

        if (isDebug) getLogger().log(Level.INFO, "IP: " + ip2.ip);

        try {
            ipplayer = datamanager.pull();
        } catch (IOException e) {
            getLogger().warning("Failed to pull data.");
            return;
        }

        if (isDebug) getLogger().log(Level.INFO, ip2.player.getName() + " (" + ip2.player.getUniqueId() + ")");

        for (var ip2s : ipplayer) {
            if (isDebug) getLogger().log(Level.INFO, ip2s.player.getName() + " (" + ip2s.player.getUniqueId() + ")");
            if (ip2.player.getUniqueId().equals(ip2s.player.getUniqueId()) &&
                    ip2.ip.equals(ip2s.ip)) return;
        }

        if (isDebug) getLogger().log(Level.INFO, "No duplicates found.");
        ipplayer.add(ip2);

        try {
            datamanager.push(ipplayer);
        } catch (IOException e) {
            getLogger().warning("Failed to push data.");
        }
    }
}
