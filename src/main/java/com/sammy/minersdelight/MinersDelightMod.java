package com.sammy.minersdelight;

import com.sammy.minersdelight.content.block.copper_pot.CopperPotBlockEntity;
import com.sammy.minersdelight.content.item.CopperCupItem;
import com.sammy.minersdelight.setup.*;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

public class MinersDelightMod implements ModInitializer {
	public static final Logger LOGGER = LogManager.getLogger();
	public static final String MODID = "miners_delight";
	public static final Random RANDOM = new Random();
	public static final NonNullSupplier<Registrate> REGISTRATE = NonNullSupplier.lazy(() -> Registrate.create(MODID));

	public static Registrate registrate(){
		return REGISTRATE.get();
	}

	@Override
	public void onInitialize() {
		MDLootModifiers.LOOT_MODIFIERS.register();
		MDLootConditions.LOOT_CONDITIONS.register();
		MDLootFunctions.LOOT_FUNCTIONS.register();
		MDMenuTypes.MENU_TYPES.register();
		MDCreativeTabs.CREATIVE_TABS.register();
		MDPotions.POTIONS.register();
		MDBlocks.register();
		MDItems.register();
		MDBlockEntities.register();
		MDWorldgen.register();
		REGISTRATE.get().register();
		MDCauldronInteractions.addCauldronInteractions();
		MDPotions.addPotionMixing();
		MDComposting.addCompostValues();
		CopperPotBlockEntity.init();
		CopperCupItem.init();
	}

	public static ResourceLocation path(String path) {
		return new ResourceLocation(MODID, path);
	}

}