package com.sammy.minersdelight.setup;

import com.sammy.minersdelight.MinersDelightMod;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class MDWorldgen {

    public static ResourceKey<ConfiguredFeature<?, ?>> FEATURE_PATCH_CAVE_CARROTS = ResourceKey.create(Registries.CONFIGURED_FEATURE, MinersDelightMod.path("patch_cave_carrots"));
    public static ResourceKey<PlacedFeature> PATCH_CAVE_CARROTS = ResourceKey.create(Registries.PLACED_FEATURE, MinersDelightMod.path("patch_cave_carrots"));

    public static void register() {
        BiomeModifications.addFeature(context -> {
            if (context.hasTag(BiomeTags.IS_OVERWORLD)) {
                var biome = context.getBiomeKey();
                return biome != Biomes.LUSH_CAVES && biome != Biomes.MUSHROOM_FIELDS;
            }
            return false;
        }, GenerationStep.Decoration.VEGETAL_DECORATION, PATCH_CAVE_CARROTS);
    }

}
