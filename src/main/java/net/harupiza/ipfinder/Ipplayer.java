package net.harupiza.ipfinder;

import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class Ipplayer {
    @Nonnull
    public String ip;
    @Nonnull
    public Player player;
    @Nonnull
    private final Ipfinder plugin;

    public Ipplayer(@Nonnull String ip, @Nonnull Player player, @Nonnull Ipfinder plugin) {
        this.ip = ip;
        this.player = player;
        this.plugin = plugin;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Ipplayer) {
            return ((Ipplayer) obj).ip.equals(this.ip) && ((Ipplayer) obj).player.getUniqueId().equals(this.player.getUniqueId());
        }
        return false;
    }
}
