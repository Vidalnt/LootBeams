package com.lootbeams;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
//import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

@Mod(LootBeams.MODID)
public class LootBeams {

	public static final String MODID = "lootbeams";
	public static final Logger LOGGER = LogManager.getLogger();
	public static final List<ItemStack> CRASH_BLACKLIST = new ArrayList<>();
	public static final ResourceLocation LOOT_DROP = new ResourceLocation(MODID, "loot_drop");
    
	public LootBeams() {
		//ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, com.lootbeams.Configuration.SPEC);
		//FMLJavaModLoadingContext.get().getModEventBus().addListener(com.lootbeams.ClientSetup::init);
	}

	public static RenderType translucentNoCull(ResourceLocation texture) {
        return RenderType.create("lootbeams_translucent", 
                                 DefaultVertexFormat.NEW_ENTITY, 
                                 VertexFormat.Mode.QUADS, 
                                 256, 
                                 false, 
                                 true,
                                 RenderType.CompositeState.builder()
                                    .setTextureState(new RenderStateShard.TextureStateShard(texture, false, false))
                                    .createCompositeState(false));
    }
}
