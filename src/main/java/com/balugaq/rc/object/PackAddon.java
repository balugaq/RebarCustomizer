package com.balugaq.rc.object;

import com.balugaq.rc.RebarCustomizer;
import io.github.pylonmc.rebar.addon.RebarAddon;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NullMarked;

import java.util.Locale;
import java.util.Set;

/**
 * @author balugaq
 */
@NullMarked
public record PackAddon(String namespace, Locale defaultLanguage, Material material) implements RebarAddon {
    public static PackAddon generate(String id, Locale defaultLanguage, Material material) {
        return new PackAddon(id, defaultLanguage, material);
    }

    @Override
    public JavaPlugin getJavaPlugin() {
        return RebarCustomizer.getInstance();
    }

    @Override
    public Material getMaterial() {
        return material;
    }

    @Override
    public NamespacedKey getKey() {
        return key(namespace);
    }

    public NamespacedKey key(String key) {
        return new NamespacedKey(namespace, key);
    }

    @Override
    public boolean suppressAddonNameWarning() {
        return true;
    }

    @Override
    public Locale getDefaultLanguage() {
        return defaultLanguage;
    }
}
