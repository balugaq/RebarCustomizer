package com.balugaq.pc.object.blocks;

import com.balugaq.pc.GlobalVars;
import com.balugaq.pc.config.FluidBlockData;
import com.balugaq.pc.config.FluidBufferBlockData;
import com.balugaq.pc.config.GuiData;
import com.balugaq.pc.config.LogisticBlockData;
import com.balugaq.pc.object.CustomRecipe;
import com.balugaq.pc.object.CustomRecipeType;
import com.balugaq.pc.object.RuntimeObject;
import com.balugaq.pc.object.Scriptable;
import com.destroystokyo.paper.event.block.BeaconEffectEvent;
import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import io.github.pylonmc.rebar.block.RebarBlock;
import io.github.pylonmc.rebar.block.base.RebarBeacon;
import io.github.pylonmc.rebar.block.base.RebarBell;
import io.github.pylonmc.rebar.block.base.RebarBreakHandler;
import io.github.pylonmc.rebar.block.base.RebarCampfire;
import io.github.pylonmc.rebar.block.base.RebarCauldron;
import io.github.pylonmc.rebar.block.base.RebarComposter;
import io.github.pylonmc.rebar.block.base.RebarFlowerPot;
import io.github.pylonmc.rebar.block.base.RebarFluidBlock;
import io.github.pylonmc.rebar.block.base.RebarFluidBufferBlock;
import io.github.pylonmc.rebar.block.base.RebarGrowable;
import io.github.pylonmc.rebar.block.base.RebarGuiBlock;
import io.github.pylonmc.rebar.block.base.RebarInteractBlock;
import io.github.pylonmc.rebar.block.base.RebarJumpBlock;
import io.github.pylonmc.rebar.block.base.RebarLeaf;
import io.github.pylonmc.rebar.block.base.RebarLectern;
import io.github.pylonmc.rebar.block.base.RebarLogisticBlock;
import io.github.pylonmc.rebar.block.base.RebarNoVanillaContainerBlock;
import io.github.pylonmc.rebar.block.base.RebarNoteBlock;
import io.github.pylonmc.rebar.block.base.RebarPiston;
import io.github.pylonmc.rebar.block.base.RebarRecipeProcessor;
import io.github.pylonmc.rebar.block.base.RebarRedstoneBlock;
import io.github.pylonmc.rebar.block.base.RebarShearable;
import io.github.pylonmc.rebar.block.base.RebarSign;
import io.github.pylonmc.rebar.block.base.RebarSneakableBlock;
import io.github.pylonmc.rebar.block.base.RebarSponge;
import io.github.pylonmc.rebar.block.base.RebarTNT;
import io.github.pylonmc.rebar.block.base.RebarTargetBlock;
import io.github.pylonmc.rebar.block.base.RebarTickingBlock;
import io.github.pylonmc.rebar.block.base.RebarTrialVault;
import io.github.pylonmc.rebar.block.base.RebarUnloadBlock;
import io.github.pylonmc.rebar.block.context.BlockBreakContext;
import io.github.pylonmc.rebar.block.context.BlockCreateContext;
import io.github.pylonmc.rebar.config.adapter.ConfigAdapter;
import io.github.pylonmc.rebar.event.RebarBlockUnloadEvent;
import io.github.pylonmc.rebar.fluid.RebarFluid;
import io.github.pylonmc.rebar.i18n.RebarArgument;
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
import io.papermc.paper.event.block.BeaconActivatedEvent;
import io.papermc.paper.event.block.BeaconDeactivatedEvent;
import io.papermc.paper.event.block.CompostItemEvent;
import io.papermc.paper.event.block.PlayerShearBlockEvent;
import io.papermc.paper.event.block.TargetHitEvent;
import io.papermc.paper.event.entity.EntityCompostItemEvent;
import io.papermc.paper.event.player.PlayerChangeBeaconEffectEvent;
import io.papermc.paper.event.player.PlayerFlowerPotManipulateEvent;
import io.papermc.paper.event.player.PlayerInsertLecternBookEvent;
import io.papermc.paper.event.player.PlayerLecternPageChangeEvent;
import io.papermc.paper.event.player.PlayerOpenSignEvent;
import it.unimi.dsi.fastutil.chars.Char2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BellResonateEvent;
import org.bukkit.event.block.BellRingEvent;
import org.bukkit.event.block.BlockCookEvent;
import org.bukkit.event.block.BlockFertilizeEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.CauldronLevelChangeEvent;
import org.bukkit.event.block.InventoryBlockStartEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.block.NotePlayEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.block.SpongeAbsorbEvent;
import org.bukkit.event.block.TNTPrimeEvent;
import org.bukkit.event.block.VaultDisplayItemEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTakeLecternBookEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.inventory.VirtualInventory;
import xyz.xenondevs.invui.inventory.event.UpdateReason;
import xyz.xenondevs.invui.window.Window;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * {@link Scriptable} proxy methods:
 * - onPreInteract
 * - onPostInteract
 * - onFlowerPotManipulated
 * - onActivated
 * - onDeactivated
 * - onEffectChange
 * - onEffectApply
 * - onRing
 * - onResonate
 * - preBreak
 * - onBreak
 * - postBreak
 * - onStartCooking
 * - onEndCooking
 * - onLevelChange
 * - onCompostByHopper
 * - onCompostByEntity
 * - onInventoryOpen
 * - onItemMoveTo
 * - onItemMoveFrom
 * - onDecayNaturally
 * - onJumpedOn
 * - onGrow
 * - onFertilize
 * - onInsertBook
 * - onRemoveBook
 * - onChangePage
 * - preOnTick
 * - onTick
 * - getTickInterval
 * - isAsync
 * - getSpeed
 * - onNotePlay
 * - onCurrentChange
 * - onShear
 * - onAbsorb
 * - onIgnite
 * - onHit
 * - onDisplayItem
 * - onUnload
 * - onExtend
 * - onRetract
 * - onSignChange
 * - onOpen
 * - onSneakedOn
 * - onUnsneakedOn
 * - createGui
 * - onPostInitialise
 *
 * @author balugaq
 */
