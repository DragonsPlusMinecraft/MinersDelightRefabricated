package com.sammy.minersdelight.mixin;

import net.minecraft.world.food.FoodProperties;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FoodProperties.class)
public interface FoodPropertiesMixin {
//    @Accessor("effects")
//    List<Pair<Supplier<MobEffectInstance>, Float>> getEffectsRaw();
}
