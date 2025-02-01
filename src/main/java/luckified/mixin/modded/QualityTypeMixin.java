package luckified.mixin.modded;

import com.tmtravlr.qualitytools.config.QualityEntry;
import com.tmtravlr.qualitytools.config.QualityType;
import luckified.ModConfig;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.List;

@Mixin(QualityType.class)
public class QualityTypeMixin {

    @Unique public float playerLuck = 0F;

    @Inject(
            method = "generateQualityTag",
            at = @At(value = "HEAD"),
            remap = false
    )
    public void extractLuck(ItemStack stack, boolean skipNormal, CallbackInfo ci){
        if (stack.hasTagCompound()) this.playerLuck = stack.getTagCompound().getFloat("reforgingLuck");
        else this.playerLuck = 0F;
    }

    @Unique private static List<String> rareQualities = Arrays.asList(ModConfig.qualityTools.rareQualityList);

    @Redirect(
            method = "chooseQualityEntry",
            at = @At(value = "FIELD", target = "Lcom/tmtravlr/qualitytools/config/QualityEntry;weight:I"),
            remap = false
    )
    public int changeWeights(QualityEntry entry){
        float weightPerLuck = ModConfig.qualityTools.rareQualityWeightPerLuck;
        if (weightPerLuck > 0. && rareQualities.contains(entry.name))
            return (int) ((entry.weight + playerLuck * weightPerLuck) / Math.min(1F, weightPerLuck));
        else
            return (int) (entry.weight / Math.min(1F, weightPerLuck));
    }

}
