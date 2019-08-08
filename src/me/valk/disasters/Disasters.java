package me.valk.disasters;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.valk.disasters.configs.ConfigLocation;
import me.valk.disasters.configs.ConfigManager;
import me.valk.disasters.listeners.ListenerChat;
import me.valk.disasters.listeners.ListenerPlayerJoin;
import me.valk.disasters.listeners.ListenerPlayerJoinLobby;
import me.valk.disasters.listeners.ListenerPlayerQuit;
import me.valk.disasters.listeners.ListenerPlayerQuitLobby;
import me.valk.disasters.commands.CmdDisaster;

public class Disasters extends JavaPlugin {
	public static File pluginFolder;
	
	public static ConfigManager mainCM;
	public static YamlConfiguration mainConfig;
	
	public static ConfigManager lobbiesCM;
	public static YamlConfiguration lobbiesConfig;
	
	public static ConfigManager messagesCM;
	public static YamlConfiguration messagesConfig;
	
	@Override
	public void onEnable() {
		pluginFolder = getDataFolder();
		initConfigs();
		registerCommands();
		registerListeners(getServer().getPluginManager());
	}
	
	private void registerCommands() {
		getCommand("disaster").setExecutor(new CmdDisaster());;
	}
	
	private void registerListeners(PluginManager pm) {
		pm.registerEvents(new ListenerChat(), this);
		pm.registerEvents(new ListenerPlayerJoin(), this);
		pm.registerEvents(new ListenerPlayerJoinLobby(), this);
		pm.registerEvents(new ListenerPlayerQuit(), this);
		pm.registerEvents(new ListenerPlayerQuitLobby(), this);
	}
	
	private void initConfigs() {
		initConfigMain();
		initConfigLobbies();
		initConfigMessages();
	}
	
	private void initConfigMain() {
		mainCM = new ConfigManager("config");
		mainConfig = mainCM.getConfig();
		
		defaultSet(mainConfig, "test", 123);
		
		mainCM.saveConfig();
	}
	
	private void initConfigLobbies() {
		lobbiesCM = new ConfigManager("lobbies");
		lobbiesConfig = lobbiesCM.getConfig();
		
		ConfigLocation configLocation = new ConfigLocation(lobbiesCM);
		
		defaultSet(lobbiesConfig, "lobbies.1.name", "Template");
		
		defaultSet(lobbiesConfig, "lobbies.1.in_progress", false);
		defaultSet(lobbiesConfig, "lobbies.1.max_players", 3);
		
		defaultSet(lobbiesConfig, "lobbies.1.players.1.Steve", "067e6162-3b6f-4ae2-a171-2470b63dff00");
		defaultSet(lobbiesConfig, "lobbies.1.players.2.valkyrienyanko", "463e6162-3h6f-4ae2-a171-2470b63dur15");
		defaultSet(lobbiesConfig, "lobbies.1.players.3.Notch", "233e6162-3h6f-6ae2-j129-6670b63yhw38");
		
		World templateWorld = Bukkit.getWorlds().get(0);
		Location templateLocation = templateWorld.getSpawnLocation();
		
		if (!lobbiesConfig.isSet("lobbies.1.loc.lobby")) {
			configLocation.set("lobbies.1.loc.lobby", templateLocation);
		}
		if (!lobbiesConfig.isSet("lobbies.1.loc.game")) {
			configLocation.set("lobbies.1.loc.game", templateLocation);
		}
		if (!lobbiesConfig.isSet("lobbies.1.loc.spectate")) {
			configLocation.set("lobbies.1.loc.spectate", templateLocation);
		}
		if (!lobbiesConfig.isSet("lobbies.1.loc.end")) {
			configLocation.set("lobbies.1.loc.end", templateLocation);
		}
		
		lobbiesCM.saveConfig();
	}
	
	private void initConfigMessages() {
		messagesCM = new ConfigManager("messages");
		messagesConfig = messagesCM.getConfig();
		
		defaultSet(messagesConfig, "messages.error.no_permission", "&cYou lack the permission to do that.");
		defaultSet(messagesConfig, "messages.error.usage_disaster", "&7Usage: /disaster [lobby | reload | version | author]");
		defaultSet(messagesConfig, "messages.error.usage_lobby", "&7Usage: /disaster lobby [create | configure | remove | join | leave]");
		defaultSet(messagesConfig, "messages.error.usage_lobby_create", "&7Usage: /disaster lobby create <name> <max_players>");
		defaultSet(messagesConfig, "messages.error.usage_lobby_remove", "&7Usage: /disaster lobby remove <name>");
		defaultSet(messagesConfig, "messages.error.usage_lobby_join", "&7Usage: /disaster lobby join <lobby>");
		defaultSet(messagesConfig, "messages.error.usage_lobby_configure", "&7Usage: /disaster lobby configure <name>");
		defaultSet(messagesConfig, "messages.error.lobby_started", "&7That lobby has already started the game.");
		defaultSet(messagesConfig, "messages.error.lobby_full", "&7That lobby is full.");
		defaultSet(messagesConfig, "messages.error.lobby_already_exists", "&cThe lobby with that name already exists!");
		defaultSet(messagesConfig, "messages.error.not_in_a_lobby", "&cYou're currently not in a lobby!");
		defaultSet(messagesConfig, "messages.error.lobby_not_found", "&cCould not find that lobby!");
		defaultSet(messagesConfig, "messages.error.lobby_not_configured", "&cThe owner of this lobby must configure it first before you can join it!");
		defaultSet(messagesConfig, "messages.error.already_in_lobby", "&cYou're already in this lobby!");
		
		defaultSet(messagesConfig, "messages.message.reloaded_configs", "&7Reloaded all configs.");
		defaultSet(messagesConfig, "messages.message.removed_lobby", "&7Removed the lobby from the config.");
		defaultSet(messagesConfig, "messages.message.left_lobby", "&7You left the lobby.");
		defaultSet(messagesConfig, "messages.message.lobby_created", "&7Sucessfully created lobby. Now configure it with /disaster lobby configure <name>");
		defaultSet(messagesConfig, "messages.message.loc_input_lobby", "&7Stand where you want the lobby location and then say anything.");
		defaultSet(messagesConfig, "messages.message.loc_input_game", "&7Stand where you want the game location and then say anything.");
		defaultSet(messagesConfig, "messages.message.loc_input_spectator", "&7Stand where you want the spectator location and then say anything.");
		defaultSet(messagesConfig, "messages.message.loc_input_end", "&7Stand where you want the end location and then say anything.");
		defaultSet(messagesConfig, "messages.message.all_locations_set", "&7All locations set!");
		
		messagesCM.saveConfig();
	}
	
	public void defaultSet(YamlConfiguration config, String path, Object value) {
		if (!config.isSet(path)) {
			config.set(path, value);
		}
	}
}
