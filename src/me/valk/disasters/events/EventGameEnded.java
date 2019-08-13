package me.valk.disasters.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EventGameEnded extends Event {
	public EventGameEnded() {

	}

	private static final HandlerList handlers = new HandlerList();

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
