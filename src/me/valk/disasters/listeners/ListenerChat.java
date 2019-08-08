package me.valk.disasters.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.valk.disasters.Disasters;
import me.valk.disasters.configs.ConfigLocation;
import me.valk.disasters.utils.TextModule;

public class ListenerChat implements Listener {
	public static List<UUID> inputLobbyLoc = new ArrayList<UUID>();
	public static List<UUID> inputGameLoc = new ArrayList<UUID>();
	public static List<UUID> inputSpectateLoc = new ArrayList<UUID>();
	public static List<UUID> inputEndLoc = new ArrayList<UUID>();
	
	@EventHandler
	private void playerChatEvent(AsyncPlayerChatEvent e) {
		YamlConfiguration messagesConfig = Disasters.messagesConfig;
		YamlConfiguration lobbiesConfig = Disasters.lobbiesConfig;
		ConfigLocation configLocation = new ConfigLocation(Disasters.lobbiesCM);
		Player p = e.getPlayer();
		Location pLoc = p.getLocation();
		UUID playerUUID = p.getUniqueId();
		if (inputLobbyLoc.contains(playerUUID)) {
			e.setCancelled(true);
			inputLobbyLoc.remove(playerUUID);
			ConfigurationSection lobbies = lobbiesConfig.getConfigurationSection("lobbies");
			String theLobby = "";
			for (String lobby : lobbies.getKeys(false)) {
				String path = "lobbies." + lobby + ".";
				String creator = lobbiesConfig.getString(path + "creator");
				if (playerUUID.toString().equals(creator)) {
					theLobby = lobby;
				}
			}
			configLocation.set("lobbies." + theLobby + ".loc.lobby", pLoc);
			Disasters.lobbiesCM.saveConfig();
			p.sendMessage(TextModule.color(messagesConfig.getString("messages.message.loc_input_game")));
			inputGameLoc.add(playerUUID);
			// Lobby location set.
			return;
		}
		
		if (inputGameLoc.contains(playerUUID)) {
			e.setCancelled(true);
			inputGameLoc.remove(playerUUID);
			ConfigurationSection lobbies = lobbiesConfig.getConfigurationSection("lobbies");
			String theLobby = "";
			for (String lobby : lobbies.getKeys(false)) {
				String path = "lobbies." + lobby + ".";
				String creator = lobbiesConfig.getString(path + "creator");
				if (playerUUID.toString().equals(creator)) {
					theLobby = lobby;
				}
			}
			configLocation.set("lobbies." + theLobby + ".loc.game", pLoc);
			Disasters.lobbiesCM.saveConfig();
			p.sendMessage(TextModule.color(messagesConfig.getString("messages.message.loc_input_spectator")));
			inputSpectateLoc.add(playerUUID);
			// Game location set.
			return;
		}
		
		if (inputSpectateLoc.contains(playerUUID)) {
			e.setCancelled(true);
			inputSpectateLoc.remove(playerUUID);
			ConfigurationSection lobbies = lobbiesConfig.getConfigurationSection("lobbies");
			String theLobby = "";
			for (String lobby : lobbies.getKeys(false)) {
				String path = "lobbies." + lobby + ".";
				String creator = lobbiesConfig.getString(path + "creator");
				if (playerUUID.toString().equals(creator)) {
					theLobby = lobby;
				}
			}
			configLocation.set("lobbies." + theLobby + ".loc.spectate", pLoc);
			Disasters.lobbiesCM.saveConfig();
			p.sendMessage(TextModule.color(messagesConfig.getString("messages.message.loc_input_end")));
			inputEndLoc.add(playerUUID);
			// Game location set.
			return;
		}
		
		if (inputEndLoc.contains(playerUUID)) {
			e.setCancelled(true);
			inputEndLoc.remove(playerUUID);
			ConfigurationSection lobbies = lobbiesConfig.getConfigurationSection("lobbies");
			String theLobby = "";
			for (String lobby : lobbies.getKeys(false)) {
				String path = "lobbies." + lobby + ".";
				String creator = lobbiesConfig.getString(path + "creator");
				if (playerUUID.toString().equals(creator)) {
					theLobby = lobby;
				}
			}
			configLocation.set("lobbies." + theLobby + ".loc.end", pLoc);
			Disasters.lobbiesCM.saveConfig();
			p.sendMessage(TextModule.color(messagesConfig.getString("messages.message.all_locations_set")));
			// End location set.
			// All locations set!
			lobbiesConfig.set("lobbies." + theLobby + ".configured", true);
			return;
		}
	}
}
