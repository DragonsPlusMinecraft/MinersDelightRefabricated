package com.sammy.minersdelight.content.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import vectorwing.farmersdelight.common.*;
import vectorwing.farmersdelight.common.item.*;

import javax.annotation.*;
import java.util.*;

//TODO: Ask Sammy; how much milk should it hold, since cups are of the size of buckets, but this extends the milk bottle
public class MilkCupItem extends MilkBottleItem {

   public MilkCupItem(Item.Properties pProperties) {
      super(pProperties);
   }

   @Environment(EnvType.CLIENT)
   public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag isAdvanced) {
      if (Configuration.FOOD_EFFECT_TOOLTIP.get()) {
         MutableComponent textEmpty = Component.translatable("farmersdelight.tooltip.milk_bottle");
         tooltip.add(textEmpty.withStyle(ChatFormatting.BLUE));
      }
   }
}