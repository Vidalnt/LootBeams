package com.lootbeams;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.fml.common.Mod;
import net.minecraft.core.registries.BuiltInRegistries;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber
public class Configuration {

	public static ModConfigSpec CLIENT_CONFIG;

	public static ModConfigSpec.BooleanValue ALL_ITEMS;
	public static ModConfigSpec.BooleanValue ONLY_EQUIPMENT;
	public static ModConfigSpec.BooleanValue ONLY_RARE;
	public static ModConfigSpec.ConfigValue<List<String>> WHITELIST;
	public static ModConfigSpec.ConfigValue<List<String>> BLACKLIST;
	public static ModConfigSpec.ConfigValue<List<String>> COLOR_OVERRIDES;

	public static ModConfigSpec.BooleanValue RENDER_NAME_COLOR;
	public static ModConfigSpec.BooleanValue RENDER_RARITY_COLOR;
	public static ModConfigSpec.DoubleValue BEAM_RADIUS;
	public static ModConfigSpec.DoubleValue BEAM_HEIGHT;
	public static ModConfigSpec.DoubleValue BEAM_Y_OFFSET;
	public static ModConfigSpec.DoubleValue BEAM_ALPHA;
	public static ModConfigSpec.DoubleValue FADE_DISTANCE;
	public static ModConfigSpec.DoubleValue RENDER_DISTANCE;

	public static ModConfigSpec.BooleanValue BORDERS;
	public static ModConfigSpec.BooleanValue RENDER_NAMETAGS;
	public static ModConfigSpec.BooleanValue RENDER_NAMETAGS_ONLOOK;
	public static ModConfigSpec.BooleanValue RENDER_STACKCOUNT;
	public static ModConfigSpec.DoubleValue NAMETAG_LOOK_SENSITIVITY;
	public static ModConfigSpec.DoubleValue NAMETAG_TEXT_ALPHA;
	public static ModConfigSpec.DoubleValue NAMETAG_BACKGROUND_ALPHA;
	public static ModConfigSpec.DoubleValue NAMETAG_SCALE;
	public static ModConfigSpec.DoubleValue NAMETAG_Y_OFFSET;
	public static ModConfigSpec.BooleanValue DMCLOOT_COMPAT_RARITY;
	public static ModConfigSpec.ConfigValue<List<String>> CUSTOM_RARITIES;
	public static ModConfigSpec.BooleanValue WHITE_RARITIES;

