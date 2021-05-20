package net.mpoisv.survival.util;

import java.lang.reflect.Field;

public class ClassUtils {
	public static Object getPrivateFieldData(Field f, Object o) {
		try {
			f.setAccessible(true);
			return f.get(o);
		}catch(Exception e) {}
		return null;
	}
	
	public static void setPrivateFieldData(Field field, Object obj, Object data) {
		try {
			field.setAccessible(true);
			field.set(obj, data);
		}catch(Exception e) { }
	}
}
