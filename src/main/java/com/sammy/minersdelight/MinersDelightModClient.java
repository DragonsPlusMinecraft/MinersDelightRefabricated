package com.sammy.minersdelight;

import com.sammy.minersdelight.setup.MDMenuTypes;
import net.fabricmc.api.ClientModInitializer;

public class MinersDelightModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        MDMenuTypes.ClientOnly.clientSetup();
    }

}
