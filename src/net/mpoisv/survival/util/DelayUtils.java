package net.mpoisv.survival.util;

import java.util.Timer;
import java.util.TimerTask;

public class DelayUtils implements Runnable {
	private final long delay;
	private final DelayRunnable run;
	public DelayUtils(long delay, DelayRunnable run) {
		this.delay = delay;
		this.run = run;
	}

	@Override
	public void run() {
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				run.run();
			}
			
		};
		
		timer.schedule(task, delay);
	}
	
	public interface DelayRunnable {
		public void run();
	}

}
