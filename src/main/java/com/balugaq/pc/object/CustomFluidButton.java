package com.balugaq.pc.object;

import com.balugaq.pc.util.Debug;
import io.github.pylonmc.rebar.fluid.RebarFluid;
import io.github.pylonmc.rebar.guide.button.FluidButton;
import io.github.pylonmc.rebar.guide.pages.fluid.FluidRecipesPage;
import io.github.pylonmc.rebar.guide.pages.fluid.FluidUsagesPage;
import lombok.Getter;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jspecify.annotations.NullMarked;
import xyz.xenondevs.invui.Click;

/**
 * {@link Scriptable} proxy methods:
 * - onPreClick
 * - onPreLeftClick
 * - onPreOtherClick
 * - onPostClick
 * - onPostLeftClick
 * - onPostOtherClick
 * 
 * @author balugaq
 */
@NullMarked
public class CustomFluidButton extends FluidButton implements Scriptable {
    @Getter
    private final NamespacedKey key;
    public CustomFluidButton(NamespacedKey key, RebarFluid fluid) {
        super(fluid);
        this.key = key;
    }

    @Override
    public void handleClick(ClickType clickType, Player player, Click click) {
        try {
            var v2 = callScriptA("onPreClick", this, clickType, player, click);
            if (v2 instanceof Boolean cancelled && cancelled) return;
            if (clickType.isLeftClick()) {
                var v = callScriptA("onPreLeftClick", this, clickType, player, click);
                if (v instanceof Boolean cancelled && cancelled) return;
                FluidRecipesPage page = new FluidRecipesPage(getCurrentFluid().getKey());
                if (!page.getPages().isEmpty()) {
                    page.open(player);
                }
                callScriptA("onPostLeftClick", this, clickType, player, click);
            } else {
                var v = callScriptA("onPreOtherClick", this, clickType, player, click);
                if (v instanceof Boolean cancelled && cancelled) return;
                FluidUsagesPage page = new FluidUsagesPage(getCurrentFluid());
                if (!page.getPages().isEmpty()) {
                    page.open(player);
                }
                callScriptA("onPostOtherClick", this, clickType, player, click);
            }
            callScriptA("onPostClick", this, clickType, player, click);
        } catch (Exception e) {
            Debug.trace(e);
        }
    }
}