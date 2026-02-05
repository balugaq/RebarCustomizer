package com.balugaq.pc.object.items;

import com.balugaq.pc.GlobalVars;
import com.balugaq.pc.object.RuntimeObject;
import com.balugaq.pc.object.Scriptable;
import com.destroystokyo.paper.event.player.PlayerReadyArrowEvent;
import io.github.pylonmc.rebar.config.RebarConfig;
import io.github.pylonmc.rebar.config.adapter.ConfigAdapter;
import io.github.pylonmc.rebar.i18n.RebarArgument;
import io.github.pylonmc.rebar.item.RebarItem;
import io.github.pylonmc.rebar.item.base.RebarArmor;
import io.github.pylonmc.rebar.item.base.RebarArrow;
import io.github.pylonmc.rebar.item.base.RebarBlockInteractor;
import io.github.pylonmc.rebar.item.base.RebarBow;
import io.github.pylonmc.rebar.item.base.RebarBrewingStandFuel;
import io.github.pylonmc.rebar.item.base.RebarBucket;
import io.github.pylonmc.rebar.item.base.RebarConsumable;
import io.github.pylonmc.rebar.item.base.RebarDispensable;
import io.github.pylonmc.rebar.item.base.RebarInteractor;
import io.github.pylonmc.rebar.item.base.RebarInventoryEffectItem;
import io.github.pylonmc.rebar.item.base.RebarInventoryTicker;
import io.github.pylonmc.rebar.item.base.RebarItemDamageable;
import io.github.pylonmc.rebar.item.base.RebarItemEntityInteractor;
import io.github.pylonmc.rebar.item.base.RebarLingeringPotion;
import io.github.pylonmc.rebar.item.base.RebarSplashPotion;
import io.github.pylonmc.rebar.item.base.RebarTool;
import io.github.pylonmc.rebar.item.base.RebarUnmergeable;
import io.github.pylonmc.rebar.item.base.RebarWeapon;
import io.github.pylonmc.rebar.item.base.VanillaCookingFuel;
import net.kyori.adventure.key.Key;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.LingeringPotionSplashEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.BrewingStandFuelEvent;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerItemMendEvent;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import java.util.List;

/**
 * {@link Scriptable} proxy methods:
 * - onUsedToClickBlock
 * - onUsedAsBrewingStandFuel
 * - onConsumed
 * - onUsedToRightClick
 * - onDispense
 * - onTick
 * - getTickInterval
 * - onSplash
 * - onBurntAsFuel
 * - onArrowReady
 * - onArrowShotFromBow
 * - onArrowHit
 * - onArrowDamage
 * - onBowReady
 * - onBowFired
 * - onBucketEmptied
 * - onBucketFilled
 * - respectCooldown
 * - onRemovedFromInventory
 * - onAddedToInventory
 * - onItemDamaged
 * - onItemBreaks
 * - onItemMended
 * - onUsedToDamageBlock
 * - onUsedToBreakBlock
 * - onUsedToDamageEntity
 * - onUsedToKillEntity
 *
 * @author balugaq
 */
