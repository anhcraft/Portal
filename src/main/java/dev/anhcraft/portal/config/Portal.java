package dev.anhcraft.portal.config;

import dev.anhcraft.config.annotations.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicInteger;

@Configurable
public class Portal {
    @Virtual
    private String id;

    @Validation(notNull = true)
    public String name;

    @Validation(notNull = true)
    public Location location;

    @Nullable
    public Material icon;

    @Nullable
    public String permission;

    public boolean disabled;

    @Exclude
    private BoundingBox boundingBox;

    @Exclude
    private AtomicInteger effectRotation;

    @NotNull
    public String getId() {
        return id;
    }

    @PostHandler
    public void resetBoundingBox() {
        boundingBox = BoundingBox.of(location, 1.5, 1.5, 1.5);
    }

    @NotNull
    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    @PostHandler
    public void resetEffectRotation() {
        effectRotation = new AtomicInteger();
    }

    @NotNull
    public AtomicInteger getEffectRotation() {
        return effectRotation;
    }
}
