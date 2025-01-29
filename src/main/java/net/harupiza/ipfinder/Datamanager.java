package net.harupiza.ipfinder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class Datamanager {
    private final Ipfinder plugin;
    private final Path path;

    public Datamanager(Ipfinder plugin) {
        this.plugin = plugin;
        path = Paths.get(plugin.getDataFolder().toString(), "data.json");

        File file = path.toFile();
        plugin.getLogger().warning(file.getParentFile().mkdirs() ? "Created data folder." : "Failed to create data folder.");

        try {
            if (!file.exists()) plugin.getLogger().warning(file.createNewFile() ? "Created data file." : "Failed to create data file.");
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to create data file.");
        }
    }

    public ArrayList<Ipplayer> pull() throws IOException {
        var mapper = new ObjectMapper();
        Dataformat[] result = mapper.readValue(Files.readString(path), new TypeReference<>(){});
        ArrayList<Ipplayer> returns = new ArrayList<>();

        for (Dataformat dataformat : result) {
            var player = plugin.getServer().getPlayer(UUID.fromString(dataformat.getUuid()));
            if (player == null) {
                if (plugin.isDebug) plugin.getLogger().log(Level.INFO, "Player not found: " + dataformat.getUuid());
                continue;
            }
            returns.add(new Ipplayer(dataformat.getIp(), player, plugin));
        }

        if (plugin.isDebug) plugin.getLogger().log(Level.INFO, plugin.ipplayer.toString());
        if (plugin.isDebug) plugin.getLogger().log(Level.INFO, Arrays.toString(result));

        return returns;
    }

    public void push(ArrayList<Ipplayer> ip2s) throws IOException {
        var mapper = new ObjectMapper();

        List<Dataformat> result = ip2s.stream().map(ip2 -> {
            var d = new Dataformat();
            d.ip = ip2.ip;
            d.uuid = ip2.player.getUniqueId().toString();
            return d;
        }).toList();

        mapper.writeValue(path.toFile(), result);
    }
}
