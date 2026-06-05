package com.balugaq.rc.exceptions;

import com.balugaq.rc.config.PackDesc;
import com.balugaq.rc.config.SaveditemDesc;
import org.jspecify.annotations.NullMarked;

/**
 * @author balugaq
 */
@NullMarked
public class UnknownSaveditemException extends UnknownItemException {
    public UnknownSaveditemException() {
        super();
    }

    public UnknownSaveditemException(PackDesc packDesc, SaveditemDesc itemDesc) {
        super(packDesc.getId() + "/" + itemDesc.getFile());
    }

    public UnknownSaveditemException(String message) {
        super(message);
    }
}
