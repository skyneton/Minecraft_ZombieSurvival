package net.mpoisv.survival.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

import net.minecraft.server.v1_12_R1.EntityBlaze;
import net.minecraft.server.v1_12_R1.EntityLiving;
import net.minecraft.server.v1_12_R1.EntityPigZombie;
import net.minecraft.server.v1_12_R1.EntityWitch;
import net.minecraft.server.v1_12_R1.EntityZombie;
import net.mpoisv.data.util.YamlUtils;
import net.mpoisv.survival.game.GameWorkerManager;
import net.mpoisv.survival.game.GameWorkerManager.DuringGameType;
import net.mpoisv.survival.game.StayWorker;
import net.mpoisv.survival.module.Map;
import net.mpoisv.survival.module.ZombieTypeModule;

public class GameUtils {
	public static ArrayList<Map> MAPS = new ArrayList<>();
	public static HashMap<Player, Integer> Survivals = new HashMap<>();
	public static ArrayList<Player> HeroSurvivals = new ArrayList<>();
	public static HashMap<Player, Integer> Zombies = new HashMap<>();
	public static ArrayList<Player> HostZombies = new ArrayList<>();
	public static int MAP_INDEX = 0;
	public static GameWorkerManager worker;
	
	public static BossBar bar = Bukkit.createBossBar("", BarColor.GREEN,BarStyle.SEGMENTED_10);
	
	public static boolean start() {
		if((worker == null || GameWorkerManager.now == DuringGameType.NOT) && MAPS.size() > 0) {
			Survivals.clear();
			HeroSurvivals.clear();
			Zombies.clear();
			HostZombies.clear();
			changeMode(new StayWorker());
			return true;
		}
		return false;
	}
	
	public static void stop() {
		bar.removeAll();
		if(worker != null) worker.isRunnable = false;
		ThreadUtils.stop();
	}
	
	public static void changeMode(GameWorkerManager worker) {
		GameUtils.worker = worker;
		ThreadUtils.submit(worker);
	}
	
	public static boolean autoPlay() {
		if(ConfigUtils.AUTO && Bukkit.getOnlinePlayers().size() >= ConfigUtils.AUTO_MIN)
			return start();
		return false;
	}
	
	@SuppressWarnings("deprecation")
	public static void setZombie(Player player, boolean host) {
		if(!Zombies.containsKey(player)) Zombies.put(player, 0);
		if(host && !HostZombies.contains(player)) HostZombies.add(player);
		if(Survivals.containsKey(player)) Survivals.remove(player);
		if(HeroSurvivals.contains(player)) HeroSurvivals.remove(player);
		String color;
		if(host) {
			color = "§4";
			player.sendTitle("당신은 §4숙주§f입니다.", "§3생존자§f를 찾아 감염시키세요!");
		}else {
			color = "§c";
		}
		
		ItemStack item = new ItemStack(Material.NETHER_STALK);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§c좀비 손");
		meta.setLore(Arrays.asList("§f우클릭시 스킬을 사용합니다."));
		item.setItemMeta(meta);
		
		player.getInventory().addItem(item);
		
		YamlConfiguration yml = YamlUtils.getYamlConfiguration(player);
		ZombieTypeModule module;
		if(yml == null) {
			module = new ZombieTypeModule();
			YamlUtils.createFileNotExist(player);
			yml = YamlUtils.getYamlConfiguration(player);
		}
		else module = ZombieTypeModule.get(yml, "zombies");
		
		if(module == null) module = new ZombieTypeModule();
		
		boolean change = false;
		if(module.getZombieTypes().size() <= 0) {
			module.getZombieTypes().add(ZombieType.DEFAULT);
			change = true;
		}
		if(module.select != ZombieSelect.AUTO && !module.getZombieTypes().contains(module.select.toParse())) {
			module.select = ZombieSelect.AUTO;
			change = true;
		}
		
		if(change && yml != null) {
			module.set(yml, "zombies");
			YamlUtils.saveYamlConfiguration(player, yml);
		}
		
		ZombieType type;
		if(module.select == ZombieSelect.AUTO)
			 type = module.getZombieTypes().get(new Random().nextInt(module.getZombieTypes().size()));
		else {
			type = module.select.toParse();
		}
		
		String prefix = "";
		switch(type) {
		case DEFAULT:
			prefix = "[일반] ";
			break;
		case HEAVY:
			prefix = "[헤비] ";
			ThreadUtils.doBukkitThread(() -> {
				player.setWalkSpeed(0.12f);
			});
			break;
		case LIGHT:
			prefix = "[라이트] ";
			ThreadUtils.doBukkitThread(() -> {
				player.setWalkSpeed(0.28f);
			});
			break;
		case VOODOO:
			prefix = "[부두] ";
			break;
		}
		
		player.setPlayerListName(color+prefix+player.getName());
		PacketUtils.disguise(player, getZombieEntity(player));
	}
	
