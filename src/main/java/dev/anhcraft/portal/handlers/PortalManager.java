package dev.anhcraft.portal.handlers;

import com.google.common.collect.ImmutableSet;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import dev.anhcraft.portal.PortalPlugin;
import dev.anhcraft.portal.config.Portal;
import dev.anhcraft.portal.config.Tunnel;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@SuppressWarnings("UnstableApiUsage")
public class PortalManager {
    private final NamespacedKey key;
    private final MutableGraph<String> graph;
    private final Map<String, ArmorStand> signs;
    private final Map<String, Portal> portals;
    private final PortalPlugin plugin;

    public PortalManager(@NotNull PortalPlugin plugin) {
        this.plugin = plugin;
        this.key = new NamespacedKey(plugin, "HOLOGRAM_SIGN");
        this.graph = GraphBuilder.directed().allowsSelfLoops(false).build();
        this.signs = new HashMap<>();
        this.portals = new HashMap<>();
    }

    public boolean registerPortal(@NotNull String id, @NotNull Portal portal){
        return portals.putIfAbsent(id, portal) == null;
    }

    @Nullable
    public Portal unregisterPortal(@NotNull String id){
        return portals.remove(id);
    }

    @Nullable
    public Portal getPortal(String id) {
        return portals.get(id);
    }

    @NotNull
    public Set<String> getRegisteredPortals() {
        return ImmutableSet.copyOf(portals.keySet());
    }

    public boolean addTunnel(@NotNull Tunnel tunnel) {
        return graph.putEdge(tunnel.getSource(), tunnel.getDestination());
    }

    public boolean removeTunnel(@NotNull Tunnel tunnel) {
        return graph.removeEdge(tunnel.getSource(), tunnel.getDestination());
    }

    @NotNull
    public Set<String> getDestinations(@NotNull String source){
        return ImmutableSet.copyOf(graph.successors(source));
    }

    @NotNull
    public Set<String> getActivePortals() {
        return ImmutableSet.copyOf(graph.nodes());
    }

    @NotNull
    public ArmorStand setSign(@NotNull String id, @NotNull Portal portal){
        ArmorStand as = portal.location.getWorld().spawn(portal.location, ArmorStand.class, armorStand -> {
            if(plugin.config.settings.showIcons && portal.icon != null && !portal.icon.isAir()) {
                Objects.requireNonNull(armorStand.getEquipment()).setHelmet(new ItemStack(portal.icon, 1));
            }
            armorStand.setVisible(false);
            armorStand.setInvulnerable(true);
            armorStand.setMarker(false);
            armorStand.setCustomName(ChatColor.translateAlternateColorCodes('&', portal.name));
            armorStand.setCustomNameVisible(true);
            armorStand.getPersistentDataContainer().set(key, PersistentDataType.STRING, id);
        });
        ArmorStand old = signs.put(id, as);
        if(old != null) {
            old.remove();
        }
        return as;
    }

    public boolean removeSign(String id){
        ArmorStand old = signs.remove(id);
        if(old != null && !old.isDead()) {
            old.remove();
        }
        return old != null;
    }

    @Nullable
    public String identifySign(@NotNull ArmorStand armorStand){
        if(armorStand.isDead()) return null;
        return armorStand.getPersistentDataContainer().get(key, PersistentDataType.STRING);
    }

    @Nullable
    public ArmorStand getSign(String id) {
        return signs.get(id);
    }

    @NotNull
    public Collection<String> getSigns(){
        return ImmutableSet.copyOf(signs.keySet());
    }
}
