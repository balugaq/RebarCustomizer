package com.balugaq.rc.integration;

import com.balugaq.rc.config.Deserializer;
import com.balugaq.rc.config.Pack;
import com.balugaq.rc.config.pack.Recipes;
import com.balugaq.rc.data.MyArrayList;
import com.balugaq.rc.data.WeightedElement;
import com.balugaq.rc.exceptions.MissingArgumentException;
import io.github.pylonmc.pylon.recipes.*;
import io.github.pylonmc.rebar.recipe.ingredient.ItemChoice;
import io.github.pylonmc.rebar.util.MiningLevel;
import io.github.pylonmc.rebar.util.WeightedSet;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import java.util.*;

/**
 * @author balugaq
 */
@NullMarked
public class PylonIntegration implements Integration {
    public static final Deserializer<MiningLevel> MINING_LEVEL = Deserializer.enumDeserializer(MiningLevel.class).forceUpperCase();
    private final Plugin plugin;
    public PylonIntegration() {
        this.plugin = Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("Pylon"));
    }

    @Override
    public Plugin plugin() {
        return plugin;
    }

    public static BloomeryDisplayRecipe advancedBloomeryDisplay(NamespacedKey key, ConfigurationSection section) {
        return new BloomeryDisplayRecipe(
                key,
                Deserializer.ITEM_CHOICE.deserialize(section.get("input")),
                Deserializer.ITEM_STACK.deserialize(section.get("result"))
        );
    }

    public static DrillingDisplayRecipe advancedDrillingDisplay(NamespacedKey key, ConfigurationSection section) {
        return new DrillingDisplayRecipe(
                key,
                Deserializer.ITEM_STACK.deserialize(section.get("input")),
                Deserializer.ITEM_STACK.deserialize(section.get("result"))
        );
    }

    public static ForgingDisplayRecipe advancedForgingDisplay(NamespacedKey key, ConfigurationSection section) {
        return new ForgingDisplayRecipe(
                key,
                Deserializer.ITEM_STACK.deserialize(section.get("input")),
                Deserializer.ITEM_STACK.deserialize(section.get("result"))
        );
    }

    public static GrindstoneRecipe advancedGrindstone(NamespacedKey key, ConfigurationSection section) {
        return new GrindstoneRecipe(
                key,
                Deserializer.ITEM_CHOICE.deserialize(section.get("input")),
                toWeightedSet(Pack.read(section, MyArrayList.class, WeightedElement.class, "results")),
                section.getInt("cycles")
        );
    }

    public static WeightedSet<ItemStack> toWeightedSet(List<WeightedElement> list) {
        WeightedSet<ItemStack> set = new WeightedSet<>();
        for (WeightedElement element : list) {
            set.add(new WeightedSet.Element<>(element.getElement(), element.getWeight()));
        }
        return set;
    }

    public static HammerRecipe advancedHammer(NamespacedKey key, ConfigurationSection section) {
        return new HammerRecipe(
                key,
                Deserializer.ITEM_CHOICE.deserialize(section.get("input")),
                Deserializer.ITEM_STACK.deserialize(section.get("result")),
                MINING_LEVEL.deserialize(section.get("mining-level")),
                section.getInt("uses")
        );
    }

    public static ShimmerAltarRecipe advancedShimmerAltar(NamespacedKey key, ConfigurationSection section) {
        var se = section.getConfigurationSection("key");
        if (se == null) throw new MissingArgumentException("key");
        Map<Character, ItemChoice> itemMap = new HashMap<>();
        for (String ke : se.getKeys(false)) {
            itemMap.put(ke.charAt(0), Deserializer.ITEM_CHOICE.deserialize(se.get(ke)));
        }
        var shape = section.getStringList("shape");
        StringBuilder ingredientChars = new StringBuilder();
        ingredientChars.append(shape.getFirst());
        ingredientChars.append(shape.get(1).charAt(2));
        ingredientChars.append(new StringBuilder(shape.get(2)).reverse());
        ingredientChars.append(shape.get(1).charAt(0));
        List<@Nullable ItemChoice> inputs = new ArrayList<>(8);
        for (int i = 0; i < ingredientChars.length(); i++) {
            char c = ingredientChars.charAt(i);
            if (c == ' ') {
                inputs.add(null);
            } else if (itemMap.containsKey(c)) {
                inputs.add(itemMap.get(c));
            } else {
                throw new IllegalArgumentException("Unknown character in shape: " + c);
            }
        }
        ItemChoice catalyst = itemMap.get(shape.get(1).charAt(1));
        if (catalyst == null) {
            throw new IllegalArgumentException("Catalyst (center item) cannot be empty");
        }

        return new ShimmerAltarRecipe(
                key,
                inputs,
                catalyst,
                Deserializer.ITEM_STACK.deserialize(section.get("result")),
                section.getInt("time-seconds")
        );
    }

    public static MeltingRecipe advancedMelting(NamespacedKey key, ConfigurationSection section) {
        return new MeltingRecipe(
                key,
                Deserializer.ITEM_CHOICE.deserialize(section.get("input")),
                Deserializer.REBAR_FLUID.deserialize(section.get("result")),
                section.getDouble("amount")
        );
    }

    public static MixingPotRecipe advancedMixingPot(NamespacedKey key, ConfigurationSection section) {
        return new MixingPotRecipe(
                key,
                Pack.read(section, MyArrayList.class, Deserializer.ITEM_CHOICE, "input-items"),
                Deserializer.FLUID_CHOICE.deserialize(section.get("input-fluid")),
                Deserializer.FLUID_OR_ITEM.deserialize(section.get("output")),
                section.getBoolean("requires-enriched-fire", false)
        );
    }

    public static MoldingRecipe advancedMolding(NamespacedKey key, ConfigurationSection section) {
        return new MoldingRecipe(
                key,
                Deserializer.ITEM_CHOICE.deserialize(section.get("input")),
                Deserializer.ITEM_STACK.deserialize(section.get("result")),
                section.getInt("clicks")
        );
    }

    public static PipeBendingRecipe advancedPipeBending(NamespacedKey key, ConfigurationSection section) {
        return new PipeBendingRecipe(
                key,
                Deserializer.ITEM_CHOICE.deserialize(section.get("input")),
                Deserializer.ITEM_STACK.deserialize(section.get("result")),
                Deserializer.ITEM_STACK.deserialize(section.get("particle-item")),
                section.getInt("time-ticks")
        );
    }

    public static PressRecipe advancedPress(NamespacedKey key, ConfigurationSection section) {
        return new PressRecipe(
                key,
                Deserializer.ITEM_CHOICE.deserialize(section.get("input")),
                section.getDouble("oil-amount")
        );
    }

    public static SmelteryRecipe advancedSmeltery(NamespacedKey key, ConfigurationSection section) {
        return new SmelteryRecipe(
                key,
                Deserializer.FLUID_MAP.deserialize(section.get("inputs")),
                Deserializer.FLUID_MAP.deserialize(section.get("outputs")),
                section.getDouble("temperature")
        );
    }

    public static TableSawRecipe advancedTableSaw(NamespacedKey key, ConfigurationSection section) {
        return new TableSawRecipe(
                key,
                Deserializer.ITEM_CHOICE.deserialize(section.get("input")),
                Deserializer.ITEM_STACK.deserialize(section.get("result")),
                Deserializer.ITEM_STACK.deserialize(section.get("particle-item")),
                section.getInt("time-ticks")
        );
    }

    @Override
    public void apply() {
        Recipes.loadAdvance(BloomeryDisplayRecipe.RECIPE_TYPE, PylonIntegration::advancedBloomeryDisplay);
        Recipes.loadAdvance(DrillingDisplayRecipe.RECIPE_TYPE, PylonIntegration::advancedDrillingDisplay);
        Recipes.loadAdvance(ForgingDisplayRecipe.RECIPE_TYPE, PylonIntegration::advancedForgingDisplay);
        Recipes.loadAdvance(GrindstoneRecipe.RECIPE_TYPE, PylonIntegration::advancedGrindstone);
        Recipes.loadAdvance(HammerRecipe.RECIPE_TYPE, PylonIntegration::advancedHammer);
        Recipes.loadAdvance(ShimmerAltarRecipe.RECIPE_TYPE, PylonIntegration::advancedShimmerAltar);
        Recipes.loadAdvance(MeltingRecipe.RECIPE_TYPE, PylonIntegration::advancedMelting);
        Recipes.loadAdvance(MixingPotRecipe.RECIPE_TYPE, PylonIntegration::advancedMixingPot);
        Recipes.loadAdvance(MoldingRecipe.RECIPE_TYPE, PylonIntegration::advancedMolding);
        Recipes.loadAdvance(PipeBendingRecipe.RECIPE_TYPE, PylonIntegration::advancedPipeBending);
        Recipes.loadAdvance(PressRecipe.RECIPE_TYPE, PylonIntegration::advancedPress);
        Recipes.loadAdvance(SmelteryRecipe.RECIPE_TYPE, PylonIntegration::advancedSmeltery);
        Recipes.loadAdvance(TableSawRecipe.RECIPE_TYPE, PylonIntegration::advancedTableSaw);
    }
}
