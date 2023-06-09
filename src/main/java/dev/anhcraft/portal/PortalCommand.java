package dev.anhcraft.portal;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import dev.anhcraft.portal.config.Portal;
import dev.anhcraft.portal.config.Tunnel;
import io.papermc.lib.PaperLib;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("portal")
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
            sender.sendMessage(ChatColor.GREEN + "Configuration reloaded.");
        } catch (Exception e) {
            e.printStackTrace();
            sender.sendMessage(ChatColor.RED + "Failed to reload configuration.");
        }
    }

    @Subcommand("list")
    @CommandPermission("portal.list")
    public void list(CommandSender sender) {
        sender.sendMessage(ChatColor.GREEN + "Portals: " + ChatColor.WHITE + String.join(", ", plugin.portalManager.getRegisteredPortals()));
    }

    @Subcommand("visit")
    @CommandPermission("portal.visit")
    public void visit(Player player, String portal) {
        Portal p = plugin.portalManager.getPortal(portal);
        if(p == null){
            player.sendMessage(ChatColor.RED + "Portal not existed.");
            return;
        }
        PaperLib.teleportAsync(player, p.location);
    }

    @Subcommand("movehere")
    @CommandPermission("portal.move-here")
    public void movePortal(Player player, String portal) {
        Portal p = plugin.portalManager.getPortal(portal);
        if(p == null){
            player.sendMessage(ChatColor.RED + "Portal not existed.");
            return;
        }
        p.location = player.getLocation().add(0, 0.5, 0);
        plugin.saveChanges();
        try {
            plugin.reload();
            player.sendMessage(ChatColor.GREEN + "Portal moved.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Subcommand("enable")
    @CommandPermission("portal.enable")
    public void enable(CommandSender sender, String portal) {
        Portal p = plugin.portalManager.getPortal(portal);
        if(p == null){
            sender.sendMessage(ChatColor.RED + "Portal not existed.");
            return;
        }
        p.disabled = false;
        plugin.saveChanges();
        try {
            plugin.reload();
            sender.sendMessage(ChatColor.GREEN + "Portal enabled.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Subcommand("disable")
    @CommandPermission("portal.disable")
    public void disable(CommandSender sender, String portal) {
        Portal p = plugin.portalManager.getPortal(portal);
        if(p == null){
            sender.sendMessage(ChatColor.RED + "Portal not existed.");
            return;
        }
        p.disabled = true;
        plugin.saveChanges();
        try {
            plugin.reload();
            sender.sendMessage(ChatColor.GREEN + "Portal disabled.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Subcommand("add-portal")
    @CommandPermission("portal.add-portal")
    public void addPortal(Player player, String portal, @Optional String name) {
        Portal p = new Portal();
        p.name = name == null ? portal : name;
        p.location = player.getLocation().add(0, 0.5, 0);
        if(plugin.portalManager.getPortal(portal) == null){
            p.resetBoundingBox();
            p.resetEffectRotation();
            plugin.config.portals.put(portal, p);
            plugin.saveChanges();
            try {
                plugin.reload();
                player.sendMessage(ChatColor.GREEN + "Portal added.");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            player.sendMessage(ChatColor.RED + "Portal already existed.");
        }
    }

    @Subcommand("delete-portal")
    @CommandPermission("portal.delete-portal")
    public void deletePortal(CommandSender sender, String portal) {
        plugin.config.portals.remove(portal);
        plugin.saveChanges();
        try {
            plugin.reload();
            sender.sendMessage(ChatColor.GREEN + "Portal removed.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Subcommand("connect")
    @CommandPermission("portal.connect")
    public void connect(CommandSender sender, String from, String to) {
        Tunnel t = new Tunnel(from, to);
        plugin.config.tunnels.add(t);
        plugin.saveChanges();
        try {
            plugin.reload();
            sender.sendMessage(ChatColor.GREEN + "Tunnel established.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
