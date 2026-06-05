package com.balugaq.rc.config.pack;

import com.balugaq.rc.config.Deserializer;
import com.balugaq.rc.config.FileObject;
import com.balugaq.rc.config.FileReader;
import com.balugaq.rc.config.InternalObjectID;
import com.balugaq.rc.config.Pack;
import com.balugaq.rc.config.PageDesc;
import com.balugaq.rc.config.RegisteredObjectID;
import com.balugaq.rc.config.ScriptDesc;
import com.balugaq.rc.config.StackTrace;
import com.balugaq.rc.config.preloads.PreparedPage;
import com.balugaq.rc.config.register.PreRegister;
import com.balugaq.rc.data.MyArrayList;
import com.balugaq.rc.exceptions.IncompatibleMaterialException;
import com.balugaq.rc.exceptions.MissingArgumentException;
import com.balugaq.rc.util.MaterialUtil;
import com.balugaq.rc.util.StringUtil;
import lombok.Data;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NullMarked;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <li>pages/
 *   <ul>
 *     <li>pages-partA.yml</li>
 *     <li>pages-partB.yml</li>
 *   </ul>
 * </li>
 * <p>
 * For each yml:
 * <p>
 * [Internal object ID]:
 *   material: [Material Format]
 *   *postload: boolean
 * <p>
 *
 * @author balugaq
 */
@Data
@NullMarked
public class Pages implements FileObject<Pages> {
    private AtomicInteger loadedPages = new AtomicInteger(0);
    private PackNamespace namespace;
    private Map<RegisteredObjectID, PreparedPage> pages = new HashMap<>();

    public Pages setPackNamespace(PackNamespace namespace) {
        this.namespace = namespace;
        return this;
    }

    // @formatter:off
    @SuppressWarnings("unchecked")@Override
    public List<FileReader<Pages>> readers() {
        return List.of(dir -> {
            List<File> files = Arrays.stream(dir.listFiles()).toList();
            List<File> ymls = files.stream().filter(file -> file.getName().endsWith(".yml") || file.getName().endsWith(".yaml")).toList();
            for (File yml : ymls) {try (var ignored = StackTrace.record("Reading file: " + StringUtil.simplifyPath(yml.getAbsolutePath()))) {
                var config = YamlConfiguration.loadConfiguration(yml);

                for (String key : config.getKeys(false)) {try (var ignored1 = StackTrace.record("Reading key: " + key)) {
                    var section = PreRegister.read(config, key);
                    if (section == null) continue;

                    if (!section.contains("material")) throw new MissingArgumentException("material");

                    var s2 = section.get("material");
                    ItemStack item = Deserializer.ITEM_STACK.deserialize(s2);
                    if (item == null) continue;
                    Material dm = MaterialUtil.getDisplayMaterial(item);
                    if (!dm.isItem() || dm.isAir()) throw new IncompatibleMaterialException("material must be items: " + item.getType());
                    var id = InternalObjectID.of(key).register(namespace);

                    ScriptDesc scriptdesc = Pack.readOrNull(section, ScriptDesc.class, "script");
                    namespace.registerScript(id, scriptdesc);
                    MyArrayList<PageDesc> parents = (MyArrayList<@NotNull PageDesc>) Pack.readOrNull(section, MyArrayList.class, PageDesc.class, "parents", e -> e.setPackNamespace(namespace));

                    boolean postLoad = section.getBoolean("postload", false);
                    pages.put(id, new PreparedPage(id, dm, parents, new ArrayList<>(), postLoad));
                } catch (Exception e) {
                    StackTrace.handle(e);
                }}
            } catch (Exception e) {
                StackTrace.handle(e);
            }}

            return this;
        });
    }
    // @formatter:off
}
