package net.mpoisv.survival.packet;

import java.util.List;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import net.mpoisv.survival.util.ClassUtils;

public class ChannelPacketInject implements Runnable {
	private final ChannelFuture cf;
	public ChannelPacketInject(ChannelFuture cf) {
		this.cf = cf;
	}

	@SuppressWarnings({ "unchecked", "unused" })
	@Override
	public void run() {
		try {
			List<String> names = cf.channel().pipeline().names();
			ChannelHandler bootstrapAcceptor = null;
			for(String name : names) {
				ChannelHandler handler = cf.channel().pipeline().get(name);
				try {
					Object isSet = (ChannelInitializer<SocketChannel>) ClassUtils.getPrivateFieldData(handler.getClass().getDeclaredField("childHandler"), handler);
					bootstrapAcceptor = handler;
				}catch(Exception e) { }
			}
			
			if(bootstrapAcceptor == null)
				bootstrapAcceptor = cf.channel().pipeline().first();
			try {
				ChannelInitializer<SocketChannel> old = (ChannelInitializer<SocketChannel>) ClassUtils.getPrivateFieldData(bootstrapAcceptor.getClass().getDeclaredField("childHandler"), bootstrapAcceptor);
				ChannelInitializer<SocketChannel> newInit = new BukkitChannelInitializer(old);
				ClassUtils.setPrivateFieldData(bootstrapAcceptor.getClass().getDeclaredField("childHandler"), bootstrapAcceptor, newInit);
			}catch(Exception e) { }
		}catch(Exception e) { }
	}
	
}
