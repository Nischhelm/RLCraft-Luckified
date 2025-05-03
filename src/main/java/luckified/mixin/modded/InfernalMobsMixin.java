package luckified.mixin.modded;

import atomicstryker.infernalmobs.common.InfernalMobsCore;
import atomicstryker.infernalmobs.common.MobModifier;
import com.llamalad7.mixinextras.sugar.Local;
import luckified.ModConfig;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Random;

@Mixin(InfernalMobsCore.class)
public class InfernalMobsMixin {

    @ModifyArg(
            method = "dropRandomEnchantedItems",
            at = @At(value = "INVOKE", target = "Latomicstryker/infernalmobs/common/InfernalMobsCore;enchantRandomly(Ljava/util/Random;Lnet/minecraft/item/ItemStack;II)V"),
            index = 2,
            remap = false
    )
    public int luckified_fixInfernalMobsEnchanting(int enchLvl, @Local(argsOnly = true) MobModifier mods, @Local(argsOnly = true) EntityLivingBase mob) {
        if (!ModConfig.infernalmobs.fixInfernalMobsEnchantability) return enchLvl;

        Random rand = mob.world.rand;
        int modStr = mods.getModSize();
        int prefix = modStr <= 5 ? 0 : (modStr <= 10 ? 1 : 2);

        int min;
        int max;
        switch (prefix) {
            case 2:
                min = ModConfig.infernalmobs.infernalMobsEnchantabilityInfernal[0];
                max = ModConfig.infernalmobs.infernalMobsEnchantabilityInfernal[1];
                break;
            case 1:
                min = ModConfig.infernalmobs.infernalMobsEnchantabilityUltra[0];
                max = ModConfig.infernalmobs.infernalMobsEnchantabilityUltra[1];
                break;
            case 0:
            default:
                min = ModConfig.infernalmobs.infernalMobsEnchantabilityElite[0];
                max = ModConfig.infernalmobs.infernalMobsEnchantabilityElite[1];
                break;
        }
        return MathHelper.getInt(rand, min, max);
    }
}
