package com.balugaq.rc.config.preloads;

import com.balugaq.rc.config.PageDesc;
import com.balugaq.rc.config.PostLoadable;
import com.balugaq.rc.config.RegisteredObjectID;
import io.github.pylonmc.rebar.fluid.tags.FluidTemperature;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import java.util.List;

/**
 * @author balugaq
 */
@NullMarked
public record PreparedFluid(
        RegisteredObjectID id,
        Material material,
        TextColor color,
        FluidTemperature temperature,
        @Nullable List<PageDesc> pages,
        boolean postLoad
) implements PostLoadable {
}
