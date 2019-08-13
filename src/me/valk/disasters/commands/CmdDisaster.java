package me.valk.disasters.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.valk.disasters.Disasters;
import me.valk.disasters.configs.ConfigLocation;
import me.valk.disasters.events.EventPlayerJoinLobby;
import me.valk.disasters.events.EventPlayerQuitLobby;
import me.valk.disasters.listeners.ListenerChat;
import me.valk.disasters.utils.TextModule;
import me.valk.disasters.utils.Utils;

public class CmdDisaster implements CommandExecutor {
	@SuppressWarnings("unused")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		YamlConfiguration messagesConfig = Disasters.messagesConfig;
		if (command.getName().equalsIgnoreCase("disaster")) {
			if (args.length < 1) {
				sender.sendMessage(TextModule.color(messagesConfig.getString("messages.error.usage_disaster")));
				return true;
			}
			
			Player p = (Player) sender;
			if (args[0].equalsIgnoreCase("reload")) { // Done
				if (!sender.hasPermission("disaster.reload")) {
					sender.sendMessage(TextModule.color(messagesConfig.getString("messages.error.no_permission")));
					return true;
				}
				
				sender.sendMessage(TextModule.color(messagesConfig.getString("messages.message.reloaded_configs")));
				Disasters.lobbiesCM.reloadConfig();
				Disasters.mainCM.reloadConfig();
				Disasters.messagesCM.reloadConfig();
				return true;
			}
			
			if (args[0].equalsIgnoreCase("version")) { // Done
				sender.sendMessage(TextModule.color("&f1&7.&f0&7.&f0&7-SNAPSHOT"));
				return true;
			}
			
			if (args[0].equalsIgnoreCase("author")) { // Done
				sender.sendMessage(TextModule.color("&7Plugin created by valkyrienyanko."));
				return true;
			}
			
			if (args[0].equalsIgnoreCase("lobby")) { // Done
				YamlConfiguration lobbiesConfig = Disasters.lobbiesConfig;
				ConfigurationSection lobbies = lobbiesConfig.getConfigurationSection("lobbies");
				
				if (args.length <= 1) {
					sender.sendMessage(TextModule.color(messagesConfig.getString("messages.error.usage_lobby")));
					return true;
				}
				
				if (args[1].equalsIgnoreCase("remove")) { // Done
					if (!sender.hasPermission("disasters.lobby.remove")) {
						sender.sendMessage(TextModule.color(messagesConfig.getString("messages.error.no_permission")));
						return true;
					}
					
					if (args.length <= 2) {
						sender.sendMessage(TextModule.color(messagesConfig.getString("messages.error.usage_lobby_remove")));
						return true;
					}
					
					String deleteLobbyName = args[2];
					String theLobby = "";
					boolean foundLobby = false;
					
					// Find the lobby.
					for (String lobby : lobbies.getKeys(false)) {
						String path = "lobbies." + lobby + ".";
						String lobbyName = lobbiesConfig.getString(path + "name");
						if (lobbyName.equalsIgnoreCase(deleteLobbyName)) {
							theLobby = lobby;
							foundLobby = true;
						}
					}
					
					if (foundLobby) {
						lobbiesConfig.set("lobbies." + theLobby, null);
						Disasters.lobbiesCM.saveConfig();
						sender.sendMessage(TextModule.color(messagesConfig.getString("messages.message.removed_lobby")));
						return true;
					} else {
						sender.sendMessage(TextModule.color(messagesConfig.getString("messages.error.lobby_not_found")));
						return true;
					}
				}
				
				if (args[1].equalsIgnoreCase("configure")) {
					if (!sender.hasPermission("disasters.lobby.configure")) {
						sender.sendMessage(TextModule.color(messagesConfig.getString("messages.error.no_permission")));
						return true;
					}
					
					if (args.length <= 2) {
						sender.sendMessage(TextModule.color(messagesConfig.getString("messages.error.usage_lobby_configure")));
						return true;
					}
					
					String configureLobbyName = args[2];
					
					// Find the lobby.
					boolean foundLobby = false;
					String pathToLobby = "";
					for (String lobby : lobbies.getKeys(false)) {
						String path = "lobbies." + lobby + ".";
						String lobbyName = lobbiesConfig.getString(path + "name");
						if (lobbyName.equalsIgnoreCase(configureLobbyName)) {
							foundLobby = true;
							pathToLobby = path;
						}
					}
					
					if (foundLobby) {
						sender.sendMessage(TextModule.color(messagesConfig.getString("messages.message.input_world")));
						ListenerChat.inputWorld.add(p.getUniqueId());
					} else {
						sender.sendMessage(TextModule.color(messagesConfig.getString("messages.error.lobby_not_found")));
						return true;
					}
				}
				
				if (args[1].equalsIgnoreCase("create")) {
					if (!sender.hasPermission("disasters.lobby.create")) {
						sender.sendMessage(TextModule.color(messagesConfig.getString("messages.error.no_permission")));
						return true;
					}
					
					if (args.length <= 3) {
						sender.sendMessage(TextModule.color(messagesConfig.getString("messages.error.usage_lobby_create")));
						return true;
					}
					
					String createLobbyName = args[2];
					
					// Check if lobby name already exists.
					for (String lobby : lobbies.getKeys(false)) {
						String path = "lobbies." + lobby + ".";
						String lobbyName = lobbiesConfig.getString(path + "name");
						if (lobbyName.equalsIgnoreCase(createLobbyName)) {
							sender.sendMessage(TextModule.color(messagesConfig.getString("messages.error.lobby_already_exists")));
							return true;
						}
					}
					
					// Create the lobby.
					int slot = 0;
					if (lobbiesConfig.isConfigurationSection("lobbies")) {
						for (String element : lobbiesConfig.getKeys(false)) {
							if (element != null) {
								slot++;
							}
						}	
					}
					
					lobbiesConfig.set("lobbies." + slot + ".name", createLobbyName);
					lobbiesConfig.set("lobbies." + slot + ".creator", p.getUniqueId().toString());
					lobbiesConfig.set("lobbies." + slot + ".configured", false);
					lobbiesConfig.set("lobbies." + slot + ".max_players", Integer.parseInt(args[3]));
					lobbiesConfig.set("lobbies." + slot + ".in_progress", false);
					
					List<String> emptyArray = new ArrayList<String>();
					lobbiesConfig.set("lobbies." + slot + ".players", emptyArray);
					
					Disasters.lobbiesCM.saveConfig();
					sender.sendMessage(TextModule.color(messagesConfig.getString("messages.message.lobby_created")));
					return true;
				}
				
				if (args[1].equalsIgnoreCase("join")) {
					if (args.length <= 2) {
						sender.sendMessage(TextModule.color(messagesConfig.getString("messages.error.usage_lobby_join")));
						return true;
					}
					
					String thePath = "";
					boolean lobbyFound = false;
					for (String lobby : lobbies.getKeys(false)) {
						String path = "lobbies." + lobby + ".";
						
						if (lobbiesConfig.getString(path + "name").equalsIgnoreCase(args[2])) {
							lobbyFound = true;
							thePath = path;
						}
					}
					
					if (!lobbyFound) {
						sender.sendMessage(TextModule.color(messagesConfig.getString("messages.error.lobby_not_found")));
						return true;
					}
					
					boolean configured = lobbiesConfig.getBoolean(thePath + "configured");
					if (!configured) {
						sender.sendMessage(TextModule.color(messagesConfig.getString("messages.error.lobby_not_configured")));
						return true;
					}
					
					boolean in_progress = lobbiesConfig.getBoolean(thePath + "in_progress");
					int maxPlayers = lobbiesConfig.getInt(thePath + "max_players");
					int curPlayers = 0;
					
					if (in_progress) {
						sender.sendMessage(TextModule.color(messagesConfig.getString("messages.error.lobby_started")));
						return true;
					}
					
					ConfigurationSection players = lobbiesConfig.getConfigurationSection(thePath + "players");
					if (players != null) {
						for (String player : players.getKeys(false)) {
							if (lobbiesConfig.getString(thePath + "players." + player).equals(p.getUniqueId().toString())) {
								sender.sendMessage(TextModule.color(messagesConfig.getString("messages.error.already_in_lobby")));
								return true;
							}
							curPlayers++;
						}
					}
					
					// We need to count the player currently trying to join as well.
					if (players != null) {
						if (curPlayers + 1 > maxPlayers) {
							sender.sendMessage(TextModule.color(messagesConfig.getString("messages.error.lobby_full")));
							return true;
						}
					}
					
					// Player can join the lobby.
					sender.sendMessage(TextModule.color(messagesConfig.getString("messages.message.joined_lobby")));
					
					lobbiesConfig.set(thePath + "players." + (curPlayers + 1), p.getUniqueId().toString());
					Disasters.lobbiesCM.saveConfig();
					
					ConfigLocation configLocation = new ConfigLocation(Disasters.lobbiesCM);
					Location locationLobby = configLocation.get(thePath + "loc.lobby");
					
					Bukkit.getServer().getPluginManager().callEvent(new EventPlayerJoinLobby(p, lobbiesConfig.getString(thePath + "name")));
					
					p.teleport(locationLobby);
					
					return true;
				}
				
				if (args[1].equalsIgnoreCase("leave")) {
					String pathToPlayer = Utils.getDisasterPlayerPath(p);
					if (pathToPlayer != "") {
						Bukkit.getServer().getPluginManager().callEvent(new EventPlayerQuitLobby(p));
						sender.sendMessage(TextModule.color(messagesConfig.getString("messages.message.left_lobby")));
						
						ConfigLocation configLocation = new ConfigLocation(Disasters.lobbiesCM);
						
						String pathToLobby = Utils.getDisasterLobbyPath(p);
						
						Location locationExit = configLocation.get(pathToLobby + "loc.end");
						
						p.teleport(locationExit);
						
						lobbiesConfig.set(pathToPlayer, null);
						Disasters.lobbiesCM.saveConfig();
					} else {
						sender.sendMessage(TextModule.color(messagesConfig.getString("messages.error.not_in_a_lobby")));
					}
					
					return true;
				}
			}
			return true;
		}
		return true;
	}
}
