package dev.anhcraft.portal.config;

import dev.anhcraft.config.ConfigDeserializer;
import dev.anhcraft.config.ConfigSerializer;
import dev.anhcraft.config.adapters.TypeAdapter;
import dev.anhcraft.config.struct.SimpleForm;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.Objects;

public class TunnelAdapter implements TypeAdapter<Tunnel> {
    @Override
    public @Nullable SimpleForm simplify(@NotNull ConfigSerializer configSerializer, @NotNull Type type, @NotNull Tunnel tunnel) throws Exception {
        return SimpleForm.of(String.format("%s > %s", tunnel.getSource(), tunnel.getDestination()));
    }

    @Override
    public @Nullable Tunnel complexify(@NotNull ConfigDeserializer configDeserializer, @NotNull Type type, @NotNull SimpleForm simpleForm) throws Exception {
        String[] s = Objects.requireNonNull(simpleForm.asString()).split(">");
        return new Tunnel(s[0].trim(), s[1].trim());
    }
}
