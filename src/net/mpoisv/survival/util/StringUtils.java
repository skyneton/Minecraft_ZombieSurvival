package net.mpoisv.survival.util;

import java.util.Random;

public class StringUtils {
	public static String randomString() {
		StringBuffer temp = new StringBuffer();
		Random random = new Random();
		for(int i = 0; i < random.nextInt(5)+5; i++) {
			switch(random.nextInt(3)) {
			case 0:
				temp.append((char) (random.nextInt(26) + 97));
				break;
			case 1:
				temp.append((char) (random.nextInt(26) + 65));
				break;
			case 2:
				temp.append((random.nextInt(10)));
				break;
			}
		}
		return temp.toString();
	}
	
	public static String color(String str) {
		return str.replaceAll("&0", "§0")
			.replaceAll("&1", "§1")
			.replaceAll("&2", "§2")
			.replaceAll("&3", "§3")
			.replaceAll("&4", "§4")
			.replaceAll("&5", "§5")
			.replaceAll("&6", "§6")
			.replaceAll("&7", "§7")
			.replaceAll("&8", "§8")
			.replaceAll("&9", "§9")
			.replaceAll("&a", "§a")
			.replaceAll("&b", "§b")
			.replaceAll("&c", "§c")
			.replaceAll("&d", "§d")
			.replaceAll("&e", "§e")
			.replaceAll("&f", "§f")
			.replaceAll("&k", "§k")
			.replaceAll("&l", "§l")
			.replaceAll("&m", "§m")
			.replaceAll("&n", "§n")
			.replaceAll("&o", "§o");
	}
	
	public static String revokeColor(String str) {
		return str.replaceAll("§0", "&0")
			.replaceAll("§1", "&1")
			.replaceAll("§2", "&2")
			.replaceAll("§3", "&3")
			.replaceAll("§4", "&4")
			.replaceAll("§5", "&5")
			.replaceAll("§6", "&6")
			.replaceAll("§7", "&7")
			.replaceAll("§8", "&8")
			.replaceAll("§9", "&9")
			.replaceAll("§a", "&a")
			.replaceAll("§b", "&b")
			.replaceAll("§c", "&c")
			.replaceAll("§d", "&d")
			.replaceAll("§e", "&e")
			.replaceAll("§f", "&f")
			.replaceAll("§k", "&k")
			.replaceAll("§l", "&l")
			.replaceAll("§m", "&m")
			.replaceAll("§n", "&n")
			.replaceAll("§o", "&o");
	}
	
	public static boolean isNumber(String str) {
		try {
			Long.parseLong(str);
			return true;
		}catch(Exception e) {}
		return false;
	}
}
