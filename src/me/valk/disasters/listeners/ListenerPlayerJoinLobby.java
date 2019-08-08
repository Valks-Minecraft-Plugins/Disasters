package me.valk.disasters.listeners;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.valk.disasters.Disasters;
import me.valk.disasters.events.EventPlayerJoinLobby;

public class ListenerPlayerJoinLobby implements Listener {
	@SuppressWarnings("unused")
	@EventHandler
	private void playerJoinLobbyEvent(EventPlayerJoinLobby e, String lobbyName) {
		Bukkit.broadcastMessage(e.getPlayer().getName() + " joined the lobby " + lobbyName + ".");
		int playersInQueue = 0;
		ConfigurationSection lobbies = Disasters.lobbiesConfig.getConfigurationSection("lobbies");
		for (String lobby : lobbies.getKeys(false)) {
			if (lobbies.getString("lobbies." + lobby + ".name").equalsIgnoreCase(lobbyName)) {
				ConfigurationSection players = Disasters.lobbiesConfig.getConfigurationSection("lobbies." + lobby + ".players");
				for (String player : players.getKeys(false)) {
					playersInQueue++;
				}
			}
		}
		
		if (playersInQueue >= 1) {
			// Start timer.
			new BukkitRunnable() {
				int seconds = 10;
				
				@Override
				public void run() {
					Bukkit.broadcastMessage("Game starting in " + seconds + "!");
					
					seconds--;
					if (seconds <= 0) {
						// Game started, teleport all players into the game.
						cancel();
					}
				}
				
			}.runTaskTimer(JavaPlugin.getPlugin(Disasters.class), 20, 20);
		}
	}
}