@NullMarked
public class CustomBlock extends RebarBlock implements RebarInteractBlock, RebarTickingBlock, RebarNoVanillaContainerBlock,
                                                       RebarBeacon, RebarBell, RebarBreakHandler, RebarCampfire, RebarCauldron,
                                                       RebarComposter, RebarFlowerPot, RebarFluidBlock, RebarFluidBufferBlock,
                                                       RebarGrowable, RebarJumpBlock, RebarLeaf, RebarLectern, RebarNoteBlock,
                                                       RebarPiston, RebarRedstoneBlock, RebarShearable, RebarSign, RebarSneakableBlock,
                                                       RebarSponge, RebarTargetBlock, RebarTNT, RebarTrialVault, RebarUnloadBlock,
                                                       RebarGuiBlock, RebarLogisticBlock, RebarRecipeProcessor<CustomRecipe>, RuntimeObject {
    private final Char2ObjectOpenHashMap<VirtualInventory> vs = new Char2ObjectOpenHashMap<>();
    private final @Nullable RecipeType<?> loadRecipeType = RebarRegistry.RECIPE_TYPES.get(getKey());
    private final @Nullable GuiData guiData = GlobalVars.getGuiData(getKey());
    private final @Nullable LogisticBlockData logisticBlockData = GlobalVars.getLogisticBlockData(getKey());
    private final @Nullable FluidBlockData fluidBlockData = GlobalVars.getFluidBlockData(getKey());
    private final @Nullable FluidBufferBlockData fluidBufferBlockData = GlobalVars.getFluidBufferBlockData(getKey());

    public CustomBlock(final Block block) {
        super(block);
    }

    public CustomBlock(final Block block, final PersistentDataContainer pdc) {
        super(block, pdc);
    }

    public CustomBlock(final Block block, final BlockCreateContext context) {
        super(block, context);
        if (fluidBlockData != null) {
            for (var e : fluidBlockData) {
                createFluidPoint(e.fluidPointType(), e.face(), context, e.allowVerticalFaces());
            }
        }
        if (fluidBufferBlockData != null) {
            for (var e : fluidBufferBlockData) {
                createFluidBuffer(e.fluid(), e.capacity(), e.input(), e.output());
            }
        }
        setRecipeProgressItem(new ProgressItem(GuiItems.background()));
    }

    @Override
    public void onInteract(final PlayerInteractEvent event, final EventPriority priority) {
        var v = callScriptA("onPreInteract", this, event, priority);
        if (v instanceof Boolean cancelled && cancelled) return;

        if (!event.getAction().isRightClick()
                || event.getPlayer().isSneaking()
                || event.getHand() != EquipmentSlot.HAND
                || event.useInteractedBlock() == Event.Result.DENY) {
            return;
        }

        if (guiData != null) {
            event.setUseInteractedBlock(Event.Result.DENY);
            event.setUseItemInHand(Event.Result.DENY);
            Window.builder()
                    .setUpperGui(createGui())
                    .setTitle(getGuiTitle())
                    .setViewer(event.getPlayer())
                    .build()
                    .open();
        }

        callScriptA("onPostInteract", this, event, priority);
    }

    @Override
    public void onFlowerPotManipulated(final PlayerFlowerPotManipulateEvent event, final EventPriority priority) {
        callScript(this, event, priority);
    }

    @Override
    public void onActivated(final BeaconActivatedEvent event, final EventPriority priority) {
        callScript(this, event, priority);
    }

    @Override
    public void onDeactivated(final BeaconDeactivatedEvent event, final EventPriority priority) {
        callScript(this, event, priority);
    }

    @Override
    public void onEffectChange(final PlayerChangeBeaconEffectEvent event, final EventPriority priority) {
        callScript(this, event, priority);
    }

    @Override
    public void onEffectApply(final BeaconEffectEvent event, final EventPriority priority) {
        callScript(this, event, priority);
    }

    @Override
    public void onRing(final BellRingEvent event, final EventPriority priority) {
        callScript(this, event, priority);
    }

    @Override
    public void onResonate(final BellResonateEvent event, final EventPriority priority) {
        callScript(this, event, priority);
    }

    @Override
    public boolean preBreak(final BlockBreakContext context) {
        var v = callScript(this, context);
        if (v == null) return true;
        if (v instanceof Boolean b) {
            return b;
        }
        return false;
    }

    @Override
    public void onBreak(final List<ItemStack> drops, final BlockBreakContext context) {
        RebarFluidBufferBlock.super.onBreak(drops, context);
        callScript(this, drops, context);
    }

    @Override
    public void postBreak(final BlockBreakContext context) {
        callScript(this, context);
    }

    @Override
    public void onStartCooking(final InventoryBlockStartEvent event, final EventPriority priority) {
        callScript(this, event, priority);
    }

    @Override
    public void onEndCooking(final BlockCookEvent event, final EventPriority priority) {
        callScript(this, event, priority);
    }

    @Override
    public void onLevelChange(final CauldronLevelChangeEvent event, final EventPriority priority) {
        callScript(this, event, priority);
    }

    @Override
    public void onCompostByHopper(final CompostItemEvent event, final EventPriority priority) {
        callScript(this, event, priority);
    }

    @Override
    public void onCompostByEntity(final EntityCompostItemEvent event, final EventPriority priority) {
        callScript(this, event, priority);
    }

    @Override
    public void onInventoryOpen(final InventoryOpenEvent event, final EventPriority priority) {
        callScript(this, event, priority);
    }

    @Override
    public void onItemMoveTo(final InventoryMoveItemEvent event, final EventPriority priority) {
        callScript(this, event, priority);
    }

    @Override
    public void onItemMoveFrom(final InventoryMoveItemEvent event, final EventPriority priority) {
        callScript(this, event, priority);
    }

    @Override
    public void onDecayNaturally(final LeavesDecayEvent event, final EventPriority priority) {
        callScript(this, event, priority);
    }

    @Override
    public void onJumpedOn(final PlayerJumpEvent event, final EventPriority priority) {
        callScript(this, event, priority);
    }

    @Override
    public void onGrow(final BlockGrowEvent event, final EventPriority priority) {
        callScript(this, event, priority);
    }

    @Override
    public void onFertilize(final BlockFertilizeEvent event, final EventPriority priority) {
        callScript(this, event, priority);
    }

    @Override
    public void onInsertBook(final PlayerInsertLecternBookEvent event, final EventPriority priority) {
        callScript(this, event, priority);
    }

    @Override
    public void onRemoveBook(final PlayerTakeLecternBookEvent event, final EventPriority priority) {
        callScript(this, event, priority);
    }

    @Override
    public void onChangePage(final PlayerLecternPageChangeEvent event, final EventPriority priority) {
        callScript(this, event, priority);
    }

    @Override
    public int getTickInterval() {
        if (!isFunctionExists("onTick")) {
            if (loadRecipeType == null) {
                setTickInterval(Integer.MAX_VALUE);
                return Integer.MAX_VALUE;
            }
        }
        if (isFunctionExists("getTickInterval")) {
            var v = callScript(this);
            if (v instanceof Number number) {
                var v2 = number.intValue();
                setTickInterval(v2);
                return v2;
            }
        }
        var settings = getSettingsOrNull();
        if (settings == null) return RebarTickingBlock.super.getTickInterval();
        var v3 = settings.get("tick-interval", ConfigAdapter.INTEGER, RebarTickingBlock.super.getTickInterval());
        setTickInterval(v3);
        return v3;
    }

    @Override
    public boolean isAsync() {
        if (isFunctionExists("isAsync")) {
            var v = callScript(this);
            if (v instanceof Boolean b) {
                setAsync(b);
                return b;
            }
        }
        var settings = getSettingsOrNull();
        if (settings == null) return RebarTickingBlock.super.isAsync();
        var v2 = settings.get("async", ConfigAdapter.BOOLEAN, RebarTickingBlock.super.isAsync());
        setAsync(v2);
        return v2;
    }

    @Override
    public void onRecipeFinished(CustomRecipe recipe) {
        getRecipeProgressItem().setItem(GuiItems.background());
        // push item or fluid
        for (var e : recipe.getResults()) {
            if (e instanceof FluidOrItem.Item item) {
                var vo = vs.get('o');
                vo.addItem(UpdateReason.SUPPRESSED, item.item());
            }
            if (e instanceof FluidOrItem.Fluid fluid) {
                setFluid(fluid.fluid(), Math.min(fluidCapacity(fluid.fluid()), fluidAmount(fluid.fluid()) + fluid.amountMillibuckets()));
            }
        }
    }

    @Override
    public void tick() {
        var v = callScriptA("preOnTick", this);
        if (v instanceof Boolean cancelled && cancelled) return;
        if (isProcessingRecipe()) {
            progressRecipe(1);
        } else {
            tryStartRecipe();
        }
        callScript("onTick", this);
    }

    public void tryStartRecipe() {
        if (isProcessingRecipe() || loadRecipeType == null) {
            return;
        }

        Collection<? extends RebarRecipe> recipes = loadRecipeType.getRecipes();
        @Nullable var vi = vs.get('i');
        @Nullable var vo = vs.get('o');
        recipe: for (RebarRecipe recipe : recipes) {
            for (var e : recipe.getInputs()) {
                switch (e) {
                    case RecipeInput.Item item -> {
                        if (logisticBlockData == null || vi == null) continue recipe;
                        if (!vi.contains(item::contains)) continue recipe;
                        if (vi.count(item::contains) < item.getAmount()) continue recipe;
                    }
                    case RecipeInput.Fluid fluid -> {
                        if (fluidBufferBlockData == null) continue recipe;
                        boolean enough = false;
                        for (var f : fluid.fluids()) {
                            if (hasFluid(f) && fluidAmount(f) >= fluid.amountMillibuckets() && fluidBufferBlockData.inputFluids().contains(f)) {
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
            if (fluidBufferBlockData != null && !canOutputFluid(countOutputFluids(recipe), fluidBufferBlockData)) {
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
                startRecipe(cr, totalSeconds);
                getRecipeProgressItem().setItem(getRepresentativeIcon(recipe));
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
                        if (hasFluid(f) && fluidAmount(f) >= fluid.amountMillibuckets() && fluidBufferBlockData.inputFluids().contains(f)) {
                            removeFluid(f, fluid.amountMillibuckets());
                        }
                    }
                }
            }
        }
    }

    public boolean handleRecipeOther(@Nullable Object... args) {
        var v = callScript(this, args);
        if (v instanceof Boolean b) {
            return b;
        }
        return true;
    }

    public int getSpeed() {
        if (isFunctionExists("getSpeed")) {
            var v = callScript(this);
            if (v instanceof Number n) {
                return n.intValue();
            }
        }

        var settings = getSettingsOrNull();
        if (settings == null) return 1;
        return settings.get("speed", ConfigAdapter.INTEGER, 1);
    }

    @Override
    public void onNotePlay(final NotePlayEvent event, final EventPriority priority) {
        callScript(this, event, priority);
    }

    @Override
    public void onCurrentChange(final BlockRedstoneEvent event, final EventPriority priority) {
        callScript(this, event, priority);
    }

    @Override
    public void onShear(final PlayerShearBlockEvent event, final EventPriority priority) {
        callScript(this, event, priority);
    }

    @Override
    public void onAbsorb(final SpongeAbsorbEvent event, final EventPriority priority) {
        callScript(this, event, priority);
    }

    @Override
    public void onIgnite(final TNTPrimeEvent event, final EventPriority priority) {
        callScript(this, event, priority);
    }

    @Override
    public void onHit(final TargetHitEvent event, final EventPriority priority) {
        callScript(this, event, priority);
    }

    @Override
    public void onDisplayItem(final VaultDisplayItemEvent event, final EventPriority priority) {
        callScript(this, event, priority);
    }

    @Override
    public void onUnload(final RebarBlockUnloadEvent event, final EventPriority priority) {
        callScript(this, event, priority);
    }

    @Override
    public void onExtend(final BlockPistonExtendEvent event, final EventPriority priority) {
        callScript(this, event, priority);
    }

    @Override
    public void onRetract(final BlockPistonRetractEvent event, final EventPriority priority) {
        callScript(this, event, priority);
    }

    @Override
    public void onSignChange(final SignChangeEvent event, final EventPriority priority) {
        callScript(this, event, priority);
    }

    @Override
    public void onOpen(final PlayerOpenSignEvent event, final EventPriority priority) {
        callScript(this, event, priority);
    }

    @Override
    public void onSneakedOn(final PlayerToggleSneakEvent event, final EventPriority priority) {
        callScript(this, event, priority);
    }

    @Override
    public void onUnsneakedOn(final PlayerToggleSneakEvent event, final EventPriority priority) {
        callScript(this, event, priority);
    }

    @Override
    public Gui createGui() {
        if (isFunctionExists("createGui")) {
            var v = callScript(this);
            if (v instanceof Gui gui) {
                return gui;
            }
        }

        if (!havePostInitialised) postInitialise();
        return CustomRecipeType.makeGui(guiData, vs, getRecipeProgressItem());
    }

    boolean havePostInitialised = false;

    @Override
    public void postInitialise() {
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
                            tryStartRecipe();
                        }
                    });
                    createLogisticGroup(e.name(), LogisticGroupType.INPUT, v);
                }
                case OUTPUT -> {
                    v.addPreUpdateHandler(RebarUtils.DISALLOW_PLAYERS_FROM_ADDING_ITEMS_HANDLER);
                    v.addPostUpdateHandler(event -> tryStartRecipe());
                    createLogisticGroup(e.name(), LogisticGroupType.OUTPUT, v);
                }
                case BOTH -> {
                    v.addPreUpdateHandler(RebarUtils.DISALLOW_PLAYERS_FROM_ADDING_ITEMS_HANDLER);
                    v.addPostUpdateHandler(event -> {
                        if (!(event.getUpdateReason() instanceof MachineUpdateReason)) {
                            tryStartRecipe();
                        }
                    });
                    createLogisticGroup(e.name(), LogisticGroupType.BOTH, v);
                }
            }
            createLogisticGroup(e.name(), e.slotType(), v);
        }

        callScript("onPostInitialise", this);
    }

    private boolean canOutputFluid(Map<RebarFluid, Double> results, FluidBufferBlockData fluidBufferBlockData) {
        for (var e : results.entrySet()) {
            if (!hasFluid(e.getKey())
            || !fluidBufferBlockData.outputFluids().contains(e.getKey())
            || fluidAmount(e.getKey()) + e.getValue() > fluidCapacity(e.getKey())) {
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

    @Override
    public List<RebarArgument> getPlaceholders() {
        return RuntimeObject.super.getPlaceholders();
    }

    @Override
    public WailaDisplay getWaila(Player player) {
        return new WailaDisplay(getDefaultWailaTranslationKey().arguments(getPlaceholders()));
    }
}
