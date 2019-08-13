package me.valk.disasters.disasters;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Flood extends Disaster implements Listener {
	public static int id = 0;
	public static void floodEffect() {
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(JavaPlugin.getProvidingPlugin(Disaster.class), new Runnable() {
			@Override
			public void run() {
				
			}
		}, 1, 1);
	}
}
