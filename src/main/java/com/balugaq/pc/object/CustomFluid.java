package com.balugaq.pc.object;

import io.github.pylonmc.rebar.fluid.RebarFluid;
import io.github.pylonmc.rebar.fluid.RebarFluidTag;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.jspecify.annotations.NullMarked;

import java.util.List;

/**
 * @author balugaq
 */
@NullMarked
public class CustomFluid extends RebarFluid {
    public CustomFluid(final NamespacedKey key, final Component name, final Material material, final List<RebarFluidTag> tags) {
        super(key, name, material, tags);
    }

    public CustomFluid(final NamespacedKey key, final Material material, final RebarFluidTag... tags) {
        super(key, material, tags);
    }
}
