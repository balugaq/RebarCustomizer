package com.balugaq.rc.exceptions;

import com.balugaq.rc.config.PackDesc;
import org.jspecify.annotations.NullMarked;

/**
 * @author balugaq
 */
@NullMarked
public class SaveditemsNotFoundException extends RuntimeException {
    public SaveditemsNotFoundException() {
        super();
    }

    public SaveditemsNotFoundException(PackDesc packDesc) {
        super(packDesc.getId());
    }
}
