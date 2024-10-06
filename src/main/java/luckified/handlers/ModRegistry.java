package luckified.handlers;


import artifacts.common.ModConfig;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import luckified.RLCraftLuckified;

@Mod.EventBusSubscriber(modid = RLCraftLuckified.MODID)
public class ModRegistry {

        public static MimicLuckHandler mimicHandler = null;

        public static void init() {
                mimicHandler = new MimicLuckHandler();
                MinecraftForge.EVENT_BUS.register(mimicHandler);
        }

        public static void post_init() {
                mimicHandler.mimicChanceWithoutLuck = ModConfig.general.unlootedChestMimicRatio;
        }
}