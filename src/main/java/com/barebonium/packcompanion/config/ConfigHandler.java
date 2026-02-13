package com.barebonium.packcompanion.config;

import com.barebonium.packcompanion.PackCompanion;
import com.barebonium.packcompanion.Tags;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(
        modid = Tags.MOD_ID,
        name = PackCompanion.MOD_ID + "/" + PackCompanion.MOD_ID
)
public class ConfigHandler {
    @Config.RequiresMcRestart
    @Config.Name("Analyze Config Settings")
    @Config.Comment("Enable PackCompanion configuration analysis.")
    public static boolean enableConfigAnalysis = true;

    @Config.RequiresMcRestart
    @Config.Name("Analyze Mod List")
    @Config.Comment("Enable PackCompanion mod analysis.")
    public static boolean enableModAnalysis = true;

    @Config.RequiresMcRestart
    @Config.Name("Enable Upload To Rentry")
    @Config.Comment("Upload the PackCompanion markdown output file to \"Rentry.co\" to view formatted markdown text.")
    public static boolean enableUploadToRentry = true;

    @Config.RequiresMcRestart
    @Config.Name("Enable Login Message")
    @Config.Comment("Enables the PackCompanion login message.")
    public static boolean enableLoginMessage = true;

    @Config.RequiresMcRestart
    @Config.Name("Enable Version Check")
    @Config.Comment("Enables the PackCompanion remote (online) master list check. Only set this to false if you want to modify the lists manually.")
    public static boolean enableVersionChecker = true;

    @Config.RequiresMcRestart
    @Config.Name("Enable Debug Mode")
    @Config.Comment("Enables debug mode with robust logging.")
    public static boolean debugMode = false;

    @Config.RequiresMcRestart
    @Config.Name("Report files count limit")
    @Config.Comment("Limit how many instances of reports you want to be present in your output folder (1-100)")
    @Config.RangeInt(min = 1, max = 100)
    public static int reportFilesCountLimit = 5;

    @Mod.EventBusSubscriber(modid = PackCompanion.MOD_ID)
    public static class ConfigChangeListener {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if(event.getModID().equals(PackCompanion.MOD_ID)) {
                ConfigManager.sync(PackCompanion.MOD_ID, Config.Type.INSTANCE);
            }
        }
    }
}