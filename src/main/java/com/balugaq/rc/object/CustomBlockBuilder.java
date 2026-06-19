package com.balugaq.rc.object;

import com.balugaq.rc.GlobalVars;
import com.balugaq.rc.config.FluidBlockData;
import com.balugaq.rc.config.FluidBufferBlockData;
import com.balugaq.rc.config.GuiData;
import com.balugaq.rc.config.LogisticBlockData;
import com.balugaq.rc.config.preloads.PreparedBlock;
import io.github.pylonmc.rebar.block.RebarBlock;
import io.github.pylonmc.rebar.block.context.BlockBreakContext;
import io.github.pylonmc.rebar.block.context.BlockCreateContext;
import io.github.pylonmc.rebar.block.interfaces.BeaconRebarBlockHandler;
import io.github.pylonmc.rebar.block.interfaces.BedRebarBlockHandler;
import io.github.pylonmc.rebar.block.interfaces.BellRebarBlockHandler;
import io.github.pylonmc.rebar.block.interfaces.BlockBreakRebarBlockHandler;
import io.github.pylonmc.rebar.block.interfaces.BrewingStandRebarBlockHandler;
import io.github.pylonmc.rebar.block.interfaces.CampfireRebarBlockHandler;
import io.github.pylonmc.rebar.block.interfaces.CargoRebarBlock;
import io.github.pylonmc.rebar.block.interfaces.CargoRebarBlockHandler;
import io.github.pylonmc.rebar.block.interfaces.CauldronRebarBlockHandler;
import io.github.pylonmc.rebar.block.interfaces.ComposterRebarBlockHandler;
import io.github.pylonmc.rebar.block.interfaces.CopperRebarBlockHandler;
import io.github.pylonmc.rebar.block.interfaces.CrafterRebarBlockHandler;
import io.github.pylonmc.rebar.block.interfaces.CulledRebarBlock;
import io.github.pylonmc.rebar.block.interfaces.DirectionalRebarBlock;
import io.github.pylonmc.rebar.block.interfaces.DispenserRebarBlockHandler;
import io.github.pylonmc.rebar.block.interfaces.EnchantingTableRebarBlockHandler;
import io.github.pylonmc.rebar.block.interfaces.EntityChangeRebarBlockHandler;
import io.github.pylonmc.rebar.block.interfaces.EntityCulledRebarBlock;
import io.github.pylonmc.rebar.block.interfaces.EntityHolderRebarBlock;
import io.github.pylonmc.rebar.block.interfaces.FallingRebarBlockHandler;
import io.github.pylonmc.rebar.block.interfaces.FireRebarBlockHandler;
import io.github.pylonmc.rebar.block.interfaces.FlowerPotRebarBlockHandler;
import io.github.pylonmc.rebar.block.interfaces.FluidBufferRebarBlock;
import io.github.pylonmc.rebar.block.interfaces.FluidRebarBlock;
import io.github.pylonmc.rebar.block.interfaces.FluidTankRebarBlock;
import io.github.pylonmc.rebar.block.interfaces.FurnaceRebarBlockHandler;
import io.github.pylonmc.rebar.block.interfaces.GhostBlockHolderRebarBlock;
import io.github.pylonmc.rebar.block.interfaces.GrowRebarBlockHandler;
import io.github.pylonmc.rebar.block.interfaces.GuiRebarBlock;
import io.github.pylonmc.rebar.block.interfaces.HopperRebarBlockHandler;
import io.github.pylonmc.rebar.block.interfaces.InteractRebarBlockHandler;
import io.github.pylonmc.rebar.block.interfaces.JumpRebarBlockHandler;
import io.github.pylonmc.rebar.block.interfaces.LeafRebarBlockHandler;
import io.github.pylonmc.rebar.block.interfaces.LecternRebarBlockHandler;
import io.github.pylonmc.rebar.block.interfaces.LogisticRebarBlock;
import io.github.pylonmc.rebar.block.interfaces.LootDispenserRebarBlockHandler;
import io.github.pylonmc.rebar.block.interfaces.NoJobRebarBlock;
import io.github.pylonmc.rebar.block.interfaces.NoVanillaInventoryRebarBlock;
import io.github.pylonmc.rebar.block.interfaces.NoteRebarBlockHandler;
import io.github.pylonmc.rebar.block.interfaces.PistonRebarBlockHandler;
import io.github.pylonmc.rebar.block.interfaces.ProcessorRebarBlock;
import io.github.pylonmc.rebar.block.interfaces.RecipeProcessorRebarBlock;
import io.github.pylonmc.rebar.block.interfaces.RedstoneRebarBlockHandler;
import io.github.pylonmc.rebar.block.interfaces.ShearRebarBlockHandler;
import io.github.pylonmc.rebar.block.interfaces.SignRebarBlockHandler;
import io.github.pylonmc.rebar.block.interfaces.SimpleRebarMultiblock;
import io.github.pylonmc.rebar.block.interfaces.SneakRebarBlockHandler;
import io.github.pylonmc.rebar.block.interfaces.SpongeRebarBlockHandler;
import io.github.pylonmc.rebar.block.interfaces.TNTRebarBlockHandler;
import io.github.pylonmc.rebar.block.interfaces.TargetRebarBlockHandler;
import io.github.pylonmc.rebar.block.interfaces.TickingRebarBlock;
import io.github.pylonmc.rebar.block.interfaces.UnloadRebarBlockHandler;
import io.github.pylonmc.rebar.block.interfaces.VaultRebarBlockHandler;
import io.github.pylonmc.rebar.block.interfaces.VirtualInventoryRebarBlock;
import io.github.pylonmc.rebar.config.ConfigSection;
import io.github.pylonmc.rebar.config.adapter.ConfigAdapter;
import io.github.pylonmc.rebar.fluid.RebarFluid;
import io.github.pylonmc.rebar.item.RebarItem;
import io.github.pylonmc.rebar.item.builder.ItemStackBuilder;
import io.github.pylonmc.rebar.logistics.LogisticGroupType;
import io.github.pylonmc.rebar.recipe.FluidOrItem;
import io.github.pylonmc.rebar.recipe.RebarRecipe;
import io.github.pylonmc.rebar.recipe.RecipeInput;
import io.github.pylonmc.rebar.recipe.RecipeType;
import io.github.pylonmc.rebar.registry.RebarRegistry;
import io.github.pylonmc.rebar.util.MachineUpdateReason;
import io.github.pylonmc.rebar.util.RebarUtils;
import io.github.pylonmc.rebar.util.gui.GuiItems;
import io.github.pylonmc.rebar.util.gui.ProgressItem;
import io.github.pylonmc.rebar.waila.WailaDisplay;
import it.unimi.dsi.fastutil.chars.Char2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.SuperMethodCall;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.Super;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.matcher.ElementMatchers;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.inventory.VirtualInventory;
import xyz.xenondevs.invui.inventory.event.UpdateReason;
import xyz.xenondevs.invui.window.Window;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @author balugaq
 */
