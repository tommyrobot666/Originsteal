package lommie.originsteal.item;

import io.github.apace100.origins.origin.Origin;
import io.github.apace100.origins.origin.OriginLayer;
import io.github.apace100.origins.origin.OriginLayerManager;
import io.github.apace100.origins.origin.OriginManager;
import lommie.originsteal.Constants;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.lang.reflect.Method;
import java.util.Collection;

public class ModTabs {
    public static final CreativeModeTab ALL_ORBS = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID,"all_orbs"),
            CreativeModeTab.builder(CreativeModeTab.Row.TOP,8)
            .icon(() -> ModItems.ORIGIN_ORB.get().getDefaultInstance())
            .title(Component.empty())
            .displayItems((parameters, output) -> {
                Collection<OriginLayer> allLayers;
                try {
                    allLayers = (Collection<OriginLayer>) OriginLayerManager.class.getMethod("values").invoke(null);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                Collection<Origin> allOrigins;
                try {
                    allOrigins = (Collection<Origin>) OriginManager.class.getMethod("values").invoke(null);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                for (OriginLayer originLayer : allLayers) {
                    for (Origin origin : allOrigins) {
                        try {
                            Method contains = OriginLayer.class.getMethod("contains", Origin.class);
                            if ((boolean) contains.invoke(originLayer,origin)) {
                                ItemStack stack = ModItems.ORIGIN_ORB.get().getDefaultInstance();
                                stack.set(ModComponents.ORIGIN_AND_LAYER.get(),
                                        new OriginOrbItem.OriginAndLayerPair(originLayer, origin));
                                output.accept(stack);
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            })
            .build());

    public static void register(){}
}
