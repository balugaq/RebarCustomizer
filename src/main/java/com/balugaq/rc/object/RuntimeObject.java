package com.balugaq.rc.object;

import com.google.errorprone.annotations.OverridingMethodsMustInvokeSuper;
import io.github.pylonmc.rebar.config.ConfigSection;
import io.github.pylonmc.rebar.config.adapter.ConfigAdapter;
import io.github.pylonmc.rebar.i18n.RebarArgument;
import org.jspecify.annotations.NullMarked;

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
    @OverridingMethodsMustInvokeSuper
    default List<RebarArgument> getPlaceholders() {
        List<RebarArgument> list = new ArrayList<>();
        var settings = ConfigSection.fromSettings(getKey());
        for (String key : settings.getKeys()) {
            var v = settings.get(key, ConfigAdapter.ANY);
            if (v != null)
                list.add(RebarArgument.of(key, ConfigAdapter.STRING.convert(v)));
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
