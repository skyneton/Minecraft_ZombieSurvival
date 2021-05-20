package net.mpoisv.survival.command.sub;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.mpoisv.survival.game.GameWorkerManager;
import net.mpoisv.survival.game.GameWorkerManager.DuringGameType;
import net.mpoisv.survival.util.GameUtils;
import net.mpoisv.survival.util.GameUtils.PlayType;

public class Type {
	public static void onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(GameWorkerManager.now != DuringGameType.IN_GAME) {
			sender.sendMessage("§c: §fZombieSurvival §c: §f인게임 플레이 중에만 사용 가능 명령어입니다.");
			return;
		}
		if(args.length < 2) {
			sender.sendMessage("§c: §fZombieSurvival §c: §f명령어를 정확히 입력하세요.");
			return;
		}
		
		Player target = Bukkit.getPlayer(args[1]);
		if(target == null || !target.isOnline()) {
			sender.sendMessage("§c: §fZombieSurvival §c: §f접속하지 않은 플레이어 입니다.");
			return;
		}
		
		if(args.length >= 3) {
			PlayType type = null;
			switch(args[2].toLowerCase()) {
				case "zombie": {
					type = PlayType.Zombie;
					GameUtils.setZombie(target, false);
					break;
				}
				case "host": {
					type = PlayType.Host;
					GameUtils.setZombie(target, true);
					break;
				}
				case "survival": {
					type = PlayType.Survival;
					GameUtils.setSurvival(target, false);
					break;
				}
				case "hero": {
					type = PlayType.Hero;
					GameUtils.setSurvival(target, true);
					break;
				}
			}
			if(type == null) {
				sender.sendMessage("§c: §fZombieSurvival §c: §f존재하지 않는 타입입니다.");
				return;
			}
			sender.sendMessage("§c: §fZombieSurvival §c: §f"+target.getName()+"님이 §e"+type.toString()+"§f 으로 지정되었습니다.");
		}else {
			sender.sendMessage("§c: §fZombieSurvival §c: §f" + target.getName()+"님의 타입은 §e" + GameUtils.getPlayType(target).toString() +"§f 입니다.");
		}
	}
	
	public static List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		ArrayList<String> arr = new ArrayList<>();
		if(args.length == 2) {
			for(Player player : Bukkit.getOnlinePlayers()) {
				if(player.getName().toLowerCase().startsWith(args[1].toLowerCase())) arr.add(player.getName());
			}
		}else if(args.length == 3) {
			List<String> temp = Arrays.asList("zombie", "host", "survival", "hero");
			for(String s : temp) {
				if(s.toLowerCase().startsWith(args[1].toLowerCase())) arr.add(s);
			}
		}
		return arr;
	}
}
