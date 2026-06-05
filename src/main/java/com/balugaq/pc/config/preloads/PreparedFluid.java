package com.balugaq.pc.config.preloads;

import com.balugaq.pc.config.PageDesc;
import com.balugaq.pc.config.PostLoadable;
import com.balugaq.pc.config.RegisteredObjectID;
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
