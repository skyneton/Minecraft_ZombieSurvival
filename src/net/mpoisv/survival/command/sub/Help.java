package net.mpoisv.survival.command.sub;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.mpoisv.survival.command.ZombieSurvivalCommand;

public class Help {
	public static void onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length >= 2) {
			sender.sendMessage("§c==================================");
			sender.sendMessage("/"+label+" "+args[1]);
			sender.sendMessage("");
			if(args[1].equalsIgnoreCase("help")) {
				sender.sendMessage("/"+label+" "+args[1]+" {sub}");
				sender.sendMessage("명령어를 확인합니다.");
			}else if(args[1].equalsIgnoreCase("reload")) {
				sender.sendMessage("Config 및 Arena등의 설정 파일을 다시 불러옵니다.");
			}else if(args[1].equalsIgnoreCase("vaccine")) {
				sender.sendMessage("/"+label+" "+args[1]+" {true/false}");
				sender.sendMessage("백신을 지급 할 것인지 설정합니다.");
			}else if(args[1].equalsIgnoreCase("auto")) {
				sender.sendMessage("/"+label+" "+args[1]+" {true/false}");
				sender.sendMessage("자동화를 사용할 것인지 설정합니다.");
			}else if(args[1].equalsIgnoreCase("hero")) {
				sender.sendMessage("/"+label+" "+args[1]+" {true/false}");
				sender.sendMessage("영웅을 스폰 할 것인지 설정합니다.");
			}else if(args[1].equalsIgnoreCase("time")) {
				sender.sendMessage("/"+label+" "+args[1]+" [option] {time}");
				sender.sendMessage("[option]의 시간을 변경합니다.");
			}else if(args[1].equalsIgnoreCase("min")) {
				sender.sendMessage("/"+label+" "+args[1]+" [option] {num}");
				sender.sendMessage("[option]의 필요 인원을 설정합니다.");
			}else if(args[1].equalsIgnoreCase("stop")) {
				sender.sendMessage("게임을 강제 종료합니다.");
			}else if(args[1].equalsIgnoreCase("start")) {
				sender.sendMessage("게임을 강제 시작합니다.");
			}else if(args[1].equalsIgnoreCase("type")) {
				sender.sendMessage("/"+label+" "+args[1]+" [player] {type}");
				sender.sendMessage("[player]의 타입(ex: 좀비)을 변경합니다.");
			}else if(args[1].equalsIgnoreCase("arena")) {
				sender.sendMessage("/"+label+" "+args[1]+" list");
				sender.sendMessage("/"+label+" "+args[1]+" info [id]");
				sender.sendMessage("/"+label+" "+args[1]+" zombie [id]");
				sender.sendMessage("/"+label+" "+args[1]+" remove [id]");
				sender.sendMessage("/"+label+" "+args[1]+" spawn [id]");
				sender.sendMessage("/"+label+" "+args[1]+" add [name] {type} {builder}");
				sender.sendMessage("/"+label+" "+args[1]+" update [id] [name] {type} {builder}");
			}else if(args[1].equalsIgnoreCase("lobby")) {
				sender.sendMessage("현재 위치를 로비로 지정합니다.");
			}else if(args[1].equalsIgnoreCase("skip")) {
				sender.sendMessage("타이머를 스킵합니다.");
			}
			sender.sendMessage("§c==================================");
			return;
		}
		sender.sendMessage("§c: §fZombieSurvival §c: §f/" + label + " help");
		sender.sendMessage("§c: §fZombieSurvival §c: §f/" + label + " reload");
		sender.sendMessage("§c: §fZombieSurvival §c: §f/" + label + " vaccine");
		sender.sendMessage("§c: §fZombieSurvival §c: §f/" + label + " auto");
		sender.sendMessage("§c: §fZombieSurvival §c: §f/" + label + " hero");
		sender.sendMessage("§c: §fZombieSurvival §c: §f/" + label + " time");
		sender.sendMessage("§c: §fZombieSurvival §c: §f/" + label + " min");
		sender.sendMessage("§c: §fZombieSurvival §c: §f/" + label + " stop");
		sender.sendMessage("§c: §fZombieSurvival §c: §f/" + label + " start");
		sender.sendMessage("§c: §fZombieSurvival §c: §f/" + label + " type");
		sender.sendMessage("§c: §fZombieSurvival §c: §f/" + label + " arena");
		sender.sendMessage("§c: §fZombieSurvival §c: §f/" + label + " lobby");
		sender.sendMessage("§c: §fZombieSurvival §c: §f/" + label + " skip");
	}
	
	public static List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		ArrayList<String> arr = new ArrayList<>();
		if(args.length == 2) {
			for(String sub : ZombieSurvivalCommand.SubCommand.main) {
				if(sub.toLowerCase().startsWith(args[1])) arr.add(sub);
			}
		}
		return arr;
	}
}
