package luckified.mixin.vanilla;

import luckified.handlers.ForgeConfigHandler;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.village.MerchantRecipeList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;
import java.util.UUID;

@Mixin(EntityVillager.ListEnchantedBookForEmeralds.class)
public class LibrarianMixin {

    @Unique
    public double playerLuck = 0;

    @Inject(
            method="addMerchantRecipe",
            at = @At(value="HEAD")
    )
    void savePlayerLuckMixin(IMerchant merchant, MerchantRecipeList recipeList, Random random, CallbackInfo ci){
        UUID playerUUID = ((EntityVillagerMixin) merchant).getLastBuyingPlayer();
        if(playerUUID != null) {
            EntityPlayer player = merchant.getWorld().getPlayerEntityByUUID(playerUUID);

            if (player == null) {
                playerLuck = 0;
            } else {
                playerLuck = player.getLuck();
            }
        }
    }

    @Redirect(
            method = "addMerchantRecipe",
            at = @At(value="INVOKE", target = "Lnet/minecraft/util/math/MathHelper;getInt(Ljava/util/Random;II)I")
    )
    private int changeLevelWeightsMixin(Random random, int minimum, int maximum){
        if(maximum > minimum) {
            //exponential weights: each level has a times wf higher probability than the one before
            double wf = 1. + playerLuck*ForgeConfigHandler.server.librarianEnchLevelWeightFactor;
            double sumweights = 0.;
            double currweight = 1.;
            for(int i=minimum; i<=maximum; i++){
                sumweights += currweight;
                currweight *= wf;
            }

            //weighted roll
            double roll = random.nextFloat()*sumweights;
            double weightCompare = 1.;
            for(int i=minimum; i<=maximum; i++){
                if(roll<weightCompare)
                    return i;
                else
                    weightCompare += wf;
            }
            return maximum;
        }
        //else
        return minimum;
    }
}
