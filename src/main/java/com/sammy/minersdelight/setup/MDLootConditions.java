package com.sammy.minersdelight.setup;

import com.sammy.minersdelight.*;
import com.sammy.minersdelight.content.loot.*;
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import net.minecraft.core.registries.*;
import net.minecraft.world.level.storage.loot.predicates.*;

import java.util.function.Supplier;

public class MDLootConditions {
	public static final LazyRegistrar<LootItemConditionType> LOOT_CONDITIONS = LazyRegistrar.create(Registries.LOOT_CONDITION_TYPE, MinersDelightMod.MODID);

	public static final Supplier<LootItemConditionType> BLOCK_TAG_CONDITION = LOOT_CONDITIONS.register("block_tag", ()->new LootItemConditionType(new LootItemBlockTagCondition.Serializer()));
}
