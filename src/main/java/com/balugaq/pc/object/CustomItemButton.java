package com.balugaq.pc.object;

import com.balugaq.pc.RebarCustomizer;
import com.balugaq.pc.util.Debug;
import io.github.pylonmc.rebar.guide.button.ItemButton;
import io.github.pylonmc.rebar.guide.pages.item.ItemRecipesPage;
import io.github.pylonmc.rebar.guide.pages.item.ItemUsagesPage;
import io.github.pylonmc.rebar.guide.pages.research.ResearchItemsPage;
import io.github.pylonmc.rebar.i18n.RebarArgument;
import io.github.pylonmc.rebar.item.RebarItem;
import io.github.pylonmc.rebar.item.builder.ItemStackBuilder;
import io.github.pylonmc.rebar.item.research.Research;
import io.papermc.paper.datacomponent.DataComponentTypes;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;
import xyz.xenondevs.invui.Click;
import xyz.xenondevs.invui.item.AbstractItem;
import xyz.xenondevs.invui.item.ItemProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;

/**
 * {@link Scriptable} proxy methods:
 * - onPreClick
 * - onPreLeftClick
 * - onPreShiftLeftClick
 * - onPreRightClick
 * - onPreShiftRightClick
 * - onPreMiddleClick
 * - onPreDropClick
 * - onPreControlDropClick
 * - onPreSwapOffhandClick
 * - onOtherClick
 * - onPostClick
 * - onPostLeftClick
 * - onPostShiftLeftClick
 * - onPostRightClick
 * - onPostShiftRightClick
 * - onPostMiddleClick
 * - onPostDropClick
 * - onPostControlDropClick
 * - onPostSwapOffhandClick
 * 
 * @see ItemButton
 * @author balugaq
 */
@NullMarked
public class CustomItemButton extends AbstractItem implements Scriptable {
    @Getter
    private final NamespacedKey key;
    @Getter
    private final BiFunction<ItemStack, Player, ItemStack> preDisplayDecorator;
    private final List<ItemStack> stacks;
    private final AtomicInteger index = new AtomicInteger(0);

    public CustomItemButton(NamespacedKey key, List<ItemStack> stacks, @Nullable BiFunction<ItemStack, Player, ItemStack> preDisplayDecorator) {
        this.key = key;
        this.stacks = new ArrayList<>(stacks);
        Collections.shuffle(this.stacks);
        this.preDisplayDecorator = preDisplayDecorator == null ? (stack, player) -> stack : preDisplayDecorator;

        if (this.stacks.isEmpty()) {
            throw new IllegalArgumentException("ItemButton must have at least one ItemStack");
        }

        if (this.stacks.size() > 1) {
            startCycleCoroutine();
        }
    }

    public CustomItemButton(NamespacedKey key, ItemStack stack) {
        this(key, toList(stack), null);
    }

    public ItemStack getCurrentStack() {
        return stacks.get(index.get());
    }

    private static List<ItemStack> toList(ItemStack... arrays) {
        return new ArrayList<>(Arrays.asList(arrays));
    }

    private void startCycleCoroutine() {
        RebarCustomizer.runTaskTimerAsync(task -> {
            index.set((index.get() + 1) % stacks.size());
            notifyWindows();
        }, 0, 20);
    }

    @Override
    @SuppressWarnings("UnstableApiUsage")
    public ItemProvider getItemProvider(Player player) {
        try {
            ItemStack displayStack = preDisplayDecorator.apply(getCurrentStack().clone(), player);
            RebarItem item = RebarItem.fromStack(displayStack);

            if (item == null) {
                return new ItemStackBuilder(displayStack);
            }

            ItemStackBuilder builder = new ItemStackBuilder(displayStack.clone());
            if (item.isDisabled()) {
                builder.set(DataComponentTypes.ITEM_MODEL, Material.STRUCTURE_VOID.getKey());
            }

            if (Research.canPlayerCraft(player, item)) {
                builder.set(DataComponentTypes.ITEM_MODEL, Material.BARRIER.getKey())
                        .set(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, false);

                if (item.getResearch() != null) {
                    builder.lore("");
                    builder.lore(Component.translatable(
                            "pylon.pyloncore.guide.button.item.not-researched-with-name",
                            RebarArgument.of("research_name", item.getResearch().getName())
                    ));
                    addResearchCostLore(builder, player, item.getResearch());
                } else {
                    builder.lore(Component.translatable("pylon.pyloncore.guide.button.item.not-researched"));
                }

                builder.lore(Component.translatable("pylon.pyloncore.guide.button.item.research-instructions"));
            }

            return builder;
        } catch (Exception e) {
            Debug.trace(e);
            return ItemStackBuilder.of(Material.BARRIER)
                    .name(Component.translatable("pylon.pyloncore.guide.button.item.error"));
        }
    }

    @Override
    public void handleClick(ClickType clickType, Player player, Click click) {
        try {
            var v2 = callScriptA("onPreClick", this, clickType, player, click);
            if (v2 instanceof Boolean cancelled && cancelled) return;
            switch (clickType) {
                case LEFT -> handleLeftClick(clickType, player, click);
                case SHIFT_LEFT -> handleShiftLeftClick(clickType, player, click);
                case RIGHT -> handleRightClick(clickType, player, click);
                case SHIFT_RIGHT -> handleShiftRightClick(clickType, player, click);
                case MIDDLE -> handleMiddleClick(clickType, player, click);
                case DROP -> handleDropClick(clickType, player, click);
                case CONTROL_DROP -> handleControlDropClick(clickType, player, click);
                case SWAP_OFFHAND -> handleSwapOffhandClick(clickType, player, click);
                default -> handleOtherClick(clickType, player, click);
            }
            callScriptA("onPostClick", this, clickType, player, click);
        } catch (Exception e) {
            Debug.trace(e);
        }
    }

