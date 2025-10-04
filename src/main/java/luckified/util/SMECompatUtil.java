package luckified.util;

import com.shultrea.rin.attributes.EnchantAttribute;
import luckified.ModConfig;
import net.minecraft.init.MobEffects;

import java.util.UUID;

public class SMECompatUtil {
    public static void registerEnchantFocusModifier(){
        MobEffects.LUCK.registerPotionAttributeModifier(
                EnchantAttribute.ENCHANTFOCUS,
                UUID.nameUUIDFromBytes("luckifiedenchantfocusforluckeffect".getBytes()).toString(),
                ModConfig.somanyenchantments.luckEnchantingPowerModifier,
                ModConfig.somanyenchantments.luckEnchantingPowerOperation
        );
    }
}
