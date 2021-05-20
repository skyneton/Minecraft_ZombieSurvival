package net.mpoisv.survival.command.sub;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.mpoisv.survival.game.GameWorkerManager;
import net.mpoisv.survival.game.GameWorkerManager.DuringGameType;
import net.mpoisv.survival.util.GameUtils;

public class Stop {
	public static void onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(GameWorkerManager.now == DuringGameType.NOT) {
			sender.sendMessage("§c: §fZombieSurvival §c: §f게임이 플레이 중이 아닙니다.");
			return;
		}
		
		GameUtils.worker.isRunnable = false;
		if(GameUtils.MAP_INDEX >= GameUtils.MAPS.size()) return;
		if(GameUtils.MAPS.size() > 0) {
			GameUtils.bar.setTitle("다음 맵: §e"+GameUtils.MAPS.get(GameUtils.MAP_INDEX).name+" §f경기장");
			GameUtils.bar.setProgress(1);
			GameUtils.bar.setVisible(true);
		}
	}
}
