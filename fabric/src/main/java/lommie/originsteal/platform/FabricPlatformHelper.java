package lommie.originsteal.platform;

import lommie.originsteal.Constants;
import lommie.originsteal.platform.services.IPlatformHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public Supplier<Item> registerItem(Item item, String id) {
        ResourceLocation itemID = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, id);
        Registry.register(BuiltInRegistries.ITEM, itemID, item);
        return () -> item;
    }

    @Override
    public <T> Supplier<DataComponentType<T>> registerDataComponentType(DataComponentType<T> componentType, String id) {
        Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, id), componentType);
        return () -> componentType;
    }
}