@RequiredArgsConstructor
public class CustomBlockBuilder {
    protected static final List<Class<?>> handlers = List.of(
            BeaconRebarBlockHandler.class,
            BedRebarBlockHandler.class,
            BellRebarBlockHandler.class,
            BlockBreakRebarBlockHandler.class,
            BrewingStandRebarBlockHandler.class,
            CampfireRebarBlockHandler.class,
            CargoRebarBlock.class,
            CargoRebarBlockHandler.class,
            CauldronRebarBlockHandler.class,
            ComposterRebarBlockHandler.class,
            CopperRebarBlockHandler.class,
            CrafterRebarBlockHandler.class,
            CulledRebarBlock.class,
            DirectionalRebarBlock.class,
            DispenserRebarBlockHandler.class,
            EnchantingTableRebarBlockHandler.class,
            EntityChangeRebarBlockHandler.class,
            EntityCulledRebarBlock.class,
            //EntityGroupCulledRebarBlock.class,
            EntityHolderRebarBlock.class,
            //FacadeRebarBlock.class,
            FallingRebarBlockHandler.class,
            FireRebarBlockHandler.class,
            FlowerPotRebarBlockHandler.class,
            FluidBufferRebarBlock.class,
            FluidRebarBlock.class,
            FluidTankRebarBlock.class,
            FurnaceRebarBlockHandler.class,
            GhostBlockHolderRebarBlock.class,
            //GroupCulledRebarBLock.class,
            GrowRebarBlockHandler.class,
            GuiRebarBlock.class,
            HopperRebarBlockHandler.class,
            InteractRebarBlockHandler.class,
            //JobRebarBlockHandler.class,
            JumpRebarBlockHandler.class,
            LeafRebarBlockHandler.class,
            LecternRebarBlockHandler.class,
            //LinkedInRebarBlock.class,
            LogisticRebarBlock.class,
            LootDispenserRebarBlockHandler.class,
            NoJobRebarBlock.class,
            NoteRebarBlockHandler.class,
            NoVanillaInventoryRebarBlock.class,
            PistonRebarBlockHandler.class,
            ProcessorRebarBlock.class,
            RecipeProcessorRebarBlock.class,
            RedstoneRebarBlockHandler.class,
            ShearRebarBlockHandler.class,
            SignRebarBlockHandler.class,
            SimpleRebarMultiblock.class,
            SneakRebarBlockHandler.class,
            SpongeRebarBlockHandler.class,
            TargetRebarBlockHandler.class,
            TickingRebarBlock.class,
            TNTRebarBlockHandler.class,
            UnloadRebarBlockHandler.class,
            VaultRebarBlockHandler.class,
            VirtualInventoryRebarBlock.class
    );

