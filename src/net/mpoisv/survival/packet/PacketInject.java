package net.mpoisv.survival.packet;

import java.lang.reflect.Field;
import java.util.HashMap;

import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.minecraft.server.v1_12_R1.Packet;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_12_R1.PlayerConnection;
import net.mpoisv.survival.util.ClassUtils;
import net.mpoisv.survival.util.PacketUtils;
import net.mpoisv.survival.util.ThreadUtils;

public class PacketInject implements Runnable {
	private final Channel channel;
	private final Player player;
	private final PlayerConnection conn;
//	public PacketInject(Channel channel) {
//		this.channel = channel;
//		
//		if(channel.pipeline().get("ZombieSurvivalPacketInject") != null)
//			channel.pipeline().remove("ZombieSurvivalPacketInject");
//	}
	private HashMap<Integer, Long> beforeSended = new HashMap<>();
	
	public PacketInject(Player player) {
		this.player = player;
		this.conn = ((CraftPlayer) this.player).getHandle().playerConnection;
		this.channel = conn.networkManager.channel;
	}
	
	@Override
	public void run() {
		if(channel == null) return;
		channel.pipeline().addBefore("packet_handler", "ZombieSurvivalPacketInject", new ChannelDuplexHandler() {
			@Override
			public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
				super.write(ctx, msg, promise);
				packetPlayOut(msg);
			}
		});
	}
	
	public void packetPlayOut(Object msg) {
		if(!msg.getClass().getSimpleName().equalsIgnoreCase("PacketPlayOutNamedEntitySpawn") && !msg.getClass().getSimpleName().contains("PacketPlayOutSpawnEntityLiving")) return;
		Packet<?> packet = null;
		int entityId = 0;
		try {
			Field f = msg.getClass().getDeclaredField("a");
			f.setAccessible(true);
			entityId = f.getInt(msg);
			packet = PacketUtils.packets.get(entityId);
		}catch(Exception e) {}
		if(packet == null || packet == msg) return;
		
		if(msg.getClass().getSimpleName().equalsIgnoreCase("PacketPlayOutNamedEntitySpawn")) {
			if(packet.getClass().getSimpleName().equalsIgnoreCase("PacketPlayOutPlayerInfo")) {
				if(beforeSended.containsKey(entityId) && System.currentTimeMillis() - beforeSended.get(entityId) <= 50) {
					beforeSended.remove(entityId);
					return;
				}
				
				beforeSended.put(entityId, System.currentTimeMillis());
				
				ThreadUtils.submit(new DelayPacketSender(10, player, new PacketPlayOutEntityDestroy(entityId)));
				ThreadUtils.submit(new DelayPacketSender(12, player, packet));
				ThreadUtils.submit(new DelayPacketSender(14, player, (Packet<?>) msg));
				return;
			}
			try {
				double x = (double) ClassUtils.getPrivateFieldData(msg.getClass().getDeclaredField("c"), msg);
				double y = (double) ClassUtils.getPrivateFieldData(msg.getClass().getDeclaredField("d"), msg);
				double z = (double) ClassUtils.getPrivateFieldData(msg.getClass().getDeclaredField("e"), msg);
				byte yaw = (byte) ClassUtils.getPrivateFieldData(msg.getClass().getDeclaredField("f"), msg);
				byte pitch = (byte) ClassUtils.getPrivateFieldData(msg.getClass().getDeclaredField("g"), msg);

				ClassUtils.setPrivateFieldData(packet.getClass().getDeclaredField("d"), msg, x);
				ClassUtils.setPrivateFieldData(packet.getClass().getDeclaredField("e"), msg, y);
				ClassUtils.setPrivateFieldData(packet.getClass().getDeclaredField("f"), msg, z);
				ClassUtils.setPrivateFieldData(packet.getClass().getDeclaredField("j"), msg, yaw);
				ClassUtils.setPrivateFieldData(packet.getClass().getDeclaredField("k"), msg, pitch);
			}catch(Exception e) {}
		}
		
		if(packet != msg) {
			ThreadUtils.submit(new DelayPacketSender(10, player, new PacketPlayOutEntityDestroy(entityId)));
			ThreadUtils.submit(new DelayPacketSender(20, player, packet));
		}
	}
}
