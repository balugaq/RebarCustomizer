package com.balugaq.rc.config;

import com.balugaq.rc.object.CustomRecipe;
import com.balugaq.rc.object.ItemStackProvider;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;
import xyz.xenondevs.invui.gui.Gui;

import java.util.List;

/**
 * @author balugaq
 */
@NullMarked
public record GuiData(
        NamespacedKey key,
        List<String> structure,
        @Nullable ItemStackProvider provider,
        Gui.Builder<?, ?> builder,
        @Nullable CustomRecipe recipe,
        List<Character> invSlotChars
) {
}
