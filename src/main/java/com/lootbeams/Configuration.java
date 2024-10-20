package com.lootbeams;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.fml.common.Mod;
import net.minecraft.core.registries.BuiltInRegistries;
import org.joml.Vector3d;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber
public class Configuration {

	public static ModConfigSpec CLIENT_CONFIG;

	public static ModConfigSpec.BooleanValue ITEMS_GLOW;
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
	public static ModConfigSpec.BooleanValue COMMON_SHORTER_BEAM;
	public static ModConfigSpec.DoubleValue BEAM_Y_OFFSET;
	public static ModConfigSpec.DoubleValue BEAM_ALPHA;
	public static ModConfigSpec.DoubleValue BEAM_FADE_DISTANCE;

	public static ModConfigSpec.BooleanValue SOLID_BEAM;
	public static ModConfigSpec.DoubleValue RENDER_DISTANCE;
	public static ModConfigSpec.BooleanValue REQUIRE_ON_GROUND;

	public static ModConfigSpec.BooleanValue GLOW_EFFECT;
	public static ModConfigSpec.DoubleValue GLOW_EFFECT_RADIUS;
	public static ModConfigSpec.BooleanValue ANIMATE_GLOW;

	public static ModConfigSpec.BooleanValue PARTICLES;

	public static ModConfigSpec.BooleanValue ADVANCED_TOOLTIPS;
	public static ModConfigSpec.BooleanValue WORLDSPACE_TOOLTIPS;
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
	public static ModConfigSpec.BooleanValue SCREEN_TOOLTIPS_REQUIRE_CROUCH;
	public static ModConfigSpec.BooleanValue COMBINE_NAME_AND_RARITY;

	public static ModConfigSpec.BooleanValue GLOWING_BEAM;

	public static ModConfigSpec.BooleanValue VANILLA_RARITIES;
	public static ModConfigSpec.BooleanValue WHITE_CENTER;
	public static ModConfigSpec.DoubleValue PARTICLE_SIZE;
	public static ModConfigSpec.DoubleValue PARTICLE_SPEED;
	public static ModConfigSpec.DoubleValue PARTICLE_RADIUS;
	public static ModConfigSpec.DoubleValue PARTICLE_COUNT;
	public static ModConfigSpec.IntValue PARTICLE_LIFETIME;
	public static ModConfigSpec.DoubleValue RANDOMNESS_INTENSITY;
	public static ModConfigSpec.BooleanValue PARTICLE_RARE_ONLY;
	public static ModConfigSpec.DoubleValue PARTICLE_DIRECTION_X;
	public static ModConfigSpec.DoubleValue PARTICLE_DIRECTION_Y;
	public static ModConfigSpec.DoubleValue PARTICLE_DIRECTION_Z;
	public static ModConfigSpec.BooleanValue SPIN_AROUND_BEAM;
	public static ModConfigSpec.BooleanValue TRAILS;
	public static ModConfigSpec.DoubleValue TRAIL_CHANCE;
	public static ModConfigSpec.BooleanValue TRAIL_PARTICLES_INVISIBLE;
	public static ModConfigSpec.DoubleValue TRAIL_WIDTH;
	public static ModConfigSpec.IntValue TRAIL_LENGTH;
	public static ModConfigSpec.IntValue TRAIL_FREQUENCY;

	public static ModConfigSpec.BooleanValue SOUND;
	public static ModConfigSpec.DoubleValue SOUND_VOLUME;
	public static ModConfigSpec.BooleanValue SOUND_ONLY_RARE;
	public static ModConfigSpec.BooleanValue SOUND_ONLY_EQUIPMENT;
	public static ModConfigSpec.ConfigValue<List<String>> SOUND_ONLY_WHITELIST;
	public static ModConfigSpec.ConfigValue<List<String>> SOUND_ONLY_BLACKLIST;
	public static ModConfigSpec.BooleanValue SOUND_ALL_ITEMS;

