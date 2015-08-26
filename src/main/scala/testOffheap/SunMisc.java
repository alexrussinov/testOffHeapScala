package testOffheap;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * Created by Aleksey Voronets on 26.08.15.
 */
public class SunMisc {
    public static final Unsafe UNSAFE;
    static {
        Unsafe value = null;
        try {
            value = Unsafe.getUnsafe();
        } catch (SecurityException e) {
            try {
                Field f = Unsafe.class.getDeclaredField("theUnsafe");
                f.setAccessible(true);
                value = (Unsafe) f.get(null);
            } catch (NoSuchFieldException nsfe) {
            } catch (IllegalAccessException iae) {
            }
        }
        UNSAFE = value;
    }
}