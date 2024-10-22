package com.lootbeams;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

// Clase de configuración de LootBeams
@Mod.EventBusSubscriber(modid = LootBeams.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Configuration {
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    // Valores booleanos para las opciones de configuración:
    public static final ModConfigSpec.BooleanValue ITEMS_GLOW = BUILDER
            .comment("If items should glow.")
            .define("items_glow", false);

    public static final ModConfigSpec.BooleanValue ALL_ITEMS = BUILDER
            .comment("If all Items Loot Beams should be rendered. Has priority over only_equipment and only_rare.")
            .define("all_items", true);

    public static final ModConfigSpec.BooleanValue ONLY_RARE = BUILDER
            .comment("If Loot Beams should only be rendered on items with rarity.")
            .define("only_rare", false);

    public static final ModConfigSpec.BooleanValue ONLY_EQUIPMENT = BUILDER
            .comment("If Loot Beams should only be rendered on equipment. (Equipment includes: Swords, Tools, Armor, Shields, Bows, Crossbows, Tridents, Arrows, and Fishing Rods)")
            .define("only_equipment", false);

    public static final ModConfigSpec.BooleanValue RENDER_NAME_COLOR = BUILDER
            .comment("If beams should be colored the same as the Items name (excludes name colors from rarity). This has priority over render_rarity_color.")
            .define("render_name_color", true);

    public static final ModConfigSpec.BooleanValue RENDER_RARITY_COLOR = BUILDER
            .comment("If beams should be colored the same as the Items rarity.")
            .define("render_rarity_color", true);

    public static final ModConfigSpec.BooleanValue SOLID_BEAM = BUILDER
            .comment("If the Loot Beam should use a solid texture or the beacon style texture.")
            .define("solid_beam", true);

    public static final ModConfigSpec.BooleanValue WHITE_CENTER = BUILDER
            .comment("If the Loot Beam should have a white center.")
            .define("white_center", true);

    public static final ModConfigSpec.BooleanValue GLOWING_BEAM = BUILDER
            .comment("If the Loot Beam should be glowing.")
            .define("glowing_beam", true);

    public static final ModConfigSpec.BooleanValue GLOW_EFFECT = BUILDER
            .comment("If the Loot Beam should have a glow effect around the base of the item.")
            .define("glow_effect", true);

    public static final ModConfigSpec.BooleanValue ANIMATE_GLOW = BUILDER
            .comment("If the glow effect should be animated.")
            .define("animate_glow", true);

    public static final ModConfigSpec.BooleanValue REQUIRE_ON_GROUND = BUILDER
            .comment("If the item must be on the ground to render a beam.")
            .define("require_on_ground", true);

    public static final ModConfigSpec.BooleanValue PARTICLES = BUILDER
            .comment("If particles should appear around the item.")
            .define("particles", true);

    public static final ModConfigSpec.BooleanValue PARTICLE_RARE_ONLY = BUILDER
            .comment("If particles should only appear on rare items.")
            .define("particle_rare_only", true);

    public static final ModConfigSpec.BooleanValue SPIN_AROUND_BEAM = BUILDER
            .comment("If the particles should spin around the beam.")
            .define("spin_around_beam", true);

    public static final ModConfigSpec.BooleanValue TRAILS = BUILDER
            .comment("If the particles should leave a trail.")
            .define("trails", true);

    public static final ModConfigSpec.BooleanValue TRAIL_PARTICLES_INVISIBLE = BUILDER
            .comment("If the particles with a trail should be invisible.")
            .define("trail_particles_invisible", true);

    public static final ModConfigSpec.BooleanValue ADVANCED_TOOLTIPS = BUILDER
            .comment("If vanilla tooltips should be rendered on items in world.")
            .define("advanced_tooltips", true);

    public static final ModConfigSpec.BooleanValue WORLDSPACE_TOOLTIPS = BUILDER
            .comment("If tooltips should be rendered in world.")
            .define("worldspace_tooltips", true);

    public static final ModConfigSpec.BooleanValue BORDERS = BUILDER
            .comment("Render nametags as bordered. Set to false for flat nametag with background.")
            .define("borders", true);

    public static final ModConfigSpec.BooleanValue RENDER_NAMETAGS = BUILDER
            .comment("If Item nametags should be rendered.")
            .define("render_nametags", true);

    public static final ModConfigSpec.BooleanValue RENDER_NAMETAGS_ONLOOK = BUILDER
            .comment("If Item nametags should be rendered when looking at items.")
            .define("render_nametags_onlook", true);

    public static final ModConfigSpec.BooleanValue RENDER_STACKCOUNT = BUILDER
            .comment("If the count of item's should also be shown in the nametag.")
            .define("render_stackcount", true);

    public static final ModConfigSpec.BooleanValue DMCLOOT_COMPAT_RARITY = BUILDER
            .comment("If a smaller tag should be rendered under with DMCLoot rarities.")
            .define("dmcloot_compat_rarity", true);

    public static final ModConfigSpec.BooleanValue WHITE_RARITIES = BUILDER
            .comment("If rarities should ignore color and render as white (This is really only used for modpacks)")
            .define("white_rarities", false);

    public static final ModConfigSpec.BooleanValue VANILLA_RARITIES = BUILDER
            .comment("If vanilla rarities should be rendered.")
            .define("vanilla_rarities", true);

    public static final ModConfigSpec.BooleanValue SCREEN_TOOLTIPS_REQUIRE_CROUCH = BUILDER
            .comment("If the player should be crouching to see extended advanced tooltips.")
            .define("screen_tooltips_require_crouch", true);

    public static final ModConfigSpec.BooleanValue COMBINE_NAME_AND_RARITY = BUILDER
            .comment("If the name and rarity should be combined into one tooltip.")
            .define("combine_name_and_rarity", false);

    public static final ModConfigSpec.BooleanValue SOUND = BUILDER
            .comment("If sounds should be played when items are dropped up.")
            .define("play_sounds", true);

    public static final ModConfigSpec.BooleanValue SOUND_ALL_ITEMS = BUILDER
            .comment("If sounds should play on all items. Has priority over sound_only_equipment and sound_only_rare.")
            .define("sound_all_items", false);

    public static final ModConfigSpec.BooleanValue SOUND_ONLY_RARE = BUILDER
            .comment("If sounds should only be played on items with rarity.")
            .define("sound_only_rare", true);

    public static final ModConfigSpec.BooleanValue SOUND_ONLY_EQUIPMENT = BUILDER
            .comment("If sounds should only be played on equipment. (Equipment includes: Swords, Tools, Armor, Shields, Bows, Crossbows, Tridents, Arrows, and Fishing Rods)")
            .define("sound_only_equipment", false);

    // Opciones de tipo Double:
    public static final ModConfigSpec.DoubleValue BEAM_ALPHA = BUILDER
            .comment("Transparency of the Loot Beam.")
            .defineInRange("beam_alpha", 0.85D, 0D, 1D);

    public static final ModConfigSpec.DoubleValue BEAM_FADE_DISTANCE = BUILDER
            .comment("The distance from the player the beam should start fading.")
            .defineInRange("beam_fade_distance", 2D, 0D, 100D);

    public static final ModConfigSpec.DoubleValue RENDER_DISTANCE = BUILDER
            .comment("How close the player has to be to see the beam. (note: in vanill ItemEntities stop rendering at 24 blocks.)")
            .defineInRange("render_distance", 48D, 0D, 1024D);

    public static final ModConfigSpec.DoubleValue SOUND_VOLUME = BUILDER
            .comment("The volume of the sound.")
            .defineInRange("sound_volume", 1D, 0D, 1D);

    public static final ModConfigSpec.DoubleValue NAMETAG_LOOK_SENSITIVITY = BUILDER
            .comment("How close the player has to look at the item to render the nametag.")
            .defineInRange("nametag_look_sensitivity", 0.018D, 0D, 5D);

    public static final ModConfigSpec.DoubleValue NAMETAG_TEXT_ALPHA = BUILDER
            .comment("Transparency of the nametag text.")
            .defineInRange("nametag_text_alpha", 1D, 0D, 1D);

    public static final ModConfigSpec.DoubleValue NAMETAG_BACKGROUND_ALPHA = BUILDER
            .comment("Transparency of the nametag background/border.")
            .defineInRange("nametag_background_alpha", 0.5D, 0D, 1D);

    public static final ModConfigSpec.DoubleValue NAMETAG_SCALE = BUILDER
            .comment("Scale of the nametag.")
            .defineInRange("nametag_scale", 1, -10D, 10D);

    public static final ModConfigSpec.DoubleValue NAMETAG_Y_OFFSET = BUILDER
            .comment("The Y-offset of the nametag.")
            .defineInRange("nametag_y_offset", 0.75D, -30D, 30D);

    public static final ModConfigSpec.DoubleValue GLOW_EFFECT_RADIUS = BUILDER
            .comment("The radius of the glow effect.")
            .defineInRange("glow_effect_radius", 0.5D, 0.00001D, 1D);

    public static final ModConfigSpec.DoubleValue PARTICLE_SIZE = BUILDER
            .comment("The size of the particles.")
            .defineInRange("particle_size", 0.25D, 0.00001D, 10D);

    public static final ModConfigSpec.DoubleValue PARTICLE_SPEED = BUILDER
            .comment("The speed of the particles.")
            .defineInRange("particle_speed", 0.2D, 0.00001D, 10D);

    public static final ModConfigSpec.DoubleValue PARTICLE_RADIUS = BUILDER
            .comment("The radius of the particles.")
            .defineInRange("particle_radius", 0.1D, 0.00001D, 10D);

    public static final ModConfigSpec.DoubleValue RANDOMNESS_INTENSITY = BUILDER
            .comment("The intensity of the randomness of the particles.")
            .defineInRange("randomness_intensity", 0.05D, 0D, 1D);

    public static final ModConfigSpec.DoubleValue PARTICLE_DIRECTION_X = BUILDER
            .comment("The direction of the particles on the X axis.")
            .defineInRange("particle_direction_x", 0D, -1D, 1D);

    public static final ModConfigSpec.DoubleValue PARTICLE_DIRECTION_Y = BUILDER
            .comment("The direction of the particles on the Y axis.")
            .defineInRange("particle_direction_y", 1D, -1D, 1D);

    public static final ModConfigSpec.DoubleValue PARTICLE_DIRECTION_Z = BUILDER
            .comment("The direction of the particles on the Z axis.")
            .defineInRange("particle_direction_z", 0D, -1D, 1D);

    public static final ModConfigSpec.DoubleValue TRAIL_CHANCE = BUILDER
            .comment("The chance of a particle leaving a trail.")
            .defineInRange("trail_chance", 0.5D, 0D, 1D);

    public static final ModConfigSpec.DoubleValue TRAIL_WIDTH = BUILDER
            .comment("The width of the trail.")
            .defineInRange("trail_width", 0.2D, 0.00001D, 10D);

    // Opciones de tipo Double:
    public static final ModConfigSpec.DoubleValue BEAM_RADIUS = BUILDER
            .comment("The radius of the beam.")
            .defineInRange("beam_radius", 1D, 0D, 5D);

    public static final ModConfigSpec.DoubleValue BEAM_HEIGHT = BUILDER
            .comment("The height of the beam.")
            .defineInRange("beam_height", 1D, 0D, 10D);

    public static final ModConfigSpec.DoubleValue BEAM_Y_OFFSET = BUILDER
            .comment("The Y-offset of the beam.")
            .defineInRange("beam_y_offset", 0D, -30D, 30D);

    // Opciones de tipo Int:
    public static final ModConfigSpec.IntValue PARTICLE_LIFETIME = BUILDER
            .comment("The lifetime of the particles in ticks.")
            .defineInRange("particle_lifetime", 15, 1, 100);

    public static final ModConfigSpec.IntValue TRAIL_LENGTH = BUILDER
            .comment("The length of the trail.")
            .defineInRange("trail_length", 30, 1, 200);

    public static final ModConfigSpec.IntValue TRAIL_FREQUENCY = BUILDER
            .comment("The frequency of the trail. The maximum value this should be is the length. The lower the frequency, the smoother the trail.")
            .defineInRange("trail_frequency", 1, 1, 200);

    // Opciones de tipo String (listas):
    public static final ModConfigSpec.ConfigValue<List<String>> WHITELIST = BUILDER
            .comment("Registry names of items that Loot Beams should render on. Example: \"minecraft:stone\", \"minecraft:iron_ingot\", You can also specify modids for a whole mod's items.")
            .defineListAllowEmpty("whitelist", new ArrayList<>(), Configuration::validateItemName);

    public static final ModConfigSpec.ConfigValue<List<String>> BLACKLIST = BUILDER
            .comment("Registry names of items that Loot Beams should NOT render on. This has priority over everything. You can also specify modids for a whole mod's items.")
            .defineListAllowEmpty("blacklist", new ArrayList<>(), Configuration::validateItemName);

    public static final ModConfigSpec.ConfigValue<List<String>> SOUND_ONLY_WHITELIST = BUILDER
            .comment("Registry names of items that sounds should play on. Example: \"minecraft:stone\", \"minecraft:iron_ingot\", You can also specify modids for a whole mod's items.")
            .defineListAllowEmpty("sound_whitelist", new ArrayList<>(), Configuration::validateItemName);

    public static final ModConfigSpec.ConfigValue<List<String>> SOUND_ONLY_BLACKLIST = BUILDER
            .comment("Registry names of items that sounds should NOT play on. This has priority over everything. You can also specify modids for a whole mod's items.")
            .defineListAllowEmpty("sound_blacklist", new ArrayList<>(), Configuration::validateItemName);

    public static final ModConfigSpec.ConfigValue<List<String>> CUSTOM_RARITIES = BUILDER
            .comment("Define what the smaller tag should render on. Example: \"Exotic\", \"Ancient\". The string supplied has to be the tooltip line below the name. This is really only used for modpacks.")
            .define("custom_rarities", new ArrayList<>());

    public static final ModConfigSpec.ConfigValue<List<String>> COLOR_OVERRIDES = BUILDER
            .comment("Overrides an item's beam color with hex color. Must follow the specific format: (registryname=hexcolor) Or (#tagname=hexcolor). Example: \"minecraft:stone=0xFFFFFF\". This also accepts modids.")
            .define("color_overrides", new ArrayList<>());

    // Especificación de configuración:
    public static final ModConfigSpec SPEC = BUILDER.build();

    // Variables para guardar los valores de configuración:
    public static boolean itemsGlow;
    public static boolean allItems;
    public static boolean onlyRare;
    public static boolean onlyEquipment;
    public static boolean renderNameColor;
    public static boolean renderRarityColor;
    public static boolean solidBeam;
    public static boolean whiteCenter;
    public static boolean glowingBeam;
    public static boolean glowEffect;
    public static boolean animateGlow;
    public static boolean requireOnGround;
    public static boolean particles;
    public static boolean particleRareOnly;
    public static boolean spinAroundBeam;
    public static boolean trails;
    public static boolean trailParticlesInvisible;
    public static boolean advancedTooltips;
    public static boolean worldspaceTooltips;
    public static boolean borders;
    public static boolean renderNametags;
    public static boolean renderNametagsOnlook;
    public static boolean renderStackcount;
    public static boolean dmclootCompatRarity;
    public static boolean whiteRarities;
    public static boolean screenTooltipsRequireCrouch;
    public static boolean combineNameAndRarity;
    public static boolean sound;
    public static boolean soundAllItems;
    public static boolean soundOnlyRare;
    public static boolean soundOnlyEquipment;

    public static double beamRadius;
    public static double beamHeight;
    public static double beamYOffset;
    public static double beamAlpha;
    public static double beamFadeDistance;
    public static double renderDistance;
    public static double glowEffectRadius;
    public static double particleSize;
    public static double particleSpeed;
    public static double particleRadius;
    public static double randomnessIntensity;
    public static double particleDirectionX;
    public static double particleDirectionY;
    public static double particleDirectionZ;
    public static double trailChance;
    public static double trailWidth;
    public static double nametagLookSensitivity;
    public static double nametagTextAlpha;
    public static double nametagBackgroundAlpha;
    public static double nametagScale;
    public static double nametagYOffset;
    public static double soundVolume;

    public static Set<Item> whitelist;
    public static Set<Item> blacklist;
    public static Set<String> customRarities;
    public static Set<String> soundOnlyWhitelist;
    public static Set<String> soundOnlyBlacklist;
    public static List<String> colorOverrides;

    // Método para validar los nombres de los items en la lista blanca:
    public static boolean validateItemName(final Object obj) {
        return obj instanceof String itemName && BuiltInRegistries.ITEM.containsKey(new ResourceLocation(itemName));
    }

    // Método para cargar la configuración:
    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        itemsGlow = ITEMS_GLOW.get();
        allItems = ALL_ITEMS.get();
        onlyRare = ONLY_RARE.get();
        onlyEquipment = ONLY_EQUIPMENT.get();
        renderNameColor = RENDER_NAME_COLOR.get();
        renderRarityColor = RENDER_RARITY_COLOR.get();
        solidBeam = SOLID_BEAM.get();
        whiteCenter = WHITE_CENTER.get();
        glowingBeam = GLOWING_BEAM.get();
        glowEffect = GLOW_EFFECT.get();
        animateGlow = ANIMATE_GLOW.get();
        requireOnGround = REQUIRE_ON_GROUND.get();
        particles = PARTICLES.get();
        particleRareOnly = PARTICLE_RARE_ONLY.get();
        spinAroundBeam = SPIN_AROUND_BEAM.get();
        trails = TRAILS.get();
        trailParticlesInvisible = TRAIL_PARTICLES_INVISIBLE.get();
        advancedTooltips = ADVANCED_TOOLTIPS.get();
        worldspaceTooltips = WORLDSPACE_TOOLTIPS.get();
        borders = BORDERS.get();
        renderNametags = RENDER_NAMETAGS.get();
        renderNametagsOnlook = RENDER_NAMETAGS_ONLOOK.get();
        renderStackcount = RENDER_STACKCOUNT.get();
        dmclootCompatRarity = DMCLOOT_COMPAT_RARITY.get();
        whiteRarities = WHITE_RARITIES.get();
        screenTooltipsRequireCrouch = SCREEN_TOOLTIPS_REQUIRE_CROUCH.get();
        combineNameAndRarity = COMBINE_NAME_AND_RARITY.get();
        sound = SOUND.get();
        soundAllItems = SOUND_ALL_ITEMS.get();
        soundOnlyRare = SOUND_ONLY_RARE.get();
        soundOnlyEquipment = SOUND_ONLY_EQUIPMENT.get();

        beamRadius = BEAM_RADIUS.get();
        beamHeight = BEAM_HEIGHT.get();
        beamYOffset = BEAM_Y_OFFSET.get();
        beamAlpha = BEAM_ALPHA.get();
        beamFadeDistance = BEAM_FADE_DISTANCE.get();
        renderDistance = RENDER_DISTANCE.get();
        glowEffectRadius = GLOW_EFFECT_RADIUS.get();
        particleSize = PARTICLE_SIZE.get();
        particleSpeed = PARTICLE_SPEED.get();
        particleRadius = PARTICLE_RADIUS.get();
        randomnessIntensity = RANDOMNESS_INTENSITY.get();
        particleDirectionX = PARTICLE_DIRECTION_X.get();
        particleDirectionY = PARTICLE_DIRECTION_Y.get();
        particleDirectionZ = PARTICLE_DIRECTION_Z.get();
        trailChance = TRAIL_CHANCE.get();
        trailWidth = TRAIL_WIDTH.get();
        nametagLookSensitivity = NAMETAG_LOOK_SENSITIVITY.get();
        nametagTextAlpha = NAMETAG_TEXT_ALPHA.get();
        nametagBackgroundAlpha = NAMETAG_BACKGROUND_ALPHA.get();
        nametagScale = NAMETAG_SCALE.get();
        nametagYOffset = NAMETAG_Y_OFFSET.get();
        soundVolume = SOUND_VOLUME.get();
        customRarities = CUSTOM_RARITIES.get().stream().collect(Collectors.toSet());
        soundOnlyWhitelist = SOUND_ONLY_WHITELIST.get().stream().collect(Collectors.toSet());
        soundOnlyBlacklist = SOUND_ONLY_BLACKLIST.get().stream().collect(Collectors.toSet());
        colorOverrides = COLOR_OVERRIDES.get();
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
                            TagKey<Item> tagKey = TagKey.create(BuiltInRegistries.ITEM.key(), registry);
                            HolderLookup<Item> itemLookup = BuiltInRegistries.ITEM.asLookup();
                            Optional<? extends HolderSet<Item>> optionalTagItems = itemLookup.get(tagKey);
                            if (optionalTagItems.isPresent()) {
                                HolderSet<Item> tagItems = optionalTagItems.get();
                                if (tagItems.contains(BuiltInRegistries.ITEM.getHolderOrThrow(BuiltInRegistries.ITEM.getResourceKey(i).orElseThrow()))) {
                                    return colorIn;
                                }
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