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

	@Config.Comment("Client-Side Options")
	@Config.Name("Client Options")
	public static final ClientConfig client = new ClientConfig();

	public static class ServerConfig {

		@Config.Comment("How many enchanting levels added per luck attribute to enchanted player generated loot, generating a GREEN item (default: 1.0). Disable with 0.0")
		@Config.Name("Luck added enchantability on loot")
		public double luckEnchantabilityLoot = 1.0;

		@Config.Comment("Base chance to roll lucky loot, generating a GREEN item (default: 0.5). Disable with 0.0")
		@Config.Name("Chance to roll lucky loot - base chance")
		public double luckyLootBaseChance = 0.5;

		@Config.Comment("Chance per luck to roll lucky loot, generating a GREEN item (default: 0.05). Disable with 0.0")
		@Config.Name("Chance to roll lucky loot - per luck")
		public double luckyLootPerLuck = 0.05;

		@Config.Comment("Change colors of items that have rolled max enchantability or have generated with luck")
		@Config.Name("Changed Item Colors")
		public boolean changeItemColors = true;

		@Config.Comment("If pregenerated loot rolls the extra chance for max enchantability (YELLOW or GOLD), it will get this many levels added (default: 10.0). Disable with 0.0")
		@Config.Name("Added Enchantability on max roll loot")
		public double addedEnchantabilityForMaxRollLoot = 10.0;

		@Config.Comment("Base chance to roll max possible level for loot enchantability, generating a YELLOW or GOLD item (default: 0.05). Disable with 0.0")
		@Config.Name("Luck forces max level loot - base chance")
		public double maxEnchantabilityLootBaseChance = 0.05;

		@Config.Comment("Chance per Luck to roll max possible level for loot enchantability, generating a GOLD item (default: 0.02). Disable with 0.0")
		@Config.Name("Luck forces max level loot - per luck")
		public double maxEnchantabilityLootPerLuck = 0.02;

		@Config.Comment("How many enchanting levels added per luck attribute in enchanting table (default: 1.0). Disable with 0.0")
		@Config.Name("Luck added enchantability on Table")
		public double luckEnchantabilityTable = 1.0;

		@Config.Comment("How much weight to give top enchant to show as clue in enchanting table per luck attribute (default: 0.5). Disable with 0.0")
		@Config.Name("Luck added top enchant weight")
		public double luckTopEnchantWeight = 0.5;

		@Config.Comment("Increase mimic spawn chance by this much per luck attribute (default: 0.001=0.1%). Disable with 0.0")
		@Config.Name("Luck increased mimic spawn chance")
		public double luckMimicChance = 0.001;
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