package net.mpoisv.survival.command.sub;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.mpoisv.survival.util.GameUtils;

public class Start {
	public static void onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(GameUtils.start()) {
			sender.sendMessage("§c: §fZombieSurvival §c: §f게임이 시작되었습니다.");
		}else {
			sender.sendMessage("§c: §fZombieSurvival §c: §f게임을 시작할 수 없습니다.");
		}
	}
}
