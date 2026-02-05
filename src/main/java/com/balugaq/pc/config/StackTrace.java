package com.balugaq.pc.config;

import com.balugaq.pc.RebarCustomizer;
import com.balugaq.pc.util.Debug;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.Getter;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NullMarked;

/**
 * @author balugaq
 */
@NullMarked
public class StackTrace implements AutoCloseable {
    private static final StackTrace instance = new StackTrace();
    @Getter
    private static final Int2ObjectOpenHashMap<String> backup = new Int2ObjectOpenHashMap<>();
    @Getter
    private static final Int2ObjectOpenHashMap<String> record = new Int2ObjectOpenHashMap<>();

    @SuppressWarnings("UnnecessaryUnicodeEscape")
    public static void handle(Throwable e) {
        Throwable t = e;
        int k = 0;
        while (t instanceof RuntimeException && k++ < 10) {
            if (t.getCause() != null) {
                t = t.getCause();
            }
        }

        Debug.warning(t.getClass().getSimpleName() + ": " + t.getMessage());

        for (int i = 1; i <= backup.size(); i++)
            Debug.warning("  ".repeat(i - 1) + "\u2514When " + backup.get(i));

        if (RebarCustomizer.getConfigManager().isDebug())
            e.printStackTrace();

        Debug.warning("-".repeat(40));
        if (RebarCustomizer.getConfigManager().isDebug())
            Thread.dumpStack();
    }

    public static void run(String position, Runnable runnable) {
        record(position);
        runnable.run();
        destroy();
    }

    @CanIgnoreReturnValue
    public static StackTrace record(String position) {
        return record(record.size() + 1, position);
    }

    @CanIgnoreReturnValue
    public static StackTrace destroy() {
        syncBackup();
        record.remove(record.size());
        return instance;
    }

    public static StackTrace record(@Range(from = 1, to = Integer.MAX_VALUE) int level, String position) {
        syncBackup();
        if (level < record.size()) {
            int c = record.size();
            for (int i = level + 1; i < c; i++) {
                record.remove(i);
            }
        }

        record.put(level, position);
        return instance;
    }

    private static void syncBackup() {
        backup.clear();
        backup.putAll(record);
    }

    public static StackTrace getInstance() {
        return instance;
    }

    @Override
    public void close() {
        destroy();
    }
}
