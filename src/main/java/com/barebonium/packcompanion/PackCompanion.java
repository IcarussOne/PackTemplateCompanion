package com.barebonium.packcompanion;

import com.barebonium.packcompanion.config.ConfigHandler;
import com.barebonium.packcompanion.utils.ConfigInitialiser;
import com.barebonium.packcompanion.utils.ModlistCheckProcessor;
import com.barebonium.packcompanion.version.VersionChecker;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
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
    public static File configDir;
    public static File baseConfig;
    public static File gameDir;

    /**
     * <a href="https://cleanroommc.com/wiki/forge-mod-development/event#overview">
     *     Take a look at how many FMLStateEvents you can listen to via the @Mod.EventHandler annotation here
     * </a>
     */
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        baseConfig = event.getModConfigurationDirectory();
        gameDir = event.getModConfigurationDirectory().getParentFile();
        configDir = new File(baseConfig, "packCompanion");
        if(ConfigHandler.enableLoginMessage) {
            MinecraftForge.EVENT_BUS.register(this);
        }
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event){
        if(ConfigHandler.packCompanionEnabled){
            ConfigInitialiser.initialise(baseConfig);
            if (!ConfigHandler.debugMode) {
                VersionChecker.checkAndDownload();
            }

            LOGGER.info("{} is Checking your modlist!", Tags.MOD_NAME);
            ModlistCheckProcessor.checkModList(baseConfig, gameDir);
        }
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if(ConfigHandler.packCompanionEnabled && ConfigHandler.enableLoginMessage){
            File reportFile = ModlistCheckProcessor.HTMLReportFile;
            if(reportFile != null) {
                ITextComponent textComponent = new TextComponentString(
                        TextFormatting.GOLD + "[Pack Companion] " + TextFormatting.GRAY + "Modlist analysis complete. Check your logs folder for the compatibility report!"
                );

                ITextComponent htmlTextComponentLink = new TextComponentString(TextFormatting.GOLD + "[Pack Companion] " + TextFormatting.GRAY + "Please click ");
                ITextComponent htmlClickableHere = new TextComponentString(TextFormatting.RED + "[ HERE ]");
                htmlClickableHere.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, reportFile.getAbsolutePath()));
                htmlClickableHere.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString("Click to open HTML report in your browser")));
                htmlTextComponentLink.appendSibling(htmlClickableHere);
                htmlTextComponentLink.appendText(TextFormatting.GRAY + " to access the Local Web version of your report.");

                ITextComponent mdTextComponentLink = new TextComponentString(TextFormatting.GOLD + "[Pack Companion] " + TextFormatting.GRAY + "Your report has been uploaded to Rentry.co, please click ");
                ITextComponent mdClickableHere = new TextComponentString(TextFormatting.RED + "[ HERE ]");

                mdClickableHere.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, ModlistCheckProcessor.lastRentryUrl));
                mdClickableHere.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString("Click to open MD report in your browser")));

                mdTextComponentLink.appendSibling(mdClickableHere);


                event.player.sendMessage(textComponent);
                if (!ModlistCheckProcessor.lastRentryUrl.isEmpty()){
                    event.player.sendMessage(mdTextComponentLink);
                }
                event.player.sendMessage(htmlTextComponentLink);
            }
        }
    }
}
