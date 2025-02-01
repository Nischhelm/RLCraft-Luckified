package luckified;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = Luckified.MODID)
public class ModConfig {
	@Config.Comment("Infernal Mobs Options")
	@Config.Name("Infernal Mobs")
	public static final InfernalMobsConfig infernalmobs = new InfernalMobsConfig();

	@Config.Comment("RLArtifacts Options")
	@Config.Name("RLArtifacts")
	public static final RLArtifactsConfig rlArtifacts = new RLArtifactsConfig();

	@Config.Comment("Bountiful Baubles Options")
	@Config.Name("Bountiful Baubles")
	public static final BountifulBaublesConfig bountifulBaubles = new BountifulBaublesConfig();

	@Config.Comment("Quality Tools Options")
	@Config.Name("Quality Tools")
	public static final QualityToolsConfig qualityTools = new QualityToolsConfig();

	@Config.Comment("Vanilla Options")
	@Config.Name("Minecraft")
	public static final VanillaConfig vanilla = new VanillaConfig();

	@Config.Comment("Defiled Lands Options")
	@Config.Name("Defiled Lands")
	public static DefiledLandsConfig defiledlands = new DefiledLandsConfig();

	public static class InfernalMobsConfig {
		@Config.Comment("Fix InfernalMobs mistakingly using material enchantability for the enchantability lvl")
		@Config.Name("InfernalMobs Fix Loot Enchantability")
		public boolean fixInfernalMobsEnchantability = true;

		@Config.Comment("Enchantability range of Items dropped by rare (blue) InfernalMobs")
		@Config.Name("InfernalMobs Rare Enchantability")
		public int[] infernalMobsEnchantabilityElite = {10,20};

		@Config.Comment("Enchantability range of Items dropped by ultra (yellow) InfernalMobs")
		@Config.Name("InfernalMobs Ultra Enchantability")
		public int[] infernalMobsEnchantabilityUltra = {20,30};

		@Config.Comment("Enchantability range of Items dropped by infernal (gold) InfernalMobs")
		@Config.Name("InfernalMobs Infernal Enchantability")
		public int[] infernalMobsEnchantabilityInfernal = {30,50};
	}

	public static class RLArtifactsConfig {
		@Config.Comment("Increase mimic spawn chance by this much per luck attribute (default: 0.001=0.1%). Disable with 0.0")
		@Config.Name("RLArtifacts: Luck increased mimic spawn chance")
		public float luckMimicChance = 0.001F;
	}

	public static class BountifulBaublesConfig {
		@Config.Comment("Increase weights for rare modifiers by this much per luck attribute (default: 0.2). Disable with 0.0")
		@Config.Name("BountifulBaubles: Luck increased weights for rare modifiers")
		public float rareModifierWeightPerLuck = 0.2F;

		@Config.Comment("Denote the modifier names which RLLuck will read as \"rare\" and increase with luck")
		@Config.Name("BountifulBaubles: Rare modifiers")
		@Config.RequiresMcRestart
		public String[] rareModifierList = {"armored","hearty","violent","menacing","quick"};
	}

	public static class QualityToolsConfig {
		@Config.Comment("Increase weights for rare qualities by this much per luck attribute (default: 1.0). Disable with 0.0")
		@Config.Name("QualityTools: Luck increased weights for rare Qualities")
		public float rareQualityWeightPerLuck = 1.0F;

		@Config.Comment("Denote the quality names which RLLuck will read as \"rare\" and increase with luck")
		@Config.Name("QualityTools: Rare qualities")
		@Config.RequiresMcRestart
		public String[] rareQualityList = {"quality.masterful.name","quality.legendary.name","quality.undying.name","quality.punishing.name","quality.arcane.name","quality.mystical.name"};
	}

	public static class VanillaConfig {
		@Config.Comment("How many enchanting levels added per luck attribute to enchanted player generated loot, generating a GREEN item (default: 1.0). Disable with 0.0")
		@Config.Name("Lucky Loot: Added enchantability per luck")
		public float luckEnchantabilityLoot = 1.0F;

		@Config.Comment("Base chance to roll lucky loot, generating a GREEN item (default: 0.5). Disable with 0.0")
		@Config.Name("Lucky Loot: Base chance")
		public float luckyLootBaseChance = 0.5F;

		@Config.Comment("Chance per luck to roll lucky loot, generating a GREEN item (default: 0.05). Disable with 0.0")
		@Config.Name("Lucky Loot: Chance per luck")
		public float luckyLootPerLuck = 0.05F;

		@Config.Comment("Change colors of items that have rolled max enchantability or have generated with luck")
		@Config.Name("Loot: Changed Item Colors")
		public boolean changeItemColors = true;

		@Config.Comment("If loot rolls the extra chance for max enchantability (YELLOW or GOLD), it will get this many levels added flat (default: 10). Disable with 0")
		@Config.Name("Max roll Loot: Added Enchantability flat")
		public int addedEnchantabilityForMaxRollLoot = 10;

		@Config.Comment("If loot rolls the extra chance for max enchantability (YELLOW or GOLD), it will get this many levels added per luck (default: 2.0). Disable with 0.0")
		@Config.Name("Max roll Loot: Added enchantability per luck")
		public float luckEnchantabilityMaxLoot = 2.0F;

		@Config.Comment("Base chance to roll max possible level for loot enchantability, generating a YELLOW or GOLD item (default: 0.05). Disable with 0.0")
		@Config.Name("Max roll Loot: Base chance")
		public float maxEnchantabilityLootBaseChance = 0.05F;

		@Config.Comment("Chance per Luck to roll max possible level for loot enchantability, generating a GOLD item (default: 0.02). Disable with 0.0")
		@Config.Name("Max roll Loot: Chance per luck")
		public float maxEnchantabilityLootPerLuck = 0.02F;

		@Config.Comment("Increase weights for higher lvls of enchants on trades with librarians by this factor per luck (default: 0.1). Disable with 0.0")
		@Config.Name("Librarian: Weight factor for higher enchant levels per luck")
		public float librarianEnchLevelWeightFactor = 0.1F;
	}

	public static class DefiledLandsConfig {
		@Config.Comment("Each point of luck will multiplicatively increase default chance to breed golden bookwyrm by this factor in percent. So with 5 Luck and Factor of 0.2, all default chances are increased by +100%, so multiplied by 2. Default: 0.2. Disable with 0.0")
		@Config.Name("DefiledLands: Increased Gold Bookwyrm chance per Luck")
		public float goldWyrmPerLuck = 0.2F;
	}

	@Mod.EventBusSubscriber(modid = Luckified.MODID)
	private static class EventHandler{

		@SubscribeEvent
		public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
			if(event.getModID().equals(Luckified.MODID)) {
				ConfigManager.sync(Luckified.MODID, Config.Type.INSTANCE);
			}
		}
	}
}