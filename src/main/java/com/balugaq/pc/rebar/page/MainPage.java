package com.balugaq.pc.rebar.page;

import com.balugaq.pc.rebar.RebarCustomizerKeys;
import io.github.pylonmc.rebar.content.guide.RebarGuide;
import io.github.pylonmc.rebar.guide.pages.base.SimpleStaticGuidePage;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

/**
 * @author balugaq
 */
@NullMarked
public class MainPage {
    private static final SimpleStaticGuidePage MAIN = new SimpleStaticGuidePage(RebarCustomizerKeys.main);

    static {
        RebarGuide.getRootPage().addPage(Material.CLOCK, MAIN);
    }

    public static void addItem(ItemStack itemStack) {
        MAIN.addItem(itemStack);
    }
}
