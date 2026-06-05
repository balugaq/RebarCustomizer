package com.balugaq.rc.config.preloads;

import com.balugaq.rc.config.PageDesc;
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
public record PreparedItem(
        RegisteredObjectID id,
        ItemStack icon,
        @Nullable List<PageDesc> pages,
        boolean postLoad
) implements PostLoadable {

}
