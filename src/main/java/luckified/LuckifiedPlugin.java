package luckified;

import fermiumbooter.FermiumRegistryAPI;
import luckified.util.EarlyConfigReader;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.CoreModManager;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.asm.mixin.MixinEnvironment;

import java.util.Map;

@IFMLLoadingPlugin.MCVersion("1.12.2")
public class LuckifiedPlugin implements IFMLLoadingPlugin {

	public LuckifiedPlugin() {
		FermiumRegistryAPI.enqueueMixin(false, "mixins.luckified.vanilla.json");
		FermiumRegistryAPI.enqueueMixin(false, "mixins.luckified.vanilla.librarians.json", () -> EarlyConfigReader.getDouble("Librarian: Weight factor for higher enchant levels per luck", ModConfig.vanilla.librarianEnchLevelWeightFactor) > 0);
		FermiumRegistryAPI.enqueueMixin(true, "mixins.luckified.bountifulbaubles.json", () -> Loader.isModLoaded("bountifulbaubles") && EarlyConfigReader.isArrayFilled("BountifulBaubles: Rare modifiers", true));
		FermiumRegistryAPI.enqueueMixin(true, "mixins.luckified.qualitytools.json", () -> Loader.isModLoaded("qualitytools") && EarlyConfigReader.isArrayFilled("QualityTools: Rare qualities", true));
		FermiumRegistryAPI.enqueueMixin(true, "mixins.luckified.rlartifacts.json", () -> Loader.isModLoaded("artifacts") && EarlyConfigReader.getDouble("RLArtifacts: Luck increased mimic spawn chance", ModConfig.rlArtifacts.luckMimicChance) > 0);
		FermiumRegistryAPI.enqueueMixin(true, "mixins.luckified.defiledlands.json", () -> Loader.isModLoaded("defiledlands") && EarlyConfigReader.getDouble("DefiledLands: Increased Gold Bookwyrm chance per Luck", ModConfig.defiledlands.goldWyrmPerLuck) > 0);
	}

	@Override
	public String[] getASMTransformerClass()
	{
		return new String[0];
	}
	
	@Override
	public String getModContainerClass()
	{
		return null;
	}
	
	@Override
	public String getSetupClass()
	{
		return null;
	}
	
	@Override
	public void injectData(Map<String, Object> data) {
		if (Boolean.FALSE.equals(data.get("runtimeDeobfuscationEnabled"))) {
			MixinEnvironment.getDefaultEnvironment().setObfuscationContext("searge");
			CoreModManager.getReparseableCoremods().removeIf(s -> StringUtils.containsIgnoreCase(s, "fermiumbooter"));
		}
	}
	
	@Override
	public String getAccessTransformerClass()
	{
		return null;
	}
}