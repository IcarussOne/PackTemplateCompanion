package com.barebonium.packcompanion.htmlcompiler;


import com.barebonium.packcompanion.PackCompanion;
import com.barebonium.packcompanion.config.ConfigHandler;
import com.barebonium.packcompanion.entries.HTMLEntry;
import com.barebonium.packcompanion.entries.ModPatchEntry;
import com.barebonium.packcompanion.enumstates.Action;
import com.barebonium.packcompanion.processors.ConfigProcessor;
import com.barebonium.packcompanion.processors.OutputProcessor;
import com.barebonium.packcompanion.utils.HouseApproval;
import com.barebonium.packcompanion.utils.MessageRegex;
import com.barebonium.packcompanion.utils.helpers.ModHelper;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HTMLGenerator {
    public static void saveAsHtml(ArrayList<HTMLEntry> htmlEntries, File file, String timeStamp) {
        String htmlHeader = "<!DOCTYPE html><html><head><title>Pack Companion Report</title>" +
                "<meta charset=\"UTF-8\">"+
                "<style>" +
                "  :root { " +
                "    --bg-color: #0d1117; --text-color: #c9d1d9; --table-border: #30363d; " +
                "    --header-bg: #161b22; --row-even: #161b22; --accent: #58a6ff; --title-border: #30363d; " +
                "  }" +
                "  body.light-mode { " +
                "    --bg-color: #ffffff; --text-color: #24292f; --table-border: #d0d7de; " +
                "    --header-bg: #f6f8fa; --row-even: #f6f8fa; --accent: #0969da; --title-border: #d8dee4; " +
                "  }" +
                "  body { font-family: -apple-system, BlinkMacSystemFont, \"Segoe UI\", Helvetica, Arial, sans-serif; " +
                "         padding: 45px; line-height: 1.5; max-width: 1012px; margin: auto; " +
                "         background-color: var(--bg-color); color: var(--text-color); transition: background 0.3s, color 0.3s; }" +
                "  table { border-collapse: collapse; width: 100%; margin-bottom: 16px; border-spacing: 0; }" +
                "  th, td { padding: 6px 13px; border: 1px solid var(--table-border); }" +
                "  th { font-weight: 600; background-color: var(--header-bg); color: var(--accent); }" +
                " td { text-align: center }"+
                "  tr:nth-child(even) { background-color: var(--row-even); }" +
                "  h1 { border-bottom: 1px solid var(--title-border); padding-bottom: .3em; display: flex; justify-content: space-between; align-items: center; }" +
                "  .PROBLEMATIC { color: #ff7b72; font-weight: bold; } " +
                "  body.light-mode .PROBLEMATIC { color: #cf222e; }" +
                "  .DEPRECATED { color: #ffdc72; font-weight: bold; } " +
                "  body.light-mode .DEPRECATED { color: #ffa700; }" +
                "  .code-snippet{font-family: 'Courier New', Courier, monospace; font-size: 85%; background-color: rgba(240, 246, 252, 0.15); padding: 0.2em 0.4em; border-radius: 3px; color: #64d3ff;}"+
                "  body.light-mode .code-snippet{ background-color: rgba(11, 36, 62, 0.9); color: #FFFFFF;}"+
                "  .config-name {color: #72ffeb;}"+
                "  h6{ color: #b9b9b952 }"+
                "  body.light-mode h6{ color: #d7d7d7 }"+
                "  body.light-mode .config-name{ color: #129d7d;}"+
                "  a:link {color: #0969da; font-weight: bold;}"+
                "  body.light-mode a:link {color: #0969da; font-weight: bold;}"+
                "  .toggle-btn { padding: 8px 16px; font-size: 14px; border-radius: 6px; cursor: pointer; " +
                "                border: 1px solid var(--table-border); background: var(--header-bg); color: var(--text-color); }" +
                "  .dropdown { margin: 20px 0; border: 1px solid var(--table-border); border-radius: 6px; overflow: hidden; }" +
                "  .dropdown summary { padding: 12px 16px; font-weight: 600; cursor: pointer; " +
                "                      background-color: var(--header-bg); color: var(--accent); " +
                "                      list-style: none; outline: none; border-bottom: 1px solid var(--table-border); }" +
                "  .dropdown summary::-webkit-details-marker { display: none; }" +
                "  .dropdown summary::before { content: '\\25B6  '; margin-right: 8px; transition: transform 0.3s ease;}" +
                "  .dropdown[open] summary::before { content: '\\25BC  '; }" +
                "  .dropdown table { margin-bottom: 0; border: none; }" +
                "  .dropdown td, .dropdown th { border-left: none; border-right: none; }" +
                "</style></head><body>" +
                "<h1>Pack Companion Report <button class='toggle-btn' onclick='toggleTheme()'>Toggle Theme</button></h1>";

        StringBuilder headerEasterEgg = new StringBuilder();
        if (HouseApproval.houseApproves){
            headerEasterEgg.append("<h6>House approves this report!</h6>");
        }
        headerEasterEgg.append("<h4>Generated On: ").append(timeStamp).append("</h4>");

        StringBuilder tableHtml = new StringBuilder();
        StringBuilder patchListTable = new StringBuilder();
        StringBuilder cleanroomTable = new StringBuilder();
        List<HTMLEntry> modPatchList = new ArrayList<>();
        if (ConfigHandler.enableModAnalysis) {
            tableHtml.append("<h2>Mod Analysis</h2> <details class=\"dropdown\">");
            tableHtml.append("<summary>Show Mod Analysis</summary>");
            tableHtml.append("<table>");
            tableHtml.append("<tr>");
            tableHtml.append("<th>").append("Mod Name").append("</th>");
            tableHtml.append("<th>").append("Status").append("</th>");
            tableHtml.append("<th>").append("Recommended Action").append("</th>");
            tableHtml.append("<th>").append("Reason").append("</th>");
            tableHtml.append("</tr>");
            PackCompanion.LOGGER.info("Beginning ModList analysis for HTML");
            for (HTMLEntry htmlEntry : htmlEntries) {
                String modName = htmlEntry.modName;
                String statusStr = htmlEntry.status.toString();
                String htmlClass = htmlEntry.status.toString();
                String actionMessage = htmlEntry.actionMessage;
                if (ConfigHandler.debugMode) {
                    PackCompanion.LOGGER.info("Processing mod {} for HTML", modName);
                }
                if (htmlEntry.action != Action.INCLUDE && !htmlEntry.isCleanroom) {
                    tableHtml.append("<tr>");
                    tableHtml.append("<td>").append(modName).append("</td>");
                    tableHtml.append("<td class=")
                            .append(htmlClass)
                            .append(">")
                            .append(statusStr)
                            .append("</td>");

                    tableHtml.append("<td>")
                            .append(actionMessage)
                            .append("</td>");
                    tableHtml.append("<td>")
                            .append(MessageRegex.translateToHTML(htmlEntry.message))
                            .append("</td>");
                    tableHtml.append("</tr>");
                } else if (htmlEntry.action == Action.INCLUDE && !htmlEntry.isCleanroom) {
                    modPatchList.add(htmlEntry);
                }
            }
            tableHtml.append("</table>");
            tableHtml.append("</details>");

            patchListTable.append("<h2>Mods and Patches to include</h2> <details class=\"dropdown\">");
            patchListTable.append("<summary>Show Mods and Patches to include</summary>");
            patchListTable.append("<table>");
            patchListTable.append("<tr>");
            patchListTable.append("<th>").append("Mod Name").append("</th>");
            patchListTable.append("<th>").append("Patch for mod").append("</th>");
            patchListTable.append("<th>").append("Description").append("</th>");
            patchListTable.append("</tr>");
            PackCompanion.LOGGER.info("Beginning ModPatchList analysis for HTML");
            for (HTMLEntry htmlEntry : modPatchList) {
                String modName = htmlEntry.modName;
                if (ConfigHandler.debugMode) {
                    PackCompanion.LOGGER.info("Processing mod {} for HTML", modName);
                }
                for (ModPatchEntry patchEntry : htmlEntry.patchList) {
                    if (patchEntry.isClassLoaded && !ModHelper.isClassLoaded(patchEntry.classpath) || !patchEntry.isClassLoaded && !ModHelper.isModLoaded(patchEntry.modId)) {
                        String patchModName = String.format("<p><a href=\"%s\">%s</a></p>",
                                patchEntry.modLink, patchEntry.modName);
                        patchListTable.append("<tr>");
                        patchListTable.append("<td>").append(patchModName).append("</td>");
                        patchListTable.append("<td>").append(modName).append("</td>");
                        patchListTable.append("<td>").append(MessageRegex.translateToHTML(patchEntry.modDescription)).append("</td>");
                    }
                }
            }

            patchListTable.append("</table>");
            patchListTable.append("</details>");

            cleanroomTable.append("<h2>Cleanroom Incompatible Mods</h2> <details class=\"dropdown\">");
            cleanroomTable.append("<summary>Show Cleanroom Incompatible Mods</summary>");
            cleanroomTable.append("<table>");
            cleanroomTable.append("<tr>");
            cleanroomTable.append("<th>").append("Mod Name").append("</th>");
            cleanroomTable.append("<th>").append("Status").append("</th>");
            cleanroomTable.append("<th>").append("Recommended Action").append("</th>");
            cleanroomTable.append("<th>").append("Reason").append("</th>");
            cleanroomTable.append("</tr>");

            for (HTMLEntry htmlEntry : htmlEntries) {
                String modName = htmlEntry.modName;
                String statusStr = htmlEntry.status.toString();
                String actionMessage = htmlEntry.actionMessage;
                String htmlClass = htmlEntry.status.toString();
                if (htmlEntry.action != Action.INCLUDE && htmlEntry.isCleanroom) {
                    cleanroomTable.append("<tr>");
                    cleanroomTable.append("<td>").append(modName).append("</td>");
                    cleanroomTable.append("<td class=")
                            .append(htmlClass)
                            .append(">")
                            .append(statusStr)
                            .append("</td>");

                    cleanroomTable.append("<td>")
                            .append(actionMessage)
                            .append("</td>");
                    cleanroomTable.append("<td>")
                            .append(MessageRegex.translateToHTML(htmlEntry.message))
                            .append("</td>");
                    cleanroomTable.append("</tr>");
                }
            }
            cleanroomTable.append("</table>");
            cleanroomTable.append("</details>");
        }



        String script = "<script>" +
                "  function toggleTheme() { document.body.classList.toggle('light-mode'); }" +
                "var links = document.links;\n" +
                "for (var i = 0; i < links.length; i++) {\n" +
                "     links[i].target = \"_blank\";\n" +
                "}"+
                "</script>";

        if(modPatchList.isEmpty()){
            patchListTable = new StringBuilder();
        }
        String ConfigTableFinal="";
        if(ConfigHandler.enableConfigAnalysis){
            ConfigTableFinal = ConfigProcessor.ConfigTable.toString();
        }

        String finalHtml = htmlHeader + headerEasterEgg + tableHtml + patchListTable + cleanroomTable + ConfigTableFinal + script + "</body></html>";
        PackCompanion.LOGGER.info("finalHtml built, Beginning writing file");
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file)))) {
            writer.println(finalHtml);
            PackCompanion.LOGGER.info("File Outputted");
        } catch (IOException e) {
            PackCompanion.LOGGER.error("Failed to write HTML report", e);
        }
        OutputProcessor.HTMLReportFile = file;
    }
}
