package luckified;

import luckified.util.SMECompatUtil;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Mod(
        modid = Luckified.MODID,
        version = Luckified.VERSION,
        name = Luckified.NAME,
        dependencies = "required-after:fermiumbooter@[1.3.2,)",
        acceptableRemoteVersions = "*"
)
public class Luckified {
    public static final String MODID = "luckified";
    public static final String VERSION = "1.1.1";
    public static final String NAME = "RLCraft Luckified";
    public static final Logger LOGGER = LogManager.getLogger();

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){
        if(Loader.isModLoaded("somanyenchantments") && ModConfig.somanyenchantments.luckEnchantingPowerModifier > 0 && modIsAboveOrEqualVersion("somanyenchantments", "1.0.0"))
            SMECompatUtil.registerEnchantFocusModifier();
    }

    //only works for mod versions of the pattern x.y.z.w.a.b.c
    private static boolean modIsAboveOrEqualVersion(String modid, String compareVersion){
        ModContainer modContainer = Loader.instance().getIndexedModList().get(modid);
        if(modContainer == null){
            Luckified.LOGGER.warn("Luckified: Mod versioning can't be compared for mod {}, mod isn't loaded", modid);
            return false;
        }
        String modVersion = modContainer.getVersion();
        if(modVersion.equals(compareVersion)) return true;
        if(!modVersion.matches("\\d+(\\.\\d+)*")){
            Luckified.LOGGER.warn("Luckified: Mod versioning can't be compared for mod {}, version {}", modid, modVersion);
            return false;
        }
        List<Integer> splitActual = Arrays.stream(modVersion.split("\\.")).map(Integer::parseInt).collect(Collectors.toList());
        List<Integer> splitCompare = Arrays.stream(compareVersion.split("\\.")).map(Integer::parseInt).collect(Collectors.toList());
        for(int i = 0; i < splitActual.size(); i++) {
            if(i >= splitCompare.size()) return true; //assumes 1.0.1 > 1.0

            int actualVersion = splitActual.get(i);
            int compVersion = splitCompare.get(i);

            if(actualVersion > compVersion) return true;
            if(actualVersion < compVersion) return false;
            //if(actualVersion == compVersion) continue;
        }
        return splitActual.size() >= splitCompare.size(); //assumes 1.0 < 1.0.1
    }
}