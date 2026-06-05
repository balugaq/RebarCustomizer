package com.balugaq.rc.util;

import com.balugaq.rc.RebarCustomizer;
import org.bukkit.NamespacedKey;
import org.jspecify.annotations.NullMarked;

/**
 * @author balugaq
 */
@NullMarked
public class Keys {
    public static NamespacedKey create(String key) {
        return new NamespacedKey(RebarCustomizer.getInstance(), key);
    }
}
