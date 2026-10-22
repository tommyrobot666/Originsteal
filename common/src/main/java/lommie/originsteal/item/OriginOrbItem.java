package lommie.originsteal.item;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import io.github.apace100.origins.origin.Origin;
import io.github.apace100.origins.origin.OriginLayer;
import io.github.apace100.origins.origin.OriginLayerManager;
import io.github.apace100.origins.origin.OriginManager;
import lommie.originsteal.Constants;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class OriginOrbItem extends Item {
    public OriginOrbItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand usedHand) {
        // values sent to client are null (too lazy to make codec), so if client tries to access them the game will crash
        if (level.isClientSide()) return InteractionResultHolder.pass(player.getItemInHand(usedHand));

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
        if (!itemstack.has(ModComponents.ORIGIN_AND_LAYER.get())){
            OriginLayer fallback = null;
            try {
                Method values = OriginLayerManager.class.getMethod("values");
                Collection<OriginLayer> values2 = (Collection<OriginLayer>) values.invoke(null);
                fallback = values2.stream().findAny().orElseThrow();
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            itemstack.set(ModComponents.ORIGIN_AND_LAYER.get(),new OriginAndLayerPair(Objects.requireNonNull(fallback),Origin.EMPTY));
                    //new OriginAndLayerPair(OriginLayerManager.values().stream().findAny().orElseThrow(),OriginManager.values().stream().findAny().orElseThrow()));
        }

        return itemstack.get(ModComponents.ORIGIN_AND_LAYER.get());
    }

    private static boolean playerCanUseOrb(Player player, ItemStack itemstack) {
        OriginAndLayerPair originAndLayer = getOriginAndLayer(itemstack);
        return !io.github.apace100.origins.registry.ModComponents.ORIGIN.get(player).isSelectingOrigin() && io.github.apace100.origins.registry.ModComponents.ORIGIN.get(player).getOrigin(originAndLayer.layer)!=originAndLayer.origin;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> toolTip, TooltipFlag tooltipFlag) {
        toolTip.add(Component.literal("i dont wana write the stream codec"));
        if (false) { // if (i wrote the codec) {
            OriginAndLayerPair originAndLayer = getOriginAndLayer(stack);

            Component layerName;
            try {
                Method getName = OriginLayer.class.getMethod("getName");
                layerName = (Component) getName.invoke(originAndLayer.layer);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            Component originName;
            try {
                Method getName = Origin.class.getMethod("getName");
                originName = (Component) getName.invoke(originAndLayer.origin);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            toolTip.add(Component.translatable(Constants.MOD_ID+".origin_layer").append(layerName));
            toolTip.add(Component.translatable(Constants.MOD_ID+".origin").append(originName));
        }
    }

    public record OriginAndLayerPair(OriginLayer layer, Origin origin){
        //RecordCodecBuilder.create(instance -> );
        public static final Codec<OriginAndLayerPair> CODEC = Codec.of(OriginAndLayerPair::encoder,OriginAndLayerPair::decoder);
        public static final StreamCodec<? super RegistryFriendlyByteBuf, OriginAndLayerPair> STREAM_CODEC = StreamCodec.of((b,p) -> {}, (b) -> new OriginAndLayerPair(null,null));

        private static <T> DataResult<Pair<OriginAndLayerPair,T>> decoder(DynamicOps<T> tDynamicOps, T t) {
            OriginLayer layer = null;
            try {
                Method get = OriginLayerManager.class.getMethod("get", ResourceLocation.class);
                layer = (OriginLayer) get.invoke(null,ResourceLocation.parse(tDynamicOps.getStringValue(tDynamicOps.get(t,"layer_id").getOrThrow()).getOrThrow()));
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            Origin origin = null;
            try {
                Method get = OriginManager.class.getMethod("get", ResourceLocation.class);
                origin = (Origin) get.invoke(null,ResourceLocation.parse(tDynamicOps.getStringValue(tDynamicOps.get(t,"origin_id").getOrThrow()).getOrThrow()));
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            return DataResult.success(Pair.of(new OriginAndLayerPair(layer,origin),t));
        }

        private <T> DataResult<T> encoder(DynamicOps<T> tDynamicOps, T t) {
            try {
                Method getId = OriginLayer.class.getMethod("getId");
                tDynamicOps.set(t,"layer_id",tDynamicOps.createString(((ResourceLocation) getId.invoke(layer)).toString()));
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            try {
                Method getId = Origin.class.getMethod("getId");
                tDynamicOps.set(t, "origin_id", tDynamicOps.createString(((ResourceLocation) getId.invoke(origin)).toString()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            return DataResult.success(t);
        }
    }
}
