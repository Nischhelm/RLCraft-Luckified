package luckified.mixin.modded;

import cursedflames.bountifulbaubles.baubleeffect.EnumBaubleModifier;
import luckified.ModLoadedUtil;
import luckified.handlers.ForgeConfigHandler;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

import static java.lang.Math.min;

@Mixin(EnumBaubleModifier.class)
public class EnumBaubleModifierMixin {

    @Shadow(remap=false) @Final private static int TOTAL_WEIGHT;
    @Shadow(remap=false) @Final private static List<EnumBaubleModifier> VALUES;

    @Unique
    private static double playerLuck;

    @Inject(
            method = "generateModifier",
            at = @At(value = "HEAD"),
            remap = false
    )
    private static void extractLuckMixin(ItemStack stack, CallbackInfo ci){
        if(ModLoadedUtil.isBountifulBaublesLoaded()) {
            if (stack.hasTagCompound())
                playerLuck = stack.getTagCompound().getDouble("reforgingLuck");
            else
                playerLuck = 0.0;
        }
    }

    @Redirect(
            method = "getWeightedRandom",
            at = @At(value = "FIELD", target = "Lcursedflames/bountifulbaubles/baubleeffect/EnumBaubleModifier;weight:I"),
            remap = false
    )
    private static int changeWeightsMixin(EnumBaubleModifier instance){
        if(ModLoadedUtil.isBountifulBaublesLoaded()) {
            double wf = ForgeConfigHandler.server.rareModifierWeightPerLuck;
            if (wf > 0.)
                for (String rareName : ForgeConfigHandler.server.rareModifierList)
                    if (instance.name.equals(rareName))
                        return (int) ((instance.weight + playerLuck * wf) / min(1.0, wf));

            return (int) (instance.weight / min(1.0, wf));
        }
        return 0;
    }

    @Redirect(
            method = "getWeightedRandom",
            at = @At(value = "FIELD", target = "Lcursedflames/bountifulbaubles/baubleeffect/EnumBaubleModifier;TOTAL_WEIGHT:I"),
            remap = false
    )
    private static int changeTotalWeightsMixin(){
        if(ModLoadedUtil.isBountifulBaublesLoaded()) {
            double wf = ForgeConfigHandler.server.rareModifierWeightPerLuck;

            if (wf > 0.) {
                int total = 0;

                for (EnumBaubleModifier mod : VALUES) {
                    boolean found = false;
                    for (String rareName : ForgeConfigHandler.server.rareModifierList) {
                        if (mod.name.equals(rareName)) {
                            total += (int) ((mod.weight + playerLuck * wf) / min(1.0, wf));
                            found = true;
                        }
                    }
                    if (!found)
                        total += (int) (mod.weight / min(1.0, wf));
                }

                return total;
            }

            return TOTAL_WEIGHT;
        }
        return 0;
    }

}
