package dev.anhcraft.portal.events;

import dev.anhcraft.portal.config.Portal;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PortalPreTeleportEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Portal destination;
    private final Portal source;
    private boolean cancelled;

    public PortalPreTeleportEvent(@NotNull Player who, @NotNull Portal destination, @Nullable Portal source) {
        super(who);
        this.destination = destination;
        this.source = source;
    }

    @NotNull
    public Portal getDestination() {
        return destination;
    }

    @Nullable
    public Portal getSource() {
        return source;
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
