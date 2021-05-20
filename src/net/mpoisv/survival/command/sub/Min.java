package net.mpoisv.survival.command.sub;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.mpoisv.survival.util.ConfigUtils;
import net.mpoisv.survival.util.GameUtils;
import net.mpoisv.survival.util.StringUtils;

public class Min {
	public static void onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length < 2) {
			sender.sendMessage("§c: §fZombieSurvival §c: §f명령어를 정확히 입력하세요.");
			return;
		}
		if(args.length >= 3 && !StringUtils.isNumber(args[2])) {
			sender.sendMessage("§c: §fZombieSurvival §c: §f인원은 정수 형태만 가능합니다.");
			return;
		}
		
		if(args[1].equalsIgnoreCase("vaccine")) {
			if(args.length == 2) {
				sender.sendMessage("§c: §fZombieSurvival §c: §f현재 백신 최소 인원은 §e" + ConfigUtils.VACCINE_MIN+"§f 입니다.");
				return;
			}
			int num = Integer.parseInt(args[2]);
			ConfigUtils.VACCINE_MIN = num;
			ConfigUtils.saveConfig();
			sender.sendMessage("§c: §fZombieSurvival §c: §f백신 최소 인원이 §e" + ConfigUtils.VACCINE_MIN +"§f 로 변경되었습니다.");
		}else if(args[1].equalsIgnoreCase("hero")) {
			if(args.length == 2) {
				sender.sendMessage("§c: §fZombieSurvival §c: §f현재 영웅 최소 인원은 §e" + ConfigUtils.HERO_MIN+"§f 입니다.");
				return;
			}
			int num = Integer.parseInt(args[2]);
			ConfigUtils.HERO_MIN = num;
			ConfigUtils.saveConfig();
			sender.sendMessage("§c: §fZombieSurvival §c: §f영웅 최소 인원이 §e" + ConfigUtils.HERO_MIN +"§f 로 변경되었습니다.");
		}else if(args[1].equalsIgnoreCase("auto")) {
			if(args.length == 2) {
				sender.sendMessage("§c: §fZombieSurvival §c: §f현재 자동화 최소 인원은 §e" + ConfigUtils.AUTO_MIN+"§f 입니다.");
				return;
			}
			int num = Integer.parseInt(args[2]);
			ConfigUtils.AUTO_MIN = num;
			ConfigUtils.saveConfig();
			sender.sendMessage("§c: §fZombieSurvival §c: §f자동화 최소 인원이 §e" + ConfigUtils.AUTO_MIN +"§f 로 변경되었습니다.");
			GameUtils.autoPlay();
		}else {
			sender.sendMessage("§c: §fZombieSurvival §c: §f명령어를 정확히 입력하세요.");
		}
	}
	
	public static List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		ArrayList<String> arr = new ArrayList<>();
		if(args.length == 2) {
			List<String> temp = Arrays.asList("vaccine", "hero", "auto");
			for(String s : temp) {
				if(s.toLowerCase().startsWith(args[1].toLowerCase())) arr.add(s);
			}
		}
		return arr;
	}
}
