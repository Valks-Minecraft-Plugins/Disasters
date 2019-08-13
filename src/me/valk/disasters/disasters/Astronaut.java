package me.valk.disasters.disasters;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class Astronaut extends Disaster implements Listener {
	public static int id = 0;
	public static void astronautEffect(List<Player> players) {
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(JavaPlugin.getProvidingPlugin(Disaster.class), new Runnable() {
			@Override
			public void run() {
				for (Player p : players) {
					p.setVelocity(new Vector(0, 0.2, 0));
				}
			}
		}, 1, 1);
	}
}
