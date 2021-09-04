package dev.anhcraft.portal.events;

import dev.anhcraft.portal.config.Portal;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class PortalPostTeleportEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    private final Portal portal;

    public PortalPostTeleportEvent(@NotNull Player who, @NotNull Portal portal) {
        super(who);
        this.portal = portal;
    }

    @NotNull
    public Portal getPortal() {
        return portal;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