    private static String makeName(PreparedBlock prepared) {
        String name = "com.balugaq.rc.object.custom.CustomBlock_";
        name += prepared.id().key().getNamespace()
                .replaceAll("_-.", "_");
        name += "_";
        name += prepared.id().key().getKey()
                .replaceAll("_-./", "_");
        return name;
    }

    public static <T extends RebarBlock> Class<T> build(PreparedBlock prepared) {
        var matcher = ElementMatchers.isDeclaredBy(RebarBlock.class)
                .and(ElementMatchers.not(ElementMatchers.isDeclaredBy(Object.class)));

        for (Class<?> handler : handlers) {
            matcher = matcher.or(ElementMatchers.isDeclaredBy(handler));
        }

        BlockDelegation delegate = new BlockDelegation(prepared.id().key());

        Class<?> loaded = new ByteBuddy()
                .subclass(RebarBlock.class)
                .name(makeName(prepared))
                .implement(handlers.toArray(new Class<?>[0]))

                .constructor(ElementMatchers.takesArguments(Block.class, BlockCreateContext.class))
                .intercept(SuperMethodCall.INSTANCE.andThen(
                        MethodDelegation.withDefaultConfiguration()
                                .filter(ElementMatchers.named("init"))
                                .to(delegate)
                ))

                .method(matcher)
                .intercept(MethodDelegation.withDefaultConfiguration()
                                   .filter(ElementMatchers.named("intercept"))
                                   .to(delegate))

                .make()
                .load(RebarBlock.class.getClassLoader())
                .getLoaded();

        return (Class<T>) loaded;
    }

    /**
     * @author balugaq
     */
    @Getter
    @RequiredArgsConstructor
    public static class BlockDelegation implements RuntimeObject {
        private final NamespacedKey key;
        private final Char2ObjectOpenHashMap<VirtualInventory> vs = new Char2ObjectOpenHashMap<>();
        private final @Nullable RecipeType<?> loadRecipeType = RebarRegistry.RECIPE_TYPES.get(getKey());
        private final @Nullable GuiData guiData = GlobalVars.getGuiData(getKey());
        private final @Nullable LogisticBlockData logisticBlockData = GlobalVars.getLogisticBlockData(getKey());
        private final @Nullable FluidBlockData fluidBlockData = GlobalVars.getFluidBlockData(getKey());
        private final @Nullable FluidBufferBlockData fluidBufferBlockData = GlobalVars.getFluidBufferBlockData(getKey());
        private boolean havePostInitialised = false;

