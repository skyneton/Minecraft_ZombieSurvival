package net.mpoisv.survival.command.sub;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.mpoisv.survival.game.GameWorkerManager;
import net.mpoisv.survival.game.GameWorkerManager.DuringGameType;
import net.mpoisv.survival.util.GameUtils;

public class Skip {
	public static void onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(GameWorkerManager.now == DuringGameType.NOT) {
			sender.sendMessage("§c: §fZombieSurvival §c:§f 게임이 플레이 중이 아닙니다.");
			return;
		}
		
		GameUtils.worker.isSkip = true;
		sender.sendMessage("§c: §fZombieSurvival §c:§f 스킵되었습니다.");
	}
}
