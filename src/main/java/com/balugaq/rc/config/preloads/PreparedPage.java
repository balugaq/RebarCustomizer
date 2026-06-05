package com.balugaq.rc.config.preloads;

import com.balugaq.rc.config.PageDesc;
import com.balugaq.rc.config.PostLoadable;
import com.balugaq.rc.config.RegisteredObjectID;
import com.balugaq.rc.config.ScriptItemDesc;
import com.balugaq.rc.data.MyArrayList;
import org.bukkit.Material;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import java.util.List;

/**
 * @author balugaq
 */
@NullMarked
public record PreparedPage(
        RegisteredObjectID id,
        Material material,
        @Nullable MyArrayList<PageDesc> parents,
        List<ScriptItemDesc> items,
        boolean postLoad
) implements PostLoadable {
}
