package com.balugaq.rc.exceptions;

import com.balugaq.rc.config.PackDesc;
import org.jspecify.annotations.NullMarked;

/**
 * @author balugaq
 */
@NullMarked
public class UnknownPackException extends RuntimeException {
    public UnknownPackException() {
        super();
    }

    public UnknownPackException(PackDesc packDesc) {
        super(packDesc.getId());
    }
}
