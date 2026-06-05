package com.balugaq.rc.config.pack;

import com.balugaq.rc.config.FileObject;
import com.balugaq.rc.config.FileReader;
import com.balugaq.rc.config.ScriptDesc;
import com.balugaq.rc.script.JSScriptExecutor;
import com.balugaq.rc.script.ScriptExecutor;
import com.balugaq.rc.util.StringUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * A collection for pack scripts.
 * <p>
 * Some special scripts have specific file names:
 * <ul>
 *     <li>Init script: init.js</li>
 *     <li>Event listener script: events.js</li>
 * </ul>
 *
 * @author lijinhong11
 */
@Data
@NoArgsConstructor(force = true)
@NullMarked
public class Scripts implements FileObject<Scripts> {
    public static final String INIT_SCRIPT_FILE = "init.js";
    public static final String EVENTS_SCRIPT_FILE = "events.js";

    @Nullable
    private ScriptExecutor eventsScript;
    private final Map<String, ScriptExecutor> scripts = new HashMap<>();

    @Nullable
    public ScriptExecutor findScript(ScriptDesc scriptDesc) {
        return scripts.get(scriptDesc.getScriptPath());
    }

    public void closeAll() {
        for (ScriptExecutor executor : scripts.values()) {
            executor.close();
        }
    }

    @Override
    public List<FileReader<Scripts>> readers() {
        return List.of(
                dir -> {
                    for (File file : Objects.requireNonNull(dir.listFiles())) {
                        if (!file.isFile()) continue;
                        if (!file.getName().endsWith(".js")) continue;
                        if (INIT_SCRIPT_FILE.equals(file.getName())) {
                            ScriptExecutor executor = createJSScriptExecutor(file);
                            executor.executeFunction("init");
                            continue;
                        }
                        if (EVENTS_SCRIPT_FILE.equals(file.getName())) {
                            eventsScript = createJSScriptExecutor(file); // todo
                            continue;
                        }
                        scripts.put(StringUtil.simplifyPath(file.getPath()), createJSScriptExecutor(file));
                    }

                    return this;
                }
        );
    }

    private ScriptExecutor createJSScriptExecutor(File file) {
        return new JSScriptExecutor(file);
    }
}
