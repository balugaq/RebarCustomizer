package com.balugaq.rc.object;

import com.balugaq.rc.config.*;
import com.balugaq.rc.data.MyArrayList;
import com.balugaq.rc.data.MyObject2ObjectOpenHashMap;
import com.balugaq.rc.data.MyObjectOpenHashSet;
import com.balugaq.rc.exceptions.InvalidEnumClassException;
import com.balugaq.rc.exceptions.UnknownDeserializerException;
import com.balugaq.rc.exceptions.WrongEnumDeserializerException;
import com.balugaq.rc.util.ReflectionUtil;
import io.github.pylonmc.rebar.config.ConfigSection;
import io.github.pylonmc.rebar.config.adapter.ConfigAdapter;
import io.github.pylonmc.rebar.fluid.RebarFluid;
import io.github.pylonmc.rebar.recipe.ConfigurableRecipeType;
import io.github.pylonmc.rebar.recipe.RebarRecipe;
import io.github.pylonmc.rebar.recipe.ingredient.*;
import io.github.pylonmc.rebar.util.gui.ProgressItem;
import it.unimi.dsi.fastutil.chars.CharOpenHashSet;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.inventory.VirtualInventory;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * @author balugaq
 */
@NullMarked
public class CustomRecipeType extends ConfigurableRecipeType<RebarRecipe> {

    public static final Map<String, Handler> DEFAULT_CONFIG_READER = Map.of(
            "inputs", new Handler(GenericDeserializer.newDeserializer(MyArrayList.class).setDeserializer(Deserializer.FLUID_OR_ITEM_CHOICE), new ArrayList<>()),
            "results", new Handler(GenericDeserializer.newDeserializer(MyArrayList.class).setDeserializer(Deserializer.FLUID_OR_ITEM), new ArrayList<>())
    );

    private final List<String> structure;
    private final ItemStackProvider provider;
    private final Map<String, Handler> configReader;

    public CustomRecipeType(final NamespacedKey key, List<String> structure, @Nullable ItemStackProvider guiProvider, @Nullable Map<String, Handler> configReader) {
        super(key);
        this.structure = structure;
        this.provider = guiProvider == null ? Pack.DEFAULT_GUI_PROVIDER : guiProvider;
        this.configReader = configReader == null ? DEFAULT_CONFIG_READER : configReader;
    }

    public Gui makeGui(Gui.Builder<?, ?> gui, @Nullable CustomRecipe recipe) {
        return makeGui(structure, provider, gui, recipe);
    }

    @Nullable
    @Contract("null, _, _ -> null; !null, _, _ -> !null")
    public static Gui makeGui(@Nullable GuiData data, @Nullable Map<Character, VirtualInventory> vs, @Nullable ProgressItem progressItem) {
        if (data == null) return null;
        return makeGui(data.structure(), data.provider(), Gui.builder(), data.recipe(), vs, progressItem);
    }

    @Nullable
    @Contract("null -> null; !null -> !null")
    public static Gui makeGui(@Nullable GuiData data) {
        return makeGui(data, null, null);
    }

    public static Gui makeGui(List<String> structure, @Nullable ItemStackProvider provider, Gui.Builder<?, ?> gui, @Nullable CustomRecipe recipe) {
        return makeGui(structure, provider, gui, recipe, null, null);
    }

    public static Gui makeGui(List<String> structure, @Nullable ItemStackProvider provider, Gui.Builder<?, ?> gui, @Nullable CustomRecipe recipe, @Nullable Map<Character, VirtualInventory> vs, @Nullable ProgressItem progressItem) {
        var s = structure.toArray(new String[0]);
        gui.setStructure(s);
        CharOpenHashSet set = new CharOpenHashSet();
        for (var s2 : s) {
            for (var c : s2.toCharArray()) {
                set.add(c);
            }
        }
        for (char c : set) {
            if (c != 'i' && c != 'o') {
                gui.addIngredient(c, (provider == null ? Pack.DEFAULT_GUI_PROVIDER : provider).display(c, recipe));
            }
        }
        if (vs != null) {
            for (Map.Entry<Character, VirtualInventory> e : vs.entrySet()) {
                gui.addIngredient(e.getKey(), e.getValue());
            }
        }
        if (progressItem != null) {
            gui.addIngredient('p', progressItem);
        }
        return gui.build();
    }

