package me.valk.disasters.listeners;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.valk.disasters.Disasters;
import me.valk.disasters.configs.ConfigLocation;
import me.valk.disasters.events.EventGameStarted;
import me.valk.disasters.events.EventPlayerJoinLobby;
import me.valk.disasters.utils.TextModule;

public class ListenerPlayerJoinLobby implements Listener {
	@EventHandler
	public void playerJoinLobbyEvent(EventPlayerJoinLobby e) {
		YamlConfiguration lobbiesConfig = Disasters.lobbiesConfig;
		
		String lobbyName = e.getLobbyName();
		Bukkit.broadcastMessage(e.getPlayer().getName() + " joined the lobby " + lobbyName + ".");
		int playersInQueue = 0;
		ConfigurationSection configLobbiesSection = Disasters.lobbiesConfig.getConfigurationSection("lobbies");
		for (String lobby : configLobbiesSection.getKeys(false)) {
			if (lobbiesConfig.getString("lobbies." + lobby + ".name").equalsIgnoreCase(lobbyName)) {
				ConfigurationSection players = Disasters.lobbiesConfig.getConfigurationSection("lobbies." + lobby + ".players");
				for (@SuppressWarnings("unused") String player : players.getKeys(false)) {
					playersInQueue++;
				}
			}
		}
		
		if (playersInQueue >= 1) {
			// Start timer.
			new BukkitRunnable() {
				int seconds = 10;
				boolean gameStarted = false;
				
				@Override
				public void run() {
					seconds--;
					if (seconds <= 0) {
						// Game started, teleport all players into the game.
						gameStarted = true;
						cancel();
					}
					
					ConfigurationSection lobbiesSection = lobbiesConfig.getConfigurationSection("lobbies");
					for (String lobby : lobbiesSection.getKeys(false)) {
						ConfigurationSection playersSection = lobbiesConfig.getConfigurationSection("lobbies." + lobby + ".players");
						for (String player : playersSection.getKeys(false)) {
							String uuid = lobbiesConfig.getString("lobbies." + lobby + ".players." + player);
							Player p = Bukkit.getPlayer(UUID.fromString(uuid));
							if (p != null) {
								p.sendMessage(TextModule.color("&7Game starting in &f" + seconds + "&7!"));
								
								if (gameStarted) {
									p.sendMessage(TextModule.color("&7Game started!"));
									
									ConfigLocation configLocation = new ConfigLocation(Disasters.lobbiesCM);
									Location locationGame = configLocation.get("lobbies." + lobby + ".loc.game");
									
									lobbiesConfig.set("lobbies." + lobby + ".in_progress", true);
									
									p.teleport(locationGame);
									
									Bukkit.getPluginManager().callEvent(new EventGameStarted(lobbyName));
								}
							}
						}
					}
				}
				
			}.runTaskTimer(JavaPlugin.getPlugin(Disasters.class), 20, 20);
		}
	}
}
