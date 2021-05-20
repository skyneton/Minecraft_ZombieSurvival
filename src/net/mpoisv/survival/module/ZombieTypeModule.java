package net.mpoisv.survival.module;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;

import net.mpoisv.survival.util.ZombieSelect;
import net.mpoisv.survival.util.ZombieType;

public class ZombieTypeModule {
	private ArrayList<ZombieType> arr = new ArrayList<>();
	public ZombieSelect select = ZombieSelect.AUTO;
	
	public ArrayList<ZombieType> getZombieTypes() {
		return arr;
	}

	
	public void set(YamlConfiguration yml, String name) {
		ArrayList<String> types = new ArrayList<>();
		for(ZombieType type : arr) types.add(type.toString());
		yml.set(name+".types", types);
		yml.set(name+".select", select.toString());
	}
	
	public static ZombieTypeModule get(YamlConfiguration yml, String name) {
		ZombieTypeModule module = new ZombieTypeModule();
		
		if(yml.contains(name+".types")) {
			List<String> types = yml.getStringList(name+".types");
			for(String type : types) {
				module.arr.add(ZombieType.valueOf(type));
			}
		}
		if(yml.contains(name+".select"))
			module.select = ZombieSelect.valueOf(yml.getString(name+".select"));
		
		return module;
	}
}
