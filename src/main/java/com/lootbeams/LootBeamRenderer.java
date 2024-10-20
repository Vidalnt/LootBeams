package com.lootbeams;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.phys.Vec3;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.registries.BuiltInRegistries;
//import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
//import net.minecraft.network.chat.TranslatableComponent;
//import net.minecraft.network.chat.Color;
//import net.minecraft.network.chat.TextVisitFactory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringDecomposer;
import net.minecraft.util.StringUtil;
//import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix4f;
import org.joml.Matrix3f;
import com.mojang.math.Vector3f;
import net.neoforged.neoforge.client.NeoForgeRenderTypes;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.Direction;
import org.joml.Quaternionf;
public class LootBeamRenderer extends RenderType {

	/**
	 * ISSUES:
	 * Beam renders behind things like chests/clouds/water/beds/entities.
	 */
    //Quaternionf.rotation
	private static final ResourceLocation LOOT_BEAM_TEXTURE = new ResourceLocation(LootBeams.MODID, "textures/entity/loot_beam.png");
	private static final RenderType LOOT_BEAM_RENDERTYPE = createRenderType();

	public LootBeamRenderer(String name, VertexFormat format, VertexFormat.Mode mode, int size, boolean crumble, boolean sorting, Runnable enable, Runnable disable) {
		super(name, format, mode, size, crumble, sorting, enable, disable);
	}

	public static void renderLootBeam(PoseStack stack, MultiBufferSource buffer, float pticks, long worldtime, ItemEntity item) {
		float beamAlpha = Configuration.BEAM_ALPHA.get().floatValue();
		float fadeDistance = Configuration.FADE_DISTANCE.get().floatValue();
		//Fade out when close
		var player = Minecraft.getInstance().player;
		var distance = player.distanceToSqr(item);
		if (distance < fadeDistance) {
			beamAlpha *= Math.max(0, distance-fadeDistance+1);
		}
		//Dont render beam if its too transparent
		if (beamAlpha <= 0.1f) {
			return;
		}

		float glowAlpha = beamAlpha * 0.4f;

		float beamRadius = 0.05f * Configuration.BEAM_RADIUS.get().floatValue();
		float glowRadius = beamRadius + (beamRadius * 0.2f);
		float beamHeight = Configuration.BEAM_HEIGHT.get().floatValue();
		float yOffset = Configuration.BEAM_Y_OFFSET.get().floatValue();

		Color color = getItemColor(item);
		float R = color.getRed() / 255f;
		float G = color.getGreen() / 255f;
		float B = color.getBlue() / 255f;

		//I will rewrite the beam rendering code soon! I promise!

		stack.pushPose();

		//Render main beam
		stack.pushPose();
		float rotation = (float) Math.floorMod(worldtime, 40L) + pticks;
		stack.mulPose(Vector3f.YP.rotationDegrees(rotation * 2.25F - 45.0F));
		stack.translate(0, yOffset, 0);
		stack.translate(0, 1, 0);
		stack.mulPose(Vector3f.XP.rotationDegrees(180));
		renderPart(stack, buffer.getBuffer(LOOT_BEAM_RENDERTYPE), R, G, B, beamAlpha, beamHeight, 0.0F, beamRadius, beamRadius, 0.0F, -beamRadius, 0.0F, 0.0F, -beamRadius);
		stack.mulPose(Vector3f.XP.rotationDegrees(-180));
		renderPart(stack, buffer.getBuffer(LOOT_BEAM_RENDERTYPE), R, G, B, beamAlpha, beamHeight, 0.0F, beamRadius, beamRadius, 0.0F, -beamRadius, 0.0F, 0.0F, -beamRadius);
		stack.popPose();

		//Render glow around main beam
		stack.translate(0, yOffset, 0);
		stack.translate(0, 1, 0);
		stack.mulPose(Vector3f.XP.rotationDegrees(180));
		renderPart(stack, buffer.getBuffer(LOOT_BEAM_RENDERTYPE), R, G, B, beamAlpha * 0.4f, beamHeight, -glowRadius, -glowRadius, glowRadius, -glowRadius, -beamRadius, glowRadius, glowRadius, glowRadius);
		stack.mulPose(Vector3f.XP.rotationDegrees(-180));
		renderPart(stack, buffer.getBuffer(LOOT_BEAM_RENDERTYPE), R, G, B, beamAlpha * 0.4f, beamHeight, -glowRadius, -glowRadius, glowRadius, -glowRadius, -beamRadius, glowRadius, glowRadius, glowRadius);

		stack.popPose();

		if (Configuration.RENDER_NAMETAGS.get()) {
			renderNameTag(stack, buffer, item, color);
		}
	}

