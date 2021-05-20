package net.mpoisv.survival.command.sub;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.mpoisv.survival.ZombieSurvival;

public class Lobby {
	public static void onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage("§c: §fZombieSurvival §c: §f플레이어만 사용이 가능합니다.");
			return;
		}
		
		Player player = (Player) sender;
		Location loc = player.getLocation().getBlock().getLocation().add(0.5, 0, 0.5);
		ZombieSurvival.setLobby(loc);
		sender.sendMessage("§c: §fZombieSurvival §c: §f현재 위치{x=§e"+loc.getX()+"§f, y=§e"+loc.getY()+"§f, z=§e"+loc.getZ()+"§f}가 로비로 설정되었습니다.");
	}
}
