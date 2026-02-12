package com.barebonium.packcompanion.events;

import com.barebonium.packcompanion.processors.OutputProcessor;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.io.File;

public class LoginHandler {
    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        File reportFile = OutputProcessor.HTMLReportFile;
        if(reportFile != null) {
            ITextComponent textComponent = new TextComponentString(TextFormatting.GOLD + "[Pack Companion] " + TextFormatting.GRAY + "Modlist analysis complete!");

            ITextComponent htmlTextComponentLink = new TextComponentString(TextFormatting.GOLD + "[Pack Companion] " + TextFormatting.GRAY + "Click ");
            htmlTextComponentLink.appendSibling(this.getClickable(reportFile.getAbsolutePath(), "Click to open HTML report in your browser"));
            htmlTextComponentLink.appendText(TextFormatting.GRAY + " to access the Local version of your report.");

            event.player.sendMessage(textComponent);
            event.player.sendMessage(htmlTextComponentLink);
            if (!OutputProcessor.lastRentryUrl.isEmpty()){
                ITextComponent mdTextComponentLink = new TextComponentString(TextFormatting.GOLD + "[Pack Companion] " + TextFormatting.GRAY + "Click ");
                mdTextComponentLink.appendSibling(this.getClickable(OutputProcessor.lastRentryUrl, "Click to open MD report in your browser"));
                mdTextComponentLink.appendText(TextFormatting.GRAY + " to access the Web version of your report.");
                event.player.sendMessage(mdTextComponentLink);
            }
        }
    }

    private ITextComponent getClickable(String link, String tooltip) {
        ITextComponent clickable = new TextComponentTranslation(TextFormatting.RED + "[HERE]");
        clickable.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, link));
        clickable.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString(tooltip)));
        return clickable;
    }
}
