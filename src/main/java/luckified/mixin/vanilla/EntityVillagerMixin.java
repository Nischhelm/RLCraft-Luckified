package luckified.mixin.vanilla;

import net.minecraft.entity.passive.EntityVillager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.UUID;

@Mixin(EntityVillager.class)
public interface EntityVillagerMixin {

    @Accessor("lastBuyingPlayer")
    public UUID getLastBuyingPlayer();
}
