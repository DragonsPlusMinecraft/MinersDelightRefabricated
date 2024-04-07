package com.sammy.minersdelight.content.block.copper_pot;

import io.github.fabricators_of_create.porting_lib.transfer.item.SlottedStackStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.UnmodifiableView;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class CopperPotItemHandler implements SlottedStackStorage
{
	private static final int SLOTS_INPUT = 4;
	private static final int SLOT_CONTAINER_INPUT = 5;
	private static final int SLOT_MEAL_OUTPUT = 6;
	private final SlottedStackStorage itemHandler;
	private final Direction side;

	public CopperPotItemHandler(SlottedStackStorage itemHandler, @Nullable Direction side) {
		this.itemHandler = itemHandler;
		this.side = side;
	}

	@Override
	public boolean isItemValid(int slot, ItemVariant resource, int count) {
		return itemHandler.isItemValid(slot, resource, count);
	}

	@Override
	public int getSlotCount() {
		return itemHandler.getSlotCount();
	}

	@Override
	public SingleSlotStorage<ItemVariant> getSlot(int slot) {
		return itemHandler.getSlot(slot);
	}

	@Override
	public @UnmodifiableView List<SingleSlotStorage<ItemVariant>> getSlots() {
		return itemHandler.getSlots();
	}

	@Override
	@Nonnull
	public ItemStack getStackInSlot(int slot) {
		return itemHandler.getStackInSlot(slot);
	}

	@Override
	public void setStackInSlot(int slot, ItemStack stack) {

	}

	@Nonnull
	public long insertSlot(int slot, ItemVariant resource, long maxAmount, TransactionContext transaction) {
		if (side == null || side.equals(Direction.UP)) {
			return slot < SLOTS_INPUT ? itemHandler.insertSlot(slot, resource, maxAmount, transaction) : 0;
		} else {
			return slot == SLOT_CONTAINER_INPUT ? itemHandler.insertSlot(slot, resource, maxAmount, transaction) : 0;
		}
	}

	@Nonnull
	public long extractSlot(int slot, ItemVariant resource, long maxAmount, TransactionContext transaction) {
		if (side == null || side.equals(Direction.UP)) {
			return slot < SLOTS_INPUT ? itemHandler.extractSlot(slot, resource, maxAmount, transaction) : 0;
		} else {
			return slot == SLOT_MEAL_OUTPUT ? itemHandler.extractSlot(slot, resource, maxAmount, transaction) : 0;
		}
	}

	@Override
	public int getSlotLimit(int slot) {
		return itemHandler.getSlotLimit(slot);
	}

	@Override
	public long insert(ItemVariant resource, long maxAmount, TransactionContext transaction) {
		return itemHandler.insert(resource, maxAmount, transaction);
	}

	@Override
	public long extract(ItemVariant resource, long maxAmount, TransactionContext transaction) {
		return itemHandler.extract(resource, maxAmount, transaction);
	}

}