    private void handleOtherClick(ClickType clickType, Player player, Click click) {
        callScriptA("onOtherClick", this, clickType, player, click);
    }

    private void handleLeftClick(ClickType clickType, Player player, Click click) {
        var v = callScriptA("onPreLeftClick", this, clickType, player, click);
        if (v instanceof Boolean cancelled && cancelled) return;
        ItemRecipesPage page = new ItemRecipesPage(getCurrentStack());
        if (!page.getPages().isEmpty()) {
            page.open(player);
        }
        callScriptA("onPostLeftClick", this, clickType, player, click, page);
    }

    private void handleShiftLeftClick(ClickType clickType, Player player, Click click) {
        var v = callScriptA("onPreShiftLeftClick", this, clickType, player, click);
        RebarItem item = RebarItem.fromStack(getCurrentStack());
        if (item == null) return;
        Research research = item.getResearch();
        if (research == null) return;
        if (research.isResearchedBy(player) || research.getCost() == null || research.getCost() > Research.getResearchPoints(player))
            return;
        research.addTo(player, false);
        Research.setResearchPoints(player, Research.getResearchPoints(player) - research.getCost());
        this.notifyWindows();
        callScriptA("onPostShiftLeftClick", this, clickType, player, click, item, research);
    }

    private void handleRightClick(ClickType clickType, Player player, Click click) {
        var v = callScriptA("onPreRightClick", this, clickType, player, click);
        if (v instanceof Boolean cancelled && cancelled) return;
        ItemUsagesPage page = new ItemUsagesPage(getCurrentStack());
        if (!page.getPages().isEmpty()) {
            page.open(player);
        }
        callScriptA("onPostRightClick", this, clickType, player, click, page);
    }

    private void handleShiftRightClick(ClickType clickType, Player player, Click click) {
        var v = callScriptA("onPreShiftRightClick", this, clickType, player, click);
        RebarItem item = RebarItem.fromStack(getCurrentStack());
        if (item != null && item.getResearch() != null && !Research.canPlayerUse(player, item)) {
            new ResearchItemsPage(item.getResearch()).open(player);
        }
        callScriptA("onPostShiftRightClick", this, clickType, player, click, item);
    }

    private void handleMiddleClick(ClickType clickType, Player player, Click click) {
        var v = callScriptA("onPreMiddleClick", this, clickType, player, click);
        if (!player.hasPermission("pylon.guide.cheat")) return;
        ItemStack stack = getCheatItemStack(getCurrentStack(), click);
        stack.setAmount(stack.getMaxStackSize());
        player.setItemOnCursor(stack);
        callScriptA("onPostMiddleClick", this, clickType, player, click, stack);
    }

    private void handleDropClick(ClickType clickType, Player player, Click click) {
        var v = callScriptA("onPreDropClick", this, clickType, player, click);
        if (!player.hasPermission("pylon.guide.cheat")) return;
        ItemStack stack = getCheatItemStack(getCurrentStack(), click);
        stack.setAmount(1);
        player.dropItem(stack);
        callScriptA("onPostDropClick", this, clickType, player, click, stack);
    }

    private void handleControlDropClick(ClickType clickType, Player player, Click click) {
        var v = callScriptA("onPreControlDropClick", this, clickType, player, click);
        if (!player.hasPermission("pylon.guide.cheat")) return;
        ItemStack stack = getCheatItemStack(getCurrentStack(), click);
        stack.setAmount(stack.getMaxStackSize());
        player.dropItem(stack);
        callScriptA("onPostControlDropClick", this, clickType, player, click, stack);
    }

    private void handleSwapOffhandClick(ClickType clickType, Player player, Click click) {
        var v = callScriptA("onPreSwapOffhandClick", this, clickType, player, click);
        if (!player.hasPermission("pylon.guide.cheat")) return;
        ItemStack stack = getCheatItemStack(getCurrentStack(), click);
        stack.setAmount(1);
        player.getInventory().addItem(stack);
        callScriptA("onPostSwapOffhandClick", this, clickType, player, click, stack);
    }

    private static ItemStack getCheatItemStack(ItemStack currentStack, Click click) {
        ItemStack clonedUnknown = currentStack.clone();
        RebarItem pylonItem = RebarItem.fromStack(clonedUnknown);

        if (pylonItem == null) {
            Material type = Registry.MATERIAL.get(clonedUnknown.getType().getKey());
            if (type == null) return new ItemStack(Material.AIR);
            int amount = click.clickType().isShiftClick() ? type.getMaxStackSize() : 1;
            return new ItemStack(type, amount);
        } else {
            ItemStack clonedRebar = pylonItem.getSchema().getItemStack();
            clonedRebar.setAmount(click.clickType().isShiftClick() ? clonedRebar.getMaxStackSize() : 1);
            return clonedRebar;
        }
    }

    public void addResearchCostLore(ItemStackBuilder item, Player player, Research research) {
        if (research.getCost() == null) {
            item.lore(Component.translatable("pylon." + research.key().namespace() + ".researches." + research.key().key() + ".unlock-instructions"));
        } else {
            var playerPoints = Research.getResearchPoints(player);
            item.lore(Component.translatable(
                    "pylon.pyloncore.guide.button.research.cost."
                            + ((research.getCost() > playerPoints) ? "not-enough" : "enough"),
            RebarArgument.of("points", playerPoints),
                    RebarArgument.of("cost", research.getCost())
                ));
        }
    }
}