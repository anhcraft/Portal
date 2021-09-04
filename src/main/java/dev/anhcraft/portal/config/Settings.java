package dev.anhcraft.portal.config;

import dev.anhcraft.config.annotations.Configurable;
import dev.anhcraft.config.annotations.Path;
import dev.anhcraft.config.annotations.Setting;
import dev.anhcraft.config.annotations.Validation;
import org.bukkit.Particle;

@Configurable
public class Settings {
    @Setting
    @Path("enable-sign")
    public boolean signEnabled = false;

    @Setting
    @Path("enable-effects")
    public boolean particleEffectEnabled = true;

    @Setting
    @Path("show-icons")
    public boolean showIcons = true;

    @Setting
    @Path("effect-rotation-delta")
    public int effectRotationDelta = 15;

    @Setting
    @Path("effect-fragmented-fans")
    public int effectFragmentedFans = 8;

    @Setting
    @Path("effect-density")
    public int effectDensity = 5;

    @Setting
    @Path("effect-inner-particle")
    @Validation(notNull = true, silent = true)
    public Particle innerParticleEffect = Particle.DRAGON_BREATH;

    @Setting
    @Path("effect-outer-particle")
    @Validation(notNull = true, silent = true)
    public Particle outerParticleEffect = Particle.CLOUD;
}
