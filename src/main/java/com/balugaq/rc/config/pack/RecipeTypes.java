package com.balugaq.rc.config.pack;

import com.balugaq.rc.config.Deserializer;
import com.balugaq.rc.config.FileObject;
import com.balugaq.rc.config.FileReader;
import com.balugaq.rc.config.GuiReader;
import com.balugaq.rc.config.InternalObjectID;
import com.balugaq.rc.config.Pack;
import com.balugaq.rc.config.RecipeTypeDesc;
import com.balugaq.rc.config.RegisteredObjectID;
import com.balugaq.rc.config.ScriptDesc;
import com.balugaq.rc.config.StackTrace;
import com.balugaq.rc.config.preloads.PreparedRecipeType;
import com.balugaq.rc.config.register.PreRegister;
import com.balugaq.rc.exceptions.InvalidStructureException;
import com.balugaq.rc.exceptions.MissingArgumentException;
import com.balugaq.rc.exceptions.UnknownRecipeTypeException;
import com.balugaq.rc.object.CustomRecipeType;
import com.balugaq.rc.util.StringUtil;
import io.github.pylonmc.rebar.recipe.RebarRecipe;
import io.github.pylonmc.rebar.recipe.RecipeType;
import lombok.Data;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jspecify.annotations.NullMarked;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * [Internal Object ID]:
 *   structure: |-
 *     B B B B B B B B B
 *     I i i I B O o o O
 *     I i i I P O o o O
 *     I i i I B O o o O
 *     B B B B B B B B B
 *   *script: [ScriptDesc]
 *   *postload: boolean
 *   *gui:
 *     [char]: [Material Format]
 *   *loader:
 *     [key]: [Adapter Desc]
 * <p>
 * [Adapter Desc]:
 * int;3
 * list.int
 * map.map.int
 *
 * @author balugaq
 */
@Data
@NullMarked
public class RecipeTypes implements FileObject<RecipeTypes> {
    private AtomicInteger loadedRecipeTypes = new AtomicInteger(0);
    private PackNamespace namespace;
    private Map<RegisteredObjectID, PreparedRecipeType> recipeTypes = new HashMap<>();

    public RecipeTypes setPackNamespace(PackNamespace namespace) {
        this.namespace = namespace;
        return this;
    }

    @Override
    public List<FileReader<RecipeTypes>> readers() {
        return List.of(dir -> {
            List<File> files = Arrays.stream(dir.listFiles()).toList();
            List<File> ymls = files.stream().filter(file -> file.getName().endsWith(".yml") || file.getName().endsWith(".yaml")).toList();

            for (File yml : ymls) {
                try (var ignored = StackTrace.record("Reading file: " + StringUtil.simplifyPath(yml.getAbsolutePath()))) {
                    var config = YamlConfiguration.loadConfiguration(yml);

                    for (String key : config.getKeys(false)) {
                        try (var ignored1 = StackTrace.record("Reading key: " + key)) {
                            var section = PreRegister.read(config, key);
                            if (section == null) continue;

                            Map<String, CustomRecipeType.Handler> loader = null;
                            if (section.contains("loader")) {
                                ConfigurationSection sec = section.getConfigurationSection("loader");
                                if (sec != null) {
                                    for (String k : sec.getKeys(false)) {
                                        var s2 = sec.getString(k);
                                        if (s2 != null) {
                                            if (loader == null) loader = new HashMap<>();
                                            loader.put(k, Deserializer.HANDLER.deserialize(s2));
                                        }
                                    }
                            /*
                            ```yml
                            loader:
                              inputs: int
                              results: int;3
                            ```
                             */
                                }
                            }
                            var id = InternalObjectID.of(key).register(namespace);
                            ScriptDesc scriptdesc = Pack.readOrNull(section, ScriptDesc.class, "script");
                            namespace.registerScript(id, scriptdesc);

                            if (!section.contains("structure")) throw new MissingArgumentException("structure");
                            var gui = GuiReader.read(section, namespace, scriptdesc);
                            if (gui == GuiReader.Result.EMPTY) throw new InvalidStructureException(gui.structure());

                            // clone
                            RecipeTypeDesc desc = Pack.readOrNull(section, RecipeTypeDesc.class, "clone", t -> t.setPackNamespace(namespace));
                            RecipeType<? extends RebarRecipe> cloneType = null;
                            if (desc != null) {
                                cloneType = desc.findRecipeType();
                                if (cloneType == null) throw new UnknownRecipeTypeException(desc.getKey().toString());
                            }

                            boolean postLoad = section.getBoolean("postload", false);
                            recipeTypes.put(id, new PreparedRecipeType(id, gui.structure(), gui.provider(), loader, postLoad, cloneType));
                        } catch (Exception e) {
                            StackTrace.handle(e);
                        }
                    }
                } catch (Exception e) {
                    StackTrace.handle(e);
                }
            }

            return this;
        });
    }
}
