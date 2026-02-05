package com.balugaq.pc.object;

import io.github.pylonmc.rebar.fluid.RebarFluid;
import io.github.pylonmc.rebar.recipe.FluidOrItem;
import io.github.pylonmc.rebar.recipe.RebarRecipe;
import io.github.pylonmc.rebar.recipe.RecipeInput;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;
import xyz.xenondevs.invui.gui.Gui;

import java.util.List;
import java.util.Map;

/**
 * @author balugaq
 */
@Data
@RequiredArgsConstructor
@NullMarked
public class CustomRecipe implements RebarRecipe {
    private final CustomRecipeType recipeType;
    private final NamespacedKey key;
    private final List<RecipeInput> inputs;
    private final List<FluidOrItem> results;
    private final int timeSeconds;
    private final Map<String, Object> other;

    @Override
    public List<RecipeInput> getInputs() {
        return inputs;
    }

    @Override
    public List<FluidOrItem> getResults() {
        return results;
    }

    @Override
    public Gui display() {
        return recipeType.makeGui(Gui.builder(), this);
    }

    /**
     * Return the namespaced identifier for this object.
     *
     * @return this object's key
     */
    @Override
    public NamespacedKey getKey() {
        return key;
    }

    @Getter
    @Setter
    @Nullable
    private Object2IntLinkedOpenHashMap<ItemStack> countOutputItems;

    @Getter
    @Setter
    @Nullable
    private Object2DoubleOpenHashMap<RebarFluid> countOutputFluids;
}
