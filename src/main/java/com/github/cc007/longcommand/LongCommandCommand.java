/*
 * The MIT License
 *
 * Copyright 2016 Rik Schaaf aka CC007 (http://coolcat007.nl/).
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.github.cc007.longcommand;

import com.google.common.base.Joiner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javafx.scene.input.KeyCode;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author Rik Schaaf aka CC007 (http://coolcat007.nl/)
 */
public class LongCommandCommand implements CommandExecutor {

    Plugin plugin;
    Map<Player, List<String>> commands;

    public LongCommandCommand(Plugin plugin) {
        this.plugin = plugin;
        this.commands = new HashMap<>();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            plugin.getLogger().log(Level.WARNING, "This plugin is for player use only.");
            return false;
        }
        return onPlayerCommand((Player) sender, command, commandLabel, args);
    }

    private boolean onPlayerCommand(Player player, Command command, String commandLabel, String[] args) {
        List<String> playerCommand;
        switch (command.getName()) {
            case "lci":
                commands.put(player, new ArrayList<String>());
                commands.get(player).add(Joiner.on(" ").join(args));
                player.sendMessage(ChatColor.GOLD + "Created new command: /" + Joiner.on("").join(commands.get(player)));
                break;
            case "lc":
                playerCommand = commands.get(player);
                if (playerCommand == null) {
                    commands.put(player, new ArrayList<String>());
                    playerCommand = commands.get(player);
                    playerCommand.add(Joiner.on(" ").join(args));
                    player.sendMessage(ChatColor.GOLD + "Created new command: '/" + Joiner.on("").join(playerCommand) + "'");
                } else {
                    playerCommand.add(Joiner.on(" ").join(args));
                    player.sendMessage(ChatColor.GOLD + "Added to command: '/" + Joiner.on("").join(playerCommand) + "'");
                }
                break;
            case "lcas":
                playerCommand = commands.get(player);
                if (playerCommand == null) {
                    player.sendMessage(ChatColor.RED + "You didn't initialize a long command yet. Use /lci <command> to create a partial command");
                    return false;
                }
                playerCommand.add(" ");
                player.sendMessage(ChatColor.GOLD + "Added to command: '/" + Joiner.on("").join(playerCommand) + "'");
                break;
            case "lcc":
                commands.remove(player);
                player.sendMessage(ChatColor.GOLD + "Cleared the command");
                break;
            case "lcs":
                playerCommand = commands.get(player);
                if (playerCommand == null) {
                    player.sendMessage(ChatColor.RED + "You didn't initialize a long command yet. Use /lci <command> to create a partial command");
                    return false;
                }
                player.sendMessage(ChatColor.GOLD + "Send the command: '/" + Joiner.on("").join(playerCommand) + "'");
                Bukkit.dispatchCommand(player, Joiner.on("").join(playerCommand));
                commands.remove(player);
                break;

            default:
                return false;
        }
        return true;
    }
}
