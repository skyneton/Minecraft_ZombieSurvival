package net.mpoisv.survival.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.mojang.authlib.GameProfile;

import net.minecraft.server.v1_12_R1.EntityLiving;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.Packet;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_12_R1.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_12_R1.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_12_R1.PlayerConnection;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerInfo.PlayerInfoData;
import net.mpoisv.survival.packet.DelayPacketSender;

public class PacketUtils {
	public static final HashMap<Integer, Packet<?>> packets = new HashMap<>();
	
	public static void disguise(Entity target, EntityLiving entity) {
		entity.setLocation(target.getLocation().getX(), target.getLocation().getY(), target.getLocation().getZ(), target.getLocation().getYaw(), target.getLocation().getPitch());
		
		PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(target.getEntityId());
		PacketPlayOutSpawnEntityLiving spawn = new PacketPlayOutSpawnEntityLiving(entity);
		try {
			Field f = spawn.getClass().getDeclaredField("a");
			f.setAccessible(true);
			f.set(spawn, target.getEntityId());
		}catch(Exception e) {
			e.printStackTrace();
			return;
		}

		if(packets.containsKey(target.getEntityId())) {
			if(packets.get(target.getEntityId()).getClass().getSimpleName().equalsIgnoreCase("PacketPlayOutPlayerInfo") && target instanceof CraftPlayer) {
				PacketPlayOutPlayerInfo destroyInfo = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, ((CraftPlayer) target).getHandle());
				PacketPlayOutPlayerInfo info = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, ((CraftPlayer) target).getHandle());
				for(Player player : Bukkit.getOnlinePlayers()) {
					if(player == target) continue;
					((CraftPlayer) player).getHandle().playerConnection.sendPacket(destroyInfo);
					((CraftPlayer) player).getHandle().playerConnection.sendPacket(info);
				}
			}
			packets.replace(target.getEntityId(), spawn);
		}else packets.put(target.getEntityId(), spawn);
		
		for(Player player : Bukkit.getOnlinePlayers()) {
			if(player == target) continue;
			((CraftPlayer) player).getHandle().playerConnection.sendPacket(destroy);
			ThreadUtils.submit(new DelayPacketSender(5, player, spawn));
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void disguise(Entity target, EntityPlayer entity, String nametag) {
		entity.setLocation(target.getLocation().getX(), target.getLocation().getY(), target.getLocation().getZ(), target.getLocation().getYaw(), target.getLocation().getPitch());

		PacketPlayOutPlayerInfo destroyInfo = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, entity);
		PacketPlayOutPlayerInfo info = new PacketPlayOutPlayerInfo();
		PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(target.getEntityId());
		PacketPlayOutNamedEntitySpawn spawn = new PacketPlayOutNamedEntitySpawn(entity);
		try {
			Field f = spawn.getClass().getDeclaredField("a");
			f.setAccessible(true);
			f.set(spawn, target.getEntityId());
		}catch(Exception e) {
			e.printStackTrace();
			return;
		}
		
		GameProfile profile = new GameProfile(entity.getProfile().getId(), nametag);
		try {
			ClassUtils.setPrivateFieldData(profile.getClass().getDeclaredField("properties"), profile, ClassUtils.getPrivateFieldData(entity.getProfile().getClass().getDeclaredField("properties"), entity.getProfile()));
			ClassUtils.setPrivateFieldData(info.getClass().getDeclaredField("a"), info, EnumPlayerInfoAction.ADD_PLAYER);
			List<PlayerInfoData> list = (List<PlayerInfoData>) ClassUtils.getPrivateFieldData(info.getClass().getDeclaredField("b"), info);
			list.add(info.new PlayerInfoData(profile, entity.ping, entity.playerInteractManager.getGameMode(), entity.getPlayerListName()));
		}catch(Exception e) {}

		if(packets.containsKey(target.getEntityId())) {
			packets.replace(target.getEntityId(), info);
		}else packets.put(target.getEntityId(), info);
		
		for(Player player : Bukkit.getOnlinePlayers()) {
			if(player == target) continue;
			((CraftPlayer) player).getHandle().playerConnection.sendPacket(destroyInfo);
			((CraftPlayer) player).getHandle().playerConnection.sendPacket(info);
			((CraftPlayer) player).getHandle().playerConnection.sendPacket(destroy);
			((CraftPlayer) player).getHandle().playerConnection.sendPacket(spawn);
//			ThreadUtils.submit(new DelayPacketSender(10, player, spawn));
		}
	}
	
	public static void disguiseNotSave(Entity target, EntityLiving entity) {
		PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(target.getEntityId());
		PacketPlayOutSpawnEntityLiving spawn = new PacketPlayOutSpawnEntityLiving(entity);
		try {
			Field f = spawn.getClass().getDeclaredField("a");
			f.setAccessible(true);
			f.set(spawn, target.getEntityId());
		}catch(Exception e) {
			e.printStackTrace();
			return;
		}
		
		for(Player player : Bukkit.getOnlinePlayers()) {
			if(player == target) continue;
			((CraftPlayer) player).getHandle().playerConnection.sendPacket(destroy);
			ThreadUtils.submit(new DelayPacketSender(5, player, spawn));
		}
	}
	
	public static void sendPacket2Player(Player player) {
		PlayerConnection conn = ((CraftPlayer) player).getHandle().playerConnection;
		for(Entry<Integer, Packet<?>> packet : packets.entrySet()) {
			PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(packet.getKey());
			conn.sendPacket(destroy);
			ThreadUtils.submit(new DelayPacketSender(5, player, packet.getValue()));
		}
	}
	
	public static void clearDisguise(Entity entity) {
		if(packets.containsKey(entity.getEntityId())) {
			packets.remove(entity.getEntityId());
		}
		
		if(entity instanceof CraftEntity) {
			disguiseNotSave(entity, (EntityLiving) ((CraftEntity)entity).getHandle());
		}
	}
	
	public static void clearDisguise(Player player) {
		if(packets.containsKey(player.getEntityId())) {
			Packet<?> packet = packets.get(player.getEntityId());
			packets.remove(player.getEntityId());
			if(packet.getClass().getSimpleName().equalsIgnoreCase("PacketPlayOutPlayerInfo")) {
				PacketPlayOutPlayerInfo destroyInfo = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, ((CraftPlayer) player).getHandle());
				PacketPlayOutPlayerInfo info = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, ((CraftPlayer) player).getHandle());
				
				for(Player p : Bukkit.getOnlinePlayers()) {
					if(p == player) continue;
					((CraftPlayer) p).getHandle().playerConnection.sendPacket(destroyInfo);
					((CraftPlayer) p).getHandle().playerConnection.sendPacket(info);
				}
			}
		}
		
		PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(player.getEntityId());
		PacketPlayOutNamedEntitySpawn spawn = new PacketPlayOutNamedEntitySpawn(((CraftPlayer) player).getHandle());
		
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(player == p) continue;
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(destroy);
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(spawn);
		}
	}
}
