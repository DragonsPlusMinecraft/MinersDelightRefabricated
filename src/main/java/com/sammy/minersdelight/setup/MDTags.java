package com.sammy.minersdelight.setup;

import com.sammy.minersdelight.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.*;
import net.minecraft.tags.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;

public class MDTags {
    public static final TagKey<Block> CAVE_CARROTS_CROP_BLOCK = conventionBlockTag("crops/cave_carrot");
    public static final TagKey<Item> CAVE_CARROTS_VEGETABLE_ITEM = conventionItemTag("vegetables/cave_carrot");
    public static final TagKey<Item> CAVE_CARROTS_CROP_ITEM = conventionItemTag("crops/cave_carrot");
    public static final TagKey<Item> BAKED_CAVE_CARROT = modItemTag("baked_cave_carrot");

    public static final TagKey<Item> MOSS = conventionItemTag("moss");

    public static final TagKey<Item> BAT_WING = modItemTag("bat_wing");
    public static final TagKey<Item> INSECT_MEAT = modItemTag("insect_meat");
    public static final TagKey<Item> COOKED_INSECT_MEAT = modItemTag("cooked_insect_meat");

    public static final TagKey<Item> SQUID = conventionItemTag("squid");
    public static final TagKey<Item> GLOW_SQUID = conventionItemTag("glow_squid");
    public static final TagKey<Item> RAW_FISHES_SQUID = conventionItemTag("raw_fishes/squid");
    public static final TagKey<Item> COOKED_FISHES_SQUID = conventionItemTag("cooked_fishes/squid");
    public static final TagKey<Item> TENTACLES = conventionItemTag("tentacles");

    public static final TagKey<Item> BC_RAW_MEATS = modItemTag("brewinandchewin:raw_meats");


    private static TagKey<Item> modItemTag(String path) {
        return TagKey.create(Registries.ITEM, path.contains(":") ? new ResourceLocation(path) : MinersDelightMod.path(path));
    }

    private static TagKey<Block> modBlockTag(String path) {
        return TagKey.create(Registries.BLOCK, path.contains(":") ? new ResourceLocation(path) : MinersDelightMod.path(path));
    }

    private static TagKey<Item> conventionItemTag(String path) {
        return TagKey.create(Registries.ITEM, new ResourceLocation("c", path));
    }

    private static TagKey<Block> conventionBlockTag(String path) {
        return TagKey.create(Registries.BLOCK, new ResourceLocation("c", path));
    }

}
