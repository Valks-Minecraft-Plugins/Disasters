package me.valk.disasters.listeners;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.valk.disasters.Disasters;
import me.valk.disasters.configs.ConfigLocation;
import me.valk.disasters.disasters.Astronaut;
import me.valk.disasters.events.EventGameStarted;
import me.valk.disasters.utils.TextModule;
import me.valk.disasters.utils.Utils;

public class ListenerGameStarted implements Listener {
	@EventHandler
	private void gameStarted(EventGameStarted e) {
		List<Player> players = Utils.getDisasterLobbyPlayers(e.getLobbyName());
		String pathLobby = Utils.getDisasterLobbyPath(e.getLobbyName());
		YamlConfiguration configLobbies = Disasters.lobbiesConfig;
		ConfigLocation configLocation = new ConfigLocation(Disasters.lobbiesCM);
		
		new BukkitRunnable() {
			boolean disaster = false;
			final int DISASTER_TIME = 30;
			int disasterCount = DISASTER_TIME;
			boolean prep = true;
			final int PREP_TIME = 10;
			int prepTimeCounter = PREP_TIME;
			
			@Override
			public void run() {
				if (prep) {
					prepTimeCounter--;
					
					for (Player p : players) {
						p.sendMessage(TextModule.color("Time till next disaster: " + prepTimeCounter));
					}
					
					if (prepTimeCounter <= 0) {
						prepTimeCounter = PREP_TIME;
						prep = false;
						disaster = true;
						
						for (Player p : players) {
							p.sendMessage(TextModule.color("Disaster is here!"));
						}
					}
				}
				
				if (disaster) {
					// Disaster incoming!
					disasterCount--;
					
					
					// Astronaut.astronautEffect(players);
					
					//configLocation.get(pathLobby)
					
					if (disasterCount <= 0) {
						disasterCount = DISASTER_TIME;
						disaster = false;
						prep = true;
						Bukkit.getScheduler().cancelTask(Astronaut.id);
					}
				}
			}
		}.runTaskTimer(JavaPlugin.getPlugin(Disasters.class), 20, 20);
	}
}
