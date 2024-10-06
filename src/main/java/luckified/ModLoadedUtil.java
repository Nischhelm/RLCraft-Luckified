package luckified;

import net.minecraftforge.fml.common.Loader;

public abstract class ModLoadedUtil {
	
	private static Boolean qualityToolsLoaded = null;
	private static Boolean bountifulBaublesLoaded = null;
	private static Boolean rlArtifactsLoaded = null;
	private static Boolean infernalmobsLoaded = null;

	public static boolean isQualityToolsLoaded() {
		if(qualityToolsLoaded == null) qualityToolsLoaded = Loader.isModLoaded("qualitytools");
		return qualityToolsLoaded;
	}
	
	public static boolean isBountifulBaublesLoaded() {
		if(bountifulBaublesLoaded == null) bountifulBaublesLoaded = Loader.isModLoaded("bountifulbaubles");
		return bountifulBaublesLoaded;
	}
	
	public static boolean isRLArtifactsLoaded() {
		if(rlArtifactsLoaded == null) rlArtifactsLoaded = Loader.isModLoaded("artifacts");
		return rlArtifactsLoaded;
	}

	public static boolean isInfernalMobsLoaded() {
		if(infernalmobsLoaded == null) infernalmobsLoaded = Loader.isModLoaded("infernalmobs");
		return infernalmobsLoaded;
	}

}