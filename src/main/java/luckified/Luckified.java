package luckified;

import luckified.util.SMECompatUtil;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.versioning.ArtifactVersion;
import net.minecraftforge.fml.common.versioning.InvalidVersionSpecificationException;
import net.minecraftforge.fml.common.versioning.VersionRange;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
        modid = Luckified.MODID,
        version = Luckified.VERSION,
        name = Luckified.NAME,
        dependencies = "required-after:fermiumbooter@[1.3.2,)",
        acceptableRemoteVersions = "*"
)
public class Luckified {
    public static final String MODID = "luckified";
    public static final String VERSION = "1.1.3.2";
    public static final String NAME = "RLCraft Luckified";
    public static final Logger LOGGER = LogManager.getLogger();

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){
        if(Loader.isModLoaded("somanyenchantments") && ModConfig.somanyenchantments.luckEnchantingPowerModifier > 0) {
            ArtifactVersion smeVersion = Loader.instance().getIndexedModList().get("somanyenchantments").getProcessedVersion();
            try {
                if (VersionRange.createFromVersionSpec("[1.0.4,)").containsVersion(smeVersion))
                    SMECompatUtil.registerEnchantFocusModifier();
            } catch (InvalidVersionSpecificationException ignored){}
        }
    }
}