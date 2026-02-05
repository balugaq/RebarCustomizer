package com.balugaq.pc.util;

import com.balugaq.pc.RebarCustomizer;
import org.jspecify.annotations.NullMarked;

/**
 * @author balugaq
 */
@NullMarked
public class StringUtil {
    public static String simplifyPath(String path) {
        String pathToRemove = RebarCustomizer.getInstance().getDataFolder().getAbsolutePath();
        return path.startsWith(pathToRemove) ? path.substring(pathToRemove.length()) : path;
    }
}
