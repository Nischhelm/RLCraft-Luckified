package luckified.mixin.vanilla.rolledlvltooltips;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Random;

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {
    @Inject(method = "buildEnchantmentList", at = @At("HEAD"))
    private static void luckified_trackOriginalLvl(
            CallbackInfoReturnable<List<EnchantmentData>> cir,
            @Local(argsOnly = true) int lvl,
            @Share("originalLvl") LocalIntRef originalLvl
    ){
        originalLvl.set(lvl);
    }

    @ModifyArg(
            method = "buildEnchantmentList",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;getEnchantmentDatas(ILnet/minecraft/item/ItemStack;Z)Ljava/util/List;")
    )
    private static int luckified_trackUsedLvls(
            int modifiedLvl,
            @Local(argsOnly = true) ItemStack stack,
            @Share("originalLvl") LocalIntRef originalLvl
    ) {
        NBTTagCompound luckified = new NBTTagCompound();
        luckified.setInteger("enchLvl", originalLvl.get());
        luckified.setInteger("modifiedLvl", modifiedLvl);
        stack.setTagInfo("luckified", luckified);
        return modifiedLvl;
    }

    @Inject(method = "addRandomEnchantment", at = @At(value = "TAIL"))
    private static void luckified_trackEnchanted(
            Random random, ItemStack stack, int level, boolean allowTreasure,
            CallbackInfoReturnable<ItemStack> cir
    ){
        NBTTagCompound nbt = stack.getTagCompound();
        if(nbt == null || !nbt.hasKey("luckified")) return;
        nbt.getCompoundTag("luckified").setBoolean("enchanted", false);
    }
}
