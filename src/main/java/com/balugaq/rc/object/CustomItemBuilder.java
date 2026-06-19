package com.balugaq.rc.object;

import com.balugaq.rc.GlobalVars;
import com.balugaq.rc.config.preloads.PreparedItem;
import io.github.pylonmc.rebar.config.ConfigSection;
import io.github.pylonmc.rebar.config.RebarConfig;
import io.github.pylonmc.rebar.config.adapter.ConfigAdapter;
import io.github.pylonmc.rebar.item.RebarItem;
import io.github.pylonmc.rebar.item.interfaces.AnvilUseRebarItemHandler;
import io.github.pylonmc.rebar.item.interfaces.ArmorRebarItem;
import io.github.pylonmc.rebar.item.interfaces.ArrowRebarItemHandler;
import io.github.pylonmc.rebar.item.interfaces.BlockBreakRebarItemHandler;
import io.github.pylonmc.rebar.item.interfaces.BlockInteractRebarItemHandler;
import io.github.pylonmc.rebar.item.interfaces.BottleRebarItemHandler;
import io.github.pylonmc.rebar.item.interfaces.BowRebarItemHandler;
import io.github.pylonmc.rebar.item.interfaces.BrewingStandFuelRebarItemHandler;
import io.github.pylonmc.rebar.item.interfaces.BucketRebarItemHandler;
import io.github.pylonmc.rebar.item.interfaces.ConsumeRebarItemHandler;
import io.github.pylonmc.rebar.item.interfaces.CooldownRebarItem;
import io.github.pylonmc.rebar.item.interfaces.DispenseRebarItemHandler;
import io.github.pylonmc.rebar.item.interfaces.DropRebarItemHandler;
import io.github.pylonmc.rebar.item.interfaces.DurabilityRebarItemHandler;
import io.github.pylonmc.rebar.item.interfaces.EntityAttackRebarItemHandler;
import io.github.pylonmc.rebar.item.interfaces.EntityInteractRebarItemHandler;
import io.github.pylonmc.rebar.item.interfaces.FurnaceBurnRebarItemHandler;
import io.github.pylonmc.rebar.item.interfaces.InteractRebarItemHandler;
import io.github.pylonmc.rebar.item.interfaces.InventoryEffectRebarItem;
import io.github.pylonmc.rebar.item.interfaces.InventoryTickerRebarItem;
import io.github.pylonmc.rebar.item.interfaces.JoinRebarItemHandler;
import io.github.pylonmc.rebar.item.interfaces.LingeringPotionRebarItemHandler;
import io.github.pylonmc.rebar.item.interfaces.PickupRebarItemHandler;
import io.github.pylonmc.rebar.item.interfaces.ProjectileRebarItemHandler;
import io.github.pylonmc.rebar.item.interfaces.RepairableRebarItem;
import io.github.pylonmc.rebar.item.interfaces.SplashPotionRebarItemHandler;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.Super;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.matcher.ElementMatchers;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * @author balugaq
 */
@RequiredArgsConstructor
public class CustomItemBuilder {
    protected static final List<Class<?>> handlers = List.of(
            AnvilUseRebarItemHandler.class,
            ArmorRebarItem.class,
            ArrowRebarItemHandler.class,
            BlockBreakRebarItemHandler.class,
            BlockInteractRebarItemHandler.class,
            BottleRebarItemHandler.class,
            BowRebarItemHandler.class,
            BrewingStandFuelRebarItemHandler.class,
            BucketRebarItemHandler.class,
            ConsumeRebarItemHandler.class,
            CooldownRebarItem.class,
            DispenseRebarItemHandler.class,
            DropRebarItemHandler.class,
            DurabilityRebarItemHandler.class,
            EntityAttackRebarItemHandler.class,
            EntityInteractRebarItemHandler.class,
            FurnaceBurnRebarItemHandler.class,
            InteractRebarItemHandler.class,
            InventoryEffectRebarItem.class,
            InventoryTickerRebarItem.class,
            JoinRebarItemHandler.class,
            LingeringPotionRebarItemHandler.class,
            PickupRebarItemHandler.class,
            ProjectileRebarItemHandler.class,
            RepairableRebarItem.class,
            SplashPotionRebarItemHandler.class
    );

