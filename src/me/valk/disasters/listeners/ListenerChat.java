package me.valk.disasters.listeners;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.valk.disasters.Disasters;
import me.valk.disasters.configs.ConfigLocation;
import me.valk.disasters.utils.TextModule;
import me.valk.disasters.utils.Utils;

public class ListenerChat implements Listener {
	public static List<UUID> inputWorld = new ArrayList<UUID>();
	public static List<UUID> inputLobbyLoc = new ArrayList<UUID>();
	public static List<UUID> inputGameLoc = new ArrayList<UUID>();
	public static List<UUID> inputSpectateLoc = new ArrayList<UUID>();
	public static List<UUID> inputEndLoc = new ArrayList<UUID>();
	public static List<UUID> inputGameRegion1 = new ArrayList<UUID>();
	public static List<UUID> inputGameRegion2 = new ArrayList<UUID>();
	
	@EventHandler
	private void playerChatEvent(AsyncPlayerChatEvent e) {
		YamlConfiguration messagesConfig = Disasters.messagesConfig;
		YamlConfiguration lobbiesConfig = Disasters.lobbiesConfig;
		ConfigLocation configLocation = new ConfigLocation(Disasters.lobbiesCM);
		Player p = e.getPlayer();
		Location pLoc = p.getLocation();
		UUID playerUUID = p.getUniqueId();
		
		if (inputWorld.contains(playerUUID)) {
			e.setCancelled(true);
			inputWorld.remove(playerUUID);
			
			String theLobby = Utils.getDisasterLobbyPath(Bukkit.getPlayer(playerUUID));
			/*ConfigurationSection lobbies = lobbiesConfig.getConfigurationSection("lobbies");
			String theLobby = "";
			for (String lobby : lobbies.getKeys(false)) {
				String path = "lobbies." + lobby + ".";
				String creator = lobbiesConfig.getString(path + "creator");
				if (playerUUID.toString().equals(creator)) {
					theLobby = lobby;
				}
			}*/
			
			World source = p.getWorld();
			File sourceFolder = source.getWorldFolder();
			
			File targetFolder = new File(Disasters.pluginFolder.getAbsolutePath() + "/worlds/" + source.getName());
			Utils.copyWorld(sourceFolder, targetFolder);
			
			lobbiesConfig.set("lobbies." + theLobby + ".world.name", p.getWorld().getName());
			Disasters.lobbiesCM.saveConfig();
			p.sendMessage(TextModule.color("World set!"));
			p.sendMessage(TextModule.color(messagesConfig.getString("messages.message.loc_input_lobby")));
			inputLobbyLoc.add(playerUUID);
			return;
		}
		
		if (inputLobbyLoc.contains(playerUUID)) {
			e.setCancelled(true);
			inputLobbyLoc.remove(playerUUID);
			String theLobby = Utils.getDisasterLobbyPath(Bukkit.getPlayer(playerUUID));
			configLocation.set("lobbies." + theLobby + ".loc.lobby", pLoc);
			Disasters.lobbiesCM.saveConfig();
			p.sendMessage(TextModule.color("Lobby location set!"));
			p.sendMessage(TextModule.color(messagesConfig.getString("messages.message.loc_input_game")));
			inputGameLoc.add(playerUUID);
			// Lobby location set.
			return;
		}
		
		if (inputGameLoc.contains(playerUUID)) {
			e.setCancelled(true);
			inputGameLoc.remove(playerUUID);
			String theLobby = Utils.getDisasterLobbyPath(Bukkit.getPlayer(playerUUID));
			configLocation.set("lobbies." + theLobby + ".loc.game", pLoc);
			Disasters.lobbiesCM.saveConfig();
			p.sendMessage(TextModule.color("Game location set!"));
			p.sendMessage(TextModule.color(messagesConfig.getString("messages.message.loc_input_spectator")));
			inputSpectateLoc.add(playerUUID);
			// Game location set.
			return;
		}
		
		if (inputSpectateLoc.contains(playerUUID)) {
			e.setCancelled(true);
			inputSpectateLoc.remove(playerUUID);
			String theLobby = Utils.getDisasterLobbyPath(Bukkit.getPlayer(playerUUID));
			configLocation.set("lobbies." + theLobby + ".loc.spectate", pLoc);
			Disasters.lobbiesCM.saveConfig();
			p.sendMessage(TextModule.color("Spectator location set!"));
			p.sendMessage(TextModule.color(messagesConfig.getString("messages.message.loc_input_end")));
			inputEndLoc.add(playerUUID);
			// Game location set.
			return;
		}
		
		if (inputEndLoc.contains(playerUUID)) {
			e.setCancelled(true);
			inputEndLoc.remove(playerUUID);
			String theLobby = Utils.getDisasterLobbyPath(Bukkit.getPlayer(playerUUID));
			configLocation.set("lobbies." + theLobby + ".loc.end", pLoc);
			Disasters.lobbiesCM.saveConfig();
			p.sendMessage(TextModule.color(messagesConfig.getString("messages.message.all_locations_set")));
			// End location set.
			inputGameRegion1.add(playerUUID);
			p.sendMessage(TextModule.color("End location set."));
			return;
		}
		
		if (inputGameRegion1.contains(playerUUID)) {
			e.setCancelled(true);
			inputGameRegion1.remove(playerUUID);
			String theLobby = Utils.getDisasterLobbyPath(Bukkit.getPlayer(playerUUID));
			configLocation.set("lobbies." + theLobby + ".loc.game.region.1", pLoc);
			Disasters.lobbiesCM.saveConfig();
			inputGameRegion2.add(playerUUID);
			p.sendMessage(TextModule.color("Game corner one set."));
			return;
		}
		
		if (inputGameRegion2.contains(playerUUID)) {
			e.setCancelled(true);
			inputGameRegion2.remove(playerUUID);
			String theLobby = Utils.getDisasterLobbyPath(Bukkit.getPlayer(playerUUID));
			configLocation.set("lobbies." + theLobby + ".loc.game.region.2", pLoc);
			lobbiesConfig.set("lobbies." + theLobby + ".configured", true);
			Disasters.lobbiesCM.saveConfig();
			p.sendMessage(TextModule.color("Game corner two set."));
			return;
			// All locations set!
		}
	}
}
