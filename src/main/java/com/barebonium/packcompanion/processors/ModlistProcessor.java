package com.barebonium.packcompanion.processors;

import com.barebonium.packcompanion.PackCompanion;
import com.barebonium.packcompanion.entries.ClassCheckEntry;
import com.barebonium.packcompanion.entries.HTMLEntry;
import com.barebonium.packcompanion.entries.ModEntry;
import com.barebonium.packcompanion.entries.ModPatchEntry;
import com.barebonium.packcompanion.enumstates.Action;
import com.barebonium.packcompanion.enumstates.Verification;
import com.barebonium.packcompanion.utils.FileHashCalculator;
import com.barebonium.packcompanion.utils.MessageRegex;
import com.barebonium.packcompanion.utils.helpers.ModHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModlistProcessor {
    private static final Gson GSON = new Gson();

    public static boolean checkModList(PrintWriter writer, List<HTMLEntry> htmlEntries) {
        boolean isSuccess = false;
        try {
            File modMasterListFile = OutputProcessor.verifyCachedFile("masterlist_mods.json");
            File classMasterListFile = OutputProcessor.verifyCachedFile("masterlist_mods_classes.json");

            JsonReader modListReader = new JsonReader(new FileReader(modMasterListFile));
            JsonReader classListReader = new JsonReader(new FileReader(classMasterListFile));

            List<ModEntry> modEntries = GSON.fromJson(modListReader, new TypeToken<List<ModEntry>>(){}.getType());
            List<ClassCheckEntry> classCheckEntries = GSON.fromJson(classListReader, new TypeToken<List<ClassCheckEntry>>(){}.getType());
            //Closing reader as it is no longer in use
            modListReader.close();

            if(modEntries == null || modEntries.isEmpty()) {
                PackCompanion.LOGGER.info("There are no Mods in the masterlist_mods file");
            } else {
                //Using a hashmap automatically places all entries in alphabetical order (mostly)
                Map<String, List<ModEntry>> loadedModEntries = new HashMap<>();
                Map<String, List<ModEntry>> loadedPatchEntries = new HashMap<>();

                //Loading all mod and patch entries from json
                for(ModEntry entry : modEntries) {
                    if(shouldGenerateEntry(entry)) {
                        String modName = ModHelper.getModName(entry.modId);
                        if(entry.action != Action.INCLUDE) {
                            //Non-patch entries
                            appendModEntries(loadedModEntries, modName, entry);
                        } else {
                            //Patch entries
                            appendModEntries(loadedPatchEntries, modName, entry);
                        }
                        htmlEntries.add(new HTMLEntry(modName, entry));
                    }
                }

                //Loading all class check entries from json
                for (ClassCheckEntry entry : classCheckEntries) {
                    if (shouldGenerateEntry(entry)) {
                        ModEntry fakeEntry = new ModEntry();
                        fakeEntry.modId = entry.modId;
                        fakeEntry.status = entry.status;
                        fakeEntry.action = entry.action;
                        fakeEntry.replacementModName = entry.replacementModName;
                        fakeEntry.replacementModLink = entry.replacementModLink;
                        fakeEntry.replacementModVersion = entry.replacementModVersion;
                        fakeEntry.patchList = entry.patchList;
                        fakeEntry.message = entry.message;
                        fakeEntry.isCleanroom = entry.isCleanroom;

                        String modName = ModHelper.getModName(entry.modId);
                        if(entry.action != Action.INCLUDE) {
                            //Non-patch entries
                            appendModEntries(loadedModEntries, modName, fakeEntry);
                        } else {
                            //Patch entries
                            appendModEntries(loadedPatchEntries, modName, fakeEntry);
                        }
                        htmlEntries.add(new HTMLEntry(modName, entry));
                    }
                }

                //Generating output file
                writeModListTable(writer, loadedModEntries);
                writePatchTable(writer, loadedPatchEntries);
                writeCleanroomTable(writer, loadedModEntries);
            }
            isSuccess = true;
            writer.println("");
        } catch (IOException e) {
            PackCompanion.LOGGER.error("Error while trying to read modlist guide", e);
        }
        return isSuccess;
    }

    private static boolean shouldGenerateEntry(ModEntry entry) {
        if(ModHelper.isModLoaded(entry.modId)) {
            //If the version is empty then the mod likely needs to be fully removed.
            if(entry.version == null) {
                return true;
            } else {
                boolean isSpecifiedVersion = ModHelper.isSpecifiedVersion(entry.modId, entry.version, entry.isMinVersion, entry.isMaxVersion);
                // Returns true if the entry is inclusive and the version is the specified version range or true
                // if the entry is not inclusive, and it is not in the specified version range.
                return (!entry.isVersionExclusive && isSpecifiedVersion) || (entry.isVersionExclusive && !isSpecifiedVersion);
            }
        }
        return false;
    }

    private static boolean shouldGenerateEntry(ClassCheckEntry entry) {
        if(ModHelper.isModLoaded(entry.modId) && entry.verification == Verification.HASHMATCH) {
            File jarFile = ModHelper.getModSource(entry.modId);
            if(jarFile != null) {
                try {
                    String jarHash = FileHashCalculator.getFileHash(jarFile, "md5");
                    return jarHash.equals(entry.versionHash);
                } catch (Exception e) {
                    PackCompanion.LOGGER.error("Error comparing Hash", e);
                    return false;
                }
            } else {
                return false;
            }
        } else if(entry.verification == Verification.CLASSLOADED){
            PackCompanion.LOGGER.warn("Classloaded status for class {}: {}", entry.className,ModHelper.isClassLoaded(entry.className) );
            return ModHelper.isClassLoaded(entry.className);
        } else {
            return false;
        }
    }

    private static void appendModEntries(Map<String, List<ModEntry>> entryMap, String modName, ModEntry modEntry) {
        if(!entryMap.containsKey(modName)) {
            entryMap.put(modName, new ArrayList<>());
        }
        entryMap.get(modName).add(modEntry);
    }

    private static void writeModListTable(PrintWriter writer, Map<String, List<ModEntry>> loadedModEntries) {
        final String analysisTableColumn = "| %-25s | %-15s | %-150s | %-200s |%n";

        boolean shouldProcess = loadedModEntries.values().stream().anyMatch(modEntries -> modEntries.stream().anyMatch(entry -> !entry.isCleanroom));
        if(shouldProcess) {
            writer.println("## Mod Analysis");
            writer.printf(analysisTableColumn, "Mod Name", "Status", "Recommended Action", "Reason");
            writer.printf(analysisTableColumn, ":---", ":---", ":---", ":---");

            loadedModEntries.forEach((modName, modEntries) -> {
                for (ModEntry entry : modEntries) {
                    if (entry.isCleanroom)
                        continue;
                    String statusStr = entry.status.toString();
                    String actionMessage = entry.action.getMarkdownActionString(entry, modName);
                    writer.printf(analysisTableColumn, modName, statusStr, actionMessage, MessageRegex.translateToMarkdown(entry.message));
                }
            });
            writer.println("");
        }
    }

    private static void writePatchTable(PrintWriter writer, Map<String, List<ModEntry>> modEntries) {
        final String patchTableColumn = "| %-200s | %-40s | %-80s |%n";

        boolean shouldProcess = modEntries.values().stream().anyMatch(entries -> entries.stream().anyMatch(entry -> entry.patchList != null && !entry.patchList.isEmpty()));
        if(shouldProcess) {
            writer.println("## Mods and Patches to include");
            writer.printf(patchTableColumn, "Mod Name", "Patch for", "Description");
            writer.printf(patchTableColumn, ":---", ":---", ":---");

            modEntries.forEach((modName, entries) -> {
                for (ModEntry entry : entries) {
                    if (entry.patchList == null)
                        continue;
                    for (ModPatchEntry patchEntry : entry.patchList) {
                        if (!ModHelper.isModLoaded(patchEntry.modId)) {
                            //TODO: This is still using the old hardcoded link format. May need to switch to the less brittle version.
                            String patchName = String.format("[%s](https://www.curseforge.com/minecraft/mc-mods/%s)", patchEntry.modName, patchEntry.modLink);
                            writer.printf(patchTableColumn, patchName, modName, patchEntry.modDescription);
                        }
                    }
                }
            });
            writer.println("");
        }
    }

    private static void writeCleanroomTable(PrintWriter writer, Map<String, List<ModEntry>> loadedModEntries) {
        final String analysisTableColumn = "| %-25s | %-15s | %-150s | %-200s |%n";

        boolean shouldProcess = loadedModEntries.values().stream().anyMatch(modEntries -> modEntries.stream().anyMatch(entry -> entry.isCleanroom));
        if(shouldProcess) {
            writer.println("## Cleanroom incompatible mods");
            writer.printf(analysisTableColumn, "Mod Name", "Status", "Recommended Action", "Reason");
            writer.printf(analysisTableColumn, ":---", ":---", ":---", ":---");

            loadedModEntries.forEach((modName, modEntries) -> {
                for (ModEntry entry : modEntries) {
                    if (!entry.isCleanroom)
                        continue;
                    String statusStr = entry.status.toString();
                    String actionMessage = entry.action.getMarkdownActionString(entry, modName);
                    writer.printf(analysisTableColumn, modName, statusStr, actionMessage, MessageRegex.translateToMarkdown(entry.message));
                }
            });
            writer.println("");
        }
    }
}
