package luckified.mixin.vanilla.rolledlvltooltips;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.sugar.Local;
import luckified.ModConfig;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow private NBTTagCompound stackTagCompound;

    @Definition(id = "hasKey", method = "Lnet/minecraft/nbt/NBTTagCompound;hasKey(Ljava/lang/String;I)Z")
    @Definition(id = "stackTagCompound", field = "Lnet/minecraft/item/ItemStack;stackTagCompound:Lnet/minecraft/nbt/NBTTagCompound;")
    @Expression("this.stackTagCompound.hasKey('display', 10)")
    @Inject(
            method = "getTooltip",
            at = @At(value = "MIXINEXTRAS:EXPRESSION")
    )
    private void luckified_displayRolledEnchLvl(
            EntityPlayer playerIn, ITooltipFlag advanced,
            CallbackInfoReturnable<List<String>> cir,
            @Local(ordinal = 0) List<String> tooltips
    ){
        if (!ModConfig.vanilla.enchLvlTooltip) return; // this is just for quick disable while playing

        if (this.stackTagCompound == null || !this.stackTagCompound.hasKey("luckified")) return;
        NBTTagCompound luckified = this.stackTagCompound.getCompoundTag("luckified");
        if(!luckified.hasKey("enchanted")) return; //this shouldn't happen anymore

        boolean wasEnchanted = luckified.getBoolean("enchanted");

        int originallvl = luckified.getInteger("enchLvl");
        int modifiedLvl = luckified.getInteger("modifiedLvl");
        int diff = modifiedLvl - originallvl;

        if(ModConfig.vanilla.simplifiedEnchLvlTooltip)
            tooltips.add(I18n.format(wasEnchanted ? "luckified.tooltip.enchantedLvl" : "luckified.tooltip.rolledlvl", modifiedLvl));
        else {
            String rolledLvlTooltip = I18n.format(wasEnchanted ? "luckified.tooltip.enchantedLvl" : "luckified.tooltip.rolledlvl", originallvl);
            if (diff > 0) rolledLvlTooltip += I18n.format("luckified.tooltip.rolledlvl.plus", diff);
            else if (diff < 0) rolledLvlTooltip += I18n.format("luckified.tooltip.rolledlvl.minus", -diff);
            tooltips.add(rolledLvlTooltip);
        }
    }
}
