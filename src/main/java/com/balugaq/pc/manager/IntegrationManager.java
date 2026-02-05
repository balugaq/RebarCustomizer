package com.balugaq.pc.manager;

import com.balugaq.pc.RebarCustomizer;
import com.balugaq.pc.config.StackTrace;
import com.balugaq.pc.integration.Integration;
import com.balugaq.pc.integration.PylonIntegration;
import com.balugaq.pc.util.Debug;
import com.balugaq.pc.util.ReflectionUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author balugaq
 */
@NullMarked
public class IntegrationManager {
    private final @Getter List<Integration> integrations = new ArrayList<>();
    public IsEnabled isEnabled;

    public IntegrationManager() {
        this.isEnabled = new IsEnabled();
        onServerDone(() -> {
            ifEnabled("Pylon", PylonIntegration::new);
        });
    }

    public void onServerDone(Runnable runnable) {
        RebarCustomizer.runTaskLater(runnable, 1);
    }

    public static IntegrationManager instance() {
        return RebarCustomizer.getIntegrationManager();
    }

    public void ifEnabled(String name, Supplier<Integration> supplier) {
        var enabled = Bukkit.getPluginManager().isPluginEnabled(name);
        try {
            ReflectionUtil.setValue(instance().isEnabled, name, enabled);
        } catch (Exception e) {
            Debug.warning(e);
        }
        if (enabled) {
            var integration = supplier.get();
            try (var sk = StackTrace.record("Hooking " + name)) {
                integration.apply();
                integrations.add(integration);
            } catch (Exception e) {
                StackTrace.handle(e);
            }
        }
    }

    /**
     * @author balugaq
     */
    @NullMarked
    public static class IsEnabled {
        public boolean Pylon;
    }
}
