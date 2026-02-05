package com.balugaq.pc.rebar;

import com.balugaq.pc.rebar.block.RecipeCopier;
import io.github.pylonmc.rebar.block.RebarBlock;
import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;

/**
 * @author balugaq
 */
@NullMarked
public class RebarCustomizerBlocks {
    static {
        RebarBlock.register(RebarCustomizerKeys.recipe_copier, Material.CRAFTING_TABLE, RecipeCopier.class);
    }

    public static void initialize() {
    }
}
