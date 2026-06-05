package com.balugaq.rc.config.preloads;

import com.balugaq.rc.config.PostLoadable;
import com.balugaq.rc.config.RegisteredObjectID;
import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;

/**
 * @author balugaq
 */
@NullMarked
public record PreparedBlock(
        RegisteredObjectID id,
        Material material,
        boolean postLoad
) implements PostLoadable {
}
