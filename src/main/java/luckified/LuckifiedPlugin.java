package luckified;

import java.util.Map;
import fermiumbooter.FermiumRegistryAPI;
import luckified.util.ModLoadedUtil;
import net.minecraftforge.fml.common.Loader;
import org.spongepowered.asm.launch.MixinBootstrap;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.MCVersion("1.12.2")
public class LuckifiedPlugin implements IFMLLoadingPlugin {

	public LuckifiedPlugin() {
		MixinBootstrap.init();

		FermiumRegistryAPI.enqueueMixin(false, "mixins.luckified.vanilla.json");
		FermiumRegistryAPI.enqueueMixin(true, "mixins.luckified.infernalmobs.json", () -> Loader.isModLoaded("infernalmobs"));
		FermiumRegistryAPI.enqueueMixin(true, "mixins.luckified.bountifulbaubles.json", () -> Loader.isModLoaded("bountifulbaubles"));
		FermiumRegistryAPI.enqueueMixin(true, "mixins.luckified.qualitytools.json", () -> Loader.isModLoaded("qualitytools"));
		FermiumRegistryAPI.enqueueMixin(true, "mixins.luckified.rlartifacts.json", () -> Loader.isModLoaded("artifacts"));
		FermiumRegistryAPI.enqueueMixin(true, "mixins.luckified.defiledlands.json", () -> Loader.isModLoaded("defiledlands"));
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
	public void injectData(Map<String, Object> data) { }
	
	@Override
	public String getAccessTransformerClass()
	{
		return null;
	}
}