package com.barebonium.packcompanion.config;

import com.barebonium.packcompanion.PackCompanion;
import net.minecraftforge.common.config.Config;
import com.barebonium.packcompanion.Tags;
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
    @Config.Comment("Enable PackCompanion Features")
    public static boolean packCompanionEnabled = true;

    @Config.RequiresMcRestart
    @Config.Name("Analyze Config Settings")
    @Config.Comment("Enable PackCompanion Config Analysis")
    public static boolean enableConfigAnalysis = true;

    @Config.RequiresMcRestart
    @Config.Name("Analyze Mod List")
    @Config.Comment("Enable PackCompanion Mod Analysis")
    public static boolean enableModAnalysis = true;

    @Config.RequiresMcRestart
    @Config.Name("Enable HTML Report")
    @Config.Comment("Enables the PackCompanion HTML format output file.")
    public static boolean enableReportHtml = true;

    @Config.RequiresMcRestart
    @Config.Name("Enable Markdown Report")
    @Config.Comment("Enables the PackCompanion markdown format output file.")
    public static boolean enableReportMarkdown = true;

    @Config.RequiresMcRestart
    @Config.Name("Enable Upload To Rentry")
    @Config.Comment("Upload the PackCompanion markdown output file to \"Rentry.co\" to view formatted viewing.")
    public static boolean enableUploadToRentry = true;

    @Config.RequiresMcRestart
    @Config.Name("Enable Login Message")
    @Config.Comment("Enables the PackCompanion login message.")
    public static boolean enableLoginMessage = true;

    @Config.RequiresMcRestart
    @Config.Name("Enable Debug Mode")
    @Config.Comment("Enables debug mode, preventing the master list from updating from the remote repository.")
    public static boolean debugMode = false;

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