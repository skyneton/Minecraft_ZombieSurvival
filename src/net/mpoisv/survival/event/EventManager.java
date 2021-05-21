package net.mpoisv.survival.event;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
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
		event.getPlayer().setHealthScale(20);
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
			event.getPlayer().setNoDamageTicks(0);
			EntityLiving entity = GameUtils.getZombieEntity(event.getPlayer());
			if(entity == null) return;
			ThreadUtils.submit(new DelayUtils(5, () -> {
				if(GameWorkerManager.now == DuringGameType.IN_GAME)
					PacketUtils.disguise(event.getPlayer(), entity);
				else GameUtils.resetPlayer(event.getPlayer());
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
			long godTime = System.currentTimeMillis() - GameUtils.zombieDatas.get(attacker).deathTime;
			if(godTime < 1500) {
				attacker.sendMessage("§c좀비로 감염후 §n"+String.format("%.2d", (double)godTime/1000) +"초§c후 타격이 가능합니다.");
				event.setCancelled(true);
				return;
			}
			if(attacker.getItemInHand().getType() != Material.NETHER_STALK) return;
			event.setDamage(victim.getMaxHealth() + 1);
		}
	}
	
	@EventHandler
	public void onFoodChange(FoodLevelChangeEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if(!(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) || event.getItem() == null) return;
		if(GameWorkerManager.now == DuringGameType.IN_GAME && GameUtils.Zombies.containsKey(event.getPlayer())) {
			if(event.getItem().getType() != Material.NETHER_STALK) return;
			long time = GameUtils.getCooltime(event.getPlayer()) * 1000 - (System.currentTimeMillis() - GameUtils.zombieDatas.get(event.getPlayer()).skillTime);
			if(time > 0) {
				event.getPlayer().sendMessage("§e스킬 쿨타임 중... §f§n"+String.format("%.2f", (double)time/1000) +"초§f 후 재사용이 가능합니다.");
				return;
			}
			event.getPlayer().sendMessage("§e스킬§f을 사용하였습니다.");
			
			GameUtils.zombieDatas.get(event.getPlayer()).skillTime = System.currentTimeMillis();
			switch(GameUtils.getZombieType(event.getPlayer())) {
			case DEFAULT: {
				event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 7 * 20, 3, false, false));
				break;
			}
			case HEAVY: {
				event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 5 * 20, 200, false, false));
				event.getPlayer().setNoDamageTicks(5 * 20);
				break;
			}
			case LIGHT: {
				event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 5 * 20, 2, true, true));
				event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 5 * 20, 0, true, true));
				break;
			}
			case VOODOO: {
				List<Entity> entities = event.getPlayer().getNearbyEntities(5, 5, 5);
				event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 4 * 20, 2, false, false));
				for(Entity entity : entities) {
					if(entity instanceof Player) {
						Player temp = (Player) entity;
						if(GameUtils.Zombies.containsKey(temp)) {
							temp.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 4 * 20, 1, false, false));
							
							temp.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§6부두술사§7에 의해 §c재생§7효과를 받습니다."));
						}
					}
				}
				break;
			}
			}
		}
	}
}
