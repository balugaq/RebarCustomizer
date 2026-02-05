package com.balugaq.pc.script.callbacks;

import com.balugaq.pc.GlobalVars;
import com.balugaq.pc.RebarCustomizer;
import com.balugaq.pc.config.Pack;
import com.balugaq.pc.config.PackDesc;
import com.balugaq.pc.manager.PackManager;
import com.caoccao.javet.annotations.V8Function;
import io.github.pylonmc.rebar.addon.RebarAddon;
import io.github.pylonmc.rebar.block.RebarBlockSchema;
import io.github.pylonmc.rebar.entity.RebarEntitySchema;
import io.github.pylonmc.rebar.fluid.RebarFluid;
import io.github.pylonmc.rebar.item.RebarItemSchema;
import io.github.pylonmc.rebar.registry.RebarRegistry;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import java.util.logging.Logger;

/**
 * @author lijinhong11
 * @author balugaq
 */
@NullMarked
public class APICallbacks {
    @V8Function
    public Logger getLogger() {
        return RebarCustomizer.getInstance().getLogger();
    }

    @V8Function
    public RebarRegistry<RebarItemSchema> getItemRegistry() {
        return RebarRegistry.ITEMS;
    }

    @V8Function
    public RebarRegistry<RebarBlockSchema> getBlockRegistry() {
        return RebarRegistry.BLOCKS;
    }

    @V8Function
    public RebarRegistry<RebarEntitySchema> getEntityRegistry() {
        return RebarRegistry.ENTITIES;
    }

    @V8Function
    public RebarRegistry<RebarFluid> getFluidRegistry() {
        return RebarRegistry.FLUIDS;
    }

    @V8Function
    public RebarRegistry<RebarAddon> getAddonRegistry() {
        return RebarRegistry.ADDONS;
    }

    @V8Function
    public NamespacedKey createKey(String namespace, String key) {
        return new NamespacedKey(namespace, key);
    }

    @V8Function
    public void sendMessage(Player player, String message) {
        player.sendMessage(GlobalVars.COMPONENT_SERIALIZER.deserialize(message));
    }

    @V8Function
    @Nullable
    public Pack getPackById(String packId) {
        return PackManager.findPack(new PackDesc(packId));
    }

    @V8Function
    @Nullable
    public ItemStack getSaveditemById(Pack pack, String itemId) {
        return PackManager.getSaveditemById(pack, itemId);
    }
}
