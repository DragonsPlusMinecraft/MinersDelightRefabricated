package com.sammy.minersdelight.setup;

import com.sammy.minersdelight.*;
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.registries.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.item.*;

public class MDCreativeTabs
{
    public static final LazyRegistrar<CreativeModeTab> CREATIVE_TABS = LazyRegistrar.create(Registries.CREATIVE_MODE_TAB, MinersDelightMod.MODID);

    public static final RegistryObject<CreativeModeTab> TAB_MINERS_DELIGHT = CREATIVE_TABS.register(MinersDelightMod.MODID,
            () -> FabricItemGroup.builder()
                    .title(Component.translatable("itemGroup.miners_delight"))
                    .icon(() -> new ItemStack(MDBlocks.COPPER_POT.get()))
                    .build());
}