	private static void renderNameTag(PoseStack stack, MultiBufferSource buffer, ItemEntity item, Color color) {
		//If player is crouching or looking at the item
		if (Minecraft.getInstance().player.isCrouching() || (Configuration.RENDER_NAMETAGS_ONLOOK.get() && isLookingAt(Minecraft.getInstance().player, item, Configuration.NAMETAG_LOOK_SENSITIVITY.get()))) {

			float foregroundAlpha = Configuration.NAMETAG_TEXT_ALPHA.get().floatValue();
			float backgroundAlpha = Configuration.NAMETAG_BACKGROUND_ALPHA.get().floatValue();
			double yOffset = Configuration.NAMETAG_Y_OFFSET.get();
			int foregroundColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) (255 * foregroundAlpha)).getRGB();
			int backgroundColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) (255 * backgroundAlpha)).getRGB();

			stack.pushPose();

			//Render nametags at heights based on player distance
			stack.translate(0.0D, Math.min(1D, Minecraft.getInstance().player.distanceToSqr(item) * 0.025D) + yOffset, 0.0D);
			stack.mulPose(Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation());

			float nametagScale = Configuration.NAMETAG_SCALE.get().floatValue();
			stack.scale(-0.02F * nametagScale, -0.02F * nametagScale, 0.02F * nametagScale);

			//Render stack counts on nametag
			Font fontrenderer = Minecraft.getInstance().font;
			String itemName = StringUtil.stripColor(item.getItem().getHoverName().getString());
			if (Configuration.RENDER_STACKCOUNT.get()) {
				int count = item.getItem().getCount();
				if (count > 1) {
					itemName = itemName + " x" + count;
				}
			}

			//Move closer to the player so we dont render in beam, and render the tag
			stack.translate(0, 0, -10);
			RenderText(fontrenderer, stack, buffer, itemName, foregroundColor, backgroundColor, backgroundAlpha);


			DrawRarity(item, foregroundAlpha, backgroundAlpha, fontrenderer, stack, buffer);

			stack.popPose();
		}
	}

	private static void DrawRarity(ItemEntity item,
								   float foregroundAlpha,
								   float backgroundAlpha,
								   Font fontRenderer,
								   PoseStack stack,
								   MultiBufferSource buffer
	) {
		//Render small tags
		stack.translate(0.0D, 10, 0.0D);
		stack.scale(0.75f, 0.75f, 0.75f);

		List<Component> tooltip = item.getItem().getTooltip(null, TooltipFlag.Default.NORMAL);


        if (tooltip.size() < 2) {
            return;
        }
		boolean textDrawn = false;

        Component tooltipRarity = tooltip.get(1);

        //Render dmcloot rarity small tags

		var rarityString = tooltipRarity.getString();

        //Render custom rarities
        if (textDrawn)
			return;


		if (!(Configuration.CUSTOM_RARITIES.get().contains(rarityString) ))//|| AlwaysHasRarity(item.getItem())))
			return;


        Color rarityColor = Configuration.WHITE_RARITIES.get() ? Color.WHITE : getRawColor(tooltipRarity);
        int foregroundColor = new Color(rarityColor.getRed(), rarityColor.getGreen(), rarityColor.getBlue(), (int) (255 * foregroundAlpha)).getRGB();
		int backgroundColor = new Color(rarityColor.getRed(), rarityColor.getGreen(), rarityColor.getBlue(), (int) (255 * backgroundAlpha)).getRGB();
        RenderText(fontRenderer, stack, buffer, rarityString, foregroundColor, backgroundColor, backgroundAlpha);
    }

	// private static boolean AlwaysHasRarity(ItemStack item) {
	// 	List<String> overrides = LootBeams.config.alwaysDrawRaritiesOn;
	// 	if (overrides.isEmpty())
	// 		return false;

	// 	for (String name : overrides.stream().filter((s) -> (!s.isEmpty())).toList()) {
	// 		ResourceLocation registry = ResourceLocation.tryParse(name.replace("#", ""));

	// 		//Modid
	// 		if (!name.contains(":"))
	// 			if (BuiltInRegistries.ITEM.getId(item.getItem()).getNamespace().equals(name))
	// 				return true;

	// 		if (registry == null)
	// 			continue;

	// 		//Tag
	// 		if (name.startsWith("#")) {
	// 			Optional<RegistryEntryList.Named<Item>> tag = BuiltInRegistries.ITEM.streamTagsAndEntries().filter(pair -> pair.getFirst().id().equals(registry))
	// 					.findFirst().map(Pair::getSecond);
	// 			//					Optional<HolderSet.Named<Item>> tag = Registry.ITEM.getTag(TagKey.create(Registry.ITEM_REGISTRY, registry));
	// 			if (tag.isPresent() && tag.get().contains(BuiltInRegistries.ITEM.getEntry(BuiltInRegistries.ITEM.getKey(item.getItem()).get()).get())) {
	// 				return true;
	// 			}
	// 		}

	// 		//Item
	// 		Optional<Item> registryItem = BuiltInRegistries.ITEM.getOrEmpty(registry);

	// 		if (registryItem.isPresent() && registryItem.get().asItem() == item.getItem())
	// 			return true;
	// 	}
	// 	return false;
	// }

	private static void RenderText(Font fontRenderer, PoseStack stack, MultiBufferSource buffer, String text, int foregroundColor, int backgroundColor, float backgroundAlpha) {
		if (Configuration.BORDERS.get()) {
			float w = -fontRenderer.width(text) / 2f;
			int bg = new Color(0, 0, 0, (int) (255 * backgroundAlpha)).getRGB();

			//Draws background (border) text
			fontRenderer.draw(stack, text, w + 1f, 0, bg);
			fontRenderer.draw(stack, text, w - 1f, 0, bg);
			fontRenderer.draw(stack, text, w, 1f, bg);
			fontRenderer.draw(stack, text, w, -1f, bg);

			//Draws foreground text in front of border
			stack.translate(0.0D, 0.0D, -0.01D);
			fontRenderer.draw(stack, text, w, 0, foregroundColor);
			stack.translate(0.0D, 0.0D, 0.01D);
		} else {
			fontRenderer.drawInBatch(text, (float) (-fontRenderer.width(text) / 2), 0f, foregroundColor, false, stack.last().pose(), buffer, false, backgroundColor, 15728864);
		}
	}


	/**
	 * Returns the color from the item's name, rarity, tag, or override.
	 */
	private static Color getItemColor(ItemEntity item) {
		if (LootBeams.CRASH_BLACKLIST.contains(item.getItem())) {
			return Color.WHITE;
		}

		try {

			//From Config Overrides
			Color override = Configuration.getColorFromItemOverrides(item.getItem().getItem());
			if (override != null) {
				return override;
			}

			//From NBT
			if (item.getItem().hasTag() && item.getItem().getTag().contains("lootbeams.color")) {
				return Color.decode(item.getItem().getTag().getString("lootbeams.color"));
			}

			//From Name
			if (Configuration.RENDER_NAME_COLOR.get()) {
				Color nameColor = getRawColor(item.getItem().getHoverName());
				if (!nameColor.equals(Color.WHITE)) {
					return nameColor;
				}
			}

			//From Rarity
			if (Configuration.RENDER_RARITY_COLOR.get() && item.getItem().getRarity().color != null) {
				return new Color(item.getItem().getRarity().color.getColor());
			} else {
				return Color.WHITE;
			}
		} catch (Exception e) {
			LootBeams.LOGGER.error("Failed to get color for (" + item.getItem().getDisplayName() + "), added to temporary blacklist");
			LootBeams.CRASH_BLACKLIST.add(item.getItem());
			LootBeams.LOGGER.info("Temporary blacklist is now : ");
			for (ItemStack s : LootBeams.CRASH_BLACKLIST) {
				LootBeams.LOGGER.info(s.getDisplayName());
			}
			return Color.WHITE;
		}
	}

	/**
	 * Gets color from the first letter in the text component.
	 */
	private static Color getRawColor(Component text) {
		List<Style> list = Lists.newArrayList();
		text.visit((acceptor, styleIn) -> {
			StringDecomposer.iterateFormatted(styleIn, acceptor, (string, style, consumer) -> {
				list.add(style);
				return true;
			});
			return Optional.empty();
		}, Style.EMPTY);
		if (list.get(0).getColor() != null) {
			return new Color(list.get(0).getColor().getValue());
		}
		return Color.WHITE;
	}

	private static void renderPart(PoseStack stack, VertexConsumer builder, float red, float green, float blue, float alpha, float height, float radius_1, float radius_2, float radius_3, float radius_4, float radius_5, float radius_6, float radius_7, float radius_8) {
		PoseStack.Entry matrixentry = stack.last();
		Matrix4f matrixpose = matrixentry.getPositionMatrix();
		Matrix3f matrixnormal = matrixentry.getNormalMatrix();
		renderQuad(matrixpose, matrixnormal, builder, red, green, blue, alpha, height, radius_1, radius_2, radius_3, radius_4);
		renderQuad(matrixpose, matrixnormal, builder, red, green, blue, alpha, height, radius_7, radius_8, radius_5, radius_6);
		renderQuad(matrixpose, matrixnormal, builder, red, green, blue, alpha, height, radius_3, radius_4, radius_7, radius_8);
		renderQuad(matrixpose, matrixnormal, builder, red, green, blue, alpha, height, radius_5, radius_6, radius_1, radius_2);
	}

	private static void renderQuad(Matrix4f pose, Matrix3f normal, VertexConsumer builder, float red, float green, float blue, float alpha, float y, float z1, float texu1, float z, float texu) {
		addVertex(pose, normal, builder, red, green, blue, alpha, y, z1, texu1, 1f, 0f);
		addVertex(pose, normal, builder, red, green, blue, alpha, 0f, z1, texu1, 1f, 1f);
		addVertex(pose, normal, builder, red, green, blue, alpha, 0f, z, texu, 0f, 1f);
		addVertex(pose, normal, builder, red, green, blue, alpha, y, z, texu, 0f, 0f);
	}

	private static void addVertex(Matrix4f pose, Matrix3f normal, VertexConsumer builder, float red, float green, float blue, float alpha, float y, float x, float z, float texu, float texv) {
		builder.vertex(pose, x, y, z).color(red, green, blue, alpha).uv(texu, texv).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
	}

	private static String toBinaryName(String mapName){
		return "L" + mapName.replace('.', '/') + ";";
	}

	private static RenderType createRenderType() {
		RenderType.CompositeState state = RenderType.CompositeState.builder().setShaderState(RENDERTYPE_BEACON_BEAM_SHADER)
            .setTextureState(new RenderStateShard.TextureStateShard(LOOT_BEAM_TEXTURE, false, false))
            .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
            .setWriteMaskState(COLOR_WRITE)
            .createCompositeState(false);
		return RenderType.create("loot_beam", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, 256, false, true, state); 
	}

	/**
	 * Checks if the player is looking at the given entity, accuracy determines how close the player has to look.
	 */
	private static boolean isLookingAt(LocalPlayer player, Entity target, double accuracy) {
		Vec3 difference = new Vec3(target.getX() - player.getX(), target.getEyeY() - player.getEyeY(), target.getZ() - player.getZ());
		double length = difference.length();
		double dot = player.getViewVector(1.0F).normalize().dot(difference.normalize());
		return dot > 1.0D - accuracy / length && !target.isInvisible();
	}

}