package com.balugaq.rc.config.pack;

import com.balugaq.rc.GlobalVars;
import com.balugaq.rc.config.RegisteredObjectID;
import com.balugaq.rc.config.ScriptDesc;
import com.balugaq.rc.object.PackAddon;
import com.balugaq.rc.script.ScriptExecutor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.Material;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import java.util.Locale;
import java.util.Set;

/**
 * @author balugaq
 */
@Data
@NoArgsConstructor(force = true)
@NullMarked
public class PackNamespace {
    private final String namespace;
    private final Locale defaultLanguage;
    private final Material material;
    private @Nullable Scripts scripts;
    private PackAddon plugin;

    public PackNamespace(String namespace, Locale defaultLanguage, Material material) {
        this.namespace = namespace;
        this.defaultLanguage = defaultLanguage;
        this.material = material;
        plugin = PackAddon.generate(namespace, defaultLanguage, material);
    }

    public static PackNamespace warp(PackID packID, Locale defaultLanguage, Material material) {
        return new PackNamespace(packID.getId().toLowerCase(), defaultLanguage, material);
    }

    public PackAddon plugin() {
        return plugin;
    }

    public void registerScript(RegisteredObjectID id, @Nullable ScriptDesc desc) {
        var exe = findScript(desc);
        if (exe == null) return;
        GlobalVars.putScript(id.key(), exe);
    }

    @Nullable
    public ScriptExecutor findScript(@Nullable ScriptDesc desc) {
        if (scripts == null || desc == null) return null;
        return scripts.findScript(desc);
    }
}
