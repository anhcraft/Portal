package dev.anhcraft.portal.config;

import dev.anhcraft.config.annotations.Configurable;
import dev.anhcraft.config.annotations.Path;
import dev.anhcraft.config.annotations.Validation;
import org.bukkit.Particle;

@Configurable
public class Settings {
    @Path("enable-sign")
    public boolean signEnabled = false;

    @Path("enable-effects")
    public boolean particleEffectEnabled = true;

    @Path("show-icons")
    public boolean showIcons = true;

    @Path("effect-rotation-delta")
    public int effectRotationDelta = 15;

    @Path("effect-fragmented-fans")
    public int effectFragmentedFans = 8;

    @Path("effect-density")
    public int effectDensity = 5;

    @Path("effect-inner-particle")
    @Validation(notNull = true, silent = true)
    public Particle innerParticleEffect = Particle.DRAGON_BREATH;

    @Path("effect-outer-particle")
    @Validation(notNull = true, silent = true)
    public Particle outerParticleEffect = Particle.CLOUD;

    @Path("throwing-velocity-alpha")
    public double throwingVelocityAlpha = 60;
}
