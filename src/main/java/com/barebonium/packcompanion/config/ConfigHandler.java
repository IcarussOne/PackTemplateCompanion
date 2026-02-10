package com.barebonium.packcompanion.config;

import com.barebonium.packcompanion.PackCompanion;
import net.minecraftforge.common.config.Config;
import com.barebonium.packcompanion.Tags;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = Tags.MOD_ID)
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
    @Config.Comment("Save PackCompanion Report as an HTML file")
    public static boolean enableReportHtml = true;

    @Config.RequiresMcRestart
    @Config.Name("Enable Markdown Report")
    @Config.Comment("Save PackCompanion Report as a MarkdownFile")
    public static boolean enableReportMarkdown = true;





    @Config.RequiresMcRestart
    @Config.Comment("Enable PackCompanion login messages")
    public static boolean enableLoginMessage = true;

    @Config.RequiresMcRestart
    @Config.Comment("Enable PackCompanion HTML login message")
    public static boolean htmlOnLoginMessageEnabled = true;

    @Config.RequiresMcRestart
    @Config.Comment("Enable PackCompanion Markdown login message")
    public static boolean mdOnLoginMessageEnabled = true;




    @Config.RequiresMcRestart
    @Config.Name("Enable Debug Mode")
    @Config.Comment("Enable master list version check debug mode.")
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