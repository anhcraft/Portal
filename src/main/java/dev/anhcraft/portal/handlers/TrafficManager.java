package dev.anhcraft.portal.handlers;

import dev.anhcraft.portal.PortalPlugin;
import dev.anhcraft.portal.config.Portal;
import dev.anhcraft.portal.events.PortalPostTeleportEvent;
import dev.anhcraft.portal.events.PortalPreTeleportEvent;
import io.papermc.lib.PaperLib;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TrafficManager {
    private final PortalPlugin plugin;
    private final Map<UUID, Byte> processing;
    private final Map<UUID, Byte> travelling;

    public TrafficManager(PortalPlugin plugin) {
        this.plugin = plugin;
        this.processing = new ConcurrentHashMap<>();
        this.travelling = new ConcurrentHashMap<>();
    }

    public boolean isProcessing(@NotNull Player player) {
        return processing.containsKey(player.getUniqueId());
    }

    public boolean isTravelling(@NotNull Player player) {
        return travelling.containsKey(player.getUniqueId());
    }

    public void unmarkProcessing(@NotNull Player player) {
        processing.remove(player.getUniqueId());
    }

    public void unmarkTravelling(@NotNull Player player) {
        travelling.remove(player.getUniqueId());
    }

    public void teleport(@NotNull Player player, @NotNull Portal destination, @Nullable Portal source){
        PortalPreTeleportEvent event = new PortalPreTeleportEvent(player, destination, source);
        Bukkit.getPluginManager().callEvent(event);
        if(event.isCancelled()) return;
        processing.put(player.getUniqueId(), (byte) 0);
        travelling.put(player.getUniqueId(), (byte) 0);
        player.playSound(player.getLocation(), Sound.ITEM_CHORUS_FRUIT_TELEPORT, 1f, 1f);
        PaperLib.teleportAsync(player, destination.location).whenComplete((done, throwable) -> {
            if (done) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        unmarkTravelling(player);
                        Bukkit.getPluginManager().callEvent(new PortalPostTeleportEvent(player, destination, source));
                        if (plugin.config.settings.throwAfterTeleportEnabled) {
                            Vector dir = destination.location.getDirection().normalize();
                            dir.multiply(plugin.config.settings.throwAfterTeleportPower);
                            dir.setY(Math.toRadians(plugin.config.settings.throwAfterTeleportAngle));
                            player.setVelocity(dir);
                        }
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                unmarkProcessing(player);
                            }
                        }.runTaskLater(plugin, plugin.config.settings.teleportCooldown);
                    }
                }.runTask(plugin);
            }
        });
    }
}
