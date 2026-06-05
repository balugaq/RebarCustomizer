package com.balugaq.rc.config.preloads;

import com.balugaq.rc.config.PostLoadable;
import com.balugaq.rc.config.RegisteredObjectID;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import java.util.List;

/**
 * @author balugaq
 */
@NullMarked
public record PreparedResearch(
        RegisteredObjectID id,
        ItemStack icon,
        @Nullable String name,
        long cost,
        List<String> unlocks,
        boolean postLoad
) implements PostLoadable {
}