    @Override
    protected CustomRecipe loadRecipe(final NamespacedKey key, final ConfigSection section) {
        Map<String, Object> other = new HashMap<>();
        for (Map.Entry<String, Handler> e : configReader.entrySet()) {
            try {
                other.put(e.getKey(), ReflectionUtil.invokeMethod(section, "get", e.getKey(), e.getValue().deserializer().toAdapter(), e.getValue().defaultValue()));
            } catch (InvocationTargetException | IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        }
        List<FluidOrItemChoice> inputs = readInputs(other.get("inputs"));
        List<FluidOrItem> results = readResults(other.get("results"));
        int timeSeconds = section.get("time-seconds", ConfigAdapter.INTEGER, 0);

        return new CustomRecipe(this, key, inputs, results, timeSeconds, other);
    }

    private List<FluidOrItem> readResults(@Nullable Object object) {
        List<FluidOrItem> s = new ArrayList<>();
        if (object == null) return s;
        for (FluidOrItemChoice r : readInputs(object)) {
            if (r instanceof ItemChoice item) {
                for (var stack : item.getRepresentativeItems()) {
                    s.add(FluidOrItem.of(stack));
                }
            } else if (r instanceof FluidChoice fluid) {
                for (RebarFluid f : fluid.getFluids()) {
                    s.add(FluidOrItem.of(f, fluid.getAmount()));
                }
            }
        }

        return s;
    }

    private List<FluidOrItemChoice> readInputs(@Nullable Object object) {
        List<FluidOrItemChoice> s = new ArrayList<>();
        if (object == null) return s;
        switch (object) {
            case ItemStack stack -> {
                return List.of(ItemChoice.exact(stack));
            }
            case RebarFluid fluid -> {
                return List.of(FluidChoice.of(fluid, 1));
            }
            case ItemChoice item -> {
                return List.of(item);
            }
            case FluidChoice fluid -> {
                return List.of(fluid);
            }
            case FluidOrItem.Item item -> {
                return List.of(ItemChoice.exact(item.item()));
            }
            case FluidWithAmount fluid -> {
                return List.of(FluidChoice.of(fluid.fluid(), fluid.amount()));
            }
            case List<?> list -> {
                for (Object o : list) {
                    List<FluidOrItemChoice> r = readInputs(o);
                    s.addAll(r);
                }
                return s;
            }
            case Set<?> set -> {
                for (Object o : set) {
                    List<FluidOrItemChoice> r = readInputs(o);
                    s.addAll(r);
                }
                return s;
            }
            default -> {
            }
        }

        return s;
    }

    @NullMarked
    public record Handler(Deserializer<?> deserializer, Object defaultValue) implements Deserializer<Handler> {
        public Handler() {
            this(null, null);
        }

        private static Deserializer<?> readDeserializer(List<String> parts) {
            if (parts.isEmpty()) {
                throw new UnknownDeserializerException();
            }

            String p = parts.getFirst();
            try {
                return ReflectionUtil.getStaticValue(Deserializer.class, p.toUpperCase(), Deserializer.class);
            } catch (IllegalAccessException | NoSuchFieldException ignored) {
            }

            if (p.equalsIgnoreCase("list")) {
                return GenericDeserializer.newDeserializer(MyArrayList.class).setDeserializer(readDeserializer(parts.subList(1, parts.size())));
            }

            if (p.equalsIgnoreCase("set")) {
                return GenericDeserializer.newDeserializer(MyObjectOpenHashSet.class).setDeserializer(readDeserializer(parts.subList(1, parts.size())));
            }

            if (p.equalsIgnoreCase("map")) {
                return BiGenericDeserializer.newDeserializer(MyObject2ObjectOpenHashMap.class)
                        .setDeserializer(readDeserializer(List.of(parts.get(0))))
                        .setDeserializer2(readDeserializer(List.of(parts.get(1))));
            }

            if (p.equalsIgnoreCase("enum")) {
                if (parts.size() < 2) {
                    throw new WrongEnumDeserializerException(parts);
                }
                Class<?> clazz = findClass(parts.get(1));
                if (clazz == null || !clazz.isEnum()) {
                    throw new InvalidEnumClassException(parts.get(1));
                }

                return Deserializer.enumDeserializer((Class<? extends Enum>) clazz);
            }

            throw new UnknownDeserializerException(parts);
        }

        @Nullable
        private static Class<?> findClass(String name) {
            try {
                return Class.forName(name);
            } catch (ClassNotFoundException e) {
                return null;
            }
        }

        @Override
        public List<ConfigReader<?, Handler>> readers() {
            return List.of(ConfigReader.of(
                    String.class, s -> {
                        String[] a = s.split(";", 2);
                        if (a.length == 0) {
                            return this;
                        }

                        String[] parts = a[0].split("\\.");
                        var adt = readDeserializer(List.of(parts));

                        if (a.length == 1) {
                            return this;
                        }

                        String def = a[1];
                        return new Handler(adt, adt.deserialize(def));
                    }
            ));
        }
    }
}
