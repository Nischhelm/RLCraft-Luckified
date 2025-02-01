package luckified.mixin.vanilla;

import com.llamalad7.mixinextras.sugar.Local;
import luckified.ModConfig;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.functions.EnchantWithLevels;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Random;

@Mixin(EnchantWithLevels.class)
public class EnchantWithLevelsMixin {
    @Final @Shadow private RandomValueRange randomLevel;

    @Redirect(
            method = "apply",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;addRandomEnchantment(Ljava/util/Random;Lnet/minecraft/item/ItemStack;IZ)Lnet/minecraft/item/ItemStack;")
    )
    private ItemStack increaseLootEnchantLevels(Random rand, ItemStack stack, int rolledLvl, boolean isTreasure, @Local(argsOnly = true) LootContext context) {
        double playerLuck = context.getLuck();

        //Don't alter the level if EnchantWithLevels has a fixed level it rolls enchants with
        if(this.randomLevel.getMin() == this.randomLevel.getMax())
            return EnchantmentHelper.addRandomEnchantment(rand, stack, rolledLvl, isTreasure);

        //Loot function rolled the max possible level naturally
        boolean natMaxRoll = rolledLvl == (int) this.randomLevel.getMax();

        //Max roll Loot (yellow/gold)
        if (rand.nextFloat() < getChance(false, playerLuck) || natMaxRoll) {
            //Always roll max
            rolledLvl = (int) this.randomLevel.getMax();

            //Add extra lvls on top
            rolledLvl += ModConfig.vanilla.addedEnchantabilityForMaxRollLoot;

            //Add even more lvls by luck
            int addedLvlsByLuck = (int) (playerLuck * ModConfig.vanilla.luckEnchantabilityMaxLoot);
            rolledLvl += addedLvlsByLuck;

            if (ModConfig.vanilla.changeItemColors) {
                //Max roll loot with added lvls from luck is gold
                if (addedLvlsByLuck > 0) stack.setStackDisplayName(TextFormatting.GOLD + stack.getDisplayName());
                //Pregenerated max roll loot or max roll loot generated with no player luck is yellow
                else stack.setStackDisplayName(TextFormatting.YELLOW + stack.getDisplayName());
            }
        }
        //Lucky Loot (green)
        else {
            int addedLvlsByLuck = (int) (playerLuck * ModConfig.vanilla.luckEnchantabilityLoot);

            if (addedLvlsByLuck > 0 && rand.nextFloat() < getChance(true, playerLuck)) {
                rolledLvl += addedLvlsByLuck;

                if (ModConfig.vanilla.changeItemColors)
                    stack.setStackDisplayName(TextFormatting.GREEN + stack.getDisplayName());
            }
        }

        return EnchantmentHelper.addRandomEnchantment(rand, stack, rolledLvl, isTreasure);
    }

    @Unique
    private static float getChance(boolean isGreen, double playerLuck){
        float chance;
        if(isGreen) {
            chance = ModConfig.vanilla.luckyLootBaseChance;
            chance += (float) (ModConfig.vanilla.luckyLootPerLuck * playerLuck);
        }
        else {
            chance = ModConfig.vanilla.maxEnchantabilityLootBaseChance;
            chance += (float) (ModConfig.vanilla.maxEnchantabilityLootPerLuck * playerLuck);
        }
        return chance;
    }
}