        @RuntimeType
        public void init(@Super(strategy = Super.Instantiation.UNSAFE) RebarBlock rebar, Block block, BlockCreateContext context) {
            if (fluidBlockData != null) {
                for (var e : fluidBlockData) {
                    ((FluidRebarBlock) rebar).createFluidPoint(e.fluidPointType(), e.face(), context, e.allowVerticalFaces());
                }
            }
            if (fluidBufferBlockData != null) {
                for (var e : fluidBufferBlockData) {
                    ((FluidBufferRebarBlock) rebar).createFluidBuffer(e.fluid(), e.capacity(), e.input(), e.output());
                }
            }
            ((RecipeProcessorRebarBlock<?>) rebar).setRecipeProgressItem(new ProgressItem(GuiItems.background()));
        }

        @RuntimeType
        public Object intercept(@Origin Method method,
                                @AllArguments Object[] rawArgs,
                                @Super(strategy = Super.Instantiation.UNSAFE) RebarBlock rebar) throws Exception {
            Object[] args = new Object[rawArgs.length + 1];
            args[0] = rebar;
            System.arraycopy(rawArgs, 0, args, 1, rawArgs.length);
            String name = method.getName();
            switch (name) {
                case "onInteract" -> { // InteractRebarBlockHandler
                    PlayerInteractEvent event = (PlayerInteractEvent) args[1];
                    var v = callScriptA("onPreInteract", args);
                    if (v instanceof Boolean cancelled && cancelled) return null;

                    if (!event.getAction().isRightClick()
                            || event.getPlayer().isSneaking()
                            || event.getHand() != EquipmentSlot.HAND
                            || event.useInteractedBlock() == Event.Result.DENY) {
                        return null;
                    }

                    callScriptA("onInteract", args);

                    if (guiData != null) {
                        event.setUseInteractedBlock(Event.Result.DENY);
                        event.setUseItemInHand(Event.Result.DENY);
                        Window.builder()
                                .setUpperGui(((GuiRebarBlock) rebar).createGui())
                                .setTitle(((GuiRebarBlock) rebar).getGuiTitle())
                                .setViewer(event.getPlayer())
                                .build()
                                .open();
                    }

                    return callScriptA("onPostInteract", args);
                }
                case "onPreBlockBreak" -> { // BlockBreakRebarBlockHandler
                    var v = callScriptA("onPreBlockBreak", args);
                    if (v == null) return true;
                    if (v instanceof Boolean b) {
                        return b;
                    }
                    return false;
                }
                case "onBlockBreak" -> { // BlockBreakRebarBlockHandler
                    ((FluidBufferRebarBlock) rebar).onBlockBreak((List<ItemStack>) args[1], (BlockBreakContext) args[2]); // FluidBufferRebarBlock
                    return callScriptA("onBlockBreak", args);
                }
                case "getTickInterval" -> { // TickingRebarBlock
                    if (!isFunctionExists("onTick")) {
                        if (loadRecipeType == null) {
                            ((TickingRebarBlock) rebar).setTickInterval(Integer.MAX_VALUE);
                            return Integer.MAX_VALUE;
                        }
                    }
                    if (isFunctionExists("getTickInterval")) {
                        var v = callScriptA("getTickInterval", args);
                        if (v instanceof Number number) {
                            var v2 = number.intValue();
                            ((TickingRebarBlock) rebar).setTickInterval(v2);
                            return v2;
                        }
                    }
                    var settings = ConfigSection.fromSettings(getKey());
                    var v3 = settings.get("tick-interval", ConfigAdapter.INTEGER, ((TickingRebarBlock) rebar).getTickInterval());
                    ((TickingRebarBlock) rebar).setTickInterval(v3);
                    return v3;
                }
                case "isAsync" -> { // TickingRebarBlock
                    if (isFunctionExists("isAsync")) {
                        var v = callScriptA("isAsync", args);
                        if (v instanceof Boolean b) {
                            ((TickingRebarBlock) rebar).setAsync(b);
                            return b;
                        }
                    }
                    var settings = ConfigSection.fromSettings(getKey());
                    var v2 = settings.get("async", ConfigAdapter.BOOLEAN, ((TickingRebarBlock) rebar).isAsync());
                    ((TickingRebarBlock) rebar).setAsync(v2);
                    return v2;
                }
                case "onRecipeFinished" -> { // RecipeProcessorRebarBlock
                    ((RecipeProcessorRebarBlock<CustomRecipe>) rebar).getRecipeProgressItem().setItem(GuiItems.background());
                    // push item or fluid
                    for (var e : ((CustomRecipe) args[1]).getResults()) {
                        if (e instanceof FluidOrItem.Item item) {
                            var vo = vs.get('o');
                            vo.addItem(UpdateReason.SUPPRESSED, item.item());
                        }
                        if (e instanceof FluidOrItem.Fluid fluid) {
                            ((FluidBufferRebarBlock) rebar).setFluid(fluid.fluid(), Math.min(((FluidBufferRebarBlock) rebar).fluidCapacity(fluid.fluid()), ((FluidBufferRebarBlock) rebar).fluidAmount(fluid.fluid()) + fluid.amountMillibuckets()));
                        }
                    }
                }
                case "onTick" -> { // TickingRebarBlock
                    var v = callScriptA("preOnTick", args);
                    if (v instanceof Boolean cancelled && cancelled) return null;
                    if (((RecipeProcessorRebarBlock<CustomRecipe>) rebar).isProcessingRecipe()) {
                        ((RecipeProcessorRebarBlock<CustomRecipe>) rebar).progressRecipe(1);
                    } else {
                        tryStartRecipe((RecipeProcessorRebarBlock<CustomRecipe>) rebar, ((FluidBufferRebarBlock) rebar));
                    }
                    callScriptA("onTick", args);
                }
                case "createGui" -> { // GuiRebarBlock
                    if (isFunctionExists("createGui")) {
                        var v = callScriptA("createGui", args);
                        if (v instanceof Gui g) {
                            return g;
                        }
                    }

                    if (!havePostInitialised) postInitialise((RecipeProcessorRebarBlock<CustomRecipe>) rebar, (FluidBufferRebarBlock) rebar, (LogisticRebarBlock) rebar);
                    return CustomRecipeType.makeGui(guiData, vs, ((RecipeProcessorRebarBlock<CustomRecipe>) rebar).getRecipeProgressItem());
                }
                case "postInitialise" -> { // RebarBlock
                    postInitialise((RecipeProcessorRebarBlock<CustomRecipe>) rebar, (FluidBufferRebarBlock) rebar, (LogisticRebarBlock) rebar);
                    return null;
                }
                case "getPlaceholders" -> { // RebarBlock
                    return RuntimeObject.super.getPlaceholders();
                }
                case "getWaila" -> { // RebarBlock
                    return WailaDisplay.of(rebar, (Player) args[1]);
                }
                case "getComponents" -> { // SimpleRebarMultiblock
                    return GlobalVars.getMultiBlockComponents(getKey());
                }
            }

            if (isFunctionExists(name)) {
                return callScriptA(name, args);
            }

            return method.invoke(rebar, rawArgs);
        }

