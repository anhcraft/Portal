package dev.anhcraft.portal.events;

import dev.anhcraft.portal.config.Portal;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class PortalPreTeleportEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Portal portal;
    private boolean cancelled;

    public PortalPreTeleportEvent(@NotNull Player who, @NotNull Portal portal) {
        super(who);
        this.portal = portal;
    }

    @NotNull
    public Portal getPortal() {
        return portal;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
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