    private static String makeName(PreparedItem prepared) {
        String name = "com.balugaq.rc.object.custom.CustomItem_";
        name += prepared.id().key().getNamespace()
                .replaceAll("_-.", "_");
        name += "_";
        name += prepared.id().key().getKey()
                .replaceAll("_-./", "_");
        return name;
    }

    public static <T extends RebarItem> Class<T> build(PreparedItem prepared) {
        var matcher = ElementMatchers.isDeclaredBy(RebarItem.class)
                .and(ElementMatchers.not(ElementMatchers.isDeclaredBy(Object.class)));

        for (Class<?> handler : handlers) {
            matcher = matcher.or(ElementMatchers.isDeclaredBy(handler));
        }

        ItemDelegation delegate = new ItemDelegation(prepared.id().key());

        Class<?> loaded = new ByteBuddy()
                .subclass(RebarItem.class)
                .name(makeName(prepared))
                .implement(handlers.toArray(new Class<?>[0]))
                .method(matcher)
                .intercept(MethodDelegation.withDefaultConfiguration()
                                   .filter(ElementMatchers.named("intercept"))
                                   .to(delegate))
                .make()
                .load(RebarItem.class.getClassLoader())
                .getLoaded();

        return (Class<T>) loaded;
    }

    /**
     * @author balugaq
     */
    @Getter
    @RequiredArgsConstructor
    public static class ItemDelegation implements RuntimeObject {
        private final NamespacedKey key;

        @RuntimeType
        public Object intercept(@Origin Method method,
                                @AllArguments Object[] rawArgs,
                                @Super(strategy = Super.Instantiation.UNSAFE) RebarItem rebar) throws Exception {
            Object[] args = new Object[rawArgs.length + 1];
            args[0] = rebar;
            System.arraycopy(rawArgs, 0, args, 1, rawArgs.length);
            String name = method.getName();
            switch (name) {
                case "getBaseTickInterval" -> {
                    if (!isFunctionExists("onTick"))
                        return Integer.MAX_VALUE;
                    if (isFunctionExists("getBaseTickInterval")) {
                        var v = callScript(this);
                        if (v instanceof Number number) {
                            return number.intValue();
                        }
                    }
                    var settings = ConfigSection.fromSettings(getKey());
                    return settings.get("tick-interval", ConfigAdapter.LONG, (long) RebarConfig.DEFAULT_TICK_INTERVAL);
                }
                case "getEquipmentType" -> {
                    return GlobalVars.getEquipmentType(getKey());
                }
                case "getPlaceholders" -> {
                    return RuntimeObject.super.getPlaceholders();
                }
                case "respectCooldown" -> {
                    if (isFunctionExists("respectCooldown")) {
                        var v = callScript(this);
                        if (v instanceof Boolean respectCooldown)
                            return respectCooldown;
                    }
                    var settings = ConfigSection.fromSettings(getKey());
                    return settings.get("respect-cooldown", ConfigAdapter.BOOLEAN, true);
                }
                case "getItemKey" -> {
                    return ((InventoryEffectRebarItem) rebar).getItemKey();
                }
                case "onTick" -> {
                    ((InventoryEffectRebarItem) rebar).onTick((Player) args[1]);
                    return callScriptA("onTick", args);
                }
                case "onRemovedFromInventory" -> {
                    ((InventoryEffectRebarItem) rebar).onRemovedFromInventory((Player) args[1]);
                    return callScriptA("onRemovedFromInventory", args);
                }
                case "onAddedToInventory" -> {
                    ((InventoryEffectRebarItem) rebar).onAddedToInventory((Player) args[0]);
                    return callScriptA("onAddedToInventory", args);
                }
                case "onFuelBrewingStand", "onConsumed", "onPotionSplash", "onItemBurnByFurnace", "onArrowReady",
                     "onArrowShotFromBow", "onBowReady", "onBowShoot", "onBucketEmpty", "onBucketFilled" -> {
                    return callOrCancelEventA(name, this, (Event & Cancellable) args[1], args);
                }
            }

            if (isFunctionExists(name)) {
                return callScriptA(name, args);
            }

            return method.invoke(rebar, rawArgs);
        }
    }
}
