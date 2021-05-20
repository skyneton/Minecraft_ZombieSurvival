package net.mpoisv.survival;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import net.mpoisv.survival.command.CommandManager;
import net.mpoisv.survival.command.TabManager;
import net.mpoisv.survival.event.EventManager;
import net.mpoisv.survival.map.MapManager;
import net.mpoisv.survival.packet.PacketInject;
import net.mpoisv.survival.util.ConfigUtils;
import net.mpoisv.survival.util.GameUtils;
import net.mpoisv.survival.util.ThreadUtils;

public class ZombieSurvival extends JavaPlugin {
	public static ZombieSurvival instance;
	public static Location lobby;
	public void onEnable() {
		init();
		Bukkit.getConsoleSender().sendMessage("§c: §fZombieSurvival §c:§f 플러그인이 로드되었습니다.");
//		new MinecraftServer(Bukkit.getServer());
	}
	
	public void onDisable() {
		GameUtils.stop();
		ConfigUtils.saveConfig();
		Bukkit.getConsoleSender().sendMessage("§c: §fZombieSurvival §c:§f 플러그인이 언로드 되었습니다.");
	}
	
	private void init() {
		instance = this;
		if(Bukkit.getPluginManager().getPlugin("PlayerDataResource") == null) {
			File playerDataResourceJar = updatePlayerDataResource();
			if(playerDataResourceJar == null) {
				Bukkit.getConsoleSender().sendMessage("§cPlayerDataResource 플러그인을 가져 올 수 없습니다.");
				Bukkit.getPluginManager().disablePlugin(this);
			}else {
				try {
					Plugin plugin = Bukkit.getPluginManager().loadPlugin(playerDataResourceJar);
					plugin.onLoad();
					Bukkit.getPluginManager().enablePlugin(plugin);
				}catch(Exception e) {
					Bukkit.getConsoleSender().sendMessage("§cPlayerDataResource 플러그인을 가져 올 수 없습니다.");
					Bukkit.getPluginManager().disablePlugin(this);
				}
			}
		}
		
		getDataFolder().mkdirs();
		getConfig().options().copyDefaults(true);
		saveConfig();
		
		ConfigUtils.loadConfig();
		getLobby();
		
		CommandManager command = new CommandManager();
		TabManager tab = new TabManager();
		getCommand("ZombieSurvival").setExecutor(command);
		getCommand("ZombieSurvival").setTabCompleter(tab);
		getCommand("vote").setExecutor(command);
		getCommand("vote").setTabCompleter(tab);
		
		MapManager.dir = getDataFolder().getAbsolutePath();
		
		Bukkit.getPluginManager().registerEvents(new EventManager(), this);
		
		MapManager.make();
		
		for(Player player : Bukkit.getOnlinePlayers()) {
			GameUtils.bar.addPlayer(player);
			ThreadUtils.submit(new PacketInject(player));
		}
		if(GameUtils.MAPS.size() > 0) {
			GameUtils.bar.setTitle("다음 맵: §e"+GameUtils.MAPS.get(GameUtils.MAP_INDEX).name+" §f경기장");
			GameUtils.bar.setVisible(true);
		}else GameUtils.bar.setVisible(false);
		
		GameUtils.autoPlay();
	}
	
	public static void getLobby() {
		File file = new File(instance.getDataFolder(), "lobby.yml");
		try {
			if(!file.exists()) file.createNewFile();
		}catch(Exception e) {}
		YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
		if(!yml.contains("lobby")) {
			yml.set("lobby", Bukkit.getWorlds().get(0).getSpawnLocation());
			
			try {
				yml.save(file);
			}catch(Exception e) {}
		}
		
		lobby = (Location) yml.get("lobby");
	}
	
	public static void setLobby(Location loc) {
		File file = new File(instance.getDataFolder(), "lobby.yml");
		try {
			if(!file.exists()) file.createNewFile();
		}catch(Exception e) {}
		YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
		yml.set("lobby", loc);
		lobby = loc;
		loc.getWorld().setSpawnLocation(loc);
		
		try {
			yml.save(file);
		}catch(Exception e) {}
	}
	
	public static File updatePlayerDataResource() {
		File playerDataResourceJar = new File("plugins/PlayerDataResource.jar");
		if(Bukkit.getPluginManager().getPlugin("PlayerDataResource") != null) {
			Bukkit.getPluginManager().disablePlugin(Bukkit.getPluginManager().getPlugin("PlayerDataResource"));
		}
		
		if(!playerDataResourceJar.exists()) {
			try {
				playerDataResourceJar.createNewFile();
			}catch(Exception e) {
				return null;
			}
		}
		
		try {
			URL url = new URL("https://github.com/skyneton/Minecraft_PlayerDataResource/releases/latest/download/PlayerDataResource.jar");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			InputStream input = conn.getInputStream();
			Files.copy(input, playerDataResourceJar.toPath(), new CopyOption[] { StandardCopyOption.REPLACE_EXISTING });
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return playerDataResourceJar;
	}
}
