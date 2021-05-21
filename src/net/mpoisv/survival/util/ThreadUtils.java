package net.mpoisv.survival.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.bukkit.Bukkit;

import net.mpoisv.survival.ZombieSurvival;

public class ThreadUtils {
	private static ExecutorService es = Executors.newFixedThreadPool(1);
	
	public static void stop() {
		es.shutdown();
		while(!es.isShutdown());
	}
	
	public static void submit(Runnable run) {
		es.submit(run);
	}
	
	public static void doBukkitThread(ThreadRunnable r) {
		Bukkit.getScheduler().runTask(ZombieSurvival.instance, new Runnable() {
			@Override
			public void run() {
				try {
					r.run();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static interface ThreadRunnable {
		public void run();
	}
}
