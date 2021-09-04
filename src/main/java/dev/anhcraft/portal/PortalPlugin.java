package dev.anhcraft.portal;

import co.aikar.commands.PaperCommandManager;
import dev.anhcraft.config.ConfigDeserializer;
import dev.anhcraft.config.ConfigSerializer;
import dev.anhcraft.config.bukkit.BukkitConfigProvider;
import dev.anhcraft.config.bukkit.adapters.LocationAdapter;
import dev.anhcraft.config.bukkit.struct.YamlConfigSection;
import dev.anhcraft.config.schema.SchemaScanner;
import dev.anhcraft.portal.config.PluginConfig;
import dev.anhcraft.portal.config.Portal;
import dev.anhcraft.portal.config.Tunnel;
import dev.anhcraft.portal.config.TunnelAdapter;
import dev.anhcraft.portal.handlers.EffectPerformer;
import dev.anhcraft.portal.handlers.EventListener;
import dev.anhcraft.portal.handlers.PortalManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.Objects;

public final class PortalPlugin extends JavaPlugin {
    public PortalManager portalManager;
    public PluginConfig config;
    private ConfigSerializer serializer;
    private ConfigDeserializer deserializer;

    @Override
    public void onEnable() {
        LocationAdapter locationAdapter = new LocationAdapter();
        locationAdapter.inlineSerialization(true);
        serializer = BukkitConfigProvider.YAML.createSerializer();
        serializer.registerTypeAdapter(Location.class, locationAdapter);
        serializer.registerTypeAdapter(Tunnel.class, new TunnelAdapter());
        deserializer = BukkitConfigProvider.YAML.createDeserializer();
        deserializer.registerTypeAdapter(Location.class, locationAdapter);
        deserializer.registerTypeAdapter(Tunnel.class, new TunnelAdapter());
        try {
            reload();
        } catch (Exception e) {
            getLogger().severe("Failed to load configuration");
        }

        getServer().getPluginManager().registerEvents(new EventListener(this), this);

        new EffectPerformer(this).runTaskTimerAsynchronously(this, 0, 10);

        PaperCommandManager pcm = new PaperCommandManager(this);
        pcm.enableUnstableAPI("help");
        pcm.registerCommand(new PortalCommand(this));
    }

    void reload() throws Exception {
        saveDefaultConfig();
        reloadConfig();
        config = deserializer.transformConfig(
                Objects.requireNonNull(SchemaScanner.scanConfig(PluginConfig.class)),
                new YamlConfigSection(getConfig())
        );

        if(portalManager != null) {
            for(String s : portalManager.getSigns()){
                portalManager.removeSign(s);
            }
        }

        portalManager = new PortalManager(this);

        Bukkit.getServer().getWorlds().stream()
                .flatMap(w -> w.getEntitiesByClass(ArmorStand.class).stream())
                .forEach(e -> {
                    if(portalManager.identifySign(e) != null) {
                        e.remove();
                    }
                });

        for(Map.Entry<String, Portal> e : config.portals.entrySet()){
            portalManager.registerPortal(e.getKey(), e.getValue());
            if(config.settings.signEnabled) {
                portalManager.setSign(e.getKey(), e.getValue());
            }
        }
        for(Tunnel e : config.tunnels){
            portalManager.addTunnel(e);
        }
    }

    public void saveChanges() {
        try {
            serializer.transformConfig(
                    Objects.requireNonNull(SchemaScanner.scanConfig(PluginConfig.class)),
                    new YamlConfigSection(getConfig()),
                    config
            );
            saveConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
