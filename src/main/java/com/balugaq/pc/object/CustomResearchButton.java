package com.balugaq.pc.object;

import com.balugaq.pc.util.Debug;
import io.github.pylonmc.rebar.guide.button.ResearchButton;
import io.github.pylonmc.rebar.guide.pages.research.ResearchItemsPage;
import io.github.pylonmc.rebar.item.research.Research;
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
 * - onPreRightClick
 * - onPreMiddleClick
 * - onPostClick
 * - onPostLeftClick
 * - onPostRightClick
 * - onPostMiddleClick
 * - onOtherClick
 * 
 * @author balugaq
 */
@Getter
@NullMarked
public class CustomResearchButton extends ResearchButton implements Scriptable {
    private final NamespacedKey key;

    public CustomResearchButton(NamespacedKey key, Research research) {
        super(research);
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
                if (getResearch().isResearchedBy(player) || getResearch().getCost() == null || getResearch().getCost() > Research.getResearchPoints(player)) {
                    return;
                }
                getResearch().addTo(player);
                Research.setResearchPoints(player, Research.getResearchPoints(player) - getResearch().getCost());
                notifyWindows();
                callScriptA("onPostLeftClick", this, clickType, player, click);
            } else if (clickType.isRightClick()) {
                var v = callScriptA("onPreRightClick", this, clickType, player, click);
                if (v instanceof Boolean cancelled && cancelled) return;
                new ResearchItemsPage(getResearch()).open(player);
                callScriptA("onPostRightClick", this, clickType, player, click);
            } else if (clickType == ClickType.MIDDLE) {
                var v = callScriptA("onPreMiddleClick", this, clickType, player, click);
                if (v instanceof Boolean cancelled && cancelled) return;
                if (player.hasPermission("pylon.command.research.modify")) {
                    getResearch().addTo(player);
                    this.notifyWindows();
                }
                callScriptA("onPostMiddleClick", this, clickType, player, click);
            } else {
                callScriptA("onOtherClick", this, clickType, player, click);
            }
        } catch (Exception e) {
            Debug.trace(e);
        }
        callScriptA("onPostClick", this, clickType, player, click);
    }
}