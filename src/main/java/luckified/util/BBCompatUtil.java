package luckified.util;

import cursedflames.bountifulbaubles.block.ContainerReforger;
import net.minecraft.inventory.Container;

public class BBCompatUtil {

    public static boolean containerIsBBReforger(Container container){
        return container instanceof ContainerReforger;
    }
}
