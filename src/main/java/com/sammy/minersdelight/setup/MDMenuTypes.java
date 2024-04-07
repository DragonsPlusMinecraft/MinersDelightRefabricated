package com.sammy.minersdelight.setup;

import com.sammy.minersdelight.*;
import com.sammy.minersdelight.content.block.copper_pot.*;
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.client.gui.screens.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.*;

import java.util.function.Supplier;

public class MDMenuTypes {


    public static final LazyRegistrar<MenuType<?>> MENU_TYPES = LazyRegistrar.create(Registries.MENU, MinersDelightMod.MODID);

    public static final Supplier<MenuType<CopperPotMenu>> COPPER_POT = MENU_TYPES
            .register("copper_pot", () -> new ExtendedScreenHandlerType<>(CopperPotMenu::new));

    public static class ClientOnly {

        public static void clientSetup() {
            MenuScreens.register(COPPER_POT.get(), CopperPotScreen::new);
        }

    }
}