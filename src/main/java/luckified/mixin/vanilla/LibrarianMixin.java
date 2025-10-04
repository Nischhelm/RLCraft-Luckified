package luckified.mixin.vanilla;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import luckified.ModConfig;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Random;
import java.util.UUID;

@Mixin(EntityVillager.ListEnchantedBookForEmeralds.class)
public class LibrarianMixin {

    @WrapOperation(
            method = "addMerchantRecipe",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;getInt(Ljava/util/Random;II)I")
    )
    private int luckified_changeLevelWeights(Random rand, int minLvl, int maxLvl, Operation<Integer> original, @Local(argsOnly = true) IMerchant merchant) {
        //Mechanic disabled
        if(ModConfig.vanilla.librarianEnchLevelWeightFactor <= 0F) return original.call(rand,minLvl,maxLvl);

        //No need to calc anything if the enchant only has one level
        if (maxLvl <= minLvl) return minLvl;

        //Get Luck of interacting player
        float playerLuck = 0;
        UUID playerUUID = ((EntityVillagerAccessor) merchant).getLastBuyingPlayer();
        if (playerUUID != null) {
            EntityPlayer player = merchant.getWorld().getPlayerEntityByUUID(playerUUID);
            if (player != null) playerLuck = player.getLuck();
        }

        //No luck means we don't need to do any extra calcs
        if (playerLuck <= 0) return MathHelper.getInt(rand, minLvl, maxLvl);

        //Exponential weights in order to make each lvl x times more probable than the one before
        //Default: every point of luck gives 10% higher chance per enchant lvl
        float weightFromLuck = 1F + playerLuck * ModConfig.vanilla.librarianEnchLevelWeightFactor;

        //Get total weights
        float totalWeights = 0F;
        float currFactor = 1F;
        for (int lvl = minLvl; lvl <= maxLvl; lvl++) {
            totalWeights += currFactor;
            //Easier way to calc currFactor = weightFromLuck ^ (lvl - minLvl)
            currFactor *= weightFromLuck;
        }

        //Weighted roll
        float roll = rand.nextFloat() * totalWeights;
        currFactor = 1F;
        for (int lvl = minLvl; lvl <= maxLvl; lvl++) {
            roll -= currFactor;
            currFactor *= weightFromLuck;
            if (roll < 0) return lvl;
        }

        return maxLvl;
    }
}