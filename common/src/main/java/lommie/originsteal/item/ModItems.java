package lommie.originsteal.item;

import lommie.originsteal.platform.Services;
import net.minecraft.world.item.Item;

public class ModItems {
    public static Item register(Item item, String id){
        return Services.PLATFORM.registerItem(item, id);
    }
}
