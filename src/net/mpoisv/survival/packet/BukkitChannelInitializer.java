package net.mpoisv.survival.packet;

import java.lang.reflect.Method;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class BukkitChannelInitializer extends ChannelInitializer<SocketChannel> {
	private final ChannelInitializer<SocketChannel> original;
	private Method method;
	
	public BukkitChannelInitializer(ChannelInitializer<SocketChannel> oldInitializer) {
		original = oldInitializer;
		try {
			method = ChannelInitializer.class.getDeclaredMethod("initChannel", Channel.class);
			method.setAccessible(true);
		}catch (Exception e) { }
	}
	
	public ChannelInitializer<SocketChannel> getOriginal() {
		return original;
	}
	
	@Override
	protected void initChannel(SocketChannel channel) throws Exception {
		method.invoke(original, channel);
//		ThreadUtils.submit(new PacketInject(channel));
	}
}
