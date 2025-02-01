package luckified.util;

import net.minecraft.inventory.Container;
import net.minecraftforge.fml.common.Loader;

public abstract class ModLoadedUtil {
	private static Boolean qualityToolsLoaded = null;
	private static Boolean bountifulBaublesLoaded = null;

	public static boolean isQualityToolsLoaded() {
		if(qualityToolsLoaded == null) qualityToolsLoaded = Loader.isModLoaded("qualitytools");
		return qualityToolsLoaded;
	}
	
	public static boolean isBountifulBaublesLoaded() {
		if(bountifulBaublesLoaded == null) bountifulBaublesLoaded = Loader.isModLoaded("bountifulbaubles");
		return bountifulBaublesLoaded;
	}

	public static boolean containerIsQTReforger(Container container) {
		return isQualityToolsLoaded() && QTCompatUtil.containerIsQTReforger(container);
	}

	public static boolean containerIsBBReforger(Container container) {
		return isBountifulBaublesLoaded() && BBCompatUtil.containerIsBBReforger(container);
	}
}