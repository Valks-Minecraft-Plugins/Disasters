package me.valk.disasters.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import me.valk.disasters.events.EventPlayerQuitLobby;

public class ListenerPlayerQuit implements Listener {
	@EventHandler
	private void playerQuitEvent(PlayerQuitEvent e) {
		Bukkit.getServer().getPluginManager().callEvent(new EventPlayerQuitLobby(e.getPlayer()));
	}
}
