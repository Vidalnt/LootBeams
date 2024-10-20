package com.lootbeams;

import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

@Mod(LootBeams.MODID)
public class LootBeams {

    public static final String MODID = "lootbeams";
    public static final Logger LOGGER = LogManager.getLogger();
    public static List<ItemStack> CRASH_BLACKLIST = new ArrayList<>();

    //public static Configuration config;

    public LootBeams() {
        //config = Configuration.load(); 
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Configuration.CLIENT_CONFIG);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        // Inicialización del lado del cliente aquí
    }
}