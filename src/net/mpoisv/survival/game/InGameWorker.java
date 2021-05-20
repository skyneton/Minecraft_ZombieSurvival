package net.mpoisv.survival.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import net.mpoisv.economy.util.MoneyUtils;
import net.mpoisv.survival.ZombieSurvival;
import net.mpoisv.survival.util.ConfigUtils;
import net.mpoisv.survival.util.GameUtils;

public class InGameWorker extends GameWorkerManager {

	@Override
	public void run() {
		init();
		
		timer = new Timer();
		task = new Running();
		timer.schedule(task, 100, 100);
	}
	
	@Override
	protected void init() {
		now = DuringGameType.IN_GAME; 
		GameUtils.bar.setVisible(false);
		map = GameUtils.MAPS.get(GameUtils.MAP_INDEX);
		map.spawn.getWorld().setTime(14000);
		raffleHostZombie();
	}
	
	private void raffleHostZombie() {
		int zombies = (int) Math.max(1, Bukkit.getOnlinePlayers().size() * 0.3);
		Random random = new Random();
		for(int i = 0; i < zombies; i++) {
			Player target = (Player)GameUtils.Survivals.keySet().toArray()[random.nextInt(GameUtils.Survivals.size())];
			GameUtils.setZombie(target, true);
			target.teleport(map.zombie);
		}
		
		for(Player player : GameUtils.Survivals.keySet()) {
			player.playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_AMBIENT, 1, 1);
		}
		Bukkit.broadcastMessage("§c[공지] 숙주 좀비가 탄생 하였습니다.");
	}
	
	private void stop(boolean isForce, boolean next) {
		for(Player player : Bukkit.getOnlinePlayers()) {
			player.setExp(0);
			player.setLevel(0);
			player.teleport(ZombieSurvival.lobby);
			GameUtils.resetPlayer(player);
		}
		map.spawn.getWorld().setTime(1000);
		if(next && ++GameUtils.MAP_INDEX >= GameUtils.MAPS.size()) GameUtils.MAP_INDEX = 0;
		if(GameUtils.MAPS.size() > 0) {
			GameUtils.bar.setTitle("다음 맵: §e"+GameUtils.MAPS.get(GameUtils.MAP_INDEX).name+" §f경기장");
			GameUtils.bar.setProgress(1);
			GameUtils.bar.setVisible(true);
		}
		if(GameUtils.worker == this) {
			now = DuringGameType.NOT;
			GameUtils.worker = null;
		}
		
		if(!isForce) {
			if(GameUtils.Survivals.size() > 0) {
				Bukkit.broadcastMessage("§c: §fZombieSurvival §c: §b생존자§f가 승리하였습니다.");
				ArrayList<Player> arr = new ArrayList<>(GameUtils.Survivals.keySet());
				Collections.sort(arr, (o1, o2) -> GameUtils.Survivals.get(o2).compareTo(GameUtils.Survivals.get(o1)));
				
				int kills = GameUtils.Survivals.get(arr.get(0));
				if(kills > 0) {
					StringBuffer buf = new StringBuffer();
					buf.append("§b"+arr.get(0).getName());
					Bukkit.broadcastMessage("§6=============== MVP ===============");
					for(Player player : arr) {
						if(arr.get(0) == player) continue;
						if(GameUtils.Survivals.get(player) == kills) {
							buf.append("§f, ");
							buf.append("§b" + player.getName());
							MoneyUtils.addMoney(player, 3);
						}else break;
					}
					Bukkit.broadcastMessage(buf.toString());
					Bukkit.broadcastMessage("kills: §e" + kills);
					Bukkit.broadcastMessage("§6=============== MVP ===============");
				}
				
				for(Player player : GameUtils.Survivals.keySet()) {
					MoneyUtils.addMoney(player, 2);
				}
			}else if(GameUtils.Zombies.size() > 0) {
				Bukkit.broadcastMessage("§c: §fZombieSurvival §c: §c좀비§f가 승리하였습니다.");
				ArrayList<Player> arr = new ArrayList<>(GameUtils.Zombies.keySet());
				Collections.sort(arr, (o1, o2) -> GameUtils.Zombies.get(o2).compareTo(GameUtils.Zombies.get(o1)));
				
				int kills = GameUtils.Zombies.get(arr.get(0));
				if(kills > 0) {
					StringBuffer buf = new StringBuffer();
					buf.append("§c"+arr.get(0).getName());
					Bukkit.broadcastMessage("§6=============== MVP ===============");
					for(Player player : arr) {
						if(arr.get(0) == player) continue;
						if(GameUtils.Zombies.get(player) == kills) {
							buf.append("§f, ");
							buf.append("§c" + player.getName());
							MoneyUtils.addMoney(player, 2);
						}else break;
					}
					Bukkit.broadcastMessage(buf.toString());
					Bukkit.broadcastMessage("kills: §e" + kills);
					Bukkit.broadcastMessage("§6=============== MVP ===============");
				}
				
				for(Player player : GameUtils.Zombies.keySet()) {
					if(GameUtils.HostZombies.contains(player)) MoneyUtils.addMoney(player, 2);
					else MoneyUtils.addMoney(player, 1);
				}
			}
		}
		
		if(isRunnable) {
			GameUtils.autoPlay();
		}
		
		timer.cancel();
	}
	
	private class Running extends TimerTask {

		@Override
		public void run() {
			long LAST_TIMER = ConfigUtils.PLAY_TIME * 10;
			TIMER++;
			if(!isRunnable) {
				Bukkit.broadcastMessage("§c: §fZombieSurvival §c: §f타이머가 중단되었습니다.");
				stop(true, false);
				return;
			}
			if(isSkip && TIMER < LAST_TIMER - 30) {
				TIMER = LAST_TIMER - 30;
			}
			if(TIMER > LAST_TIMER) {
				stop(false, true);
				return;
			}
			
			if(Bukkit.getOnlinePlayers().size() <= 0) {
				stop(true, true);
				return;
			}
			
			if(GameUtils.Survivals.size() <= 0) {
				stop(false, true);
				return;
			}
			
			if(GameUtils.Zombies.size() <= 0) {
				Bukkit.broadcastMessage("§c: §fZombieSurvival §c: §f좀비가 존재하지 않아 §c숙주§f를 §n재추첨§f합니다.");
				raffleHostZombie();
			}
			
			long temp = LAST_TIMER - TIMER;
			long realTime = temp/10;
			if(temp % 10 != 0) realTime++;
			if((realTime == ConfigUtils.PLAY_TIME || realTime % 30 == 0 || realTime == 15 || realTime == 10 || realTime <= 5) && temp % 10 == 0) {
				Bukkit.broadcastMessage("§c: §fZombieSurvival §c: §e"+temp/10+"§f초 후 게임이 종료됩니다.");
			}
			
			for(Player player : Bukkit.getOnlinePlayers()) {
				double progress = temp <= 0 ? 0 : (double) temp / LAST_TIMER;
				if(progress > 1) progress = 1;
				player.setExp((float) progress);
				player.setLevel((int) (realTime < 0 ? 0 : realTime));
			}
		}
		
	}

}
