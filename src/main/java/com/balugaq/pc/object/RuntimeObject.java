package com.balugaq.pc.object;

import com.balugaq.pc.util.Debug;
import com.balugaq.pc.util.ReflectionUtil;
import com.google.errorprone.annotations.OverridingMethodsMustInvokeSuper;
import io.github.pylonmc.rebar.config.Config;
import io.github.pylonmc.rebar.config.adapter.ConfigAdapter;
import io.github.pylonmc.rebar.i18n.RebarArgument;
import io.github.pylonmc.rebar.util.RebarUtils;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link Scriptable} proxy methods:
 * - getPlaceholders
 *
 * @author balugaq
 */
@NullMarked
public interface RuntimeObject extends Scriptable {
    @Nullable
    default Config getSettingsOrNull() {
        try {
            return (Config) ReflectionUtil.invokeStaticMethod(RebarUtils.class, "mergeGlobalConfig", RebarUtils.getAddon(getKey()), "settings/" + getKey().getKey() + ".yml", "settings/" + getKey().getNamespace() + "/" + getKey().getKey() + ".yml", false);
        } catch (IllegalAccessException | InvocationTargetException e) {
            Debug.warning(e);
            return null;
        }
    }

    @OverridingMethodsMustInvokeSuper
    default List<RebarArgument> getPlaceholders() {
        List<RebarArgument> list = new ArrayList<>();
        var settings = getSettingsOrNull();
        if (settings != null) {
            for (String key : settings.getKeys()) {
                var v = settings.get(key, ConfigAdapter.ANY);
                if (v != null)
                    list.add(RebarArgument.of(key, ConfigAdapter.STRING.convert(v)));
            }
        }

        var v = callScript(this);
        if (v instanceof List<?> l2) {
            for (Object o : l2) {
                if (o instanceof RebarArgument argument)
                    list.add(argument);
            }
        }

        return list;
    }
}
