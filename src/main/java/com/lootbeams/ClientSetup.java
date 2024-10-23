package com.lootbeams;

//import com.lootbeams.compat.ApotheosisCompat;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Overlay;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.sounds.WeighedSoundEvents;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.RenderGuiOverlayEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.event.RenderNameTagEvent;
import net.neoforged.neoforge.client.gui.overlay.VanillaGuiOverlay;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.TickEvent;
import net.neoforged.neoforge.event.TickEvent.ClientTickEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraft.core.registries.BuiltInRegistries;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Mod.EventBusSubscriber(modid = LootBeams.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {
	@SubscribeEvent
	public static void init(FMLClientSetupEvent ignored) {
	 	ignored.enqueueWork(() -> {
	 		NeoForge.EVENT_BUS.addListener(ClientSetup::onItemCreation);
	 		NeoForge.EVENT_BUS.addListener(ClientSetup::entityRemoval);
			NeoForge.EVENT_BUS.addListener(ClientSetup::onRenderNameplate);
	 		NeoForge.EVENT_BUS.addListener(ClientSetup::onLevelRender);
	 	});
	}

	public static HitResult getEntityItem(Player player) {
		Minecraft mc = Minecraft.getInstance();
		double distance = player.getBlockReach();
		float partialTicks = mc.getDeltaFrameTime();
		Vec3 position = player.getEyePosition(partialTicks);
		Vec3 view = player.getViewVector(partialTicks);
		if (mc.hitResult != null && mc.hitResult.getType() != HitResult.Type.MISS)
			distance = mc.hitResult.getLocation().distanceTo(position);
		return getEntityItem(player, position, position.add(view.x * distance, view.y * distance, view.z * distance));

	}
	public static HitResult getEntityItem(Player player, Vec3 position, Vec3 look) {
		Vec3 include = look.subtract(position);
		List list = player.level().getEntities(player, player.getBoundingBox().expandTowards(include.x, include.y, include.z));
		for (int i = 0; i < list.size(); ++i) {
			Entity entity = (Entity) list.get(i);
			if (entity instanceof ItemEntity) {
				AABB axisalignedbb = entity.getBoundingBox().inflate(0.5).inflate(0.0,0.5,0.0);
				Optional<Vec3> vec = axisalignedbb.clip(position, look);
				if (vec.isPresent())
					return new EntityHitResult(entity, vec.get());
				else if (axisalignedbb.contains(position))
					return new EntityHitResult(entity);
			}
		}
		return null;
	}
	//@SubscribeEvent
	public static void onItemCreation(EntityJoinLevelEvent event){
		if (event.getEntity() instanceof ItemEntity ie) {
			LootBeamRenderer.TOOLTIP_CACHE.computeIfAbsent(ie, itemEntity -> itemEntity.getItem().getTooltipLines(null, TooltipFlag.Default.NORMAL));
			if (!LootBeamRenderer.LIGHT_CACHE.contains(ie)) {
				LootBeamRenderer.LIGHT_CACHE.add(ie);
			}
		}
	}
	public static final List<Consumer<PoseStack>> delayedRenders = new ArrayList<>();
	//@SubscribeEvent
	public static void onLevelRender(RenderLevelStageEvent event) {
		if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
			PoseStack stack = event.getPoseStack();
			stack.pushPose();
			Vec3 pos = event.getCamera().getPosition();
			stack.translate(-pos.x, -pos.y, -pos.z);
			delayedRenders.forEach(consumer -> consumer.accept(stack));
			stack.popPose();
		}
		if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_WEATHER) {
			delayedRenders.clear();
		}
	}
	//@SubscribeEvent
	public static void entityRemoval(EntityLeaveLevelEvent event) {
		if (event.getEntity() instanceof ItemEntity ie) {
			LootBeamRenderer.TOOLTIP_CACHE.remove(ie);
			LootBeamRenderer.LIGHT_CACHE.remove(ie);
		}
	}

	public static int overrideLight(ItemEntity ie, int light) {
		if (Configuration.ALL_ITEMS.get()
				|| (Configuration.ONLY_EQUIPMENT.get() && isEquipmentItem(ie.getItem().getItem()))
				|| (Configuration.ONLY_RARE.get() && LootBeamRenderer.compatRarityCheck(ie, false))
				|| (isItemInRegistryList(Configuration.WHITELIST.get(), ie.getItem().getItem()))) {
			light = 15728640;
		}

		return light;
	}

	//@SubscribeEvent
	public static void onRenderNameplate(RenderNameTagEvent event) {
		if (!(event.getEntity() instanceof ItemEntity itemEntity)
				|| Minecraft.getInstance().player.distanceToSqr(itemEntity) > Math.pow(Configuration.RENDER_DISTANCE.get(), 2)) {
			return;
		}

		Item item = itemEntity.getItem().getItem();
		boolean shouldRender = (Configuration.ALL_ITEMS.get()
				|| (Configuration.ONLY_EQUIPMENT.get() && isEquipmentItem(item))
				|| (Configuration.ONLY_RARE.get() && LootBeamRenderer.compatRarityCheck(itemEntity, false))
				|| (isItemInRegistryList(Configuration.WHITELIST.get(), itemEntity.getItem().getItem())))
				&& !isItemInRegistryList(Configuration.BLACKLIST.get(), itemEntity.getItem().getItem());

		if (shouldRender && (!Configuration.REQUIRE_ON_GROUND.get() || itemEntity.onGround())) {
			delayedRenders.add(stack -> {
				stack.pushPose();
				stack.translate(itemEntity.getX(), itemEntity.getY(), itemEntity.getZ());
				LootBeamRenderer.renderLootBeam(stack, event.getMultiBufferSource(), event.getPartialTick(), itemEntity.level().getGameTime(), itemEntity);
				stack.popPose();
			});
		}
	}

	public static boolean isEquipmentItem(Item item) {
		return item instanceof TieredItem || item instanceof ArmorItem || item instanceof ShieldItem || item instanceof BowItem || item instanceof CrossbowItem;
	}

	private static boolean isItemInRegistryList(List<String> registryNames, Item item) {
		if (registryNames.isEmpty()) {
			return false;
		}

		for (String id : registryNames.stream().filter(s -> !s.isEmpty()).toList()) {
			if (!id.contains(":") && BuiltInRegistries.ITEM.getKey(item).getNamespace().equals(id)) {
				return true;
			}

			ResourceLocation itemResource = ResourceLocation.tryParse(id);
			if (itemResource != null && BuiltInRegistries.ITEM.get(itemResource).asItem() == item.asItem()) {
				return true;
			}
		}

		return false;
	}

}
