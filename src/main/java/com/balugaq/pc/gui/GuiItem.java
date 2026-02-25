package com.balugaq.pc.gui;

import com.balugaq.pc.RebarCustomizer;
import com.balugaq.pc.exceptions.JumpoutException;
import com.balugaq.pc.exceptions.WrongStateException;
import com.balugaq.pc.listener.ChatInputListener;
import io.github.pylonmc.rebar.block.RebarBlock;
import io.github.pylonmc.rebar.block.base.RebarGuiBlock;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;
import org.slf4j.helpers.MessageFormatter;
import xyz.xenondevs.invui.Click;
import xyz.xenondevs.invui.item.AbstractItem;
import xyz.xenondevs.invui.item.ItemProvider;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.balugaq.pc.util.Lang.gui_err_1;

/**
 * @author balugaq
 */
@Getter
@NullMarked
public class GuiItem<T extends RebarBlock & RebarGuiBlock> extends AbstractItem {
    private final T data;
    private BiFunction<T, Player, @Nullable ItemProvider> itemProvider;
    private ClickHandler<T> clickHandler;

    public GuiItem(T data) {
        this.data = data;
        this.itemProvider = (block, p) -> null;
        this.clickHandler = denyClick();
    }

    public static <T extends RebarBlock & RebarGuiBlock> GuiItem<T> create(@NotNull T data) {
        return new GuiItem<>(data);
    }

    public static <T> T assertNotNull(@Nullable T o) {
        if (o == null) throw new JumpoutException();
        return o;
    }

    public static <T> T assertNotNull(@Nullable T o, String message) {
        if (o == null) throw new WrongStateException(message);
        return o;
    }

    public static void assertFalse(boolean stmt, String message) {
        assertTrue(!stmt, message);
    }

    public static void assertTrue(boolean stmt, String message) {
        if (!stmt) throw new WrongStateException(message);
    }

    public static void done(Player player, String literal, Object... args) {
        player.sendMessage(MessageFormatter.arrayFormat(literal, args).getMessage());
    }

    public static void done(Player player, ComponentLike component) {
        player.sendMessage(component.asComponent());
    }

    public static <T extends RebarBlock & RebarGuiBlock, K> K assertBlock(T block, Class<K> expected) {
        if (expected.isInstance(block)) {
            return expected.cast(block);
        } else {
            throw new WrongStateException(gui_err_1 + expected.getSimpleName());
        }
    }

    @Nullable
    public static NamespacedKey toNamespacedKey(String string) {
        return NamespacedKey.fromString(string, RebarCustomizer.getInstance());
    }

    public static void waitInput(Player player, String literal, Consumer<String> callback) {
        waitInput(player, Component.text(literal), callback);
    }

    public static void waitInput(Player player, ComponentLike component, Consumer<String> callback) {
        player.sendMessage(component);
        player.closeInventory();
        ChatInputListener.waitInput(player.getUniqueId(), callback);
    }

    public GuiItem<T> item(Function<T, ItemProvider> itemProvider) {
        this.itemProvider = (b, p) -> itemProvider.apply(b);
        return this;
    }

    public GuiItem<T> item(BiFunction<T, Player, ItemProvider> itemProvider) {
        this.itemProvider = itemProvider;
        return this;
    }

    public GuiItem<T> click(ClickHandler<T> clickHandler) {
        this.clickHandler = clickHandler;
        return this;
    }

    @Override
    public @Nullable ItemProvider getItemProvider(Player player) {
        return itemProvider.apply(data, player);
    }

    @Override
    public void handleClick(ClickType clickType, Player player, Click click) {
        try {
            boolean updateWindow = clickHandler.handleClick(data, clickType, player, click);
            if (updateWindow) notifyWindows();
        } catch (Exception e) {
            if (e instanceof JumpoutException) {
                return;
            }
            if (e instanceof WrongStateException) {
                player.sendMessage(e.getMessage());
                return;
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    public static <T extends RebarBlock & RebarGuiBlock> ClickHandler<T> denyClick() {
        return (data, clickType, player, click) -> {
            return false;
        };
    }
}
