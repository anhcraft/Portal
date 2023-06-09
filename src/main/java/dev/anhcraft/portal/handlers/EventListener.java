package dev.anhcraft.portal.handlers;

import dev.anhcraft.portal.PortalPlugin;
import dev.anhcraft.portal.config.Portal;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class EventListener implements Listener {
    private final PortalPlugin plugin;
    private final TrafficManager trafficManager;

    public EventListener(PortalPlugin plugin) {
        this.plugin = plugin;
        this.trafficManager = new TrafficManager(plugin);
    }

    @EventHandler
    public void move(PlayerMoveEvent event) {
        if (plugin.portalManager == null) return;
        if(trafficManager.isTravelling(event.getPlayer())) {
            event.setCancelled(true);
            return;
        }
        if(trafficManager.isProcessing(event.getPlayer())) return;
        Location d = event.getTo();
        for(String portal : plugin.portalManager.getActivePortals()){
            Portal p = plugin.portalManager.getPortal(portal);
            if(p != null && !p.disabled && p.getBoundingBox().contains(d.getX(), d.getY(), d.getZ()) && p.location.getWorld().equals(d.getWorld())) {
                Set<String> list = plugin.portalManager.getDestinations(portal);
                if(list.isEmpty()) continue;
                list.stream()
                        .skip(ThreadLocalRandom.current().nextInt(list.size()))
                        .map(plugin.portalManager::getPortal)
                        .filter(Objects::nonNull)
                        .filter(it -> it.permission == null || event.getPlayer().hasPermission(it.permission))
                        .findAny()
                        .ifPresent(dest -> trafficManager.teleport(event.getPlayer(), dest, p));
                break;
            }
        }
    }

    @EventHandler
    public void quit(PlayerQuitEvent event) {
        trafficManager.unmarkProcessing(event.getPlayer());
        trafficManager.unmarkTravelling(event.getPlayer());
    }

    @EventHandler
    public void damage(EntityDamageEvent event) {
        if(event.getEntity() instanceof ArmorStand) {
            String id = plugin.portalManager.identifySign((ArmorStand) event.getEntity());
            if (id != null && plugin.portalManager.getPortal(id) != null) {
                event.setCancelled(true);
                event.setDamage(0);
            }
        } else if(event.getEntity() instanceof Player &&
                event.getCause() == EntityDamageEvent.DamageCause.FALL &&
                trafficManager.isProcessing((Player) event.getEntity())) {
            event.setCancelled(true);
            event.setDamage(0);
        }
    }
}
