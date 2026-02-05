package com.balugaq.pc.util;

import com.balugaq.pc.RebarCustomizer;
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
