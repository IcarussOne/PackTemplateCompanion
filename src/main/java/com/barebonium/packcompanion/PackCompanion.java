package com.barebonium.packcompanion;

import com.barebonium.packcompanion.config.ConfigHandler;
import com.barebonium.packcompanion.events.LoginHandler;
import com.barebonium.packcompanion.processors.OutputProcessor;
import com.barebonium.packcompanion.version.VersionChecker;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(
        modid = PackCompanion.MOD_ID,
        name = PackCompanion.MOD_NAME,
        version = PackCompanion.MOD_VERSION
)
public class PackCompanion {
    public static final String MOD_ID = Tags.MOD_ID;
    public static final String MOD_NAME = Tags.MOD_NAME;
    public static final String MOD_VERSION = Tags.VERSION;

    public static final Logger LOGGER = LogManager.getLogger(Tags.MOD_NAME);
    public static boolean isCleanroomEnv;
    public static File configDir;
    public static File cacheDir;
    public static File outputDir;
    public static File gameDir;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        try {
            Class.forName("com.cleanroommc.boot.Main");
            isCleanroomEnv = true;
        } catch (ClassNotFoundException e) {
            isCleanroomEnv = false;
        }
        gameDir = event.getModConfigurationDirectory().getParentFile();
        configDir = OutputProcessor.initializeDirectory(event.getModConfigurationDirectory(), PackCompanion.MOD_ID);
        cacheDir = OutputProcessor.initializeDirectory(configDir, "cache");
        outputDir = OutputProcessor.initializeDirectory(configDir, "output");
        if(ConfigHandler.enableLoginMessage) {
            MinecraftForge.EVENT_BUS.register(new LoginHandler());
        }
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event){
        long start = System.currentTimeMillis();
        if (ConfigHandler.enableVersionChecker) {
            VersionChecker.checkAndDownload();
        }
        LOGGER.info("{} is Checking your modlist!", Tags.MOD_NAME);
        OutputProcessor.runPackCompanionChecks();
        long end = System.currentTimeMillis();
        LOGGER.info("{} took {}ms to complete its analysis.", PackCompanion.MOD_NAME, end - start);
    }

}
