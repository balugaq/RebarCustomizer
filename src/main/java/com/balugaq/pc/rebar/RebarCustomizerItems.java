package com.balugaq.pc.rebar;

import com.balugaq.pc.rebar.page.MainPage;
import io.github.pylonmc.rebar.item.RebarItem;
import io.github.pylonmc.rebar.item.builder.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

/**
 * @author balugaq
 */
@NullMarked
public class RebarCustomizerItems {
    // @formatter:off
    public static final ItemStack recipe_copier = ItemStackBuilder.rebar(
            Material.CRAFTING_TABLE,
            RebarCustomizerKeys.recipe_copier
    ).build();

    static {
        RebarItem.register(
                RebarItem.class,
                recipe_copier,
                RebarCustomizerKeys.recipe_copier // block signature
        );
        MainPage.addItem(recipe_copier);
    }
    public static void initialize() {
    }
}
