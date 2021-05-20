package net.mpoisv.survival.command.sub;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.mpoisv.survival.util.ConfigUtils;
import net.mpoisv.survival.util.StringUtils;

public class Time {
	public static void onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length < 2) {
			sender.sendMessage("§c: §fZombieSurvival §c: §f명령어를 정확히 입력하세요.");
			return;
		}
		if(args.length >= 3 && !StringUtils.isNumber(args[2])) {
			sender.sendMessage("§c: §fZombieSurvival §c: §f시간은 정수 형태만 가능합니다.");
			return;
		}
		
		if(args[1].equalsIgnoreCase("vaccine")) {
			if(args.length == 2) {
				sender.sendMessage("§c: §fZombieSurvival §c: §f현재 백신 지급 시간은 §e" + ConfigUtils.VACCINE_TIME +"§f 입니다.");
				return;
			}
			long time = Long.parseLong(args[2]);
			ConfigUtils.VACCINE_TIME = time;
			ConfigUtils.saveConfig();
			sender.sendMessage("§c: §fZombieSurvival §c: §f백신 지급 시간이 §e" + ConfigUtils.VACCINE_TIME +"§f 로 변경되었습니다.");
		}else if(args[1].equalsIgnoreCase("location")) {
			if(args.length == 2) {
				sender.sendMessage("§c: §fZombieSurvival §c: §f현재 좌표 출력 시간은 §e" + ConfigUtils.LOCATION_TIME +"§f 입니다.");
				return;
			}
			long time = Long.parseLong(args[2]);
			ConfigUtils.LOCATION_TIME = time;
			ConfigUtils.saveConfig();
			sender.sendMessage("§c: §fZombieSurvival §c: §f좌표 출력 시간이 §e" + ConfigUtils.LOCATION_TIME +"§f 로 변경되었습니다.");
		}else if(args[1].equalsIgnoreCase("lobby")) {
			if(args.length == 2) {
				sender.sendMessage("§c: §fZombieSurvival §c: §f현재 시작 대기 시간은 §e" + ConfigUtils.STAY_TIME +"§f 입니다.");
				return;
			}
			long time = Long.parseLong(args[2]);
			ConfigUtils.STAY_TIME = time;
			ConfigUtils.saveConfig();
			sender.sendMessage("§c: §fZombieSurvival §c: §f시작 대기 시간이 §e" + ConfigUtils.STAY_TIME +"§f 로 변경되었습니다.");
		}else if(args[1].equalsIgnoreCase("spawn")) {
			if(args.length == 2) {
				sender.sendMessage("§c: §fZombieSurvival §c: §f현재 숙주 스폰 시간은 §e" + ConfigUtils.SPAWN_TIME +"§f 입니다.");
				return;
			}
			long time = Long.parseLong(args[2]);
			ConfigUtils.SPAWN_TIME = time;
			ConfigUtils.saveConfig();
			sender.sendMessage("§c: §fZombieSurvival §c: §f숙주 스폰 시간이 §e" + ConfigUtils.SPAWN_TIME +"§f 로 변경되었습니다.");
		}else if(args[1].equalsIgnoreCase("play")) {
			if(args.length == 2) {
				sender.sendMessage("§c: §fZombieSurvival §c: §f현재 게임 플레이 시간은 §e" + ConfigUtils.PLAY_TIME +"§f 입니다.");
				return;
			}
			long time = Long.parseLong(args[2]);
			ConfigUtils.PLAY_TIME = time;
			ConfigUtils.saveConfig();
			sender.sendMessage("§c: §fZombieSurvival §c: §f게임 플레이 시간이 §e" + ConfigUtils.PLAY_TIME +"§f 로 변경되었습니다.");
		}else sender.sendMessage("§c: §fZombieSurvival §c: §f명령어를 정확히 입력하세요.");
	}
	
	public static List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		ArrayList<String> arr = new ArrayList<>();
		if(args.length == 2) {
			List<String> temp = Arrays.asList("vaccine", "location", "lobby", "spawn", "play");
			for(String s : temp) {
				if(s.toLowerCase().startsWith(args[1].toLowerCase())) arr.add(s);
			}
		}
		return arr;
	}
}
