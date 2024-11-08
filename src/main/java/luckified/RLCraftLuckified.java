package luckified;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import luckified.handlers.ModRegistry;

@Mod(modid = RLCraftLuckified.MODID, version = RLCraftLuckified.VERSION, name = RLCraftLuckified.NAME, dependencies = "required-after:fermiumbooter", acceptableRemoteVersions = "*")
public class RLCraftLuckified {
    public static final String MODID = "luckified";
    public static final String VERSION = "1.0.5";
    public static final String NAME = "RLCraft Luckified";
    public static final Logger LOGGER = LogManager.getLogger();

	@Instance(MODID)
	public static RLCraftLuckified instance;
	
	@EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ModRegistry.init();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        ModRegistry.post_init();
    }

}