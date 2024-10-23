package com.lootbeams;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import net.neoforged.neoforge.client.event.RenderGuiOverlayEvent;
//import net.neoforged.client.event.TextureStitchEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;

import java.io.IOException;
import java.util.function.Consumer;

@Mod.EventBusSubscriber(modid = LootBeams.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModClientEvents {
    public static final ResourceLocation GLOW_TEXTURE = new ResourceLocation(LootBeams.MODID, "glow");

    public static ShaderInstance PARTICLE_ADDITIVE_MULTIPLY;

    @SubscribeEvent
    public static void registerShaders(RegisterShadersEvent event) throws IOException {
        registerShader(event, "particle_add", DefaultVertexFormat.PARTICLE, (s) -> {
            PARTICLE_ADDITIVE_MULTIPLY = s;
        });
    }

    private static void registerShader(RegisterShadersEvent event, String id, VertexFormat format, Consumer<ShaderInstance> callback) throws IOException {
        event.registerShader(new ShaderInstance(event.getResourceProvider(), new ResourceLocation(LootBeams.MODID, id), format), callback);
    }
}