	public static EntityLiving getZombieEntity(Player player) {
		String temp = player.getPlayerListName().substring(2);
		if(temp.startsWith("[일반] ")) {
			return new EntityZombie(((CraftWorld) player.getWorld()).getHandle());
		}
		if(temp.startsWith("[헤비] ")) {
			return new EntityPigZombie(((CraftWorld) player.getWorld()).getHandle());
		}
		if(temp.startsWith("[라이트] ")) {
			return new EntityBlaze(((CraftWorld) player.getWorld()).getHandle());
		}
		if(temp.startsWith("[부두] ")) {
			return new EntityWitch(((CraftWorld) player.getWorld()).getHandle());
		}
		
		return null;
	}
	
	public static ZombieType getZombieType(Player player) {
		String temp = player.getPlayerListName().substring(2);
		if(temp.startsWith("[일반] ")) {
			return ZombieType.DEFAULT;
		}
		if(temp.startsWith("[헤비] ")) {
			return ZombieType.HEAVY;
		}
		if(temp.startsWith("[라이트] ")) {
			return ZombieType.LIGHT;
		}
		if(temp.startsWith("[부두] ")) {
			return ZombieType.VOODOO;
		}
		return null;
	}
	
	public static void setSurvival(Player player, boolean hero) {
		if(!Survivals.containsKey(player)) Survivals.put(player, 0);
		if(hero && !HeroSurvivals.contains(player)) HeroSurvivals.add(player);
		if(Zombies.containsKey(player)) Zombies.remove(player);
		if(HostZombies.contains(player)) HostZombies.remove(player);
		if(hero)
			player.setPlayerListName("§3"+player.getName());
		else
			player.setPlayerListName("§b"+player.getName());
		
		PacketUtils.disguise(player, ((CraftPlayer) player).getHandle(), "");
	}
	
	@SuppressWarnings("deprecation")
	public static void resetPlayer(Player player) {
		player.resetMaxHealth();
		player.setFoodLevel(20);
		player.setPlayerListName(player.getName());
		
		player.getInventory().setHelmet(null);
		player.getInventory().setChestplate(null);
		player.getInventory().setLeggings(null);
		player.getInventory().setBoots(null);
		
		for(PotionEffect effect : player.getActivePotionEffects()) player.removePotionEffect(effect.getType());
		PacketUtils.clearDisguise(player);
		
		player.getInventory().remove(Material.NETHER_STALK);
		ThreadUtils.doBukkitThread(() -> {
			player.setWalkSpeed(0.2f);
		});
	}
	
	public static PlayType getPlayType(Player player) {
		if(HostZombies.contains(player)) return PlayType.Host;
		if(Zombies.containsKey(player)) return PlayType.Zombie;
		if(HeroSurvivals.contains(player)) return PlayType.Hero;
		return PlayType.Survival;
	}
	
	public static enum PlayType {
		Zombie, Host, Survival, Hero;
		public static String[] stringValues() {
			PlayType[] v = values();
			String[] arr = new String[v.length];
			for(int i = 0; i < v.length; i++) {
				arr[i] = v[i].toString();
			}
			
			return arr;
		}
	}
}
