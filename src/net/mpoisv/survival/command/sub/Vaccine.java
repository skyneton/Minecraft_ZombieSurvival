package net.mpoisv.survival.command.sub;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.mpoisv.survival.util.ConfigUtils;

public class Vaccine {
	public static void onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length == 1) {
			sender.sendMessage("§c: §fZombieSurvival §c:§f 백신 지급 상태: §c" + ConfigUtils.VACCINE);
			return;
		}
		else if(args[1].equalsIgnoreCase("true") || args[1].equals("false")) {
			ConfigUtils.VACCINE = Boolean.valueOf(args[1]);
			ConfigUtils.saveConfig();
			sender.sendMessage("§c: §fZombieSurvival §c:§f 백신 지급 상태가 변경되었습니다.");
		}
		sender.sendMessage("§c: §fZombieSurvival §c:§f 존재하지 않는 명령어입니다.");
	}
}
