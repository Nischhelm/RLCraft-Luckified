package luckified.mixin.modded;

import luckified.ModConfig;
import lykrast.defiledlands.common.entity.passive.EntityBookWyrm;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityBookWyrm.class)
public abstract class EntityBookWyrmMixin {

    @Shadow(remap = false) public abstract boolean isGolden();

    @Unique private EntityPlayer lastInteractingPlayer = null;

    @Inject(
            method = "processInteract",
            at = @At("HEAD")
    )
    public void saveLastInteractingPlayer(EntityPlayer player, EnumHand hand, CallbackInfoReturnable<Boolean> cir){
        this.lastInteractingPlayer = player;
    }

    @Inject(
            method = "setOffspringAttributes",
            at = @At(value = "INVOKE", target = "Llykrast/defiledlands/common/entity/passive/EntityBookWyrm;getDigestTime()I",ordinal = 0),
            remap = false
    )
    public void increaseGoldChance(EntityBookWyrm parent, EntityBookWyrm child, CallbackInfo ci){
        if(lastInteractingPlayer == null) return;
        float luck = lastInteractingPlayer.getLuck();
        if(luck <= 0) return;

        boolean isGold = child.getRNG().nextFloat() < getChildGoldChance(parent, luck);
        child.setGolden(isGold);

        //Reset player, so no love arrow shenanigans
        lastInteractingPlayer = null;
    }

    @Unique
    private float getChildGoldChance(EntityBookWyrm parent, float luck) {
        //Recreate DefiledLands rolling of gold Bookwyrms with additional luck
        boolean thisIsGold = this.isGolden();
        boolean parentIsGold = parent.isGolden();

        //Default chances
        float childGoldChance = 0.04F;                              //gold+purple
        if (!thisIsGold && !parentIsGold) childGoldChance = 0.01F;  //purple+purple
        if (thisIsGold && parentIsGold) childGoldChance = 0.1F;     //gold+gold

        //Multiply with luck
        childGoldChance *= 1F + luck * ModConfig.defiledlands.goldWyrmPerLuck;
        return childGoldChance;
    }
}
