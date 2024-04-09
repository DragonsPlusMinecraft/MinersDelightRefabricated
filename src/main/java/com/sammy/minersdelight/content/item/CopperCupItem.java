package com.sammy.minersdelight.content.item;

import com.sammy.minersdelight.setup.MDItems;
import io.github.fabricators_of_create.porting_lib.fluids.sound.SoundActions;
import net.fabricmc.fabric.api.lookup.v1.item.ItemApiLookup;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorageUtil;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.FullItemFluidStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.storage.base.BlankVariantView;
import net.fabricmc.fabric.api.transfer.v1.storage.base.InsertionOnlyStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@SuppressWarnings("UnstableApiUsage")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CopperCupItem extends Item implements DispensibleContainerItem, ItemApiLookup.ItemApiProvider<Storage<FluidVariant>, ContainerItemContext> {

    public static final Function<Item, ItemStack> BUCKET_TO_CUP = (item) -> {
        if (Items.WATER_BUCKET.equals(item)) {
            return MDItems.WATER_CUP.asStack();
        } else if (Items.MILK_BUCKET.equals(item)) {
            return MDItems.MILK_CUP.asStack();
        } else if (Items.POWDER_SNOW_BUCKET.equals(item)) {
            return MDItems.POWDERED_SNOW_CUP.asStack();
        } else if (Items.BUCKET.equals(item)) {
            return MDItems.COPPER_CUP.asStack();
        }
        return ItemStack.EMPTY;
    };

    private final Fluid content;

    public CopperCupItem(Fluid pContent, Item.Properties pProperties) {
        super(pProperties);
        this.content = pContent;
        FluidStorage.ITEM.registerForItems(this, this);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack pStack, Player pPlayer, LivingEntity pInteractionTarget, InteractionHand pUsedHand) {
        if (content.equals(Fluids.EMPTY)) {
            pPlayer.setItemInHand(pUsedHand, Items.BUCKET.getDefaultInstance());
            pInteractionTarget.interact(pPlayer, pUsedHand);
            Item maybeFilledBucket = pPlayer.getItemInHand(pUsedHand).getItem();
            ItemStack filledResult = ItemUtils.createFilledResult(pStack, pPlayer, BUCKET_TO_CUP.apply(maybeFilledBucket));
            pPlayer.setItemInHand(pUsedHand, filledResult);
        }
        return super.interactLivingEntity(pStack, pPlayer, pInteractionTarget, pUsedHand);
    }

    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        BlockHitResult blockhitresult = getPlayerPOVHitResult(pLevel, pPlayer, this.content == Fluids.EMPTY ? ClipContext.Fluid.SOURCE_ONLY : ClipContext.Fluid.NONE);
//      InteractionResultHolder<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onBucketUse(pPlayer, pLevel, itemstack, blockhitresult);
//      if (ret != null)
//         return ret;
        if (blockhitresult.getType() == HitResult.Type.MISS) {
            return InteractionResultHolder.pass(itemstack);
        } else if (blockhitresult.getType() != HitResult.Type.BLOCK) {
            return InteractionResultHolder.pass(itemstack);
        }
        BlockPos blockpos = blockhitresult.getBlockPos();
        Direction direction = blockhitresult.getDirection();
        BlockPos blockpos1 = blockpos.relative(direction);
        BlockState blockstate = pLevel.getBlockState(blockpos);
        if (pLevel.mayInteract(pPlayer, blockpos) && pPlayer.mayUseItemAt(blockpos1, direction, itemstack)) {
            var storage = FluidStorage.SIDED.find(pLevel, blockpos, direction.getOpposite());
            if (storage != null && FluidStorageUtil.interactWithFluidStorage(storage, pPlayer, pHand)) {
                return InteractionResultHolder.sidedSuccess(pPlayer.getItemInHand(pHand), pLevel.isClientSide());
            } else if (this.content == Fluids.EMPTY) {
                if (blockstate.getBlock() instanceof BucketPickup bucketpickup) {
                    ItemStack bucket = bucketpickup.pickupBlock(pLevel, blockpos, blockstate);
                    if (!bucket.isEmpty()) {
                        ItemStack cup = BUCKET_TO_CUP.apply(bucket.getItem());
                        if (!cup.isEmpty()) {
                            pPlayer.awardStat(Stats.ITEM_USED.get(this));
                            bucketpickup.getPickupSound().ifPresent((p_150709_) -> pPlayer.playSound(p_150709_, 1.0F, 1.0F));
                            pLevel.gameEvent(pPlayer, GameEvent.FLUID_PICKUP, blockpos);
                            ItemStack itemstack2 = ItemUtils.createFilledResult(itemstack, pPlayer, cup);
                            if (!pLevel.isClientSide) {
                                CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer) pPlayer, cup);
                            }

                            return InteractionResultHolder.sidedSuccess(itemstack2, pLevel.isClientSide());
                        } else {
                            pLevel.setBlock(blockpos, blockstate, 3);
                        }
                    }
                }

                return InteractionResultHolder.fail(itemstack);
            } else {
                BlockPos blockpos2 = canBlockContainFluid(pLevel, blockpos, blockstate) ? blockpos : blockpos1;
                if (this.emptyContents(pPlayer, pLevel, blockpos2, blockhitresult)) {
                    this.checkExtraContent(pPlayer, pLevel, itemstack, blockpos2);
                    if (pPlayer instanceof ServerPlayer) {
                        CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) pPlayer, blockpos2, itemstack);
                    }

                    pPlayer.awardStat(Stats.ITEM_USED.get(this));
                    return InteractionResultHolder.sidedSuccess(getEmptySuccessItem(itemstack, pPlayer), pLevel.isClientSide());
                } else {
                    return InteractionResultHolder.fail(itemstack);
                }
            }
        } else {
            return InteractionResultHolder.fail(itemstack);
        }
    }

    public static ItemStack getEmptySuccessItem(ItemStack pBucketStack, Player pPlayer) {
        return !pPlayer.getAbilities().instabuild ? new ItemStack(MDItems.COPPER_CUP.get()) : pBucketStack;
    }

    public void checkExtraContent(@Nullable Player pPlayer, Level pLevel, ItemStack pContainerStack, BlockPos pPos) {
    }

    public boolean emptyContents(@Nullable Player pPlayer, Level pLevel, BlockPos pPos, @Nullable BlockHitResult pResult) {
        if (!(this.content instanceof FlowingFluid)) {
            return false;
        } else {
            BlockState blockstate = pLevel.getBlockState(pPos);
            Block block = blockstate.getBlock();
            boolean flag = blockstate.canBeReplaced(this.content);
            boolean flag1 = blockstate.isAir() || flag || block instanceof LiquidBlockContainer && ((LiquidBlockContainer) block).canPlaceLiquid(pLevel, pPos, blockstate, this.content);
            if (!flag1) {
                return pResult != null && this.emptyContents(pPlayer, pLevel, pResult.getBlockPos().relative(pResult.getDirection()), null);
            } else if (pLevel.dimensionType().ultraWarm() && this.content.is(FluidTags.WATER)) {
                int i = pPos.getX();
                int j = pPos.getY();
                int k = pPos.getZ();
                pLevel.playSound(pPlayer, pPos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F + (pLevel.random.nextFloat() - pLevel.random.nextFloat()) * 0.8F);

                for (int l = 0; l < 8; ++l) {
                    pLevel.addParticle(ParticleTypes.LARGE_SMOKE, (double) i + Math.random(), (double) j + Math.random(), (double) k + Math.random(), 0.0D, 0.0D, 0.0D);
                }

                return true;
            } else if (block instanceof LiquidBlockContainer && ((LiquidBlockContainer) block).canPlaceLiquid(pLevel, pPos, blockstate, content)) {
                ((LiquidBlockContainer) block).placeLiquid(pLevel, pPos, blockstate, ((FlowingFluid) this.content).getSource(false));
                this.playEmptySound(pPlayer, pLevel, pPos);
                return true;
            } else {
                if (!pLevel.isClientSide && flag && !blockstate.liquid()) {
                    pLevel.destroyBlock(pPos, true);
                }

                if (!pLevel.setBlock(pPos, this.content.defaultFluidState().createLegacyBlock(), 11) && !blockstate.getFluidState().isSource()) {
                    return false;
                } else {
                    this.playEmptySound(pPlayer, pLevel, pPos);
                    return true;
                }
            }
        }
    }

    //TODO: Not sure why PortingLib throws an exception when getting FluidType for vanilla fluids,
    // considering other modded fluids may also throw that exception,
    // a try-catch here should be a proper workaround? ;(
    protected void playEmptySound(@Nullable Player pPlayer, LevelAccessor pLevel, BlockPos pPos) {
        SoundEvent soundevent;
        try {
            soundevent = Objects.requireNonNull(this.content.getFluidType().getSound(pPlayer, pLevel, pPos, SoundActions.BUCKET_EMPTY));
        } catch (Exception ignored) {
            soundevent = this.content.is(FluidTags.LAVA) ? SoundEvents.BUCKET_EMPTY_LAVA : SoundEvents.BUCKET_EMPTY;
        }
        pLevel.playSound(pPlayer, pPos, soundevent, SoundSource.BLOCKS, 1.0F, 1.0F);
        pLevel.gameEvent(pPlayer, GameEvent.FLUID_PLACE, pPos);
    }

    private boolean canBlockContainFluid(Level worldIn, BlockPos posIn, BlockState blockstate) {
        return blockstate.getBlock() instanceof LiquidBlockContainer && ((LiquidBlockContainer) blockstate.getBlock()).canPlaceLiquid(worldIn, posIn, blockstate, this.content);
    }

    @Override
    public @Nullable Storage<FluidVariant> find(ItemStack itemStack, ContainerItemContext context) {
        if (this.content == Fluids.EMPTY)
            return new EmptyStorage(context);
        return new FullItemFluidStorage(context, MDItems.COPPER_CUP.asItem(), FluidVariant.of(this.content), FluidConstants.BUCKET);
    }

    protected class EmptyStorage implements InsertionOnlyStorage<FluidVariant> {
        private final ContainerItemContext context;
        private final List<StorageView<FluidVariant>> blankView = List.of(new BlankVariantView<>(FluidVariant.blank(), FluidConstants.BUCKET));

        public EmptyStorage(ContainerItemContext context) {
            this.context = context;
        }

        @Override
        public long insert(FluidVariant resource, long maxAmount, TransactionContext transaction) {
            StoragePreconditions.notBlankNotNegative(resource, maxAmount);

            if (!context.getItemVariant().isOf(CopperCupItem.this)) return 0;

            Item fullBucket = BUCKET_TO_CUP.apply(resource.getFluid().getBucket()).getItem();

            if (maxAmount >= FluidConstants.BUCKET) {
                ItemVariant newVariant = ItemVariant.of(fullBucket, context.getItemVariant().getNbt());

                if (context.exchange(newVariant, 1, transaction) == 1) {
                    return FluidConstants.BUCKET;
                }
            }

            return 0;
        }

        @Override
        public Iterator<StorageView<FluidVariant>> iterator() {
            return blankView.iterator();
        }
    }

}