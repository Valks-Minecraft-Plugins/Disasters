package me.valk.disasters.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.valk.disasters.events.EventPlayerQuitLobby;

public class ListenerPlayerQuitLobby implements Listener {
	@EventHandler
	private void playerLeaveLobby(EventPlayerQuitLobby e) {
		Bukkit.broadcastMessage(e.getPlayer().getName() + " left lobby.");
	}
}
