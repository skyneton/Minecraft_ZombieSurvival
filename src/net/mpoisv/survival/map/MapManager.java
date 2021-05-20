package net.mpoisv.survival.map;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import net.mpoisv.survival.game.GameWorkerManager;
import net.mpoisv.survival.game.GameWorkerManager.DuringGameType;
import net.mpoisv.survival.module.Map;
import net.mpoisv.survival.module.Map.GamePlayType;
import net.mpoisv.survival.util.GameUtils;

public class MapManager {
	public static String dir;
	public static void make() {
		File directory = new File(dir, "map");
		if(!directory.exists()) directory.mkdirs();
		for(String uuid : getMapList()) {
			File file = new File(directory, uuid+".yml");
			YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
			GameUtils.MAPS.add(new Map(file.getName().substring(0, file.getName().length() - 4), file.getAbsolutePath(), yml.getString("name"), yml.getString("builder"), GamePlayType.valueOf(yml.getString("type")), (Location) yml.get("spawn"), (Location) yml.get("zombie")));
		}
	}
	
	public static List<String> getMapList() {
		File file = new File(dir, "map.yml");
		if(!file.exists()) {
			try {
				file.createNewFile();
			}catch(Exception e) {}
			return new ArrayList<>();
		}
		
		YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
		if(yml.contains("list")) {
			return yml.getStringList("list");
		}
		
		return new ArrayList<>();
	}
	
	public static void saveMapList() {
		File file = new File(dir, "map.yml");
		YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
		yml.set("list", getMapId());
		
		try {
			yml.save(file);
		}catch(Exception e) {}
	}
	
	public static ArrayList<String> getMapId() {
		ArrayList<String> arr = new ArrayList<>();
		for(Map map : GameUtils.MAPS) {
			arr.add(map.uuid);
		}
		
		return arr;
	}
	
	public static void deleteMap(int index) {
		Map map = GameUtils.MAPS.get(index);
		GameUtils.MAPS.remove(index);
		if(GameUtils.MAP_INDEX < index) {
			if(GameUtils.MAP_INDEX >= GameUtils.MAPS.size()) GameUtils.MAP_INDEX = 0;
			if(!(GameWorkerManager.now == DuringGameType.NOT || GameWorkerManager.now == DuringGameType.STAY_LOBBY) || GameUtils.MAPS.size() <= 0) {
				if(GameUtils.worker != null) {
					GameUtils.worker.isRunnable = false;
				}
				GameUtils.autoPlay();
			}
		}
		new File(map.dir).delete();
		saveMapList();
	}
	
	public static void addMap(String name, String builder, GamePlayType type, Location spawn, Location zombie) {
		String id = UUID.randomUUID().toString();
		File file = new File(dir, "map/"+id+".yml");
		try {
			if(!file.exists()) file.createNewFile();
		}catch (Exception e) {}
		
		Map map = new Map(id, file.getAbsolutePath(), name, builder, type, spawn, zombie);
		map.save();
		GameUtils.MAPS.add(map);
		saveMapList();

		GameUtils.bar.setTitle("다음 맵: §e"+GameUtils.MAPS.get(GameUtils.MAP_INDEX).name+" §f경기장");
		GameUtils.bar.setProgress(1);
		GameUtils.bar.setVisible(true);
		
		GameUtils.autoPlay();
	}
}
