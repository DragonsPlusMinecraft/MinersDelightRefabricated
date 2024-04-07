package com.sammy.minersdelight.setup;

import com.sammy.minersdelight.*;
import com.sammy.minersdelight.content.block.copper_pot.*;
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import net.minecraft.core.registries.*;
import net.minecraft.world.level.storage.loot.functions.*;

import java.util.function.Supplier;

public class MDLootFunctions
{
    public static final LazyRegistrar<LootItemFunctionType> LOOT_FUNCTIONS = LazyRegistrar.create(Registries.LOOT_FUNCTION_TYPE, MinersDelightMod.MODID);

    public static final Supplier<LootItemFunctionType> COPY_MEAL = LOOT_FUNCTIONS.register("copy_meal", () -> new LootItemFunctionType(new CopperPotCopyMealFunction.Serializer()));
}