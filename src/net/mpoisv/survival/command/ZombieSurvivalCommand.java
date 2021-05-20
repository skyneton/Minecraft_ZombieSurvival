package net.mpoisv.survival.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.mpoisv.survival.command.sub.*;
import net.mpoisv.survival.game.InGameWorker;
import net.mpoisv.survival.util.ConfigUtils;

public class ZombieSurvivalCommand {
	public static void onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length > 0) {
			if(args[0].equalsIgnoreCase("help")) Help.onCommand(sender, cmd, label, args);
			else if(args[0].equalsIgnoreCase("reload")) Reload.onCommand(sender, cmd, label, args);
			else if(args[0].equals("vaccine")) Vaccine.onCommand(sender, cmd, label, args);
			else if(args[0].equals("auto")) Auto.onCommand(sender, cmd, label, args);
			else if(args[0].equals("hero")) Hero.onCommand(sender, cmd, label, args);
			else if(args[0].equals("time")) Time.onCommand(sender, cmd, label, args);
			else if(args[0].equals("min")) Min.onCommand(sender, cmd, label, args);
			else if(args[0].equals("stop")) Stop.onCommand(sender, cmd, label, args);
			else if(args[0].equals("start")) Start.onCommand(sender, cmd, label, args);
			else if(args[0].equals("type")) Type.onCommand(sender, cmd, label, args);
			else if(args[0].equals("arena")) Arena.onCommand(sender, cmd, label, args);
			else if(args[0].equals("lobby")) Lobby.onCommand(sender, cmd, label, args);
			else if(args[0].equals("skip")) Skip.onCommand(sender, cmd, label, args);
			else sender.sendMessage("§c: §fZombieSurvival §c:§f 존재하지 않는 명령어입니다.");
		}else {
			sender.sendMessage("§c============================");
			sender.sendMessage("자동화 상태: §c"+ConfigUtils.AUTO);
			sender.sendMessage("백신 상태: §c"+ConfigUtils.VACCINE);
			sender.sendMessage("영웅 상태: §c"+ConfigUtils.HERO);
			sender.sendMessage("");
			sender.sendMessage("로비 대기 시간: §e"+ConfigUtils.STAY_TIME);
			sender.sendMessage("숙주 스폰 대기 시간: §e"+ConfigUtils.SPAWN_TIME);
			sender.sendMessage("인게임 플레이 시간: §e"+ConfigUtils.PLAY_TIME);
			sender.sendMessage("자동화 최소 인원: §e"+ConfigUtils.AUTO_MIN);
			sender.sendMessage("");
			sender.sendMessage("타이머 상태: §c"+InGameWorker.now.toString());
			sender.sendMessage("§c============================");
			sender.sendMessage("§c/"+label+" help");
		}
	}
	
	public static List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		ArrayList<String> arr = new ArrayList<>();
		if(args.length == 1) {
			for(String sub : SubCommand.main) {
				if(sub.toLowerCase().startsWith(args[0].toLowerCase())) arr.add(sub);
			}
		}else if(args.length >= 2) {
			if(args[0].equalsIgnoreCase("help")) return Help.onTabComplete(sender, cmd, label, args);
			else if(args[0].equalsIgnoreCase("time")) return Time.onTabComplete(sender, cmd, label, args);
			else if(args[0].equalsIgnoreCase("min")) return Min.onTabComplete(sender, cmd, label, args);
			else if(args[0].equalsIgnoreCase("type")) return Type.onTabComplete(sender, cmd, label, args);
			else if(args[0].equalsIgnoreCase("arena")) return Arena.onTabComplete(sender, cmd, label, args);
			else if(args.length == 2) {
				if(args[0].equalsIgnoreCase("vaccine") || args[0].equalsIgnoreCase("auto") || args[0].equalsIgnoreCase("hero")) {
					for(String sub : SubCommand.choice) {
						if(sub.toLowerCase().startsWith(args[1].toLowerCase())) arr.add(sub);
					}
				}
			}
		}
		
//		Collections.sort(arr);
		return arr;
	}
	
	public static class SubCommand {
		public static List<String> main = Arrays.asList("help", "reload", "vaccine", "auto", "hero", "time", "min", "stop", "start", "type", "arena", "lobby", "skip");
		public static List<String> choice = Arrays.asList("true", "false");
	}
}
