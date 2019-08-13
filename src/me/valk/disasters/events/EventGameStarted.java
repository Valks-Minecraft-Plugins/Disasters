package me.valk.disasters.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EventGameStarted extends Event {
	private String lobbyName;
	
	public EventGameStarted(String lobbyName) {
		this.lobbyName = lobbyName;
	}
	
	public String getLobbyName() {
		return lobbyName;
	}
	
	private static final HandlerList handlers = new HandlerList();

	public HandlerList getHandlers() {
	    return handlers;
	}

	public static HandlerList getHandlerList() {
	    return handlers;
	}
}
