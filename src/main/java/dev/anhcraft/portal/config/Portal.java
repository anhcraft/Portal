package dev.anhcraft.portal.config;

import dev.anhcraft.config.annotations.Configurable;
import dev.anhcraft.config.annotations.PostHandler;
import dev.anhcraft.config.annotations.Setting;
import dev.anhcraft.config.annotations.Validation;
import org.bukkit.Location;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicInteger;

@Configurable
public class Portal {
    @Setting
    @Validation(notNull = true)
    public String name;

    @Setting
    @Validation(notNull = true)
    public Location location;

    private BoundingBox boundingBox;
    private AtomicInteger effectRotation;

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
