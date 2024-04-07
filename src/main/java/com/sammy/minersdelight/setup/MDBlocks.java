package com.sammy.minersdelight.setup;

import com.sammy.minersdelight.*;
import com.sammy.minersdelight.content.block.*;
import com.sammy.minersdelight.content.block.copper_pot.*;
import com.sammy.minersdelight.content.block.sticky_basket.*;
import com.tterrag.registrate.*;
import com.tterrag.registrate.builders.*;
import com.tterrag.registrate.providers.loot.*;
import com.tterrag.registrate.util.entry.*;
import com.tterrag.registrate.util.nullness.*;
import io.github.fabricators_of_create.porting_lib.models.generators.ConfiguredModel;
import io.github.fabricators_of_create.porting_lib.models.generators.ModelFile;
import net.minecraft.advancements.critereon.*;
import net.minecraft.client.renderer.*;
import net.minecraft.resources.*;
import net.minecraft.tags.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.entries.*;
import net.minecraft.world.level.storage.loot.functions.*;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.minecraft.world.level.storage.loot.providers.number.*;
import vectorwing.farmersdelight.common.block.*;
import vectorwing.farmersdelight.common.tag.*;

import java.util.function.*;

import static com.sammy.minersdelight.MinersDelightMod.*;

public class MDBlocks {
    public static final Registrate BLOCK_REGISTRATE = MinersDelightMod.registrate().defaultCreativeTab(MDCreativeTabs.TAB_MINERS_DELIGHT.getKey());