	static {
		ModConfigSpec.Builder clientBuilder = new ModConfigSpec.Builder();

		clientBuilder.comment("Beam Config").push("Loot Beams");
		RENDER_NAME_COLOR = clientBuilder.comment("If beams should be colored the same as the Items name (excludes name colors from rarity). This has priority over render_rarity_color.").define("render_name_color", true);
		RENDER_RARITY_COLOR = clientBuilder.comment("If beams should be colored the same as the Items rarity.").define("render_rarity_color", true);
		RENDER_DISTANCE = clientBuilder.comment("How close the player has to be to see the beam. (note: in vanill ItemEntities stop rendering at 24 blocks.)").defineInRange("render_distance", 48D, 0D, 1024D);
		COLOR_OVERRIDES = clientBuilder.comment("Overrides an item's beam color with hex color. Must follow the specific format: (registryname=hexcolor) Or (#tagname=hexcolor). Example: \"minecraft:stone=0xFFFFFF\". This also accepts modids.").define("color_overrides", new ArrayList<>());

		clientBuilder.comment("Beam Configuration").push("Beam");
		BEAM_RADIUS = clientBuilder.comment("The radius of the Loot Beam.").defineInRange("beam_radius", 0.55D, 0D, 5D);
		BEAM_HEIGHT = clientBuilder.comment("The height of the Loot Beam.").defineInRange("beam_height", 1.5D, 0D, 10D);
		BEAM_Y_OFFSET = clientBuilder.comment("The Y-offset of the loot beam.").defineInRange("beam_y_offset", 0.5D, -30D, 30D);
		COMMON_SHORTER_BEAM = clientBuilder.comment("If the Loot Beam should be shorter for common items.").define("common_shorter_beam", true);
		BEAM_ALPHA = clientBuilder.comment("Transparency of the Loot Beam.").defineInRange("beam_alpha", 0.75D, 0D, 1D);
		BEAM_FADE_DISTANCE = clientBuilder.comment("The distance from the player the beam should start fading.").defineInRange("beam_fade_distance", 2D, 0D, 100D);
		SOLID_BEAM = clientBuilder.comment("If the Loot Beam should use a solid texture or the beacon style texture.").define("solid_beam", true);
		WHITE_CENTER = clientBuilder.comment("If the Loot Beam should have a white center.").define("white_center", true);
		GLOWING_BEAM = clientBuilder.comment("If the Loot Beam should be glowing.").define("glowing_beam", true);
		GLOW_EFFECT = clientBuilder.comment("If the Loot Beam should have a glow effect around the base of the item.").define("glow_effect", true);
		GLOW_EFFECT_RADIUS = clientBuilder.comment("The radius of the glow effect.").defineInRange("glow_effect_radius", 0.5D, 0.00001D, 1D);
		ANIMATE_GLOW = clientBuilder.comment("If the glow effect should be animated.").define("animate_glow", true);
		REQUIRE_ON_GROUND = clientBuilder.comment("If the item must be on the ground to render a beam.").define("require_on_ground", true);
		clientBuilder.pop();

		clientBuilder.comment("Particle Config").push("Particles");
		PARTICLES = clientBuilder.comment("If particles should appear around the item.").define("particles", true);
		PARTICLE_SIZE = clientBuilder.comment("The size of the particles.").defineInRange("particle_size", 0.25D, 0.00001D, 10D);
		PARTICLE_SPEED = clientBuilder.comment("The speed of the particles.").defineInRange("particle_speed", 0.2D, 0.00001D, 10D);
		PARTICLE_RADIUS = clientBuilder.comment("The radius of the particles.").defineInRange("particle_radius", 0.1D, 0.00001D, 10D);
		RANDOMNESS_INTENSITY = clientBuilder.comment("The intensity of the randomness of the particles.").defineInRange("randomness_intensity", 0.05D, 0D, 1D);
		PARTICLE_COUNT = clientBuilder.comment("The amount of particles to spawn per second.").defineInRange("particle_count", 15D, 1D, 20D);
		PARTICLE_LIFETIME = clientBuilder.comment("The lifetime of the particles in ticks.").defineInRange("particle_lifetime", 15, 1, 100);
		PARTICLE_RARE_ONLY = clientBuilder.comment("If particles should only appear on rare items.").define("particle_rare_only", true);
		PARTICLE_DIRECTION_X = clientBuilder.comment("The direction of the particles on the X axis.").defineInRange("particle_direction_x", 0D, -1D, 1D);
		PARTICLE_DIRECTION_Y = clientBuilder.comment("The direction of the particles on the Y axis.").defineInRange("particle_direction_y", 1D, -1D, 1D);
		PARTICLE_DIRECTION_Z = clientBuilder.comment("The direction of the particles on the Z axis.").defineInRange("particle_direction_z", 0D, -1D, 1D);
		SPIN_AROUND_BEAM = clientBuilder.comment("If the particles should spin around the beam.").define("spin_around_beam", true);
		TRAILS = clientBuilder.comment("If the particles should leave a trail.").define("trails", true);
		TRAIL_CHANCE = clientBuilder.comment("The chance of a particle leaving a trail.").defineInRange("trail_chance", 0.5D, 0D, 1D);
		TRAIL_PARTICLES_INVISIBLE = clientBuilder.comment("If the particles with a trail should be invisible.").define("trail_particles_invisible", true);
		TRAIL_WIDTH = clientBuilder.comment("The width of the trail.").defineInRange("trail_width", 0.2D, 0.00001D, 10D);
		TRAIL_LENGTH = clientBuilder.comment("The length of the trail.").defineInRange("trail_length", 30, 1, 200);
		TRAIL_FREQUENCY = clientBuilder.comment("The frequency of the trail. The maximum value this should be is the length. The lower the frequency, the smoother the trail.").defineInRange("trail_frequency", 1, 1, 200);
		clientBuilder.pop();

		clientBuilder.comment("Item Config").push("Items");
		ITEMS_GLOW = clientBuilder.comment("If items should glow.").define("items_glow", false);
		ALL_ITEMS = clientBuilder.comment("If all Items Loot Beams should be rendered. Has priority over only_equipment and only_rare.").define("all_items", false);
		ONLY_RARE = clientBuilder.comment("If Loot Beams should only be rendered on items with rarity.").define("only_rare", true);
		ONLY_EQUIPMENT = clientBuilder.comment("If Loot Beams should only be rendered on equipment. (Equipment includes: Swords, Tools, Armor, Shields, Bows, Crossbows, Tridents, Arrows, and Fishing Rods)").define("only_equipment", true);
		WHITELIST = clientBuilder.comment("Registry names of items that Loot Beams should render on. Example: \"minecraft:stone\", \"minecraft:iron_ingot\", You can also specify modids for a whole mod's items.").define("whitelist", new ArrayList<>());
		BLACKLIST = clientBuilder.comment("Registry names of items that Loot Beams should NOT render on. This has priority over everything. You can also specify modids for a whole mod's items.").define("blacklist", new ArrayList<>());
		clientBuilder.pop();

		clientBuilder.comment("Item nametags").push("Nametags");
		ADVANCED_TOOLTIPS = clientBuilder.comment("If vanilla tooltips should be rendered on items in world.").define("advanced_tooltips", true);
		WORLDSPACE_TOOLTIPS = clientBuilder.comment("If tooltips should be rendered in world.").define("worldspace_tooltips", true);
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
		VANILLA_RARITIES = clientBuilder.comment("If vanilla rarities should be rendered.").define("vanilla_rarities", true);
		SCREEN_TOOLTIPS_REQUIRE_CROUCH = clientBuilder.comment("If the player should be crouching to see extended advanced tooltips.").define("screen_tooltips_require_crouch", true);
		COMBINE_NAME_AND_RARITY = clientBuilder.comment("If the name and rarity should be combined into one tooltip.").define("combine_name_and_rarity", false);
		clientBuilder.pop();

		clientBuilder.comment("Sounds").push("Sounds");
		SOUND = clientBuilder.comment("If sounds should be played when items are dropped up.").define("play_sounds", true);
		SOUND_VOLUME = clientBuilder.comment("The volume of the sound.").defineInRange("sound_volume", 1D, 0D, 1D);
		SOUND_ALL_ITEMS = clientBuilder.comment("If sounds should play on all items. Has priority over sound_only_equipment and sound_only_rare.").define("sound_all_items", false);
		SOUND_ONLY_RARE = clientBuilder.comment("If sounds should only be played on items with rarity.").define("sound_only_rare", true);
		SOUND_ONLY_EQUIPMENT = clientBuilder.comment("If sounds should only be played on equipment. (Equipment includes: Swords, Tools, Armor, Shields, Bows, Crossbows, Tridents, Arrows, and Fishing Rods)").define("sound_only_equipment", false);
		SOUND_ONLY_WHITELIST = clientBuilder.comment("Registry names of items that sounds should play on. Example: \"minecraft:stone\", \"minecraft:iron_ingot\", You can also specify modids for a whole mod's items.").define("sound_whitelist", new ArrayList<>());
		SOUND_ONLY_BLACKLIST = clientBuilder.comment("Registry names of items that sounds should NOT play on. This has priority over everything. You can also specify modids for a whole mod's items.").define("sound_blacklist", new ArrayList<>());
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
							if (BuiltInRegistries.ITEM.tags().getTag(TagKey.create(BuiltInRegistries.ITEM.key(), registry)).contains(i)) {
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
