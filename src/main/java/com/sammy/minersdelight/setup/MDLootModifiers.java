package com.sammy.minersdelight.setup;

import com.mojang.serialization.*;
import com.sammy.minersdelight.*;
import com.sammy.minersdelight.content.loot.*;
import io.github.fabricators_of_create.porting_lib.loot.IGlobalLootModifier;
import io.github.fabricators_of_create.porting_lib.loot.PortingLibLoot;
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;

import java.util.function.Supplier;

public class MDLootModifiers {
	public static final LazyRegistrar<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = LazyRegistrar.create(PortingLibLoot.GLOBAL_LOOT_MODIFIER_SERIALIZERS_KEY, MinersDelightMod.MODID);

	public static final Supplier<Codec<? extends IGlobalLootModifier>> ADD_ITEMS = LOOT_MODIFIERS.register("add_items", AddSeveralItemsModifier.CODEC);
	public static final Supplier<Codec<? extends IGlobalLootModifier>> REPLACE_ITEMS = LOOT_MODIFIERS.register("replace_items", ReplaceDroppedLootModifier.CODEC);

}
