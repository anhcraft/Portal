package dev.anhcraft.portal.config;

import dev.anhcraft.config.annotations.Configurable;
import dev.anhcraft.config.annotations.Validation;

import java.util.List;
import java.util.Map;

@Configurable
public class PluginConfig {
    @Validation(notNull = true)
    public Settings settings;

    @Validation(notNull = true)
    public Map<String, Portal> portals;

    @Validation(notNull = true)
    public List<Tunnel> tunnels;
}
