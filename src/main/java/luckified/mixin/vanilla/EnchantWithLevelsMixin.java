package luckified.mixin.vanilla;

import luckified.handlers.ForgeConfigHandler;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.functions.EnchantWithLevels;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(EnchantWithLevels.class)
public class EnchantWithLevelsMixin {

    @Final
    @Shadow
    private RandomValueRange randomLevel;
    @Final
    @Shadow
    private boolean isTreasure;

    public double playerLuck = 0;

    @Inject(
            method = "apply",
            at = @At(value="HEAD")
    )
    private void saveLuckFromContextMixin(ItemStack stack, Random rand, LootContext context, CallbackInfoReturnable<ItemStack> cir){
        playerLuck = context.getLuck();
    }

    @Redirect(
            method = "apply",
            at = @At(value="INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;addRandomEnchantment(Ljava/util/Random;Lnet/minecraft/item/ItemStack;IZ)Lnet/minecraft/item/ItemStack;")
    )
    private ItemStack increaseLootEnchantLevelsMixin(Random rand, ItemStack stack, int level, boolean isTreasure){
        int addedLvl = (int) (playerLuck * ForgeConfigHandler.server.luckEnchantabilityLoot);

        int rolledLvl = this.randomLevel.generateInt(rand);
        boolean natMaxRoll = this.randomLevel.getMax()>this.randomLevel.getMin() && rolledLvl == (int) this.randomLevel.getMax();

        float yellowChance = (float) (ForgeConfigHandler.server.maxEnchantabilityLootBaseChance+ForgeConfigHandler.server.maxEnchantabilityLootPerLuck*playerLuck);
        if(rand.nextFloat() < yellowChance || natMaxRoll){
            int goldLvl = (int) (this.randomLevel.getMax()+addedLvl*2+ForgeConfigHandler.server.addedEnchantabilityForMaxRollLoot);

            if(ForgeConfigHandler.server.changeItemColors) {
                if (addedLvl > 0)
                    stack.setStackDisplayName(TextFormatting.GOLD + stack.getDisplayName());
                else  //Pregenerated loot or loot generated with no player luck
                    stack.setStackDisplayName(TextFormatting.YELLOW + stack.getDisplayName());
            }

            return EnchantmentHelper.addRandomEnchantment(rand, stack, goldLvl, this.isTreasure);

        } else {
            float greenChance = (float) (ForgeConfigHandler.server.luckyLootBaseChance+ForgeConfigHandler.server.luckyLootPerLuck*playerLuck);

            if(addedLvl > 0 && rand.nextFloat() < greenChance) {
                int greenLvl = rolledLvl+addedLvl;

                if(ForgeConfigHandler.server.changeItemColors)
                    stack.setStackDisplayName(TextFormatting.GREEN + stack.getDisplayName());

                return EnchantmentHelper.addRandomEnchantment(rand, stack, greenLvl, this.isTreasure);
            }

            else  //Pregenerated loot or loot generated with no player luck
                return EnchantmentHelper.addRandomEnchantment(rand, stack, rolledLvl, this.isTreasure);
        }

    }
}