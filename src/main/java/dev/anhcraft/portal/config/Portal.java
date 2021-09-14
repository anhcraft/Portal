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
    @Setting
    @Virtual
    private String id;

    @Setting
    @Validation(notNull = true)
    public String name;

    @Setting
    @Validation(notNull = true)
    public Location location;

    @Setting
    @Nullable
    public Material icon;

    @Setting
    @Nullable
    public String permission;

    private BoundingBox boundingBox;
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