@NullMarked
public class CustomItem extends RebarItem implements RebarArmor, RebarArrow, RebarBlockInteractor, RebarBow, RebarBrewingStandFuel,
                                                     RebarBucket, RebarConsumable, RebarDispensable, RebarInteractor, RebarInventoryEffectItem,
                                                     RebarInventoryTicker, RebarItemDamageable, RebarItemEntityInteractor, RebarLingeringPotion,
                                                     RebarSplashPotion, RebarTool, RebarUnmergeable, RebarWeapon, VanillaCookingFuel, RuntimeObject {
    public CustomItem(final ItemStack stack) {
        super(stack);
    }

    @Override
    public void onUsedToClickBlock(final PlayerInteractEvent event) {
        callScript(this, event);
    }

    @Override
    public void onUsedAsBrewingStandFuel(final BrewingStandFuelEvent event) {
        callOrCancelEvent(this, event);
    }

    @Override
    public void onConsumed(final PlayerItemConsumeEvent event) {
        callOrCancelEvent(this, event);
    }

    @Override
    public void onUsedToRightClick(final PlayerInteractEvent event) {
        callScript(this, event);
    }

    @Override
    public void onUsedToRightClickEntity(final PlayerInteractEntityEvent event) {
        callScript(this, event);
    }

    @Override
    public void onDispense(final BlockDispenseEvent event) {
        callScript(this, event);
    }

    @Override
    public long getTickInterval() {
        if (!isFunctionExists("onTick"))
            return Integer.MAX_VALUE;
        if (isFunctionExists("getTickInterval")) {
            var v = callScript(this);
            if (v instanceof Number number) {
                return number.intValue();
            }
        }
        var settings = getSettingsOrNull();
        if (settings == null) return RebarConfig.DEFAULT_TICK_INTERVAL;
        return settings.get("tick-interval", ConfigAdapter.LONG, (long) RebarConfig.DEFAULT_TICK_INTERVAL);
    }

    @Override
    public void onSplash(final LingeringPotionSplashEvent event) {
        callOrCancelEvent(this, event);
    }

    @Override
    public void onSplash(final PotionSplashEvent event) {
        callOrCancelEvent(this, event);
    }

    @Override
    public void onBurntAsFuel(final FurnaceBurnEvent event) {
        callOrCancelEvent(this, event);
    }

    @Override
    public void onArrowReady(final PlayerReadyArrowEvent event) {
        callOrCancelEvent(this, event);
    }

    @Override
    public void onArrowShotFromBow(final EntityShootBowEvent event) {
        callOrCancelEvent(this, event);
    }

    @Override
    public void onArrowHit(final ProjectileHitEvent event) {
        callOrCancelEvent(this, event);
    }

    @Override
    public void onArrowDamage(final EntityDamageByEntityEvent event) {
        callScript(this, event);
    }

    @Override
    public void onBowReady(final PlayerReadyArrowEvent event) {
        callOrCancelEvent(this, event);
    }

    @Override
    public void onBowFired(final EntityShootBowEvent event) {
        callOrCancelEvent(this, event);
    }

    @Override
    public void onBucketEmptied(final PlayerBucketEmptyEvent event) {
        callOrCancelEvent(this, event);
    }

    @Override
    public void onBucketFilled(final PlayerBucketFillEvent event) {
        callOrCancelEvent(this, event);
    }

    @Override
    public boolean respectCooldown() {
        if (isFunctionExists("respectCooldown")) {
            var v = callScript(this);
            if (v instanceof Boolean respectCooldown)
                return respectCooldown;
        }
        var settings = getSettingsOrNull();
        if (settings == null) return true;
        return settings.get("respect-cooldown", ConfigAdapter.BOOLEAN, true);
    }

    @Override
    public NamespacedKey getItemKey() {
        return RebarInventoryEffectItem.super.getItemKey();
    }

    @Override
    public void onTick(final Player player) {
        RebarInventoryEffectItem.super.onTick(player);
        callScript(this, player);
    }

    @Override
    public void onRemovedFromInventory(final Player player) {
        RebarInventoryEffectItem.super.onRemovedFromInventory(player);
        callScript(this, player);
    }

    @Override
    public void onAddedToInventory(final Player player) {
        RebarInventoryEffectItem.super.onAddedToInventory(player);
        callScript(this, player);
    }

    @Override
    public void onItemDamaged(final PlayerItemDamageEvent event) {
        callScript(this, event);
    }

    @Override
    public void onItemBreaks(final PlayerItemBreakEvent event) {
        callScript(this, event);
    }

    @Override
    public void onItemMended(final PlayerItemMendEvent event) {
        callScript(this, event);
    }

    @Override
    public void onUsedToDamageBlock(final BlockDamageEvent event) {
        callScript(this, event);
    }

    @Override
    public void onUsedToBreakBlock(final BlockBreakEvent event) {
        callScript(this, event);
    }

    @Override
    public void onUsedToDamageEntity(final EntityDamageByEntityEvent event) {
        callScript(this, event);
    }

    @Override
    public void onUsedToKillEntity(final EntityDeathEvent event) {
        callScript(this, event);
    }

    @Override
    public Key getEquipmentType() {
        return GlobalVars.getEquipmentType(getKey());
    }

    @Override
    public List<RebarArgument> getPlaceholders() {
        return RuntimeObject.super.getPlaceholders();
    }
}
