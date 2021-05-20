package net.mpoisv.survival.module;

import java.io.File;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import net.mpoisv.survival.util.StringUtils;

public class Map {
	public final String uuid;
	public String dir;
	public String name;
	public String builder;
	public GamePlayType type;
	public Location spawn, zombie;
	
	public Map(String uuid, String dir, String name, String builder, GamePlayType type, Location spawn, Location zombie) {
		this.uuid = uuid;
		this.dir = dir;
		this.name = name;
		if(name != null) this.name = StringUtils.color(name);
		this.builder = builder;
		if(builder != null) this.builder = StringUtils.color(builder);
		this.type = type;
		this.spawn = spawn;
		this.zombie = zombie;
	}
	
	public File getFile() {
		if(dir == null) return null;
		return new File(dir);
	}
	
	public void save() {
		File file = getFile();
		if(file == null) return;
		YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
		String n = name;
		if(n != null) StringUtils.revokeColor(name);
		String b = builder;
		if(b != null) StringUtils.revokeColor(b);
		yml.set("name", n);
		yml.set("builder", b);
		yml.set("type", type.toString());
		yml.set("spawn", spawn);
		yml.set("zombie", zombie);
		try {
			yml.save(file);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static enum GamePlayType {
		SURVIVAL, INVISIBILITY, ESCAPE
	}
}
