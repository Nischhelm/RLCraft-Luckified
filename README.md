## RLLuck (RLCraft Luckified)

Adds more uses to Luck Attribute in RLCraft

The players Luck Attribute will
- Increase enchantability on enchanting table
- Increase chance to see top enchant as clue in enchanting table
- Increase enchantability of (not pregenerated) loot gear (green items)
- Increase chances to spawn a mimic

The mod also adds rare loot (yellow or gold if also using luck) which will always roll the highest possible enchantability for that loot table.

Fully configurable via config.

#### Planned Features:
- Increase weights on rare modifiers on Baubles (BountifulBaubles Reforging Station)
- Increase weights on rare qualities on Items (QualityTools Reforging Station)

#### Explanation of the Loot gear colors:

Many loot tables enchant items in a range of enchantabilities. An example is the vanilla End Treasure chest, which enchants diamond gear in a range of lvl 20-39 enchants (thats the levels you see on the enchanting table). 

When loot generates, it rolls a level in the set range with equal chances for each level. Minecraft adds some more modifiers on that rolled level which can also reduce it by up to 15%.

    Specifically, it adds

    1 + randInt(matEnch/4+1) + randInt(matEnch/4+1)
    which is a triangular distribution between 1 and 1+matEnch/2 with maximumm at 1+matEnch/4

    and then applies a global multiplier between -15% and +15% with a triangular distribution around +-0%.

<span style="color:green">Green</span> gear will not change anything about this calculation and only add the players luck attribute to the rolled level (times a configurable multiplier). 
This only works if a player opens a non-pregenerated chest that can generate enchanted loot with a range of enchantability levels (like End Treasure chests).

There is a separate (configurable) chance on any loot table with enchanted gear to roll a <span style="color:yellow">yellow</span> or <span style="color:gold">gold</span> item. This item will always have the maximum possible enchantability of that loot table and add a configurable additional levels (default: 10) on top. 

If it is a non-pregenerated chest, the players luck attribute will increase the chance to roll such an item and will add on top of the rolled lvl (x2), creating a <span style="color:gold">gold</span> item.

If the loot is pregenerated or if the chest is opened without luck, the item will be <span style="color:yellow">yellow</span> instead.

In short:

<span style="color:green">Green</span>: RolledLvl + Luck, Chance: 50%+5% x Luck, slightly better than normal

<span style="color:yellow">Yellow</span>: MaxLvl + 10, Chance: 5%, good loot

<span style="color:gold">Gold</span>: MaxLvl + 10 + 2x Luck, Chance: 5% + 2% x Luck, exceptional loot 

Check the pictures here in main folder for some distributions of the rolled lvls for the different gear tiers.