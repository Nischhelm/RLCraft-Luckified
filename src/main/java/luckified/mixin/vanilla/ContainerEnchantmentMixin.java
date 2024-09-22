package luckified.mixin.vanilla;

import luckified.handlers.ForgeConfigHandler;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerEnchantment;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(ContainerEnchantment.class)
public class ContainerEnchantmentMixin {

    @Unique
    public EntityPlayer player;

    @Inject(
            method = "transferStackInSlot",
            at = @At(value="HEAD")
    )
    private void saveEntityPlayerMixin(EntityPlayer playerIn, int index, CallbackInfoReturnable<Boolean> cir){
        player = playerIn;
    }


    @Redirect(
            method = "onCraftMatrixChanged",
            at = @At(value="INVOKE",target = "Lnet/minecraft/enchantment/EnchantmentHelper;calcItemStackEnchantability(Ljava/util/Random;IILnet/minecraft/item/ItemStack;)I")
    )
    private int overWriteEnchantmentLevelMixin(Random rand, int i1, int power, ItemStack itemstack){
        double playerLuck = player==null ? 0 : player.getLuck();
        return EnchantmentHelper.calcItemStackEnchantability(rand, i1, power, itemstack)+(int) (playerLuck* ForgeConfigHandler.server.luckEnchantabilityTable);
    }

    @Redirect(
            method = "onCraftMatrixChanged",
            at = @At(value="INVOKE",target = "Ljava/util/Random;nextInt(I)I")
    )
    private int luckDependentClue(Random rand, int listSize){
        double playerLuck = player==null ? 0 : player.getLuck();
        int zeroWeight = (int) (ForgeConfigHandler.server.luckTopEnchantWeight * playerLuck);
        int weightedSize = listSize + zeroWeight;
        if (rand.nextInt(weightedSize) < 1 + zeroWeight) {
            return 0;
        } else
            return 1 + rand.nextInt(listSize - 1);
    }
}
