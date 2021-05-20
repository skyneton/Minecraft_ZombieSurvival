package net.mpoisv.survival.game;

import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.mpoisv.survival.ZombieSurvival;
import net.mpoisv.survival.util.ConfigUtils;
import net.mpoisv.survival.util.GameUtils;

public class ZombieSpawnWorker extends GameWorkerManager {

	@Override
	public void run() {
		init();
		
		timer = new Timer();
		task = new Running();
		timer.schedule(task, 100, 100);
	}
	
	@Override
	protected void init() {
		now = DuringGameType.ZOMBIE_SPAWN;
		map = GameUtils.MAPS.get(GameUtils.MAP_INDEX);
		
		for(Player player : Bukkit.getOnlinePlayers()) {
			player.teleport(map.spawn);
			GameUtils.setSurvival(player, false);
		}

		Bukkit.broadcastMessage("\n§6=========================\n");
		Bukkit.broadcastMessage(" 맵 이름: §e"+map.name);
		if(map.builder != null)
			Bukkit.broadcastMessage(" 맵 제작자: §e"+map.builder);
		Bukkit.broadcastMessage(" 맵 ID: §e"+(GameUtils.MAP_INDEX + 1));
		Bukkit.broadcastMessage("\n 종류: §e" + map.type.toString());
		Bukkit.broadcastMessage("\n§6=========================\n");
	}
	
	private void stop() {
		for(Player player : Bukkit.getOnlinePlayers()) {
			player.teleport(ZombieSurvival.lobby);
		}
		if(++GameUtils.MAP_INDEX >= GameUtils.MAPS.size()) GameUtils.MAP_INDEX = 0;
		if(GameUtils.MAPS.size() > 0) {
			GameUtils.bar.setTitle("다음 맵: §e"+GameUtils.MAPS.get(GameUtils.MAP_INDEX).name+" §f경기장");
			GameUtils.bar.setProgress(1);
			GameUtils.bar.setVisible(true);
		}else GameUtils.bar.setVisible(false);
		if(GameUtils.worker == this) {
			now = DuringGameType.NOT;
			GameUtils.worker = null;
		}
		
		if(isRunnable) {
			GameUtils.autoPlay();
		}
	}
	
	private class Running extends TimerTask {

		@Override
		public void run() {
			long LAST_TIMER = ConfigUtils.SPAWN_TIME * 10;
			TIMER++;
			if(!isRunnable) {
				Bukkit.broadcastMessage("§c: §fZombieSurvival §c: §f타이머가 중단되었습니다.");
				stop();
				timer.cancel();
				return;
			}
			if(isSkip && TIMER < LAST_TIMER - 30) {
				TIMER = LAST_TIMER - 30;
			}
			if(TIMER > LAST_TIMER) {
				GameUtils.changeMode(new InGameWorker());
				timer.cancel();
				return;
			}
			
			long temp = LAST_TIMER - TIMER;
			long realTime = temp/10;
			if(temp % 10 != 0) realTime++;
			if((realTime == ConfigUtils.SPAWN_TIME || realTime % 30 == 0 || realTime == 15 || realTime == 10 || realTime <= 5) && temp % 10 == 0) {
				Bukkit.broadcastMessage("§c: §fZombieSurvival §c: §e"+realTime+"§f초 후 숙주가 스폰됩니다.");
			}
			
			GameUtils.bar.setTitle("이번 맵: §e"+map.name+" §f경기장  §e"+realTime+"§f초 후 숙주가 스폰됩니다.");
			if(temp <= 0) GameUtils.bar.setProgress(0);
			else {
				double progress = (double) temp / LAST_TIMER;
				if(progress > 1) progress = 1;
				GameUtils.bar.setProgress(progress);
			}
		}
		
	}

}
