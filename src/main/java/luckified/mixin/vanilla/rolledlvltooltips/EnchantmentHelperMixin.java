package luckified.mixin.vanilla.rolledlvltooltips;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
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
    @WrapMethod(method = "buildEnchantmentList")
    private static List<EnchantmentData> luckified_trackOriginalLvl(
            Random rand, ItemStack stack, int lvl, boolean allowTreasure,
            Operation<List<EnchantmentData>> original
    ){
        if(stack.getTagCompound() == null) stack.setTagCompound(new NBTTagCompound());
        NBTTagCompound stackNBT = stack.getTagCompound();

        NBTTagCompound luckified = new NBTTagCompound();
        luckified.setInteger("enchLvl", lvl);
        stackNBT.setTag("luckified", luckified);

        List<EnchantmentData> addedEnchs = original.call(rand, stack, lvl, allowTreasure);

        //cleanup
        if(addedEnchs.isEmpty()) {
            stackNBT.removeTag("luckified");
            if(stackNBT.isEmpty()) stack.setTagCompound(null);
        }

        return addedEnchs;
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
