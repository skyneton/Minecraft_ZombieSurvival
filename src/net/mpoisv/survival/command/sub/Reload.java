package net.mpoisv.survival.command.sub;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.mpoisv.survival.ZombieSurvival;
import net.mpoisv.survival.map.MapManager;
import net.mpoisv.survival.util.ConfigUtils;
import net.mpoisv.survival.util.GameUtils;

public class Reload {
	public static void onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		ZombieSurvival.instance.reloadConfig();
		ConfigUtils.loadConfig();
		ZombieSurvival.getLobby();
		
		MapManager.make();
		if(GameUtils.worker != null) GameUtils.worker.isRunnable = false;
		GameUtils.autoPlay();
		sender.sendMessage("§c: §fZombieSurvival §c: §f데이터가 리로드 되었습니다.");
	}
}
