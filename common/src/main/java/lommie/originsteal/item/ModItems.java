package lommie.originsteal.item;

import lommie.originsteal.platform.Services;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

import java.util.function.Supplier;

public class ModItems {
    public static Supplier<Item> register(Item item, String id){
        return Services.PLATFORM.registerItem(item, id);
    }

    public static final Supplier<Item> ORIGIN_ORB =
            register(new OriginOrbItem(new Item.Properties().stacksTo(1)
                            .fireResistant().rarity(Rarity.EPIC)),"origin_orb");

    public static void register(){}
}