	static {
		ModConfigSpec.Builder clientBuilder = new ModConfigSpec.Builder();

		clientBuilder.comment("Beam Config").push("Loot Beams");
		RENDER_NAME_COLOR = clientBuilder.comment("If beams should be colored the same as the Items name (excludes name colors from rarity). This has priority over render_rarity_color.").define("render_name_color", true);
		RENDER_RARITY_COLOR = clientBuilder.comment("If beams should be colored the same as the Items rarity.").define("render_rarity_color", true);
		BEAM_RADIUS = clientBuilder.comment("The radius of the Loot Beam.").defineInRange("beam_radius", 1D, 0D, 5D);
		BEAM_HEIGHT = clientBuilder.comment("The height of the Loot Beam.").defineInRange("beam_height", 1D, 0D, 10D);
		BEAM_Y_OFFSET = clientBuilder.comment("The Y-offset of the loot beam.").defineInRange("beam_y_offset", 0D, -30D, 30D);
		BEAM_ALPHA = clientBuilder.comment("Transparency of the Loot Beam.").defineInRange("beam_alpha", 0.85D, 0D, 1D);
		FADE_DISTANCE = clientBuilder.comment("Fade Distance of the Loot Beam.").defineInRange("fade_distance", 2.0D, 0D, 5D);
		RENDER_DISTANCE = clientBuilder.comment("How close the player has to be to see the beam. (note: ItemEntities stop rendering at 24 blocks, so that is the limit for beams)").defineInRange("render_distance", 24D, 0D, 24D);
		COLOR_OVERRIDES = clientBuilder.comment("Overrides an item's beam color with hex color. Must follow the specific format: (registryname=hexcolor) Or (#tagname=hexcolor). Example: \"minecraft:stone=0xFFFFFF\". This also accepts modids.").define("color_overrides", new ArrayList<>());

		clientBuilder.comment("Item Config").push("Items");
		ALL_ITEMS = clientBuilder.comment("If all Items Loot Beams should be rendered. Has priority over only_equipment and only_rare.").define("all_items", true);
		ONLY_RARE = clientBuilder.comment("If Loot Beams should only be rendered on items with rarity.").define("only_rare", false);
		ONLY_EQUIPMENT = clientBuilder.comment("If Loot Beams should only be rendered on equipment. (Equipment includes: Swords, Tools, Armor, Shields, Bows, Crossbows, Tridents, Arrows, and Fishing Rods)").define("only_equipment", false);
		WHITELIST = clientBuilder.comment("Registry names of items that Loot Beams should render on. Example: \"minecraft:stone\", \"minecraft:iron_ingot\", You can also specify modids for a whole mod's items.").define("whitelist", new ArrayList<>());
		BLACKLIST = clientBuilder.comment("Registry names of items that Loot Beams should NOT render on. This has priority over everything. You can also specify modids for a whole mod's items.").define("blacklist", new ArrayList<>());
		clientBuilder.pop();

		clientBuilder.comment("Item nametags").push("Nametags");
		BORDERS = clientBuilder.comment("Render nametags as bordered. Set to false for flat nametag with background.").define("borders", true);
		RENDER_NAMETAGS = clientBuilder.comment("If Item nametags should be rendered.").define("render_nametags", true);
		RENDER_NAMETAGS_ONLOOK = clientBuilder.comment("If Item nametags should be rendered when looking at items.").define("render_nametags_onlook", true);
		RENDER_STACKCOUNT = clientBuilder.comment("If the count of item's should also be shown in the nametag.").define("render_stackcount", true);
		NAMETAG_LOOK_SENSITIVITY = clientBuilder.comment("How close the player has to look at the item to render the nametag.").defineInRange("nametag_look_sensitivity", 0.018D, 0D, 5D);
		NAMETAG_TEXT_ALPHA = clientBuilder.comment("Transparency of the nametag text.").defineInRange("nametag_text_alpha", 1D, 0D, 1D);
		NAMETAG_BACKGROUND_ALPHA = clientBuilder.comment("Transparency of the nametag background/border.").defineInRange("nametag_background_alpha", 0.5D, 0D, 1D);
		NAMETAG_SCALE = clientBuilder.comment("Scale of the nametag.").defineInRange("nametag_scale", 1, -10D, 10D);
		NAMETAG_Y_OFFSET = clientBuilder.comment("The Y-offset of the nametag.").defineInRange("nametag_y_offset", 0.75D, -30D, 30D);
		DMCLOOT_COMPAT_RARITY = clientBuilder.comment("If a smaller tag should be rendered under with DMCLoot rarities.").define("dmcloot_compat_rarity", true);
		CUSTOM_RARITIES = clientBuilder.comment("Define what the smaller tag should render on. Example: \"Exotic\", \"Ancient\". The string supplied has to be the tooltip line below the name. This is really only used for modpacks.").define("custom_rarities", new ArrayList<>());
		WHITE_RARITIES = clientBuilder.comment("If rarities should ignore color and render as white (This is really only used for modpacks)").define("white_rarities", false);
		clientBuilder.pop();

		clientBuilder.pop();

		CLIENT_CONFIG = clientBuilder.build();
	}

	public static Color getColorFromItemOverrides(Item i) {
		List<String> overrides = COLOR_OVERRIDES.get();
		if (overrides.size() > 0) {
			for (String unparsed : overrides.stream().filter((s) -> (!s.isEmpty())).collect(Collectors.toList())) {
				String[] configValue = unparsed.split("=");
				if (configValue.length == 2) {
					String nameIn = configValue[0];
					ResourceLocation registry = ResourceLocation.tryParse(nameIn.replace("#", ""));
					Color colorIn = null;
					try {
						colorIn = Color.decode(configValue[1]);
					} catch (Exception e) {
						LootBeams.LOGGER.error(String.format("Color overrides error! \"%s\" is not a valid hex color for \"%s\"", configValue[1], nameIn));
						return null;
					}

					//Modid
					if (!nameIn.contains(":")) {
						if (BuiltInRegistries.ITEM.getKey(i).getNamespace().equals(nameIn)) {
							return colorIn;
						}

					}

					if (registry != null) {
						//Tag
						if (nameIn.startsWith("#")) {
							if (i.getTags().contains(registry)) {
								return colorIn;
							}
						}

						//Item
						Item registryItem = BuiltInRegistries.ITEM.get(registry);
						if (registryItem != null && registryItem.asItem() == i) {
							return colorIn;
						}

					}
				}
			}
		}
		return null;
	}
}
