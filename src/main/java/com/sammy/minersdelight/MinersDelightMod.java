package com.sammy.minersdelight;

import com.sammy.minersdelight.data.*;
import com.sammy.minersdelight.setup.*;
import com.tterrag.registrate.*;
import com.tterrag.registrate.util.nullness.*;
import net.minecraft.data.*;
import net.minecraft.resources.*;
import net.minecraftforge.common.*;
import net.minecraftforge.data.event.*;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.javafmlmod.*;
import org.apache.logging.log4j.*;

import java.util.*;

@Mod(MinersDelightMod.MODID)
public class MinersDelightMod {
	public static final Logger LOGGER = LogManager.getLogger();
	public static final String MODID = "miners_delight";
	public static final Random RANDOM = new Random();
	public static final NonNullSupplier<Registrate> REGISTRATE = NonNullSupplier.lazy(() -> Registrate.create(MODID));

	public static Registrate registrate(){
		return REGISTRATE.get();
	}

	public MinersDelightMod() {
		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
		IEventBus forgeBus = MinecraftForge.EVENT_BUS;
		MDLootModifiers.LOOT_MODIFIERS.register(modBus);
		MDLootConditions.LOOT_CONDITIONS.register(modBus);
		MDMenuTypes.MENU_TYPES.register(modBus);
		MDCreativeTabs.CREATIVE_TABS.register(modBus);
		MDBlocks.register();
		MDItems.register();
		MDBlockEntities.register();
		MDWorldgen.register();
		modBus.addListener(MDCauldronInteractions::addCauldronInteractions);
		modBus.addListener(MDComposting::addCompostValues);
		modBus.addListener(DataOnly::gatherData);
	}

	public static ResourceLocation path(String path) {
		return new ResourceLocation(MODID, path);
	}

	public static class DataOnly {
		public static void gatherData(GatherDataEvent event) {
			final DataGenerator generator = event.getGenerator();
			final PackOutput packOutput = generator.getPackOutput();
			generator.addProvider(true, new MDLangMerger(packOutput));
			generator.addProvider(true, new MDRecipeProvider(packOutput));
		}
	}
}