        private void postInitialise(RecipeProcessorRebarBlock<CustomRecipe> recipeProcessor, FluidBufferRebarBlock fluidBuffer, LogisticRebarBlock logistic, Object... args) {
            if (havePostInitialised || logisticBlockData == null || guiData == null) {
                return;
            }

            havePostInitialised = true;
            for (var e : logisticBlockData) {
                var size = 0;
                for (var line : guiData.structure()) {
                    for (var c : line.toCharArray()) {
                        if (c == e.invSlotChar()) {
                            size += 1;
                        }
                    }
                }
                var v = new VirtualInventory(size);
                vs.put(e.invSlotChar(), v);
                switch (e.slotType()) {
                    case INPUT -> {
                        v.addPostUpdateHandler(event -> {
                            if (!(event.getUpdateReason() instanceof MachineUpdateReason)) {
                                tryStartRecipe(recipeProcessor, fluidBuffer);
                            }
                        });
                        logistic.createLogisticGroup(e.name(), LogisticGroupType.INPUT, v);
                    }
                    case OUTPUT -> {
                        v.addPreUpdateHandler(RebarUtils.DISALLOW_PLAYERS_FROM_ADDING_ITEMS_HANDLER);
                        v.addPostUpdateHandler(event -> tryStartRecipe(recipeProcessor, fluidBuffer));
                        logistic.createLogisticGroup(e.name(), LogisticGroupType.OUTPUT, v);
                    }
                    case BOTH -> {
                        v.addPreUpdateHandler(RebarUtils.DISALLOW_PLAYERS_FROM_ADDING_ITEMS_HANDLER);
                        v.addPostUpdateHandler(event -> {
                            if (!(event.getUpdateReason() instanceof MachineUpdateReason)) {
                                tryStartRecipe(recipeProcessor, fluidBuffer);
                            }
                        });
                        logistic.createLogisticGroup(e.name(), LogisticGroupType.BOTH, v);
                    }
                }
                logistic.createLogisticGroup(e.name(), e.slotType(), v);
            }

            callScriptA("onPostInitialise", args);
        }

