package net.mpoisv.survival.game;

import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.Bukkit;

import net.mpoisv.survival.util.ConfigUtils;
import net.mpoisv.survival.util.GameUtils;

public class StayWorker extends GameWorkerManager {

	@Override
	public void run() {
		init();
		
		timer = new Timer();
		task = new Running();
		timer.schedule(task, 100, 100);
	}
	
	@Override
	protected void init() {
		now = DuringGameType.STAY_LOBBY;
		GameUtils.bar.setVisible(true);
	}
	
	private class Running extends TimerTask {

		@Override
		public void run() {
			long LAST_TIMER = ConfigUtils.STAY_TIME * 10;
			TIMER++;
			if(!isRunnable) {
				Bukkit.broadcastMessage("§c: §fZombieSurvival §c: §f타이머가 중단되었습니다.");
				if(GameUtils.worker == StayWorker.this) {
					now = DuringGameType.NOT;
					GameUtils.worker = null;
				}
				timer.cancel();
				return;
			}
			if(isSkip && TIMER < LAST_TIMER - 30) {
				TIMER = LAST_TIMER - 30;
			}
			if(TIMER > LAST_TIMER) {
				Bukkit.broadcastMessage("§c: §fZombieSurvival §c: §f게임이 시작됩니다.");
				GameUtils.changeMode(new ZombieSpawnWorker());
				timer.cancel();
				return;
			}
			
			long temp = LAST_TIMER - TIMER;
			long realTime = temp / 10;
			if(temp % 10 != 0) realTime++;
			if((realTime == ConfigUtils.STAY_TIME || realTime % 30 == 0 || realTime == 15 || realTime == 10 || realTime <= 5) && temp % 10 == 0) {
				Bukkit.broadcastMessage("§c: §fZombieSurvival §c: §e"+realTime+"§f초 후 게임이 시작됩니다.");
			}
			
			map = GameUtils.MAPS.get(GameUtils.MAP_INDEX);
			GameUtils.bar.setTitle("다음 맵: §e"+map.name+" §f경기장  §e" + realTime + "§f초 후 게임이 시작됩니다.");
			if(temp <= 0) GameUtils.bar.setProgress(0);
			else {
				double progress = (double) temp / LAST_TIMER;
				if(progress > 1) progress = 1;
				GameUtils.bar.setProgress(progress);
			}
		}
		
	}
	
}
