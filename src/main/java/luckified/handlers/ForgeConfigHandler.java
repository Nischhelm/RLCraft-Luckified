package luckified.handlers;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import luckified.RLCraftLuckified;

@Config(modid = RLCraftLuckified.MODID)
public class ForgeConfigHandler {
	
	@Config.Comment("Server-Side Options")
	@Config.Name("Server Options")
	public static final ServerConfig server = new ServerConfig();

	/*@Config.Comment("Client-Side Options")
	@Config.Name("Client Options")
	public static final ClientConfig client = new ClientConfig();*/

	public static class ServerConfig {

		@Config.Comment("How many enchanting levels added per luck attribute to enchanted player generated loot, generating a GREEN item (default: 1.0). Disable with 0.0")
		@Config.Name("Lucky Loot: Added enchantability per luck")
		public double luckEnchantabilityLoot = 1.0;

		@Config.Comment("Base chance to roll lucky loot, generating a GREEN item (default: 0.5). Disable with 0.0")
		@Config.Name("Lucky Loot: Base chance")
		public double luckyLootBaseChance = 0.5;

		@Config.Comment("Chance per luck to roll lucky loot, generating a GREEN item (default: 0.05). Disable with 0.0")
		@Config.Name("Lucky Loot: Chance per luck")
		public double luckyLootPerLuck = 0.05;

		@Config.Comment("Change colors of items that have rolled max enchantability or have generated with luck")
		@Config.Name("Loot: Changed Item Colors")
		public boolean changeItemColors = true;

		@Config.Comment("If loot rolls the extra chance for max enchantability (YELLOW or GOLD), it will get this many levels added flat (default: 10.0). Disable with 0.0")
		@Config.Name("Max roll Loot: Added Enchantability flat")
		public double addedEnchantabilityForMaxRollLoot = 10.0;

		@Config.Comment("If loot rolls the extra chance for max enchantability (YELLOW or GOLD), it will get this many levels added per luck (default: 2.0). Disable with 0.0")
		@Config.Name("Max roll Loot: Added enchantability per luck")
		public double luckEnchantabilityMaxLoot = 2.0;

		@Config.Comment("Base chance to roll max possible level for loot enchantability, generating a YELLOW or GOLD item (default: 0.05). Disable with 0.0")
		@Config.Name("Max roll Loot: Base chance")
		public double maxEnchantabilityLootBaseChance = 0.05;

		@Config.Comment("Chance per Luck to roll max possible level for loot enchantability, generating a GOLD item (default: 0.02). Disable with 0.0")
		@Config.Name("Max roll Loot: Chance per luck")
		public double maxEnchantabilityLootPerLuck = 0.02;

		@Config.Comment("How many enchanting levels added per luck attribute in enchanting table (default: 1.0). Disable with 0.0")
		@Config.Name("Enchanting Table: Added Enchantability per Luck")
		public double luckEnchantabilityTable = 1.0;

		@Config.Comment("How much weight to give top enchant to show as clue in enchanting table per luck attribute (default: 0.5). Disable with 0.0")
		@Config.Name("Enchanting Table: Added weight for top enchant per luck")
		public double luckTopEnchantWeight = 0.5;

		@Config.Comment("Distribute enchant levels on Librarian book trade unlocks exponentially, with each level having a (1+factor x luck) higher probability than the one before (default: 0.1). Disable with 0.0")
		@Config.Name("Librarian: Weight factor for higher enchant levels per luck")
		public double librarianEnchLevelWeightFactor = 0.1;

		@Config.Comment("Increase mimic spawn chance by this much per luck attribute (default: 0.001=0.1%). Disable with 0.0")
		@Config.Name("RLArtifacts: Luck increased mimic spawn chance")
		public double luckMimicChance = 0.001;

		@Config.Comment("Increase weights for rare qualities by this much per luck attribute (default: 1.0). Disable with 0.0")
		@Config.Name("QualityTools: Luck increased weights for rare Qualities")
		public double rareQualityWeightPerLuck = 1.0;

		@Config.Comment("Denote the quality weight which RLLuck will read as \"rare\" (default: 5)")
		@Config.Name("QualityTools: Rare qualities")
		public String[] rareQualityList = {"quality.masterful.name","quality.legendary.name","quality.undying.name","quality.punishing.name","quality.arcane.name","quality.mystical.name"};

		@Config.Comment("Increase weights for rare modifiers by this much per luck attribute (default: 1.0). Disable with 0.0")
		@Config.Name("BountifulBaubles: Luck increased weights for rare modifiers")
		public double rareModifierWeightPerLuck = 0.2;

		@Config.Comment("Denote the modifier names which RLLuck will read as \"rare\"")
		@Config.Name("BountifulBaubles: Rare modifiers")
		public String[] rareModifierList = {"armored","hearty","violent","menacing","quick"};

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

	public static class ClientConfig {

		/*@Config.Comment("Example client side config option")
		@Config.Name("Example Client Option")
		public boolean exampleClientOption = true;*/
	}

	@Mod.EventBusSubscriber(modid = RLCraftLuckified.MODID)
	private static class EventHandler{

		@SubscribeEvent
		public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
			if(event.getModID().equals(RLCraftLuckified.MODID)) {
				ConfigManager.sync(RLCraftLuckified.MODID, Config.Type.INSTANCE);
			}
		}
	}
}