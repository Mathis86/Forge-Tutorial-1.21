package net.deadsck.tutorialmod.item.custom;

import net.deadsck.tutorialmod.block.ModBlocks;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.Map;

public class ChiselItem extends Item {
    private static final Map<Block, Block> CHISEL_MAP = Map.of(
            Blocks.STONE, Blocks.STONE_BRICKS,
            Blocks.IRON_BLOCK, Blocks.DIAMOND_BLOCK,
            Blocks.IRON_ORE, Blocks.DIAMOND_ORE,
            Blocks.DIAMOND_BLOCK, Blocks.IRON_BLOCK,
            Blocks.DIAMOND_ORE, Blocks.IRON_ORE,
            ModBlocks.MAGIC_BLOCK.get(), Blocks.DIRT
    );

    public ChiselItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Level level = pContext.getLevel();
        Block clickedBlock = level.getBlockState(pContext.getClickedPos()).getBlock();

        if (CHISEL_MAP.containsKey(clickedBlock) && !level.isClientSide()) {
            // Récupère le joueur
            ServerPlayer player = (ServerPlayer) pContext.getPlayer();

            // Savoir dans quelle main se trouve l'objet
            EquipmentSlot slot = pContext.getHand() == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;

            // Change le bloc
            level.setBlockAndUpdate(pContext.getClickedPos(), CHISEL_MAP.get(clickedBlock).defaultBlockState());

            // Réduit la durabilité du Chisel
            pContext.getItemInHand().hurtAndBreak(1, ((ServerLevel) level), player,
                    item -> {
                        player.onEquippedItemBroken(item, slot);
                        level.playSound(null, pContext.getClickedPos(), SoundEvents.LARGE_AMETHYST_BUD_BREAK, SoundSource.PLAYERS);
                    });

            // Joue un son lors de l'utilisation
            level.playSound(null, pContext.getClickedPos(), SoundEvents.AMETHYST_BLOCK_BREAK, SoundSource.BLOCKS);
        }

        return InteractionResult.SUCCESS;
    }
}
