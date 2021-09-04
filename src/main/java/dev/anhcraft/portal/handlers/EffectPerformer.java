package dev.anhcraft.portal.handlers;

import dev.anhcraft.portal.PortalPlugin;
import dev.anhcraft.portal.config.Portal;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class EffectPerformer extends BukkitRunnable {
    private static final double[] RADIUS = new double[]{0.75, 1, 1.25, 1.5};
    private final PortalPlugin plugin;

    public EffectPerformer(PortalPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for(Portal p : plugin.config.portals.values()){
            World w = p.location.getWorld();
            Vector o = p.location.toVector();
            Vector v = new Vector();
            double rt = Math.toRadians(p.getEffectRotation().getAndAdd(15));
            for (int i = 0; i < RADIUS.length; i++) {
                double r = RADIUS[i];
                for (double rad = 0; rad <= 2 * Math.PI; rad += Math.PI / 8) {
                    v.setX(Math.cos(rad));
                    v.setY(0);
                    v.setZ(Math.sin(rad));
                    v.rotateAroundX(rt);
                    v.rotateAroundZ(rt);
                    v.multiply(r);
                    v.add(o);
                    w.spawnParticle(
                            i == RADIUS.length - 1 ? Particle.CLOUD : Particle.DRAGON_BREATH,
                            v.getX(), v.getY(), v.getZ(), 5, 0, 0, 0, 0, null
                    );
                }
            }
        }
    }
}
