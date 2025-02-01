package luckified.mixin.modded;

import cursedflames.bountifulbaubles.baubleeffect.EnumBaubleModifier;
import luckified.ModConfig;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.List;

import static java.lang.Math.min;

@Mixin(EnumBaubleModifier.class)
public class EnumBaubleModifierMixin {

    @Shadow(remap = false) @Final private static int TOTAL_WEIGHT;
    @Shadow(remap = false) @Final private static List<EnumBaubleModifier> VALUES;

    @Unique private static float playerLuck = 0F;

    @Inject(
            method = "generateModifier",
            at = @At(value = "HEAD"),
            remap = false
    )
    private static void extractLuck(ItemStack stack, CallbackInfo ci) {
        if (stack.hasTagCompound()) playerLuck = stack.getTagCompound().getFloat("reforgingLuck");
        else playerLuck = 0F;
    }

    @Redirect(
            method = "getWeightedRandom",
            at = @At(value = "FIELD", target = "Lcursedflames/bountifulbaubles/baubleeffect/EnumBaubleModifier;weight:I"),
            remap = false
    )
    private static int changeWeights(EnumBaubleModifier instance) {
        return getLuckWeight(instance);
    }

    @Unique private static List<String> rareModifiers = Arrays.asList(ModConfig.bountifulBaubles.rareModifierList);

    @Redirect(
            method = "getWeightedRandom",
            at = @At(value = "FIELD", target = "Lcursedflames/bountifulbaubles/baubleeffect/EnumBaubleModifier;TOTAL_WEIGHT:I"),
            remap = false
    )
    private static int changeTotalWeights() {
        if (ModConfig.bountifulBaubles.rareModifierWeightPerLuck <= 0F)
            return TOTAL_WEIGHT;

        int total = 0;
        for (EnumBaubleModifier mod : VALUES)
            total += getLuckWeight(mod);

        return total;
    }

    @Unique
    private static int getLuckWeight(EnumBaubleModifier mod) {
        float weightPerLuck = ModConfig.bountifulBaubles.rareModifierWeightPerLuck;
        //Dividing by weightPerLuck to not get truncation errors when its below 1
        if (weightPerLuck > 0 && rareModifiers.contains(mod.name))
            return (int) ((mod.weight + playerLuck * weightPerLuck) / Math.min(1F, weightPerLuck));
        else
            return (int) (mod.weight / Math.min(1F, weightPerLuck));
    }
}
