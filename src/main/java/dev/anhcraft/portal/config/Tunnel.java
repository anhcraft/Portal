package dev.anhcraft.portal.config;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tunnel tunnel = (Tunnel) o;
        return Objects.equals(source, tunnel.source) && Objects.equals(destination, tunnel.destination);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, destination);
    }
}
