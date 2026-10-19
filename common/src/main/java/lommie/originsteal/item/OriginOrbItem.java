package lommie.originsteal.item;

import io.github.apace100.origins.origin.Origin;
import io.github.apace100.origins.origin.OriginLayer;
import lommie.originsteal.Constants;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class OriginOrbItem extends Item {
    public OriginOrbItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand usedHand) {
        ItemStack itemstack = player.getItemInHand(usedHand);
        OriginAndLayerPair originAndLayer = getOriginAndLayer(itemstack);
        if (playerCanUseOrb(player,itemstack)){
            Origin original = io.github.apace100.origins.registry.ModComponents.ORIGIN.get(player).getOrigin(originAndLayer.layer);
            io.github.apace100.origins.registry.ModComponents.ORIGIN.get(player).setOrigin(originAndLayer.layer,originAndLayer.origin);
            player.sendSystemMessage(Component.translatable(Constants.MOD_ID+".origin_orb.changed_origin"));

            return InteractionResultHolder.success(withNewOriginAndLayer(itemstack, new OriginAndLayerPair(originAndLayer.layer,original)));
        }

        return super.use(level, player, usedHand);
    }

    private static ItemStack withNewOriginAndLayer(ItemStack itemstack, OriginAndLayerPair originAndLayerPair) {
        itemstack.set(ModComponents.ORIGIN_AND_LAYER.get(),originAndLayerPair);
        return itemstack;
    }

    private static OriginAndLayerPair getOriginAndLayer(ItemStack itemstack) {
        return itemstack.get(ModComponents.ORIGIN_AND_LAYER.get());
    }

    private static boolean playerCanUseOrb(Player player, ItemStack itemstack) {
        OriginAndLayerPair originAndLayer = getOriginAndLayer(itemstack);
        return !io.github.apace100.origins.registry.ModComponents.ORIGIN.get(player).isSelectingOrigin() && io.github.apace100.origins.registry.ModComponents.ORIGIN.get(player).getOrigin(originAndLayer.layer)!=originAndLayer.origin;
    }

    public record OriginAndLayerPair(OriginLayer layer, Origin origin){}
}
