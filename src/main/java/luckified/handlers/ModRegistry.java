package luckified.handlers;


import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import luckified.RLCraftLuckified;

@Mod.EventBusSubscriber(modid = RLCraftLuckified.MODID)
public class ModRegistry {


        public static void init() {
                MinecraftForge.EVENT_BUS.register(new MimicLuckHandler());
        }
}