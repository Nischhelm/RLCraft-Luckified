package luckified.mixin.modded;

import com.tmtravlr.qualitytools.config.QualityEntry;
import com.tmtravlr.qualitytools.config.QualityType;
import luckified.handlers.ForgeConfigHandler;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

import static java.lang.Math.min;

@Mixin(QualityType.class)
public class QualityTypeMixin {

    @Unique
    public double playerLuck;

    @Shadow(remap=false)
    public QualityEntry[] qualities;

    @Final
    @Shadow(remap=false)
    public static Random RAND;

    @Inject(
            method = "generateQualityTag",
            at = @At(value = "HEAD"),
            remap = false
    )
    public void extractLuckMixin(ItemStack stack, boolean skipNormal, CallbackInfo ci){
        if(stack.hasTagCompound())
            this.playerLuck = stack.getTagCompound().getDouble("reforgingLuck");
        else
            this.playerLuck = 0.0;
    }

    @Redirect(
            method = "chooseQualityEntry",
            at = @At(value = "FIELD", target = "Lcom/tmtravlr/qualitytools/config/QualityEntry;weight:I"),
            remap = false
    )
    public int changeWeightsMixin(QualityEntry entry){
        double wf = ForgeConfigHandler.server.rareQualityWeightPerLuck;
        if(wf > 0.)
            for(String rareName: ForgeConfigHandler.server.rareQualityList)
                if(entry.name.equals(rareName))
                    return (int) ((entry.weight + playerLuck*wf)/min(1.0,wf));

        return (int) (entry.weight/min(1.0,wf));
    }

}
