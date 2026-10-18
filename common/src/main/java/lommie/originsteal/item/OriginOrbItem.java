package lommie.originsteal.item;

import io.github.apace100.origins.origin.Origin;
import io.github.apace100.origins.origin.OriginLayer;
import io.github.apace100.origins.registry.ModComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class OriginOrbItem extends Item {
    public OriginOrbItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemstack = player.getItemInHand(usedHand);
        OriginAndLayerPair originAndLayer = getOriginAndLayer(itemstack);
        if (playerCanUseOrb(player,itemstack)){
            Origin original = ModComponents.ORIGIN.get(player).getOrigin(originAndLayer.layer);
            ModComponents.ORIGIN.get(player).setOrigin(originAndLayer.layer,originAndLayer.origin);
            return InteractionResultHolder.success(withNewOriginAndLayer(itemstack, new OriginAndLayerPair(originAndLayer.layer,original)));
        }

        return super.use(level, player, usedHand);
    }

    private ItemStack withNewOriginAndLayer(ItemStack itemstack, OriginAndLayerPair originAndLayerPair) {
        return null;
    }

    private OriginAndLayerPair getOriginAndLayer(ItemStack itemstack) {
        return new OriginAndLayerPair(null,null);
    }

    private static boolean playerCanUseOrb(Player player, ItemStack itemstack) {
        return true;
    }

    public record OriginAndLayerPair(OriginLayer layer, Origin origin){}
}
