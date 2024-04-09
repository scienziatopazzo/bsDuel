package dev.vedcodee.it.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import dev.vedcodee.it.Main;
import dev.vedcodee.it.arena.Arena;
import dev.vedcodee.it.kit.DuelKit;
import dev.vedcodee.it.utils.ChatUtils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.awt.*;
import java.util.HashMap;

@CommandAlias("duel|arena")
public class ArenaCommand extends BaseCommand {

    @Subcommand("create")
    @CommandPermission("bsduel.admin")
    public void onCreate(Player player, String[] args) {
        if(checkSyntax(player, args, 1)) return;
        String name = args[0];
        HashMap<String, String> placeholders = new HashMap<>();
        placeholders.put("arena", name);
        if(Arena.getArenaByName(name) != null) {
            ChatUtils.sendMessage(player, "arena_areal_exist", placeholders);
            return;
        }
        new Arena(name);
        ChatUtils.sendMessage(player, "created", placeholders);
        ChatUtils.sendMessage(player, "simplify_setspawn", placeholders);
        TextComponent messsage_1 = new TextComponent(ChatUtils.replace(Main.getInstance().getMessageConfiguration().getString("simplify_setspawn_1"), placeholders));
        TextComponent messsage_2 = new TextComponent(ChatUtils.replace(Main.getInstance().getMessageConfiguration().getString("simplify_setspawn_2"), placeholders));
        messsage_1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/arena set 1 " + name));
        messsage_2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/arena set 2 " + name));
        player.spigot().sendMessage(messsage_1);
        player.spigot().sendMessage(messsage_2);
    }

    @Subcommand("set")
    @CommandPermission("bsduel.admin")
    public void onSet(Player player, String[] args) {
        if(checkSyntax(player, args, 2)) return;
        int spawn;
        try {
            spawn = Integer.parseInt(args[0]);
        } catch (Exception e) {
            ChatUtils.sendMessage(player, "syntax_error", new HashMap<>());
            return;
        }

        String arena_name = args[1];
        Arena arena = Arena.getArenaByName(arena_name);
        if(arena == null) {
            ChatUtils.sendMessage(player, "syntax_error", new HashMap<>());
            return;
        }
        arena.setSpawn(spawn, player.getLocation());
        HashMap<String, String> placeholders = new HashMap<>();
        placeholders.put("arena", arena_name);
        placeholders.put("num", String.valueOf(spawn));
        ChatUtils.sendMessage(player, "spawn_set", placeholders);
    }

    @Subcommand("kit create")
    @CommandPermission("bsduel.admin")
    public void onKitCreate(Player player, String[] args) {
        if(checkSyntax(player, args, 1)) return;
        String name = args[0];
        HashMap<String, String> placeholders = new HashMap<>();
        placeholders.put("kit", name);
        if(DuelKit.getKitByName(name) != null) {
            ChatUtils.sendMessage(player, "kit_areal_exist", placeholders);
            return;
        }
        DuelKit kit = new DuelKit(name);
        kit.loadContent(player);
        ChatUtils.sendMessage(player, "kit_created", placeholders);
    }


    @Subcommand("kit get")
    @CommandPermission("bsduel.admin")
    public void onKitGet(Player player, String[] args) {
        if(checkSyntax(player, args, 1)) return;
        String name = args[0];
        HashMap<String, String> placeholders = new HashMap<>();
        placeholders.put("kit", name);
        if(DuelKit.getKitByName(name) == null) {
            ChatUtils.sendMessage(player, "kit_not_exist", placeholders);
            return;
        }
        DuelKit kit = DuelKit.getKitByName(name);
        player.getInventory().setContents(kit.getContent());
        ChatUtils.sendMessage(player, "kit_get", placeholders);
    }


    @Subcommand("kit delete")
    @CommandPermission("bsduel.admin")
    public void onKitDelete(Player player, String[] args) {
        if(checkSyntax(player, args, 1)) return;
        String name = args[0];
        HashMap<String, String> placeholders = new HashMap<>();
        placeholders.put("kit", name);
        if(DuelKit.getKitByName(name) == null) {
            ChatUtils.sendMessage(player, "kit_not_exist", placeholders);
            return;
        }
        DuelKit kit = DuelKit.getKitByName(name);
        kit.delete();
        ChatUtils.sendMessage(player, "kit_deleted", placeholders);
    }




    private boolean checkSyntax(Player player, String[] args, int min) {
        if(args.length < min) {
            ChatUtils.sendMessage(player, "syntax_error", new HashMap<>());
            return true;
        }
        return false;
    }


}
