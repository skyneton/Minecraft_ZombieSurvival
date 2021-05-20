package net.mpoisv.survival.packet;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.entity.Player;

import io.netty.channel.ChannelFuture;
import net.minecraft.server.v1_12_R1.ServerConnection;
import net.mpoisv.survival.util.ClassUtils;
import net.mpoisv.survival.util.ThreadUtils;

public class MinecraftServer {
	@SuppressWarnings("unchecked")
	public MinecraftServer(Server server) {
		ServerConnection serverConnection = ((CraftServer) server).getServer().getServerConnection();
		List<ChannelFuture> list = null;
		try {
			list = (List<ChannelFuture>) ClassUtils.getPrivateFieldData(serverConnection.getClass().getDeclaredField("g"), serverConnection);;
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		if(list == null) return;
		
		for(Player player : Bukkit.getOnlinePlayers()) {
			ThreadUtils.submit(new PacketInject(player));
		}
		
		for(ChannelFuture cf : list) {
			ThreadUtils.submit(new ChannelPacketInject(cf));
		}
	}
}
