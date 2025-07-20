package net.deadsck.tutorialmod.block.custom;

import net.deadsck.tutorialmod.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.Map;

public class MagicBlock extends Block {
    private static final Map<Item, Item> MAGIC_BLOCK_MAP = Map.of(
            ModItems.ALEXANDRITE.get(), Items.DIAMOND,
            Items.AMETHYST_SHARD, Items.AMETHYST_CLUSTER
    );

    public MagicBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, BlockHitResult pHitResult) {
        // Jouer un son au clic droit
        pLevel.playSound(pPlayer, pPos, SoundEvents.ALLAY_DEATH, SoundSource.BLOCKS);

        return InteractionResult.SUCCESS;
    }

    @Override
    public void stepOn(Level pLevel, BlockPos pPos, BlockState pState, Entity pEntity) {
        if (pEntity instanceof ItemEntity itemEntity && !pLevel.isClientSide()) {
            Item item = itemEntity.getItem().getItem();
            int stackCount = itemEntity.getItem().getCount();

            if (MAGIC_BLOCK_MAP.containsKey(item)) {
                if (item == Items.AMETHYST_SHARD) {
                    int amethystClusterCount = stackCount / 4;
                    int amethystShardCount = stackCount % 4;

                    if (amethystClusterCount > 0) {
                        itemEntity.setItem(new ItemStack(Items.AMETHYST_CLUSTER, amethystClusterCount));
                        pLevel.playSound(null, pPos, SoundEvents.ALLAY_ITEM_TAKEN, SoundSource.BLOCKS);
                    }
                }
                else {
                    itemEntity.setItem(new ItemStack(MAGIC_BLOCK_MAP.get(item), stackCount));
                    pLevel.playSound(null, pPos, SoundEvents.ALLAY_ITEM_TAKEN, SoundSource.BLOCKS);
                }

            }

            else if (!MAGIC_BLOCK_MAP.containsValue(item) && item != Items.DIRT) {
                itemEntity.setItem(new ItemStack(Items.DIRT, stackCount));
                pLevel.playSound(null, pPos, SoundEvents.ALLAY_HURT, SoundSource.BLOCKS);
            }
        }
        super.stepOn(pLevel, pPos, pState, pEntity);
    }
}
