package luckified.mixin.modded;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import luckified.ModConfig;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "artifacts.common.entity.EntityMimic$MimicEventHandler")
public class MimicLuckMixin {
    @ModifyExpressionValue(
            method = "onRightClick",
            at = @At(value = "FIELD", target = "Lartifacts/common/ModConfig$General;unlootedChestMimicRatio:D", ordinal = 1),
            remap = false
    )
    private static double luckified_changeMimicChanceOnOpen(double original, @Local EntityPlayer player){
        return original + luckified$getAddedLuckChance(player);
    }

    @ModifyExpressionValue(
            method = "onBlockBreak",
            at = @At(value = "FIELD", target = "Lartifacts/common/ModConfig$General;unlootedChestMimicRatio:D", ordinal = 1),
            remap = false
    )
    private static double luckified_changeMimicChanceOnBreak(double original, @Local EntityPlayer player){
        return original + luckified$getAddedLuckChance(player);
    }

    @Unique
    private static double luckified$getAddedLuckChance(EntityPlayer player){
        return player.getLuck() * ModConfig.rlArtifacts.luckMimicChance;
    }
}
