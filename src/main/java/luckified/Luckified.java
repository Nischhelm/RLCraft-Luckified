package luckified;

import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
        modid = Luckified.MODID,
        version = Luckified.VERSION,
        name = Luckified.NAME,
        dependencies = "required-after:fermiumbooter",
        acceptableRemoteVersions = "*"
)
public class Luckified {
    public static final String MODID = "luckified";
    public static final String VERSION = "1.1.1";
    public static final String NAME = "RLCraft Luckified";
    public static final Logger LOGGER = LogManager.getLogger();
}