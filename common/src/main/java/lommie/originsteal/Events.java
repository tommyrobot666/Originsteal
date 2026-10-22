package lommie.originsteal;

import io.github.apace100.origins.origin.Origin;
import io.github.apace100.origins.origin.OriginLayer;
import io.github.apace100.origins.origin.OriginLayerManager;
import io.github.apace100.origins.origin.OriginManager;
import io.github.apace100.origins.registry.ModComponents;
import lommie.originsteal.item.ModItems;
import lommie.originsteal.item.OriginOrbItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;

import java.lang.reflect.Method;
import java.util.Collection;

public class Events {
    public static void onPlayerDeath(ServerPlayer oldPlayer, ServerPlayer newPlayer){
        Collection<OriginLayer> allLayers;
        try {
            allLayers = (Collection<OriginLayer>) OriginLayerManager.class.getMethod("values").invoke(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            Method getId = Origin.class.getMethod("getId");
            Method getLayerId = OriginLayer.class.getMethod("getId");
            Method get = OriginManager.class.getMethod("get", ResourceLocation.class);
            for (OriginLayer layer : allLayers) {
                Origin origin = ModComponents.ORIGIN.get(oldPlayer).getOrigin(layer);
                if (!(getId.invoke(origin).equals(ResourceLocation.fromNamespaceAndPath("origins","human"))
                || origin.equals(Origin.EMPTY))){
                    oldPlayer.level().addFreshEntity(new ItemEntity(oldPlayer.level(), oldPlayer.getX(), oldPlayer.getY(), oldPlayer.getZ(),
                            OriginOrbItem.withNewOriginAndLayer(ModItems.ORIGIN_ORB.get().getDefaultInstance(),new OriginOrbItem.OriginAndLayerPair(layer,origin))));
                    if (getLayerId.invoke(layer).equals(ResourceLocation.fromNamespaceAndPath("origins","origin"))){
                        ModComponents.ORIGIN.get(newPlayer).setOrigin(layer, (Origin) get.invoke(null,ResourceLocation.fromNamespaceAndPath("origins","human")));
                    } else {
                        ModComponents.ORIGIN.get(newPlayer).setOrigin(layer, Origin.EMPTY);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
