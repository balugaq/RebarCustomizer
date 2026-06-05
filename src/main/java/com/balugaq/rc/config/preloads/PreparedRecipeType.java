package com.balugaq.rc.config.preloads;

import com.balugaq.rc.config.PostLoadable;
import com.balugaq.rc.config.RegisteredObjectID;
import com.balugaq.rc.object.CustomRecipeType;
import com.balugaq.rc.object.ItemStackProvider;
import io.github.pylonmc.rebar.recipe.RecipeType;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import java.util.List;
import java.util.Map;

/**
 * @author balugaq
 */
@NullMarked
public record PreparedRecipeType(
        RegisteredObjectID id,
        List<String> structure,
        @Nullable ItemStackProvider guiProvider,
        @Nullable Map<String, CustomRecipeType.Handler> configReader,
        boolean postLoad,
        @Nullable RecipeType<?> cloneType
) implements PostLoadable {
}
