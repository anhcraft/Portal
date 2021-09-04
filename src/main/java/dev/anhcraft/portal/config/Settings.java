package dev.anhcraft.portal.config;

import dev.anhcraft.config.annotations.Configurable;
import dev.anhcraft.config.annotations.Path;
import dev.anhcraft.config.annotations.Setting;

@Configurable
public class Settings {
    @Setting
    @Path("enable-sign")
    public boolean signEnabled;
}
