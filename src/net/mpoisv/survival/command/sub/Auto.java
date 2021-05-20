package net.mpoisv.survival.command.sub;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.mpoisv.survival.util.ConfigUtils;
import net.mpoisv.survival.util.GameUtils;

public class Auto {
	public static void onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length == 1) {
			sender.sendMessage("§c: §fZombieSurvival §c:§f 자동화 상태: §c" + ConfigUtils.AUTO);
			return;
		}
		else if(args[1].equalsIgnoreCase("true") || args[1].equals("false")) {
			ConfigUtils.AUTO = Boolean.valueOf(args[1]);
			ConfigUtils.saveConfig();
			sender.sendMessage("§c: §fZombieSurvival §c:§f 자동화 상태가 변경되었습니다.");
			GameUtils.autoPlay();
			return;
		}
		sender.sendMessage("§c: §fZombieSurvival §c:§f 명령어를 정확히 입력하세요.");
	}
}
