package com.balugaq.pc.gui;

import com.balugaq.pc.RebarCustomizer;
import com.balugaq.pc.rebar.RebarCustomizerKeys;
import io.github.pylonmc.rebar.block.RebarBlock;
import io.github.pylonmc.rebar.block.base.RebarGuiBlock;
import io.github.pylonmc.rebar.item.builder.ItemStackBuilder;
import io.papermc.paper.datacomponent.DataComponentTypes;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;
import xyz.xenondevs.invui.item.AbstractItem;
import xyz.xenondevs.invui.window.Window;

import java.util.Optional;

/**
 * @author balugaq
 */
@SuppressWarnings("UnstableApiUsage")
@Getter
@NullMarked
public class ButtonSet<T extends RebarBlock & RebarGuiBlock> {
    public final T block;
    public AbstractItem
            blackBackground,
            grayBackground,
            inputBorder,
            outputBorder;

    public ButtonSet(T b2) {
        this.block = b2;
        blackBackground = create()
                .item(block -> ItemStackBuilder.rebar(
                        Material.BLACK_STAINED_GLASS_PANE,
                        RebarCustomizerKeys.black_background
                ))
                .click(deny());

        grayBackground = create()
                .item(block -> ItemStackBuilder.rebar(
                        Material.GRAY_STAINED_GLASS_PANE,
                        RebarCustomizerKeys.gray_background
                ))
                .click(deny());

        inputBorder = create()
                .item(block -> ItemStackBuilder.rebar(
                        Material.BLUE_STAINED_GLASS_PANE,
                        RebarCustomizerKeys.input_border
                ))
                .click(deny());

        outputBorder = create()
                .item(block -> ItemStackBuilder.rebar(
                        Material.ORANGE_STAINED_GLASS_PANE,
                        RebarCustomizerKeys.output_border
                ))
                .click(deny());
    }

    public GuiItem<T> create() {
        return GuiItem.create(block);
    }

    public static <T extends RebarBlock & RebarGuiBlock> ClickHandler<T> deny() {
        return (data, clickType, player, click) -> {
            return false;
        };
    }

    public static <T extends RebarBlock & RebarGuiBlock, K> K assertBlock(T block, Class<K> expected) {
        return GuiItem.assertBlock(block, expected);
    }

    public static <T> T assertNotNull(@Nullable T o, String message) {
        return GuiItem.assertNotNull(o, message);
    }

    public static void done(Player player, String literal, Object... args) {
        GuiItem.done(player, literal, args);
    }

    public static void assertTrue(boolean stmt, String message) {
        GuiItem.assertTrue(stmt, message);
    }

    public static void done(Player player, ComponentLike component) {
        GuiItem.done(player, component);
    }

    public void reopen(Player player) {
        RebarCustomizer.runTaskLater(
                () -> {
                    Window.builder()
                            .setUpperGui(getBlock().createGui())
                            .setTitle(getBlock().getNameTranslationKey())
                            .setViewer(player)
                            .build()
                            .open();
                }, 1L
        );
    }

    public static Component displayName(ItemStack itemStack) {
        return Optional.ofNullable(itemStack.getData(DataComponentTypes.ITEM_NAME)).orElse(Component.text(""));
    }

    public static <T extends RebarBlock & RebarGuiBlock> ClickHandler<T> allow() {
        return (data, clickType, player, click) -> false;
    }

    public static <T> T assertNotNull(@Nullable T o) {
        return GuiItem.assertNotNull(o);
    }

    public static void assertFalse(boolean stmt, String message) {
        GuiItem.assertFalse(stmt, message);
    }

    public static boolean isOutput(int n) {
        return n > 1000;
    }
}