        private boolean canOutputFluid(FluidBufferRebarBlock fluidBuffer, Map<RebarFluid, Double> results, FluidBufferBlockData fluidBufferBlockData) {
            for (var e : results.entrySet()) {
                if (!fluidBuffer.hasFluid(e.getKey())
                        || !fluidBufferBlockData.outputFluids().contains(e.getKey())
                        || fluidBuffer.fluidAmount(e.getKey()) + e.getValue() > fluidBuffer.fluidCapacity(e.getKey())) {
                    return false;
                }
            }
            return true;
        }

        private boolean canOutputItems(Object2IntLinkedOpenHashMap<ItemStack> results, VirtualInventory inventory) {
            int[] lefts = inventory.simulateAdd(results.sequencedKeySet().stream().toList());

            for (final int i : lefts) if (i != 0) return false;

            return true;
        }

        private static Object2DoubleOpenHashMap<RebarFluid> countOutputFluids(RebarRecipe recipe) {
            if (recipe instanceof CustomRecipe cr && cr.getCountOutputFluids() != null) {
                return cr.getCountOutputFluids();
            }

            var ret = new Object2DoubleOpenHashMap<RebarFluid>();
            for (var e : recipe.getResults()) {
                if (e instanceof FluidOrItem.Fluid fluid) {
                    ret.addTo(fluid.fluid(), fluid.amountMillibuckets());
                }
            }

            if (recipe instanceof CustomRecipe cr) {
                cr.setCountOutputFluids(ret);
            }

            return ret;
        }

        private static Object2IntLinkedOpenHashMap<ItemStack> countResults(RebarRecipe recipe) {
            if (recipe instanceof CustomRecipe cr && cr.getCountOutputItems() != null) {
                return cr.getCountOutputItems();
            }

            var ret = new Object2IntLinkedOpenHashMap<ItemStack>();
            for (var e : recipe.getResults()) {
                if (e instanceof FluidOrItem.Item item) {
                    var it = item.item();
                    ret.addTo(it.asOne(), it.getAmount());
                }
            }

            if (recipe instanceof CustomRecipe cr) {
                cr.setCountOutputItems(ret);
            }

            return ret;
        }

        private static ItemStackBuilder getRepresentativeIcon(RebarRecipe recipe) {
            for (var r : recipe.getResults()) {
                if (r instanceof FluidOrItem.Item item) {
                    return ItemStackBuilder.of(item.item().asOne()).clearLore();
                }
                if (r instanceof FluidOrItem.Fluid fluid) {
                    return ItemStackBuilder.of(fluid.fluid().getItem().asOne()).clearLore();
                }
            }

            return ItemStackBuilder.of(Material.IRON_PICKAXE);
        }

