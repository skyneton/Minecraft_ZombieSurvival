package net.mpoisv.survival.command.sub;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.mpoisv.survival.map.MapManager;
import net.mpoisv.survival.module.Map;
import net.mpoisv.survival.module.Map.GamePlayType;
import net.mpoisv.survival.util.GameUtils;
import net.mpoisv.survival.util.StringUtils;

public class Arena {
	public static void onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage("§c: §fZombieSurvival §c: §f플레이어만 사용이 가능합니다.");
			return;
		}
		
		Player player = (Player) sender;
		
		if(args.length >= 2) {
			if(args[1].equalsIgnoreCase("add")) {
				if(args.length >= 3) {
					String name = StringUtils.color(args[2].replaceAll("___", " "));
					GamePlayType type = GamePlayType.SURVIVAL;
					String builder = null;
					if(args.length >= 4) {
						if(Arrays.asList("SURVIVAL", "INVISIBILITY", "ESCAPE").contains(args[3].toUpperCase())) type = GamePlayType.valueOf(args[3].toUpperCase());
						else if(args[3].equals("0") || args[3].equals("생존")) type = GamePlayType.SURVIVAL;
						else if(args[3].equals("1") || args[3].equals("투명")) type = GamePlayType.INVISIBILITY;
						else if(args[3].equals("2") || args[3].equals("탈출")) type = GamePlayType.ESCAPE;
						else {
							builder = StringUtils.color(args[3].replaceAll("___", " "));
						}
					}
					if(args.length >= 5) builder = StringUtils.color(args[4].replaceAll("___", " "));
					Location loc = player.getLocation().getBlock().getLocation().add(0.5, 0, 0.5);
					MapManager.addMap(name, builder, type, loc, loc);
					
					sender.sendMessage("§c: §fZombieSurvival §c: §f현재 위치{x=§e"+loc.getX()+"§f, y=§e"+loc.getY()+"§f, z=§e"+loc.getZ()+"§f}가 "+name+"§f 경기장의 스폰 장소로 설정되었습니다.");
					return;
				}
			}else if(args[1].equalsIgnoreCase("remove")) {
				if(args.length >= 3 && StringUtils.isNumber(args[2])) {
					int id = Integer.parseInt(args[2]);
					if(id <= GameUtils.MAPS.size()) {
						Map map = GameUtils.MAPS.get(id);
						sender.sendMessage("§c: §fZombieSurvival §c: §f위치{x=§e"+map.spawn.getX()+"§f, y=§e"+map.spawn.getY()+"§f, z=§e"+map.spawn.getZ()+"§f}의 "+map.name+"§f 경기장이 삭제되었습니다.");
						MapManager.deleteMap(id);
					}else sender.sendMessage("§c: §fZombieSurvival §c: §f맵 ID를 정확히 입력하세요.");
					return;
				}
			}else if(args[1].equalsIgnoreCase("info")) {
				if(args.length >= 3 && StringUtils.isNumber(args[2])) {
					int id = Integer.parseInt(args[2]);
					if(id <= GameUtils.MAPS.size()) {
						Map map = GameUtils.MAPS.get(id);
						sender.sendMessage("§c========================================");
						sender.sendMessage(map.name + "§f 경기장");
						sender.sendMessage("§c"+map.type.toString());
						sender.sendMessage("{x=§e"+map.spawn.getX()+"§f, y=§e"+map.spawn.getY()+"§f, z=§e"+map.spawn.getZ()+"§f}");
						sender.sendMessage("§c========================================");
					}else sender.sendMessage("§c: §fZombieSurvival §c: §f맵 ID를 정확히 입력하세요.");
					return;
				}
			}else if(args[1].equalsIgnoreCase("list")) {
				int page = 0;
				if(args.length >= 3 && StringUtils.isNumber(args[2]))
					page = Integer.parseInt(args[2]);
				sender.sendMessage("§c========================================");
				for(int i = page * 6; i < page * 6 + 6 && i < GameUtils.MAPS.size(); i++) {
					Map map = GameUtils.MAPS.get(i);
					sender.sendMessage("§a"+String.format("%-"+String.valueOf(page * 6 + 6).length()+"s. §f", i)+map.name + "§f 경기장");
				}
				sender.sendMessage("§c========================================");
				sender.sendMessage("§e"+page+"§f 페이지");
				return;
			}else if(args[1].equalsIgnoreCase("update")) {
				if(args.length >= 4 && StringUtils.isNumber(args[2])) {
					int id = Integer.parseInt(args[2]);
					if(id <= GameUtils.MAPS.size()) {
						Map map = GameUtils.MAPS.get(id);
						String name = StringUtils.color(args[3].replaceAll("___", " "));
						GamePlayType type = GamePlayType.SURVIVAL;
						String builder = null;
						if(args.length >= 5) {
							if(Arrays.asList("SURVIVAL", "INVISIBILITY", "ESCAPE").contains(args[4].toUpperCase())) type = GamePlayType.valueOf(args[4].toUpperCase());
							else if(args[4].equals("0") || args[4].equals("생존")) type = GamePlayType.SURVIVAL;
							else if(args[4].equals("1") || args[4].equals("투명")) type = GamePlayType.INVISIBILITY;
							else if(args[4].equals("2") || args[4].equals("탈출")) type = GamePlayType.ESCAPE;
							else {
								builder = StringUtils.color(args[4].replaceAll("___", " "));
							}
						}
						if(args.length >= 6) builder = StringUtils.color(args[5].replaceAll("___", " "));
						map.name = name;
						map.type = type;
						map.builder = builder;
						map.save();
						sender.sendMessage("§c: §fZombieSurvival §c: §f맵 ID §e"+id+"의 정보가 업데이트 되었습니다.");
					}else sender.sendMessage("§c: §fZombieSurvival §c: §f맵 ID를 정확히 입력하세요.");
					return;
				}
			}else if(args[1].equalsIgnoreCase("spawn")) {
				if(args.length >= 3 && StringUtils.isNumber(args[2])) {
					int id = Integer.parseInt(args[2]);
					if(id <= GameUtils.MAPS.size()) {
						Map map = GameUtils.MAPS.get(id);
						Location loc = player.getLocation().getBlock().getLocation().add(0.5, 0, 0.5);
						map.spawn = loc;
						sender.sendMessage("§c: §fZombieSurvival §c: §f현재 위치{x=§e"+loc.getX()+"§f, y=§e"+loc.getY()+"§f, z=§e"+loc.getZ()+"§f}가 "+map.name+"§f 경기장의 스폰 장소로 설정되었습니다.");
						map.save();
					}else sender.sendMessage("§c: §fZombieSurvival §c: §f맵 ID를 정확히 입력하세요.");
					return;
				}
			}else if(args[1].equalsIgnoreCase("zombie")) {
				if(args.length >= 3 && StringUtils.isNumber(args[2])) {
					int id = Integer.parseInt(args[2]);
					if(id <= GameUtils.MAPS.size()) {
						Map map = GameUtils.MAPS.get(id);
						Location loc = player.getLocation().getBlock().getLocation().add(0.5, 0, 0.5);
						map.zombie = loc;
						sender.sendMessage("§c: §fZombieSurvival §c: §f현재 위치{x=§e"+loc.getX()+"§f, y=§e"+loc.getY()+"§f, z=§e"+loc.getZ()+"§f}가 "+map.name+"§f 경기장의 §n좀비§f 스폰 장소로 설정되었습니다.");
						map.save();
					}else sender.sendMessage("§c: §fZombieSurvival §c: §f맵 ID를 정확히 입력하세요.");
					return;
				}
			}
		}
		sender.sendMessage("§c: §fZombieSurvival §c:§f 명령어를 정확히 입력하세요.");
	}
	
	public static List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		ArrayList<String> arr = new ArrayList<>();
		if(args.length == 2) {
			for(String sub : SubCommand.main) {
				if(sub.toLowerCase().startsWith(args[1].toLowerCase())) arr.add(sub);
			}
		}else if(args.length == 3) {
			if(args[1].equalsIgnoreCase("info") || args[1].equalsIgnoreCase("remove")) {
				for(int i = 0; i < GameUtils.MAPS.size(); i++) {
					if(String.valueOf(i).toLowerCase().startsWith(args[2].toLowerCase())) arr.add(String.valueOf(i));
				}
			}
		}
		
//		Collections.sort(arr);
		return arr;
	}
	
	public static class SubCommand {
		public static List<String> main = Arrays.asList("spawn", "add", "zombie", "list", "update", "remove", "info");
	}
}
