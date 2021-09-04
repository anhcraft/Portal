package dev.anhcraft.portal.config;

import org.jetbrains.annotations.NotNull;

public class Tunnel {
    private final String source;
    private final String destination;

    public Tunnel(@NotNull String source, @NotNull String destination) {
        this.source = source;
        this.destination = destination;
    }

    @NotNull
    public String getSource() {
        return source;
    }

    @NotNull
    public String getDestination() {
        return destination;
    }
}
