package com.sammy.minersdelight.logic;

import com.google.gson.*;
import com.sammy.minersdelight.MinersDelightMod;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.*;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.*;
import net.minecraft.util.profiling.*;
import net.minecraft.world.item.*;

import java.util.*;

public class CupConversionReloadListener extends SimpleJsonResourceReloadListener implements IdentifiableResourceReloadListener {
    public static final HashMap<Item, Item> BOWL_TO_CUP = new HashMap<>();
    public static final ResourceLocation ID = MinersDelightMod.path("cup_conversion");
    private static final Gson GSON = (new GsonBuilder()).create();

    public CupConversionReloadListener() {
        super(GSON, "cup_conversion");
    }

    public static void register() {
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new CupConversionReloadListener());
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objectIn, ResourceManager resourceManagerIn, ProfilerFiller profilerIn) {
        BOWL_TO_CUP.clear();
        for (int i = 0; i < objectIn.size(); i++) {
            ResourceLocation location = (ResourceLocation) objectIn.keySet().toArray()[i];
            JsonObject object = objectIn.get(location).getAsJsonObject();
            JsonArray entries = object.getAsJsonArray("entries");
            for (JsonElement entry : entries) {
                JsonObject entryObject = entry.getAsJsonObject();
                Item bowlFood = itemFromJson(entryObject.get("bowl"));
                Item cupFood = itemFromJson(entryObject.get("cup"));
                BOWL_TO_CUP.put(bowlFood, cupFood);
            }
        }
    }

    public static Item itemFromJson(JsonElement pItemObject) {
        String s = pItemObject.getAsJsonPrimitive().getAsString();
        Item item = BuiltInRegistries.ITEM.get(new ResourceLocation(s));
        if (item == Items.AIR) {
            throw new JsonSyntaxException("Invalid item: " + s);
        } else {
            return item;
        }
    }

    @Override
    public ResourceLocation getFabricId() {
        return ID;
    }

}