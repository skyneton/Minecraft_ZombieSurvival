package net.mpoisv.survival.packet;

import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_12_R1.Packet;

public class DelayPacketSender implements Runnable {
	private final Player player;
	private final Packet<?> packet;
	private final long delay;
	public DelayPacketSender(long delay, Player player, Packet<?> packet) {
		this.player = player;
		this.packet = packet;
		this.delay = delay;
	}

	@Override
	public void run() {
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				if(player != null && player.isOnline())
					((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
			}
		};
		
		timer.schedule(task, delay);
	}
}