    public static final BlockEntry<CopperPotBlock> COPPER_POT = setupBlock("copper_pot", CopperPotBlock::new, BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK).strength(0.5F, 6.0F).sound(SoundType.LANTERN))
            .blockstate((ctx, p) -> {
            })
            .item().properties(p -> p.stacksTo(1)).build()
            .tag(BlockTags.MINEABLE_WITH_PICKAXE)
            .addLayer(()-> RenderType::cutout)
            .register();

    public static final BlockEntry<StickyBasketBlock> STICKY_BASKET = setupBlock("sticky_basket", StickyBasketBlock::new, BlockBehaviour.Properties.of().strength(1.5F).sound(SoundType.BAMBOO_WOOD))
            .blockstate((ctx, p) -> {
            })
            .item().build()
            .tag(BlockTags.MINEABLE_WITH_AXE)
            .addLayer(()-> RenderType::cutout)
            .register();

    public static final BlockEntry<StuffedSquidFeastBlock> STUFFED_SQUID = setupBlock("stuffed_squid", StuffedSquidFeastBlock::new, BlockBehaviour.Properties.copy(Blocks.CAKE))
            .blockstate((ctx, p) -> {
                Function<BlockState, ModelFile> modelFunc = s -> {
                    int servings = s.getValue(StuffedSquidFeastBlock.SERVINGS);
                    String path = servings == 0 ? "block/stuffed_squid_block_leftover" : "block/stuffed_squid_block_stage" + (5-servings);
                    return p.models().getExistingFile(path(path));
                };
                p.getVariantBuilder(ctx.get()).forAllStates(s -> ConfiguredModel.builder()
                        .modelFile(modelFunc.apply(s))
                        .rotationY((((int) s.getValue(FeastBlock.FACING).toYRot() + 180))).build());
            })
            .loot(stuffedSquidTable())
            .item().defaultModel().properties(p -> p.stacksTo(1)).build()
            .tag(BlockTags.MINEABLE_WITH_AXE)
            .register();

    public static final BlockEntry<WildCaveCarrotBlock> WILD_CAVE_CARROTS = setupBlock("wild_cave_carrots", WildCaveCarrotBlock::new, BlockBehaviour.Properties.copy(Blocks.TALL_GRASS))
            .blockstate((ctx, p) -> p.getVariantBuilder(ctx.get()).forAllStates(s -> {
                String name = ctx.getId().getPath();
                if (s.getValue(WildCaveCarrotBlock.STONE)) {
                    name = "stone_" + name;
                }
                ModelFile cross = p.models().withExistingParent(name, new ResourceLocation("block/cross")).texture("cross", path("block/" + name));
                return ConfiguredModel.builder().modelFile(cross).build();
            }))
            .item().model((ctx, prov) -> prov.blockSprite(ctx::getEntry)).tag(ModTags.WILD_CROPS_ITEM).build()
            .tag(BlockTags.SMALL_FLOWERS, ModTags.WILD_CROPS, ModTags.COMPOST_ACTIVATORS)
            .addLayer(()-> RenderType::cutout)
            .register();

    public static final BlockEntry<GossypiumFlowerBlock> GOSSYPIUM = setupBlock("gossypium", GossypiumFlowerBlock::new, BlockBehaviour.Properties.copy(Blocks.TALL_GRASS))
            .blockstate((ctx, p) -> p.getVariantBuilder(ctx.get()).forAllStates(s -> {
                String name = ctx.getId().getPath();
                if (s.getValue(WildCaveCarrotBlock.STONE)) {
                    name = "stone_" + name;
                }
                ModelFile cross = p.models().withExistingParent(name, new ResourceLocation("block/cross")).texture("cross", path("block/" + name));
                return ConfiguredModel.builder().modelFile(cross).build();
            }))
            .item().model((ctx, prov) -> prov.blockSprite(ctx::getEntry)).tag(ModTags.WILD_CROPS_ITEM).build()
            .tag(BlockTags.SMALL_FLOWERS)
            .addLayer(()-> RenderType::cutout)
            .register();

    public static final BlockEntry<CaveCarrotBlock> CAVE_CARROTS = setupBlock("cave_carrots", CaveCarrotBlock::new, BlockBehaviour.Properties.copy(Blocks.CARROTS))
            .blockstate((ctx, p) -> p.getVariantBuilder(ctx.get()).forAllStates(s -> {
                String name = ctx.getId().getPath()  + "_" + s.getValue(CaveCarrotBlock.AGE);
                ModelFile crop = p.models().withExistingParent(name, new ResourceLocation("block/crop")).texture("crop", path("block/" + name));
                return ConfiguredModel.builder().modelFile(crop).build();
            }))
            .tag(BlockTags.CROPS, MDTags.CAVE_CARROTS_CROP_BLOCK)
            .loot(caveCarrotTable())
            .addLayer(()-> RenderType::cutout)
            .register();

    public static final BlockEntry<Block> CAVE_CARROT_CRATE = setupBlock("cave_carrot_crate", Block::new, BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD))
            .blockstate((ctx, p) -> p.getVariantBuilder(ctx.get()).forAllStates(s -> {
                String name = ctx.getId().getPath();
                return ConfiguredModel.builder().modelFile(p.models().getExistingFile(path("block/"+name))).build();
            }))
            .simpleItem()
            .tag(BlockTags.MINEABLE_WITH_AXE)
            .register();


    public static <T extends Block> BlockBuilder<T, Registrate> setupBlock(String name, NonNullFunction<BlockBehaviour.Properties, T> factory, BlockBehaviour.Properties properties) {
        return BLOCK_REGISTRATE.block(name, factory).properties((x) -> properties);
    }

    public static <T extends StuffedSquidFeastBlock> NonNullBiConsumer<RegistrateBlockLootTables, T> stuffedSquidTable() {
        return (l, b) -> {
            LootTable.Builder builder = LootTable.lootTable();
            LootPool.Builder fullBlock = LootPool.lootPool().when(ExplosionCondition.survivesExplosion()).add(LootItem.lootTableItem(MDBlocks.STUFFED_SQUID.get().asItem())
                    .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(b).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(StuffedSquidFeastBlock.SERVINGS, 5)))
                    .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))));
            LootPool.Builder bowl = LootPool.lootPool().when(ExplosionCondition.survivesExplosion()).add(LootItem.lootTableItem(Items.BOWL)
                    .when(InvertedLootItemCondition.invert(LootItemBlockStatePropertyCondition.hasBlockStateProperties(b).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(StuffedSquidFeastBlock.SERVINGS, 5))))
                    .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))));
            l.add(b, builder.withPool(fullBlock).withPool(bowl));
        };
    }

    public static <T extends CaveCarrotBlock> NonNullBiConsumer<RegistrateBlockLootTables, T> caveCarrotTable() {
        return (l, b) -> {
            LootTable.Builder builder = LootTable.lootTable();
            LootPool.Builder fullyGrown = LootPool.lootPool().when(ExplosionCondition.survivesExplosion()).add(LootItem.lootTableItem(MDItems.CAVE_CARROT.get())
                    .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(b).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CaveCarrotBlock.AGE, 3)))
                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 4))));
            LootPool.Builder notFullyGrown = LootPool.lootPool().when(ExplosionCondition.survivesExplosion()).add(LootItem.lootTableItem(MDItems.CAVE_CARROT.get())
                    .when(InvertedLootItemCondition.invert(LootItemBlockStatePropertyCondition.hasBlockStateProperties(b).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CaveCarrotBlock.AGE, 3))))
                    .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))));
            l.add(b, builder.withPool(fullyGrown).withPool(notFullyGrown));
        };
    }

    public static void register() {
    }
}