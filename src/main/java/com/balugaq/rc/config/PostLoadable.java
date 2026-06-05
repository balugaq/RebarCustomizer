package com.balugaq.rc.config;

import org.jspecify.annotations.NullMarked;

/**
 * @author balugaq
 */
@NullMarked
public interface PostLoadable {
    boolean postLoad();
}
