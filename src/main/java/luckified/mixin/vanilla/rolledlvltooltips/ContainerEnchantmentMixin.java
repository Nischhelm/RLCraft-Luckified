package luckified.mixin.vanilla.rolledlvltooltips;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerEnchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ContainerEnchantment.class)
public class ContainerEnchantmentMixin {
    @Inject(
            method = "enchantItem",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/EntityPlayer;onEnchant(Lnet/minecraft/item/ItemStack;I)V")
    )
    private void luckified_trackEnchanted(
            EntityPlayer playerIn, int id,
            CallbackInfoReturnable<Boolean> cir,
            @Local(ordinal = 0) ItemStack stack
    ){
        NBTTagCompound nbt = stack.getTagCompound();
        if(nbt == null || !nbt.hasKey("luckified")) return;
        nbt.getCompoundTag("luckified").setBoolean("enchanted", true);
    }
}
