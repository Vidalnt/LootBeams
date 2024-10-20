package com.lootbeams;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.*;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.RenderNameTagEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = LootBeams.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientSetup {

	public static void init(FMLClientSetupEvent e) {
		NeoForge.EVENT_BUS.addListener(ClientSetup::onRenderNameplate);
	}

	public static void onRenderNameplate(RenderNameTagEvent event) {
		if (event.getEntity() instanceof ItemEntity) {
			ItemEntity itemEntity = (ItemEntity) event.getEntity();
			if (Minecraft.getInstance().player.distanceToSqr(itemEntity) > Configuration.RENDER_DISTANCE.get() * Configuration.RENDER_DISTANCE.get()) {
				return;
			}


			boolean shouldRender = false;
			if (Configuration.ALL_ITEMS.get()) {
				shouldRender = true;
			} else {
				if (Configuration.ONLY_EQUIPMENT.get()) {
					List<Class<? extends Item>> equipmentClasses = Arrays.asList(SwordItem.class, TieredItem.class, ArmorItem.class, ShieldItem.class, BowItem.class, CrossbowItem.class, TridentItem.class, ArrowItem.class, FishingRodItem.class);
					for (Class<? extends Item> item : equipmentClasses) {
						if (item.isAssignableFrom(itemEntity.getItem().getItem().getClass())) {
							shouldRender = true;
							break;
						}
					}
				}

				if (Configuration.ONLY_RARE.get()) {
					shouldRender = itemEntity.getItem().getRarity() != Rarity.COMMON;
				}

				if (isItemInRegistryList(Configuration.WHITELIST.get(), itemEntity.getItem().getItem())) {
					shouldRender = true;
				}
			}
			if (isItemInRegistryList(Configuration.BLACKLIST.get(), itemEntity.getItem().getItem())) {
				shouldRender = false;
			}

			if (shouldRender) {
				LootBeamRenderer.renderLootBeam(event.getPoseStack(), event.getMultiBufferSource(), event.getPartialTick(), itemEntity.getLevel().getGameTime(), itemEntity);
			}
		}
	}

	/**
	 * Checks if the given item is in the given list of registry names.
	 */
	private static boolean isItemInRegistryList(List<String> registryNames, Item item) {
		if (registryNames.size() > 0) {
			for (String id : registryNames.stream().filter((s) -> (!s.isEmpty())).collect(Collectors.toList())) {
				if (!id.contains(":")) {
					if (BuiltInRegistries.ITEM.getKey(item).getNamespace().equals(id)) {
						return true;
					}
				}
				ResourceLocation itemResource = ResourceLocation.tryParse(id);
				if (itemResource != null && BuiltInRegistries.ITEM.get(itemResource).asItem() == item.asItem()) {
					return true;
				}

				
			}
		}
		return false;
	}

}
