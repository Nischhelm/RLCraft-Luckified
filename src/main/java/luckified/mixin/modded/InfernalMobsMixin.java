package luckified.mixin.modded;

import atomicstryker.infernalmobs.common.InfernalMobsCore;
import luckified.ModLoadedUtil;
import luckified.handlers.ForgeConfigHandler;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Random;

import static java.lang.Math.floor;

@Mixin(InfernalMobsCore.class)
public class InfernalMobsMixin {

    @Shadow(remap = false)
    private void enchantRandomly(Random rand, ItemStack itemStack, int itemEnchantability, int modStr) { }

    @Redirect(
            method = "dropRandomEnchantedItems",
            at = @At(value = "INVOKE", target = "Latomicstryker/infernalmobs/common/InfernalMobsCore;enchantRandomly(Ljava/util/Random;Lnet/minecraft/item/ItemStack;II)V"),
            remap = false
    )
    void fixInfernalMobsEnchantingMixin(InfernalMobsCore instance, Random rand, ItemStack itemStack, int itemEnchantability, int modStr){
        if(ModLoadedUtil.isInfernalMobsLoaded()) {
            int enchLvl = itemEnchantability;
            if (ForgeConfigHandler.server.fixInfernalMobsEnchantability) {
                int prefix = modStr <= 5 ? 0 : (modStr <= 10 ? 1 : 2);
                int enchLvlMin = 0;
                int enchLvlMax = 0;

                switch (prefix) {
                    case 0:
                        enchLvlMin = ForgeConfigHandler.server.infernalMobsEnchantabilityElite[0];
                        enchLvlMax = ForgeConfigHandler.server.infernalMobsEnchantabilityElite[1];
                        break;
                    case 1:
                        enchLvlMin = ForgeConfigHandler.server.infernalMobsEnchantabilityUltra[0];
                        enchLvlMax = ForgeConfigHandler.server.infernalMobsEnchantabilityUltra[1];
                        break;
                    case 2:
                        enchLvlMin = ForgeConfigHandler.server.infernalMobsEnchantabilityInfernal[0];
                        enchLvlMax = ForgeConfigHandler.server.infernalMobsEnchantabilityInfernal[1];
                        break;
                }
                if (enchLvlMax >= enchLvlMin)
                    enchLvl = enchLvlMin + (int) floor(rand.nextFloat() * (enchLvlMax - enchLvlMin + 1));
            }
            enchantRandomly(rand, itemStack, enchLvl, modStr);
        }
    }
}
