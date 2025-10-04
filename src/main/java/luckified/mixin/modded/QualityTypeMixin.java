package luckified.mixin.modded;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.tmtravlr.qualitytools.config.QualityEntry;
import com.tmtravlr.qualitytools.config.QualityType;
import luckified.ModConfig;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.List;

@Mixin(QualityType.class)
public class QualityTypeMixin {

    @Unique public float luckified$playerLuck = 0F;

    @Inject(
            method = "generateQualityTag",
            at = @At(value = "HEAD"),
            remap = false
    )
    public void luckified_extractLuck(ItemStack stack, boolean skipNormal, CallbackInfo ci){
        if (stack.hasTagCompound()) this.luckified$playerLuck = stack.getTagCompound().getFloat("reforgingLuck");
        else this.luckified$playerLuck = 0F;
    }

    @Unique private static List<String> luckified$rareQualities = null;

    @WrapOperation(
            method = "chooseQualityEntry",
            at = @At(value = "FIELD", target = "Lcom/tmtravlr/qualitytools/config/QualityEntry;weight:I"),
            remap = false
    )
    public int luckified_changeWeights(QualityEntry entry, Operation<Integer> original){
        if(luckified$rareQualities == null)
            luckified$rareQualities = Arrays.asList(ModConfig.qualityTools.rareQualityList);

        float weightPerLuck = ModConfig.qualityTools.rareQualityWeightPerLuck;
        if (weightPerLuck > 0. && luckified$rareQualities.contains(entry.name))
            return (int) ((original.call(entry) + luckified$playerLuck * weightPerLuck) / Math.min(1F, weightPerLuck));
        else
            return (int) (original.call(entry) / Math.min(1F, weightPerLuck));
    }

}
