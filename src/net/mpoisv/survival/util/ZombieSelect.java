package net.mpoisv.survival.util;

public enum ZombieSelect {
	AUTO, DEFAULT, LIGHT, HEAVY, VOODOO;

	public ZombieType toParse() {
		switch(this) {
			case AUTO: return null;
			case DEFAULT: return ZombieType.DEFAULT;
			case LIGHT: return ZombieType.LIGHT;
			case HEAVY: return ZombieType.HEAVY;
			case VOODOO: return ZombieType.VOODOO;
		}
		
		
		return ZombieType.DEFAULT;
	}
}
