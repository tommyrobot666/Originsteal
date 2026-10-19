package lommie.originsteal.platform;

import lommie.originsteal.platform.services.IPlatformHelper;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;

import java.util.function.Supplier;

public class ForgePlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {

        return "Forge";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return !FMLLoader.isProduction();
    }

    @Override
    public Supplier<Item> registerItem(Item item, String id) {
        throw new RuntimeException();
    }

    @Override
    public <T> Supplier<DataComponentType<T>> registerDataComponentType(DataComponentType<T> componentType, String id) {
        throw new RuntimeException();
    }
}