package net.mpoisv.survival.util;

import net.mpoisv.survival.ZombieSurvival;

public class ConfigUtils {
	public static boolean VACCINE = false;
	public static long VACCINE_TIME = 100;
	public static double VACCINE_PERCENT = 0.2;
	public static int VACCINE_MIN = 5;
	public static boolean HERO = false;
	public static int HERO_MIN = 4;
	public static boolean AUTO = true;
	public static long PLAY_TIME = 300;
	public static long STAY_TIME = 60;
	public static long SPAWN_TIME = 20;
	public static int AUTO_MIN = 3;
	public static boolean KILL_LOG = false;
	public static boolean LOCATION_LOG = false;
	public static long LOCATION_TIME = 100;
	
	public static void loadConfig() {
		ConfigUtils.VACCINE = ZombieSurvival.instance.getConfig().getBoolean("백신 사용");
		ConfigUtils.VACCINE_TIME = ZombieSurvival.instance.getConfig().getLong("백신 지급 시간");
		ConfigUtils.VACCINE_PERCENT = ZombieSurvival.instance.getConfig().getDouble("백신 지급 유저");
		ConfigUtils.VACCINE_MIN = ZombieSurvival.instance.getConfig().getInt("백신 최소 인원");
		ConfigUtils.HERO = ZombieSurvival.instance.getConfig().getBoolean("영웅 사용");
		ConfigUtils.HERO_MIN = ZombieSurvival.instance.getConfig().getInt("영웅 최소 인원");
		ConfigUtils.AUTO = ZombieSurvival.instance.getConfig().getBoolean("자동화 사용");
		ConfigUtils.PLAY_TIME = ZombieSurvival.instance.getConfig().getLong("게임 플레이 시간");
		ConfigUtils.STAY_TIME = ZombieSurvival.instance.getConfig().getLong("시작 대기 시간");
		ConfigUtils.SPAWN_TIME = ZombieSurvival.instance.getConfig().getLong("숙주 스폰 시간");
		ConfigUtils.AUTO_MIN = ZombieSurvival.instance.getConfig().getInt("자동화 최소 인원");
		ConfigUtils.KILL_LOG = ZombieSurvival.instance.getConfig().getBoolean("죽인 사람 출력");
		ConfigUtils.LOCATION_LOG = ZombieSurvival.instance.getConfig().getBoolean("좌표 출력 사용");
		ConfigUtils.LOCATION_TIME = ZombieSurvival.instance.getConfig().getLong("좌표 출력 시간");
	}
	
	public static void saveConfig() {
		ZombieSurvival.instance.getConfig().set("백신 사용", ConfigUtils.VACCINE);
		ZombieSurvival.instance.getConfig().set("백신 지급 시간", ConfigUtils.VACCINE_TIME);
		ZombieSurvival.instance.getConfig().set("백신 지급 유저", ConfigUtils.VACCINE_PERCENT);
		ZombieSurvival.instance.getConfig().set("백신 최소 인원", ConfigUtils.VACCINE_MIN);
		ZombieSurvival.instance.getConfig().set("영웅 사용", ConfigUtils.HERO);
		ZombieSurvival.instance.getConfig().set("영웅 최소 인원", ConfigUtils.HERO_MIN);
		ZombieSurvival.instance.getConfig().set("자동화 사용", ConfigUtils.AUTO);
		ZombieSurvival.instance.getConfig().set("게임 플레이 시간", ConfigUtils.PLAY_TIME);
		ZombieSurvival.instance.getConfig().set("시작 대기 시간", ConfigUtils.STAY_TIME);
		ZombieSurvival.instance.getConfig().set("숙주 스폰 시간", ConfigUtils.SPAWN_TIME);
		ZombieSurvival.instance.getConfig().set("자동화 최소 인원", ConfigUtils.AUTO_MIN);
		ZombieSurvival.instance.getConfig().set("죽인 사람 출력", ConfigUtils.KILL_LOG);
		ZombieSurvival.instance.getConfig().set("좌표 출력 사용", ConfigUtils.LOCATION_LOG);
		ZombieSurvival.instance.getConfig().set("좌표 출력 시간", ConfigUtils.LOCATION_TIME);
		
		ZombieSurvival.instance.saveConfig();
	}
}
