package luckified.mixin.vanilla.rolledlvltooltips;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Random;

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {
    @Inject(method = "buildEnchantmentList", at = @At("HEAD"))
    private static void luckified_trackOriginalLvl(
            CallbackInfoReturnable<List<EnchantmentData>> cir,
            @Local(argsOnly = true) int lvl,
            @Local(argsOnly = true) ItemStack stack
    ){
        NBTTagCompound luckified = new NBTTagCompound();
        luckified.setInteger("enchLvl", lvl);
        stack.setTagInfo("luckified", luckified);
    }

    @Inject(method = "getEnchantmentDatas", at = @At(value = "HEAD"))
    private static void luckified_trackModifiedLvl(int modifiedLvl, ItemStack stack, boolean allowTreasure, CallbackInfoReturnable<List<EnchantmentData>> cir) {
        NBTTagCompound nbt = stack.getTagCompound();
        if(nbt == null || !nbt.hasKey("luckified")) return; //if some mod uses getEnchantmentDatas separately
        NBTTagCompound luckified = nbt.getCompoundTag("luckified");
        luckified.setInteger("modifiedLvl", modifiedLvl);
    }

    @Inject(method = "addRandomEnchantment", at = @At(value = "TAIL"))
    private static void luckified_trackNotEnchanted(
            Random random, ItemStack stack, int level, boolean allowTreasure,
            CallbackInfoReturnable<ItemStack> cir
    ){
        NBTTagCompound nbt = stack.getTagCompound();
        if(nbt == null || !nbt.hasKey("luckified")) return;
        nbt.getCompoundTag("luckified").setBoolean("enchanted", false); // not enchanted by enchanting table
    }
}
