package me.valk.disasters.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.valk.disasters.Disasters;

public class Utils {
	public static void copyWorld(File source, File target){
	    try {
	        ArrayList<String> ignore = new ArrayList<String>(Arrays.asList("uid.dat", "session.dat"));
	        if(!ignore.contains(source.getName())) {
	            if(source.isDirectory()) {
	                if(!target.exists())
	                target.mkdirs();
	                String files[] = source.list();
	                for (String file : files) {
	                    File srcFile = new File(source, file);
	                    File destFile = new File(target, file);
	                    copyWorld(srcFile, destFile);
	                }
	            } else {
	                InputStream in = new FileInputStream(source);
	                OutputStream out = new FileOutputStream(target);
	                byte[] buffer = new byte[1024];
	                int length;
	                while ((length = in.read(buffer)) > 0)
	                    out.write(buffer, 0, length);
	                in.close();
	                out.close();
	            }
	        }
	    } catch (IOException e) {
	    	System.out.println(e);
	    }
	}
	
	public static String getDisasterPlayerPath(Player p) {
		YamlConfiguration lobbiesConfig = Disasters.lobbiesConfig;
		ConfigurationSection lobbiesConfigSection = lobbiesConfig.getConfigurationSection("lobbies");
		for (String lobby : lobbiesConfigSection.getKeys(false)) {
			String path = "lobbies." + lobby + ".";
			ConfigurationSection playersConfigSection = lobbiesConfig.getConfigurationSection(path + "players");
			for (String element : playersConfigSection.getKeys(false)) {
				String playerUUID = lobbiesConfig.getString(path + "players." + element);
				if (playerUUID.equalsIgnoreCase(p.getUniqueId().toString())) {
					return path + "players." + element;
				}
			}
		}
		return "";
	}
	
	public static String getDisasterLobbyPath(Player p) {
		YamlConfiguration lobbiesConfig = Disasters.lobbiesConfig;
		ConfigurationSection lobbiesConfigSection = lobbiesConfig.getConfigurationSection("lobbies");
		for (String lobby : lobbiesConfigSection.getKeys(false)) {
			String path = "lobbies." + lobby + ".";
			System.out.println(path);
			System.out.println(lobbiesConfig);
			ConfigurationSection playersConfigSection = lobbiesConfig.getConfigurationSection(path + "players");
			System.out.println(playersConfigSection);
			for (String element : playersConfigSection.getKeys(false)) {
				String playerUUID = lobbiesConfig.getString(path + "players." + element);
				if (playerUUID.equalsIgnoreCase(p.getUniqueId().toString())) {
					return path;
				}
			}
		}
		return "";
	}
	
	public static String getDisasterLobbyPath(String lobbyName) {
		YamlConfiguration lobbiesConfig = Disasters.lobbiesConfig;
		
		ConfigurationSection lobbiesConfigSection = lobbiesConfig.getConfigurationSection("lobbies");
		for (String lobby : lobbiesConfigSection.getKeys(false)) {
			String path = "lobbies." + lobby + ".";
			if (lobbiesConfig.getString(path + "name").equals(lobbyName)) {
				return lobbyName;
			}
		}
		return "";
	}
	
	public static List<Player> getDisasterLobbyPlayers(String lobbyName) {
		List<Player> players = new ArrayList<Player>();
		
		YamlConfiguration lobbiesConfig = Disasters.lobbiesConfig;
		
		ConfigurationSection lobbiesConfigSection = lobbiesConfig.getConfigurationSection("lobbies");
		for (String lobby : lobbiesConfigSection.getKeys(false)) {
			String path = "lobbies." + lobby + ".";
			if (lobbiesConfig.getString(path + "name").equals(lobbyName)) {
				ConfigurationSection playersConfigSection = lobbiesConfig.getConfigurationSection(path + "players");
				for (String element : playersConfigSection.getKeys(false)) {
					String playerUUID = lobbiesConfig.getString(path + "players." + element);
					players.add(Bukkit.getPlayer(UUID.fromString(playerUUID)));
				}
			}
		}

		return players;
	}
}
