package com.barebonium.packcompanion.processors;

import com.barebonium.packcompanion.PackCompanion;
import com.barebonium.packcompanion.config.ConfigHandler;
import com.barebonium.packcompanion.entries.HTMLEntry;
import com.barebonium.packcompanion.htmlcompiler.HTMLGenerator;
import com.barebonium.packcompanion.utils.HouseApproval;
import com.barebonium.packcompanion.utils.RentryUploader;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

import static com.barebonium.packcompanion.PackCompanion.logsDir;
import static java.nio.file.Files.readAllBytes;

public class OutputProcessor {
    public static File HTMLReportFile;
    public static String lastRentryUrl = "";

    public static String timeStamp;
    public static String timeStampFile;
    public static String fileName;

    public static boolean mdModAnalysisSuccessful = false;
    public static boolean mdConfigAnalysisSuccessful = false;

    public static ArrayList<HTMLEntry> htmlEntries = new ArrayList<>();

    public static void runPreInitPackCompanionChecks() {
        timeStamp = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
        timeStampFile = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss").format(new Date());
        fileName = "companionOutput-" + timeStampFile + ".md";

        try {
            cleanupOldReports(PackCompanion.outputDir, ".md", ConfigHandler.reportFilesCountLimit);
            cleanupOldReports(PackCompanion.outputDir, ".html", ConfigHandler.reportFilesCountLimit);

            File outputLog = new File(PackCompanion.outputDir, fileName);
            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(outputLog, true)));

            File crashAssistantLog = new File(logsDir, "pack-companion-report.log");

            writer.println("# Pack Companion Report");
            writer.println("");
            if (HouseApproval.houseApproves){
                writer.println("###### House Approves this report!");
                writer.println("");
            }
            writer.printf("### Generated on: %s%n",timeStamp);
            writer.println("");

            if(ConfigHandler.enableModAnalysis) {
                mdModAnalysisSuccessful |= ModlistProcessor.checkModList(writer, htmlEntries);
                writer.flush();
                try {
                    Files.copy(
                            outputLog.toPath(),
                            crashAssistantLog.toPath(),
                            java.nio.file.StandardCopyOption.REPLACE_EXISTING
                    );
                    if (ConfigHandler.debugMode) {
                        PackCompanion.LOGGER.info("PreInit Report mirrored to logs for CrashAssistant support.");
                    }
                } catch (IOException e) {
                    PackCompanion.LOGGER.error("Failed to copy report to logs folder", e);
                }
            }else{
                mdModAnalysisSuccessful = true;
            }

            PackCompanion.LOGGER.info("Loaded mods Successfully Analysed");
            writer.close();

        } catch (Exception e) {
            PackCompanion.LOGGER.error("Encountered an exception while compiling HTML",e);
        }
    }
    public static void runPostInitPackCompanionChecks() {
        try {
            File outputLog = new File(PackCompanion.outputDir, fileName);
            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(outputLog, true)));

            File crashAssistantLog = new File(logsDir, "pack-companion-report.log");

            if(ConfigHandler.enableConfigAnalysis && mdModAnalysisSuccessful) {
                mdConfigAnalysisSuccessful |= ConfigProcessor.checkConfigs(writer);
            }else{
                mdConfigAnalysisSuccessful = true;
            }

            try {
                Files.copy(
                        outputLog.toPath(),
                        crashAssistantLog.toPath(),
                        java.nio.file.StandardCopyOption.REPLACE_EXISTING
                );
                if (ConfigHandler.debugMode) {
                    PackCompanion.LOGGER.info("PostInit Report mirrored to logs for CrashAssistant support.");
                }
            } catch (IOException e) {
                PackCompanion.LOGGER.error("Failed to copy report to logs folder", e);
            }

            PackCompanion.LOGGER.info("Please consult the Output log at {}", outputLog.getPath());
            writer.close();
            if(mdConfigAnalysisSuccessful && mdModAnalysisSuccessful) {
                if (ConfigHandler.debugMode) {
                    PackCompanion.LOGGER.info("Analysis confirmed, Beginning HTML compilation");
                }
                File htmlOutput = new File(PackCompanion.outputDir, fileName.replace(".md", ".html"));
                HTMLGenerator.saveAsHtml(htmlEntries, htmlOutput, timeStamp);
                if (ConfigHandler.enableUploadToRentry) {
                    try {
                        byte[] encoded = readAllBytes(outputLog.toPath());
                        String content = new String(encoded, StandardCharsets.UTF_8);
                        String rentryUrl = RentryUploader.uploadToRentry(content);

                        if (rentryUrl != null) {
                            PackCompanion.LOGGER.info("Report uploaded to Rentry: {}", rentryUrl);
                            lastRentryUrl = rentryUrl;
                        }
                    } catch (IOException e) {
                        PackCompanion.LOGGER.error("Failed to read MD file for upload", e);
                    }
                }
            } else if (ConfigHandler.debugMode) {
                PackCompanion.LOGGER.info("Analysis Failed, Skipping HTML compilation");
            }

        } catch (Exception e) {
            PackCompanion.LOGGER.error("Encountered an exception while compiling HTML",e);
        }
    }


    public static File verifyCachedFile(String fileName) throws FileNotFoundException {
        File file = new File(PackCompanion.cacheDir, fileName);
        if (!file.exists()) {
            throw new FileNotFoundException(String.format("modList JSON could not be fetched at %s", file.getPath()));
        }
        return file;
    }

    public static File initializeDirectory(File parentDir, String childDir) {
        File directory = new File(parentDir, childDir);
        if (!directory.exists()) directory.mkdir();
        return directory;
    }
    public static void cleanupOldReports(File directory, String format, int maxFilesPresent){
        if(!directory.exists()){
            return;
        }
        File[] files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(format));
        if(files!=null &&  files.length>maxFilesPresent){
            Arrays.sort(files, Comparator.comparing(File::lastModified));
            int filesToDelete = files.length-maxFilesPresent;
            for(int i=0; i<filesToDelete; i++){
                if(files[i].delete()){
                    PackCompanion.LOGGER.warn("Report deleted from output folder: {}", files[i].getPath());
                }
            }
        }
    }
}
