package lommie.originsteal.item;

import lommie.originsteal.platform.Services;
import net.minecraft.core.component.DataComponentType;

import java.util.function.Supplier;

public class ModComponents {
    public static <T> Supplier<DataComponentType<T>> register(DataComponentType<T> componentType, String id){
        return Services.PLATFORM.registerDataComponentType(componentType, id);
    }

    public static final Supplier<DataComponentType<OriginOrbItem.OriginAndLayerPair>> ORIGIN_AND_LAYER =
            register(DataComponentType.<OriginOrbItem.OriginAndLayerPair>builder().persistent(OriginOrbItem.OriginAndLayerPair.CODEC).build(),"origin_and_layer");

    public static void register(){}
}
