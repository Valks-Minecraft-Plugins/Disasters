package me.valk.disasters.listeners;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import me.valk.disasters.Disasters;
import me.valk.disasters.configs.ConfigLocation;
import me.valk.disasters.utils.Utils;

public class ListenerPlayerDeath implements Listener {
	@SuppressWarnings("deprecation")
	@EventHandler
	private void playerDeathEvent(PlayerDeathEvent e) {
		Player p = e.getEntity();
		String lobby = Utils.getDisasterLobbyPath(p);
		if (lobby != "") {
			p.setHealth(p.getMaxHealth());
			String spectateLocPath = lobby + "loc.spectate";
			ConfigLocation configLoc = new ConfigLocation(Disasters.lobbiesCM);
			Location spectateLoc = configLoc.get(spectateLocPath);
			p.teleport(spectateLoc);
			p.setGameMode(GameMode.SPECTATOR);
		}
	}
}
