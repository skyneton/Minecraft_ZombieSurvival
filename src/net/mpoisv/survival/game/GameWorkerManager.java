package net.mpoisv.survival.game;

import java.util.Timer;
import java.util.TimerTask;

import net.mpoisv.survival.module.Map;

public abstract class GameWorkerManager implements Runnable {
	public boolean isForced = false;
	public boolean isRunnable = true;
	public boolean isSkip = false;
	public static DuringGameType now = DuringGameType.NOT;
	protected long TIMER = -1;
	protected Timer timer;
	protected TimerTask task;
	
	public Map map;
	
	protected void init() {}
	
	public static enum DuringGameType {
		NOT, STAY_LOBBY, ZOMBIE_SPAWN, IN_GAME
	}
}
