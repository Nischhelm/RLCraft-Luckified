package luckified.mixin.vanilla;

import luckified.util.ModLoadedUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagFloat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Container.class)
public abstract class ContainerMixin {

    @Inject(
            method = "slotClick",
            at = @At("RETURN")
    )
    public void luckified_addLuckToReforgers(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player, CallbackInfoReturnable<ItemStack> cir) {
        Container container = ((Container) (Object) this);
        if (ModLoadedUtil.containerIsQTReforger(container) || ModLoadedUtil.containerIsBBReforger(container)) {
            Slot slot = container.getSlot(0);

            if (slot.getHasStack()) {
                ItemStack stack = slot.getStack();
                float playerLuck = 0F;
                if (player != null) playerLuck = player.getLuck();

                stack.setTagInfo("reforgingLuck", new NBTTagFloat(Math.max(0F,playerLuck)));
            }
        }
    }
}
