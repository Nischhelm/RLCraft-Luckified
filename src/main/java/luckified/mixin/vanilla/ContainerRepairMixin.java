package luckified.mixin.vanilla;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.inventory.ContainerRepair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ContainerRepair.class)
public class ContainerRepairMixin {

    @WrapOperation(
            method = "updateRepairOutput",
            at = @At(value = "INVOKE", target = "Ljava/lang/String;equals(Ljava/lang/Object;)Z")
    )
    //NetHandlerServer replaces § in item rename packets then fails the equals check and desyncs
    private boolean luckified_fixAnvilingColoredItemName(String guiName, Object itemStackName, Operation<Boolean> original){
        String soSpecial = "§";
        soSpecial = soSpecial.substring(soSpecial.length()-1); //even intellij says this is always "§" but it's not

        return original.call(guiName, itemStackName) ||
                //for whatever reason just using "§" makes minecraft read Å§ and fail the equals compare
                original.call(guiName, ((String) itemStackName).replaceAll(soSpecial,""));
    }
}
