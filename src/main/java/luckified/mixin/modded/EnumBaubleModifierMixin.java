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

@Mixin(EnumBaubleModifier.class)
public class EnumBaubleModifierMixin {

    @Shadow(remap = false) @Final private static int TOTAL_WEIGHT;
    @Shadow(remap = false) @Final private static List<EnumBaubleModifier> VALUES;

    @Unique private static float luckified$playerLuck = 0F;

    @Inject(
            method = "generateModifier",
            at = @At(value = "HEAD"),
            remap = false
    )
    private static void luckified_extractLuck(ItemStack stack, CallbackInfo ci) {
        if (stack.hasTagCompound()) luckified$playerLuck = stack.getTagCompound().getFloat("reforgingLuck");
        else luckified$playerLuck = 0F;
    }

    @Redirect(
            method = "getWeightedRandom",
            at = @At(value = "FIELD", target = "Lcursedflames/bountifulbaubles/baubleeffect/EnumBaubleModifier;weight:I"),
            remap = false
    )
    private static int luckified_changeWeights(EnumBaubleModifier instance) {
        return luckified$getLuckWeight(instance);
    }

    @Unique private static List<String> luckified$rareModifiers = null;

    @Redirect(
            method = "getWeightedRandom",
            at = @At(value = "FIELD", target = "Lcursedflames/bountifulbaubles/baubleeffect/EnumBaubleModifier;TOTAL_WEIGHT:I"),
            remap = false
    )
    private static int luckified_changeTotalWeights() {
        if (ModConfig.bountifulBaubles.rareModifierWeightPerLuck <= 0F)
            return TOTAL_WEIGHT;

        int total = 0;
        for (EnumBaubleModifier mod : VALUES)
            total += luckified$getLuckWeight(mod);

        return total;
    }

    @Unique
    private static int luckified$getLuckWeight(EnumBaubleModifier mod) {
        if(luckified$rareModifiers == null)
            luckified$rareModifiers = Arrays.asList(ModConfig.bountifulBaubles.rareModifierList);

        float weightPerLuck = ModConfig.bountifulBaubles.rareModifierWeightPerLuck;
        //Dividing by weightPerLuck to not get truncation errors when its below 1
        if (weightPerLuck > 0 && luckified$rareModifiers.contains(mod.name))
            return (int) ((mod.weight + luckified$playerLuck * weightPerLuck) / Math.min(1F, weightPerLuck));
        else
            return (int) (mod.weight / Math.min(1F, weightPerLuck));
    }
}
