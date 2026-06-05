package com.balugaq.pc.object;

import io.github.pylonmc.rebar.fluid.RebarFluid;
import io.github.pylonmc.rebar.fluid.RebarFluidTag;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NullMarked;

import java.util.List;

/**
 * @author balugaq
 */
@NullMarked
public class CustomFluid extends RebarFluid {
    public CustomFluid(@NotNull final NamespacedKey key, @NotNull final TextColor color, @NotNull final Component nameWithoutColor, @NotNull final Material material, @NotNull final List<RebarFluidTag> tags) {
        super(key, color, nameWithoutColor, material, tags);
    }

    public CustomFluid(@NotNull final NamespacedKey key, @NotNull final TextColor color, @NotNull final Material material, @NotNull final RebarFluidTag... tags) {
        super(key, color, material, tags);
    }
}
