package luckified.mixin.vanilla;

import com.tmtravlr.qualitytools.reforging.ContainerReforgingStation;
import cursedflames.bountifulbaubles.block.ContainerReforger;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagDouble;
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
    public void addLuckMixin(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player, CallbackInfoReturnable<ItemStack> cir) {
        Container container = ((Container)(Object)this);
        if(container instanceof ContainerReforgingStation || container instanceof ContainerReforger) {
            Slot slot = container.getSlot(0);

            if(!slot.getStack().isEmpty()){
                ItemStack stack = slot.getStack();
                double playerLuck = 0.0;
                if (player != null) playerLuck = player.getLuck();

                stack.setTagInfo("reforgingLuck", new NBTTagDouble(playerLuck));
            }
        }
    }
}
