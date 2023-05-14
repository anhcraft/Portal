package dev.anhcraft.portal.config;

import dev.anhcraft.config.annotations.Configurable;
import dev.anhcraft.config.annotations.Path;
import dev.anhcraft.config.annotations.Validation;
import org.bukkit.Particle;

@Configurable(keyNamingStyle = Configurable.NamingStyle.TRAIN_CASE)
public class Settings {
    @Path("enable-sign")
    public boolean signEnabled = false;

    @Path("enable-effects")
    public boolean particleEffectEnabled = true;

    public boolean showIcons = true;

    public int effectInterval = 5;

    public int effectRotationDelta = 15;

    public int effectFragmentedFans = 8;

    public int effectDensity = 3;

    @Path("effect-inner-particle")
    @Validation(notNull = true, silent = true)
    public Particle innerParticleEffect = Particle.DRAGON_BREATH;

    @Path("effect-outer-particle")
    @Validation(notNull = true, silent = true)
    public Particle outerParticleEffect = Particle.CLOUD;

    public double throwingVelocityAlpha = 60;
}
