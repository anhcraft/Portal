package dev.anhcraft.portal;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import dev.anhcraft.portal.config.Portal;
import dev.anhcraft.portal.config.Tunnel;
import net.kyori.adventure.audience.MessageType;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("portal|thongdao|thongdit")
public class PortalCommand extends BaseCommand {
    private final PortalPlugin plugin;

    public PortalCommand(PortalPlugin plugin) {
        this.plugin = plugin;
    }

    @HelpCommand
    @CatchUnknown
    public void doHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }

    @Subcommand("reload")
    @CommandPermission("portal.reload")
    public void reload(CommandSender sender) {
        try {
            plugin.reload();
        } catch (Exception e) {
            e.printStackTrace();
            sender.sendMessage(Identity.nil(), Component.text("Failed to reload configuration.").color(NamedTextColor.RED), MessageType.SYSTEM);
            return;
        }
        sender.sendMessage(Identity.nil(), Component.text("Configuration reloaded.").color(NamedTextColor.GREEN), MessageType.SYSTEM);
    }

    @Subcommand("visit")
    @CommandPermission("portal.visit")
    public void visit(Player player, String portal) {
        Portal p = plugin.portalManager.getPortal(portal);
        if(p == null){
            player.sendMessage(Identity.nil(), Component.text("Portal not existed.").color(NamedTextColor.RED), MessageType.SYSTEM);
            return;
        }
        player.teleportAsync(p.location);
    }

    @Subcommand("config add-portal")
    @CommandPermission("portal.config.add-portal")
    public void addPortal(Player player, String portal, @Optional String name) {
        Portal p = new Portal();
        p.name = name == null ? portal : name;
        p.location = player.getLocation().add(0, 0.5, 0);
        if(plugin.portalManager.getPortal(portal) == null){
            p.resetBoundingBox();
            p.resetEffectRotation();
            plugin.config.portals.put(portal, p);
            plugin.saveChanges();
            player.sendMessage(Identity.nil(), Component.text("Portal added. Do /portal reload after done.").color(NamedTextColor.GREEN), MessageType.SYSTEM);
        } else {
            player.sendMessage(Identity.nil(), Component.text("Portal already existed.").color(NamedTextColor.RED), MessageType.SYSTEM);
        }
    }

    @Subcommand("config connect")
    @CommandPermission("portal.config.connect")
    public void connect(Player player, String from, String to) {
        Tunnel t = new Tunnel(from, to);
        plugin.config.tunnels.add(t);
        plugin.saveChanges();
        player.sendMessage(Identity.nil(), Component.text("Tunnel added. Do /portal reload after done.").color(NamedTextColor.GREEN), MessageType.SYSTEM);
    }
}
