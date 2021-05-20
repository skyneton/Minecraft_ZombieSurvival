package net.mpoisv.survival.event;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import net.minecraft.server.v1_12_R1.EntityLiving;
import net.mpoisv.survival.ZombieSurvival;
import net.mpoisv.survival.game.GameWorkerManager;
import net.mpoisv.survival.game.InGameWorker;
import net.mpoisv.survival.game.GameWorkerManager.DuringGameType;
import net.mpoisv.survival.packet.PacketInject;
import net.mpoisv.survival.util.ConfigUtils;
import net.mpoisv.survival.util.DelayUtils;
import net.mpoisv.survival.util.GameUtils;
import net.mpoisv.survival.util.PacketUtils;
import net.mpoisv.survival.util.ThreadUtils;

public class EventManager implements Listener {
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		ThreadUtils.submit(new PacketInject(event.getPlayer()));
		GameUtils.bar.addPlayer(event.getPlayer());
		GameUtils.autoPlay();
		GameUtils.resetPlayer(event.getPlayer());
		if(InGameWorker.now == null) {
			event.getPlayer().teleport(ZombieSurvival.lobby);
		}else {
			switch(InGameWorker.now) {
				case IN_GAME:
					GameUtils.setZombie(event.getPlayer(), false);
				case ZOMBIE_SPAWN:
					event.getPlayer().teleport(GameUtils.worker.map.spawn);
					break;
				default:
					event.getPlayer().teleport(ZombieSurvival.lobby);
			}
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		if((GameWorkerManager.now == DuringGameType.STAY_LOBBY || GameWorkerManager.now == DuringGameType.ZOMBIE_SPAWN) && !GameUtils.worker.isForced) {
			if(Bukkit.getOnlinePlayers().size() -1 < ConfigUtils.AUTO_MIN) {
				GameUtils.worker.isRunnable = false;
				Bukkit.broadcastMessage("§c: §fZombieSurvival §c: §f인원이 부족하여 타이머가 중지되었습니다.");
				
				if(GameUtils.MAP_INDEX >= GameUtils.MAPS.size()) return;
				if(GameUtils.MAPS.size() > 0) {
					GameUtils.bar.setTitle("다음 맵: §e"+GameUtils.MAPS.get(GameUtils.MAP_INDEX).name+" §f경기장");
					GameUtils.bar.setProgress(1);
					GameUtils.bar.setVisible(true);
				}
			}
		}
		
		if(GameUtils.Survivals.containsKey(event.getPlayer()))
			GameUtils.Survivals.remove(event.getPlayer());
		if(GameUtils.Zombies.containsKey(event.getPlayer()))
			GameUtils.Zombies.remove(event.getPlayer());
		if(PacketUtils.packets.containsKey(event.getPlayer().getEntityId())) {
			PacketUtils.packets.remove(event.getPlayer().getEntityId());
		}
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		event.setKeepInventory(true);
		event.setKeepLevel(true);
		event.setDroppedExp(0);
		event.getDrops().clear();
		if(ConfigUtils.KILL_LOG) {
			if(event.getEntity().getKiller() != null && GameWorkerManager.now == DuringGameType.IN_GAME) {
				if(GameUtils.Survivals.containsKey(event.getEntity().getKiller())) {
					event.setDeathMessage("§7☠ "+event.getEntity().getKiller().getPlayerListName()+" §f▄︻┻┳═=── "+event.getEntity().getPlayerListName());
					GameUtils.Survivals.replace(event.getEntity().getKiller(), GameUtils.Survivals.get(event.getEntity().getKiller()) + 1);
				}else {
					event.setDeathMessage("§7☠ "+event.getEntity().getKiller().getPlayerListName()+" ▶▶▶ "+event.getEntity().getPlayerListName());
					GameUtils.setZombie(event.getEntity(), false);
					GameUtils.Zombies.replace(event.getEntity().getKiller(), GameUtils.Zombies.get(event.getEntity().getKiller()) + 1);
				}
			}
		}else event.setDeathMessage(null);
		Location loc = event.getEntity().getLocation();
		if(GameWorkerManager.now == DuringGameType.IN_GAME && (GameUtils.worker.map.zombie.getWorld() != loc.getWorld() || GameUtils.worker.map.zombie.distance(loc) >= 700)) loc = GameUtils.worker.map.zombie;
		event.getEntity().spigot().respawn();
		if(GameWorkerManager.now == DuringGameType.IN_GAME)
			event.getEntity().teleport(loc);
		else event.getEntity().teleport(ZombieSurvival.lobby);
	}
	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent event) {
		if(GameWorkerManager.now == DuringGameType.IN_GAME && GameUtils.Zombies.containsKey(event.getPlayer())) {
			EntityLiving entity = GameUtils.getZombieEntity(event.getPlayer());
			if(entity == null) return;
			ThreadUtils.submit(new DelayUtils(5, () -> {
				if(GameWorkerManager.now == DuringGameType.IN_GAME)
					PacketUtils.disguise(event.getPlayer(), entity);
			}));
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event) {
		if(!(event.getDamager() instanceof Player && event.getEntity() instanceof Player)) return;
		Player victim = (Player) event.getEntity();
		Player attacker = (Player) event.getDamager();
		
		if(GameWorkerManager.now == DuringGameType.IN_GAME && GameUtils.Zombies.containsKey(attacker)) {
			if(attacker.getItemInHand().getType() != Material.NETHER_STALK) return;
			event.setDamage(victim.getMaxHealth() + 1);
		}
	}
	
	@EventHandler
	public void onFoodChange(FoodLevelChangeEvent event) {
		event.setCancelled(true);
	}
}
