package luckified.handlers;

import artifacts.common.ModConfig;
import luckified.ModLoadedUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MimicLuckHandler {

    double mimicChanceWithoutLuck = 0.;
    boolean chancesIncreased = false;

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onRightClickPre(PlayerInteractEvent.RightClickBlock event) {
        if(ModLoadedUtil.isRLArtifactsLoaded() && !chancesIncreased) {
            if (event.getUseBlock() != Event.Result.DENY && !event.getWorld().isRemote && event.getEntityPlayer() != null && ModConfig.general.unlootedChestMimicRatio > 0.0 && ForgeConfigHandler.server.luckMimicChance > 0.0) {
                BlockPos pos = event.getPos();
                World world = event.getWorld();
                TileEntity tile = world.getTileEntity(pos);
                Block block = world.getBlockState(pos).getBlock();
                EntityPlayer player = event.getEntityPlayer();
                if (tile instanceof TileEntityChest && block instanceof BlockChest && !player.isSpectator()) {
                    //mimicChanceWithoutLuck = ModConfig.general.unlootedChestMimicRatio;

                    double addedChance = event.getEntityPlayer().getLuck() * ForgeConfigHandler.server.luckMimicChance;
                    ModConfig.general.unlootedChestMimicRatio += addedChance;
                    chancesIncreased = true;
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onRightClickAfter(PlayerInteractEvent.RightClickBlock event) {
        if(ModLoadedUtil.isRLArtifactsLoaded()) {
            if (chancesIncreased) {
                ModConfig.general.unlootedChestMimicRatio = mimicChanceWithoutLuck;
                chancesIncreased = false;
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onBlockBreakPre(BlockEvent.BreakEvent event) {
        if(ModLoadedUtil.isRLArtifactsLoaded() && !chancesIncreased) {
            if (!event.getWorld().isRemote && event.getPlayer() != null && ModConfig.general.unlootedChestMimicRatio > 0.0 && ForgeConfigHandler.server.luckMimicChance > 0.0) {
                BlockPos pos = event.getPos();
                World world = event.getWorld();
                TileEntity tile = world.getTileEntity(pos);
                Block block = world.getBlockState(pos).getBlock();
                EntityPlayer player = event.getPlayer();
                if (tile instanceof TileEntityChest && block instanceof BlockChest && !player.isSpectator()) {
                    //mimicChanceWithoutLuck = ModConfig.general.unlootedChestMimicRatio;

                    double addedChance = player.getLuck() * ForgeConfigHandler.server.luckMimicChance;
                    ModConfig.general.unlootedChestMimicRatio += addedChance;
                    chancesIncreased = true;
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onBlockBreakAfter(BlockEvent.BreakEvent event) {
        if(ModLoadedUtil.isRLArtifactsLoaded()) {
            if (chancesIncreased) {
                ModConfig.general.unlootedChestMimicRatio = mimicChanceWithoutLuck;
                chancesIncreased = false;
            }
        }
    }
}
