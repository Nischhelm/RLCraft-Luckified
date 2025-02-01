package luckified.util;

import com.tmtravlr.qualitytools.reforging.ContainerReforgingStation;
import net.minecraft.inventory.Container;

public class QTCompatUtil {
    public static boolean containerIsQTReforger(Container container){
        return container instanceof ContainerReforgingStation;
    }
}