        public void tryStartRecipe(RecipeProcessorRebarBlock<CustomRecipe> recipeProcessor, FluidBufferRebarBlock fluidBuffer) {
            if (recipeProcessor.isProcessingRecipe() || loadRecipeType == null) {
                return;
            }

            Collection<? extends RebarRecipe> recipes = loadRecipeType.getRecipes();
            @Nullable VirtualInventory vi = vs.get('i');
            @Nullable VirtualInventory vo = vs.get('o');
            recipe: for (RebarRecipe recipe : recipes) {
                for (RecipeInput e : recipe.getInputs()) {
                    switch (e) {
                        case RecipeInput.Item item -> {
                            if (logisticBlockData == null || vi == null) continue recipe;
                            if (!vi.contains(item::matches)) continue recipe;
                            if (vi.count(item::matches) < item.getAmount()) continue recipe;
                        }
                        case RecipeInput.Fluid fluid -> {
                            if (fluidBufferBlockData == null) continue recipe;
                            boolean enough = false;
                            for (RebarFluid f : fluid.fluids()) {
                                if (fluidBuffer.hasFluid(f) && fluidBuffer.fluidAmount(f) >= fluid.amountMillibuckets() && fluidBufferBlockData.inputFluids().contains(f)) {
                                    enough = true;
                                    break;
                                }
                            }
                            if (!enough) continue recipe;
                        }
                        default -> {
                            continue recipe;
                        }
                    }
                }

                var ret = countResults(recipe);
                if (!ret.isEmpty() && (vo == null || (vo != null && logisticBlockData != null && !canOutputItems(ret, vo)))) {
                    continue;
                }
                if (fluidBufferBlockData != null && !canOutputFluid(fluidBuffer, countOutputFluids(recipe), fluidBufferBlockData)) {
                    continue;
                }

                if (recipe instanceof CustomRecipe cr) {
                    if (!cr.getOther().isEmpty()) {
                        if (!handleRecipeOther(this, recipe, logisticBlockData, fluidBufferBlockData, loadRecipeType)) {
                            continue;
                        }
                    }
                }

                // found recipe
                if (recipe instanceof CustomRecipe cr) {
                    int totalSeconds = (int) Math.round((double) cr.getTimeSeconds() / getSpeed());
                    recipeProcessor.startRecipe(cr, totalSeconds);
                    recipeProcessor.getRecipeProgressItem().setItem(getRepresentativeIcon(recipe));
                }

                // consume items and fluids
                for (var e : recipe.getInputs()) {
                    if (e instanceof RecipeInput.Item item) {
                        int remainToConsume = item.getAmount();
                        for (int i = 0; i < vi.getSize(); i++) {
                            ItemStack stack = vi.getItem(i);
                            if (stack == null || stack.getType().isAir()) continue;
                            if (item.matches(stack)) {
                                int consume = Math.min(remainToConsume, stack.getAmount());
                                vi.setItemAmount(UpdateReason.SUPPRESSED, i, stack.getAmount() - consume);
                                remainToConsume -= consume;
                            }
                        }
                    }
                    if (e instanceof RecipeInput.Fluid fluid) {
                        for (var f : fluid.fluids()) {
                            if (fluidBuffer.hasFluid(f) && fluidBuffer.fluidAmount(f) >= fluid.amountMillibuckets() && fluidBufferBlockData.inputFluids().contains(f)) {
                                fluidBuffer.removeFluid(f, fluid.amountMillibuckets());
                            }
                        }
                    }
                }
            }
        }

        public boolean handleRecipeOther(@Nullable Object... args) {
            var v = callScriptA("handleRecipeOther", args);
            if (v instanceof Boolean b) {
                return b;
            }
            return true;
        }

        public int getSpeed() {
            if (isFunctionExists("getSpeed")) {
                var v = callScriptA("getSpeed", this);
                if (v instanceof Number n) {
                    return n.intValue();
                }
            }

            var settings = ConfigSection.fromSettings(getKey());
            return settings.get("speed", ConfigAdapter.INTEGER, 1);
        }
    }
}
