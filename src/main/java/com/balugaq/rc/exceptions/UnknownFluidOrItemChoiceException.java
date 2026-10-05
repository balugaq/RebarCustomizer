package com.balugaq.rc.exceptions;

import org.jspecify.annotations.NullMarked;

/**
 * @author balugaq
 */
@NullMarked
public class UnknownFluidOrItemChoiceException extends RuntimeException {
    public UnknownFluidOrItemChoiceException() {
        super();
    }

    public UnknownFluidOrItemChoiceException(String message) {
        super(message);
    }
}
